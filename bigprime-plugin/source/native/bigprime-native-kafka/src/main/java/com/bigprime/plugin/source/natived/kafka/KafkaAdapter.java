package com.bigprime.plugin.source.natived.kafka;

import com.bigprime.parser.sql.SqlBase;
import com.bigprime.parser.sql.SqlBaseToken;
import com.bigprime.source.spi.interfaces.impl.NativeAdapter;
import com.bigprime.source.spi.model.Response;
import com.bigprime.source.spi.model.Time;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class KafkaAdapter
        extends NativeAdapter {
    protected KafkaConnection kafkaConnection;
    private final KafkaParser parser;

    public KafkaAdapter(KafkaConnection kafkaConnection, KafkaParser parser) {
        super(kafkaConnection, parser);
        this.kafkaConnection = kafkaConnection;
        this.parser = parser;
    }

    @Override
    public Response handlerExecute(String content) {
        Time processorTime = new Time();
        processorTime.setStart(new Date().getTime());
        Response response = this.kafkaConnection.getResponse();
        if (response.getIsConnected()) {
            List<String> headers = new ArrayList<>();
            List<Object> columns = new ArrayList<>();
            try {
                SqlBase sqlBase = this.parser.getSqlBase();
                if (sqlBase.isSuccessful()) {
                    AdminClient client = this.kafkaConnection.getClient();
                    if (ObjectUtils.isNotEmpty(this.parser.getSqlBase().getColumns())) {
                        headers.addAll(this.parser.getSqlBase().getColumns());
                    } else {
                        headers.add("*");
                    }
                    this.adapter(client, sqlBase)
                            .forEach(column -> columns.add(Collections.singletonList(column)));
                    response.setIsSuccessful(Boolean.TRUE);
                } else {
                    response.setIsSuccessful(Boolean.FALSE);
                    response.setMessage(sqlBase.getMessage());
                }
            } catch (Exception ex) {
                response.setIsSuccessful(Boolean.FALSE);
                response.setMessage(ex.getMessage());
            } finally {
                response.setHeaders(headers);
                response.setColumns(formatJson(headers, columns));
            }
        }
        processorTime.setEnd(new Date().getTime());
        response.setProcessor(processorTime);
        return response;
    }

    private List<String> adapter(AdminClient client, SqlBase info) {
        List<String> array = new ArrayList<>();
        if (info.getToken().equalsIgnoreCase(SqlBaseToken.SHOW.name())) {
            if (info.getChildToken().equalsIgnoreCase(SqlBaseToken.TOPICS.name())
                    || info.getChildToken().equalsIgnoreCase("DATABASES")) {
                this.adapterShowTopics(client, array);
            } else if (info.getChildToken().equalsIgnoreCase(SqlBaseToken.CONSUMERS.name())
                    || info.getChildToken().equalsIgnoreCase("TABLES")) {
                this.adapterShowConsumers(client, info, array);
            }
        }
        return array;
    }

    private void adapterShowTopics(AdminClient client, List<String> array) {
        try {
            client.listTopics()
                    .listings()
                    .get()
                    .forEach(v -> array.add(v.name()));
        } catch (Exception e) {
            Preconditions.checkArgument(false, ExceptionUtils.getMessage(e));
        }
    }

    private void adapterShowConsumers(AdminClient client, SqlBase info, List<String> array) {
        try {
            if (StringUtils.isNotEmpty(info.getTable())) {
                client.listConsumerGroups()
                        .all()
                        .get()
                        .parallelStream()
                        .forEach(v -> {
                            try {
                                DescribeConsumerGroupsResult describeConsumerGroupsResult = client.describeConsumerGroups(Collections.singleton(v.groupId()));
                                ConsumerGroupDescription consumerGroupDescription = describeConsumerGroupsResult.all().get().get(v.groupId());
                                if (consumerGroupDescription.members().stream().anyMatch(member ->
                                        member.assignment().topicPartitions().stream().anyMatch(tp ->
                                                tp.topic().equals(info.getTable().replace("`", ""))))) {
                                    array.add(v.groupId());
                                }
                            } catch (Exception e) {
                                Preconditions.checkArgument(false, ExceptionUtils.getMessage(e));
                            }
                        });
            } else {
                client.listConsumerGroups()
                        .all()
                        .get()
                        .forEach(v -> array.add(v.groupId()));
            }
        } catch (Exception e) {
            Preconditions.checkArgument(false, ExceptionUtils.getMessage(e));
        }
    }
}
