package com.bigprime.handler.currency;

import com.bigprime.handler.currency.model.FileStorageModel;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文件存储操作帮助类
 *
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class FileStorageHandler {
    private final EasyEntityQuery query;

    /**
     * 根据ID获取数据
     *
     * @param id Id
     * @return
     */
    public FileStorageModel getById(Long id) {
        return query.queryable(FileStorageModel.class).whereById(id).firstOrNull();
    }


    /**
     * 保存数据
     *
     * @param model 保存的实体
     */
    public long save(FileStorageModel model) {
        return query.insertable(model).executeRows(true);
    }

    /**
     * 更新数据
     * @param model
     * @return
     */
    public long update(FileStorageModel model) {
        return query.updatable(model).executeRows();
    }

    /**
     * 根据ID删除
     *
     * @param id
     */
    public long delete(Long id) {
        return query.deletable(FileStorageModel.class)
//                .disableLogicDelete()
//                .allowDeleteStatement(true)
                .whereById(id)
                .executeRows();
    }

    /**
     * 根据AttachmentUuid获取数据
     *
     * @param attachmentUuid
     * @return
     */
    public List<FileStorageModel> getByAttachmentUuid(String attachmentUuid) {
        return query.queryable(FileStorageModel.class).where(s -> s.attachmentUuid().eq(attachmentUuid)).toList();
    }

    /**
     * 根据AttachmentUuid集合获取数据
     * @param attachmentUuids
     * @return
     */
    public List<FileStorageModel> getByAttachmentUuids(List<String> attachmentUuids) {
        return query.queryable(FileStorageModel.class).where(s -> s.attachmentUuid().in(attachmentUuids)).toList();
    }
}
