package com.bigprime.db.ddl.model;

import com.bigprime.annotation.FieldDescription;
import com.bigprime.common.constant.DataOperateEnum;
import lombok.Data;

import java.util.List;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class TableBase<C extends ColumnBase,I extends IndexBase> {
    /**
     * 列信息
     */
    private List<C> columns;

    /**
     * 索引信息
     */
    private List<I> indexes;


    @FieldDescription(description = "库实例", isShow = false, sort = 1)
    private String schemaName;

    @FieldDescription(description = "表名称", sort = 2)
    private String tableName;

    @FieldDescription(description = "中文名称", canUpdate = true, sort = 3)
    private String tableComment;

    @FieldDescription(description = "表操作，N:无操作 U:更新 D:删除", isShow = false, sort = 199)
    private DataOperateEnum operate = DataOperateEnum.N;
}
