package com.bigprime.common.filter;

import com.bigprime.common.base.BaseModel;
import com.bigprime.common.base.SecurityUser;
import com.bigprime.common.base.UserDetail;
import com.easy.query.core.basic.extension.interceptor.EntityInterceptor;
import com.easy.query.core.expression.sql.builder.EntityInsertExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityUpdateExpressionBuilder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author lyw
 * @version 1.0
 */
@Component
public class MyEntityFilter implements EntityInterceptor {
    @Override
    public void configureInsert(Class<?> entityClass, EntityInsertExpressionBuilder entityInsertExpressionBuilder, Object entity) {
        UserDetail user = SecurityUser.getUser();
        Date now = new Date();
        BaseModel baseEntity = (BaseModel) entity;
        if(user.getId() != null){
            baseEntity.setCreator(user.getId());
            baseEntity.setUpdater(user.getId());
        }
        baseEntity.setCreateTime(now);
        baseEntity.setUpdateTime(now);
        baseEntity.setDeleted(false);
    }

    @Override
    public void configureUpdate(Class<?> entityClass, EntityUpdateExpressionBuilder entityUpdateExpressionBuilder, Object entity) {
        UserDetail user = SecurityUser.getUser();
        BaseModel baseEntity = (BaseModel) entity;
        if(user.getId() != null){
            baseEntity.setUpdater(user.getId());
        }
        baseEntity.setUpdateTime(new Date());
    }

    @Override
    public String name() {
        return "MyEntityFilter";
    }

    @Override
    public boolean apply(Class<?> entityClass) {
        return BaseModel.class.isAssignableFrom(entityClass);
    }
}
