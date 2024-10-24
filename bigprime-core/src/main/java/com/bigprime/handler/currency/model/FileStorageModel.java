package com.bigprime.handler.currency.model;

import com.bigprime.common.base.BaseModel;
import com.bigprime.handler.currency.model.proxy.FileStorageModelProxy;
import com.easy.query.core.annotation.EntityProxy;
import com.easy.query.core.annotation.Table;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;

/**
 * 文件存储
 *
 * @author lyw
 * @version 1.0
 */
@Data
@Table("file_storage")
@EntityProxy
public class FileStorageModel extends BaseModel implements ProxyEntityAvailable<FileStorageModel , FileStorageModelProxy> {

    /**
     * 文件大小
     */
    private Long size;

    /**
     * config id
     */
    private Long configId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 分组uuid
     */
    private String attachmentUuid;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * url
     */
    private String url;

}
