package com.bigprime.handler.sys.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.sys.model.proxy.SysOrgModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * 机构管理
 * @author lyw
 * @version 1.0
 */
@Data
@Table("sys_org")
@EntityProxy
public class SysOrgModel extends BaseModel implements ProxyEntityAvailable<SysOrgModel , SysOrgModelProxy> {

    /**
     * 机构名称
     */
    private String name;

    /**
     * 上级ID
     */
    private Long pid;

    /**
     * 排序
     */
    private Integer sort;
}
