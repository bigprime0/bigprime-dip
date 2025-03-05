package com.bigprime.db.ddl.model;

import com.bigprime.annotation.FieldDescription;
import com.bigprime.common.constant.DataOperateEnum;
import lombok.Data;

/**
 * @author lyw
 * @version 1.0
 */
@Data
public class IndexBase {
    @FieldDescription(description = "索引操作，N:无操作 I:新增 U:更新 D:删除", isShow = false, sort = 199)
    private DataOperateEnum operate = DataOperateEnum.N;
}
