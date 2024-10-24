package com.bigprime.service.sys;

import com.bigprime.handler.sys.SysMenuHandler;
import com.bigprime.handler.sys.SysRoleMenuHandler;
import com.bigprime.handler.sys.model.SysMenuModel;
import lombok.AllArgsConstructor;
import com.bigprime.common.base.UserDetail;
import com.bigprime.common.exception.ServerException;
import com.bigprime.common.utils.DozerUtils;
import com.bigprime.common.utils.TreeUtils;
import com.bigprime.vo.sys.SysMenuVO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单管理业务类
 * @author lyw
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class SysMenuService {

    private final SysMenuHandler modelHandler;
    private final SysRoleMenuHandler sysRoleMenuHandler;

    /**
     * 菜单导航
     * @param user
     * @return
     */
    public List<SysMenuVO> getUserMenuList(UserDetail user) {
        List<SysMenuModel> menuList = modelHandler.getAll();
        if(user.getSuperAdmin().equals(1)){
            //管理员返回全部
            List<SysMenuVO> list = DozerUtils.mapList(menuList, SysMenuVO.class);
            return TreeUtils.build(list);

        }else {
            List<SysMenuModel> list = new ArrayList<>();
            List<SysMenuModel> userModels = modelHandler.getList(user.getId());
            if(userModels.size() > 0){
                Map<Long,SysMenuModel> allMaps = menuList.stream().collect(Collectors.toMap(SysMenuModel::getId,SysMenuModel -> SysMenuModel));
                Map<Long,SysMenuModel> userMaps = new HashMap<>();
                userModels.forEach(o -> {
                    getParent(allMaps, userMaps, o);
                });
                list = new ArrayList<>(userMaps.values());
            }
            return TreeUtils.build(DozerUtils.mapList(list, SysMenuVO.class));
        }
    }

    /**
     * 查找所有父级信息
     * @param allMaps
     * @param userMaps
     * @param model
     */
    public void getParent(Map<Long,SysMenuModel> allMaps,Map<Long,SysMenuModel> userMaps,SysMenuModel model){
        if (!userMaps.containsKey(model.id)){
            userMaps.put(model.id, model);
        }
        if(model.getPid() > 0 && allMaps.containsKey(model.getPid())){
            getParent(allMaps, userMaps, allMaps.get(model.getPid()));
        }
    }


    /**
     * 保存数据
     * @param vo
     * @return
     */
    public long save(SysMenuVO vo) {
        return modelHandler.save(DozerUtils.map(vo, SysMenuModel.class));
    }

    /**
     * 更新数据
     * @param vo
     * @return
     */
    public long update(SysMenuVO vo) {
        // 上级菜单不能为自己
        if(vo.getId().equals(vo.getPid())){
            throw new ServerException("上级菜单不能为自己");
        }
        return modelHandler.update(DozerUtils.map(vo, SysMenuModel.class));
    }

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    public long delete(Long id) {
        Long count = modelHandler.getCountByPid(id);
        if(count > 0){
            throw new ServerException("请先删除子菜单");
        }
        //删除菜单
        modelHandler.delete(id);
        //删除菜单角色
        return sysRoleMenuHandler.deleteByMenuId(id);
    }
}
