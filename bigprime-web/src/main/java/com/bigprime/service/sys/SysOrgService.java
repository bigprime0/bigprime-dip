package com.bigprime.service.sys;

import com.bigprime.handler.sys.SysOrgHandler;
import com.bigprime.handler.sys.SysUserHandler;
import com.bigprime.handler.sys.model.SysOrgModel;
import com.bigprime.vo.sys.SysOrgVO;
import lombok.AllArgsConstructor;
import com.bigprime.common.constant.Constant;
import com.bigprime.common.exception.ServerException;
import com.bigprime.common.utils.DozerUtils;
import com.bigprime.common.utils.TreeUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 机构管理业务类
 * @author lyw
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class SysOrgService {

    private final SysOrgHandler modelHandler;
    private final SysUserHandler sysUserHandler;


    /**
     * 保存数据
     * @param vo
     * @return
     */
    public long save(SysOrgVO vo) {
        return modelHandler.save(DozerUtils.map(vo, SysOrgModel.class));
    }

    /**
     * 更新数据
     * @param vo
     * @return
     */
    public long update(SysOrgVO vo) {
        if (vo.getId().equals(vo.getPid())) {
            throw new ServerException("上级组织不能为自身");
        }
        List<Long> subOrgList = modelHandler.getDeptAndSubDeptList(vo.getId());
        if (subOrgList.contains(vo.getPid())) {
            throw new ServerException("上级组织不能为下级");
        }
        return modelHandler.update(DozerUtils.map(vo, SysOrgModel.class));
    }

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    public long delete(Long id) {
        long count = modelHandler.getSubCount(id);
        if (count > 0) {
            throw new ServerException("请先删除子组织");
        }

        // 判断机构下面是否有用户
        count = sysUserHandler.getCountByOrgId(id);
        if (count > 0) {
            throw new ServerException("组织下面有用户，不能删除");
        }
        return modelHandler.delete(id);
    }

    /**
     * 获取全部数据
     * @return
     */
    public List<SysOrgVO> getList(){
        List<SysOrgVO> list = setParentName(DozerUtils.mapList(modelHandler.getAll(), SysOrgVO.class));
        return TreeUtils.build(list);
    }

    public List<SysOrgModel> getAll(){
        return modelHandler.getAll();
    }


    /**
     * 转换父级信息
     * @param list
     * @return
     */
    public List<SysOrgVO> setParentName(List<SysOrgVO> list){
        if(!list.isEmpty()){
            List<Long> parentIds = new ArrayList<>();
            for (SysOrgVO vo : list){
                if(!Constant.ROOT.equals(vo.getPid()) && !parentIds.contains(vo.getPid())){
                    parentIds.add(vo.getPid());
                }
            }
            if(parentIds.size() > 0){
                Map<Long,String> map = modelHandler.getByIds(parentIds).stream().collect(Collectors.toMap(SysOrgModel::getId,SysOrgModel::getName));
                for (SysOrgVO vo : list){
                    if(vo.getPid() > 0 && map.containsKey(vo.getPid())){
                        vo.setParentName(map.get(vo.getPid()));
                    }
                }
            }
        }
        return list;
    }
}
