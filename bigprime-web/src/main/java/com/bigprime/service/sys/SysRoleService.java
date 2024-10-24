package com.bigprime.service.sys;

import com.bigprime.handler.sys.SysRoleHandler;
import com.bigprime.handler.sys.SysRoleMenuHandler;
import com.bigprime.handler.sys.SysUserRoleHandler;
import com.bigprime.handler.sys.model.SysRoleModel;
import com.bigprime.query.BaseQuery;
import com.bigprime.vo.sys.SysRoleVO;
import lombok.AllArgsConstructor;
import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.utils.DozerUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色管理业务类
 * @author lyw
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class SysRoleService {

    private final SysRoleHandler modelHandler;
    private final SysRoleMenuHandler sysRoleMenuHandler;
    private final SysUserRoleHandler sysUserRoleHandler;


    /**
     * 根据条件获取分页数据
     * @param query 请求参数
     * @return
     */
    public MyPageResult<SysRoleVO> getPage(BaseQuery query){
        MyPageResult<SysRoleModel> result = modelHandler.getPage(query.getSearch(),query.getPage(), query.getLimit());
        return new MyPageResult(setMenuIds(result.getList()), result.getTotal());
    }

    /**
     * 赋值菜单信息
     * @param models
     * @return
     */
    public List<SysRoleVO> setMenuIds(List<SysRoleModel> models){
        List<SysRoleVO> list = new ArrayList<>();
        if(models.size() > 0){
            //获取角色ID集合
            List<Long> roleIds = models.stream().map(SysRoleModel::getId).collect(Collectors.toList());
            //获取角色对应的菜单集合
            Map<Long,List<Long>> mapList = sysRoleMenuHandler.getByRoleIds(roleIds);
            list = DozerUtils.mapList(models, SysRoleVO.class);
            for (SysRoleVO vo : list) {
                if(mapList.containsKey(vo.getId())){
                    vo.setMenuIdList(mapList.get(vo.getId()));
                }
            }
        }
        return list;
    }

    /**
     * 保存数据
     * @param vo
     * @return
     */
    public long save(SysRoleVO vo) {
        SysRoleModel model = DozerUtils.map(vo, SysRoleModel.class);
        long exCount = modelHandler.save(model);
        //保存角色菜单关系
        sysRoleMenuHandler.saveOrUpdate(model.getId(), vo.getMenuIdList());
        return exCount;
    }

    /**
     * 更新数据
     * @param vo
     * @return
     */
    public long update(SysRoleVO vo) {
        //更新数据
        long exCount = modelHandler.update(DozerUtils.map(vo, SysRoleModel.class));
        //更新角色菜单关系
        sysRoleMenuHandler.saveOrUpdate(vo.getId(), vo.getMenuIdList());
        return exCount;
    }

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    public long delete(Long id) {
        //删除角色
        long exCount = modelHandler.delete(id);

        //删除用户角色关系
        sysUserRoleHandler.deleteByRoleIds(id);

        //删除角色菜单关系
        sysRoleMenuHandler.deleteByRoleIds(id);
        return exCount;
    }

    /**
     * 获取全部数据
     * @return
     */
    public List<SysRoleVO> getAll(){
        return DozerUtils.mapList(modelHandler.getAll(), SysRoleVO.class);
    }
}
