package com.bigprime.common.base;

import com.bigprime.common.utils.DateUtils;
import com.easy.query.core.annotation.*;
import com.easy.query.core.basic.extension.logicdel.LogicDeleteStrategyEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lyw
 * @version 1.0
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseModel implements Serializable {
    /**
     * id
     */
    @Column(primaryKey = true, generatedKey = true)
    public Long id;

    /**
     * 创建人
     */
    @UpdateIgnore
    public Long  creator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    @UpdateIgnore
    public Date createTime;

    /**
     * 更新人
     */
    public Long  updater;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    public Date updateTime;

    /**
     * 是否删除
     */
    @LogicDelete(strategy = LogicDeleteStrategyEnum.BOOLEAN)
    @UpdateIgnore
    public boolean deleted;
}
