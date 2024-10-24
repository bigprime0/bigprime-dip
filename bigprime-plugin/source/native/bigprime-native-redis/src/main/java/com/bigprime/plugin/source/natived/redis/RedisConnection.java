package com.bigprime.plugin.source.natived.redis;

import cn.hutool.core.util.StrUtil;
import com.bigprime.source.spi.internals.AbstractConnection;
import com.bigprime.source.spi.model.SourceConfig;
import com.bigprime.source.spi.model.Response;
import redis.clients.jedis.Jedis;

public class RedisConnection
        extends AbstractConnection
{
    private SourceConfig config;
    private Response response;
    private Jedis jedis;

    public RedisConnection(SourceConfig config, Response response)
    {
        super(config, response);
        this.config = config;
    }

    @Override
    protected String formatJdbcUrl()
    {
        return null;
    }

    @Override
    protected java.sql.Connection openConnection()
    {
        try {
            this.response = getResponse();
            this.jedis = new Jedis(this.config.getHost(), this.config.getPort());
            if (StrUtil.isNotBlank(this.config.getUsername()) && StrUtil.isNotBlank(this.config.getPassword())) {
                this.jedis.auth(this.config.getUsername(), this.config.getPassword());
            }
            if (StrUtil.isNotBlank(this.config.getPassword())) {
                this.jedis.auth(this.config.getPassword());
            }
            this.jedis.ping("BigPrime");
            response.setIsConnected(Boolean.TRUE);
        }
        catch (Exception ex) {
            response.setIsConnected(Boolean.FALSE);
            response.setMessage(ex.getMessage());
        }
        return null;
    }

    @Override
    public void destroy()
    {
        this.jedis.close();
    }

    public Jedis getJedis()
    {
        return jedis;
    }
}
