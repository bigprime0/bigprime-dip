package com.bigprime.service.currency;

import com.bigprime.handler.database.DataBaseHandler;
import com.bigprime.handler.database.model.DataDatabaseModel;
import com.bigprime.handler.integration.IntegrationJobHandler;
import com.bigprime.handler.integration.model.IntegrationJobStatistic;
import com.bigprime.plugin.manager.Plugin;
import com.bigprime.plugin.manager.constant.PluginType;
import com.bigprime.plugin.manager.internals.impl.SourcePlugin;
import com.bigprime.source.spi.model.TableModel;
import com.bigprime.vo.currency.IntegrationStatisticVO;
import com.bigprime.vo.currency.SourceStatisticVO;
import com.bigprime.vo.currency.StatisticVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lyw
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class CurrencyService {
    private final IntegrationJobHandler integrationJobHandler;
    private final DataBaseHandler dataBaseHandler;

    public StatisticVO getHomeStatistic() {
        IntegrationStatisticVO integration = IntegrationStatisticVO.builder()
                .numberOfTasks(0)
                .instances(0)
                .numberOfFailures(0)
                .inOperation(0)
                .successNumbers(0)
                .build();

        List<IntegrationJobStatistic> jobs = integrationJobHandler.getCount();
        for (IntegrationJobStatistic job : jobs) {
            integration.setNumberOfTasks(integration.getNumberOfTasks() + job.getCount());
        }

        List<IntegrationJobStatistic> jobLogs = integrationJobHandler.getJobLogCount();
        for (IntegrationJobStatistic job : jobLogs) {
            integration.setInstances(integration.getInstances() + job.getCount());

            if (job.getStatus().equals("running")) {
                integration.setInOperation(integration.getInOperation() + job.getCount());
            }

            if (job.getStatus().equals("success")) {
                integration.setSuccessNumbers(integration.getSuccessNumbers() + job.getCount());
            }

            if (job.getStatus().equals("error")) {
                integration.setNumberOfFailures(integration.getNumberOfFailures() + job.getCount());
            }
        }

        StatisticVO resultVo = StatisticVO.builder()
                .integration(integration)
                .source(new ArrayList<>())
                .build();

        List<DataDatabaseModel> list = dataBaseHandler.getList("");
        Map<Long, SourceStatisticVO> sourceMaps = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            DataDatabaseModel model = list.get(i);
            try {
                if (model.getActive() == 1) {
                    SourcePlugin plugin = Plugin.getInstance(PluginType.SOURCE, model.getId());
                    List<TableModel> tableModels = plugin.getTables();
                    SourceStatisticVO vo = SourceStatisticVO.builder()
                            .name(model.getName())
                            .sourceType(model.getProduct())
                            .tableQuantity(tableModels.size())
                            .build();
                    Double size = 0.00;
                    Long rows = 0L;
                    for (TableModel tableModel : tableModels) {
                        if (tableModel.getDataLength() != null && tableModel.getIndexLength() != null) {
                            size += (tableModel.getDataLength() + tableModel.getIndexLength()) / 1024.00 / 1024.00;
                        }
                        if (tableModel.getRows() != null) {
                            rows += tableModel.getRows();
                        }
                    }
                    vo.setVolume(size);
                    vo.setRows(rows);
                    sourceMaps.put(model.getId(), vo);
                }
            } catch (Exception e) {
                sourceMaps.put(model.getId(), SourceStatisticVO.builder()
                        .name(model.getName())
                        .sourceType(model.getProduct())
                        .tableQuantity(0).volume(0.00).rows(0L)
                        .build());
            }
        }
        resultVo.setSource(sourceMaps.values().stream().collect(Collectors.toList()));
        return resultVo;
    }
}
