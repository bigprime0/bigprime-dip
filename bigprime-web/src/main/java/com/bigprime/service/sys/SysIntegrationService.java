package com.bigprime.service.sys;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.handler.sys.SysIntegrationHandler;
import com.bigprime.handler.sys.model.SysIntegrationModel;
import com.bigprime.common.utils.StrUtils;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SysIntegrationService {

    private SysIntegrationHandler handler;

    public long save(SysIntegrationModel model) {
        return handler.save(model);
    }

    public long update(SysIntegrationModel model) {
        return handler.update(model);
    }

    public long delete(Long id) {
        return handler.delete(id);
    }

    public SysIntegrationModel get(long id) {
        return handler.getById(id);
    }

    public Pair<Boolean, String> clone(String product, String dbType, String category, String newType,boolean isOverlay) {
        if (StrUtils.IsNullOrEmpty(product)) return Pair.of(false, "缺少克隆参数{product}");
        if (StrUtils.IsNullOrEmpty(dbType)) return Pair.of(false, "缺少克隆参数{dbType}");
        if (StrUtils.IsNullOrEmpty(category)) return Pair.of(false, "缺少克隆参数{category}");
        if (StrUtils.IsNullOrEmpty(newType)) newType = StrUtil.format("{}_{}", dbType, IdUtil.getSnowflakeNextIdStr());
        var flag = handler.clone(product, dbType, category, newType,isOverlay);
        if (flag) return Pair.of(true, "success");
        return Pair.of(false, "fail");
    }

    public MyPageResult<SysIntegrationModel> getPageList(String product, String dbType, String category, Integer page, Integer limit) {
        return handler.getPageList(product, dbType, category, page, limit);
    }

    public List<SysIntegrationModel> list(String product, String dbType, String category){
        return handler.list(product,dbType,category);
    }
}
