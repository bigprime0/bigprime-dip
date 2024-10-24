package com.bigprime.service.sys;

import com.bigprime.common.base.MyPageResult;
import com.bigprime.common.constant.SuperAdminEnum;
import com.bigprime.common.exception.ServerException;
import com.bigprime.common.utils.DozerUtils;
import com.bigprime.handler.sys.SysOrgHandler;
import com.bigprime.handler.sys.SysUserHandler;
import com.bigprime.handler.sys.SysUserRoleHandler;
import com.bigprime.handler.sys.model.SysUserModel;
import com.bigprime.query.BaseQuery;
import com.bigprime.vo.sys.SysUserVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理业务类
 *
 * @author lyw
 * @version 1.0
 */
@Service
@AllArgsConstructor
public class SysUserService {

    private final SysUserHandler modelHandler;
    private final SysUserRoleHandler sysUserRoleHandler;
    private final SysOrgHandler sysOrgHandler;

    /**
     * 根据条件获取分页数据
     *
     * @param query 请求参数
     * @return
     */
    public MyPageResult<SysUserVO> getPage(BaseQuery query) {
        MyPageResult<SysUserModel> result = modelHandler.getPage(query.getSearch(), query.getPage(), query.getLimit());
        return new MyPageResult(setRoleIds(result.getList()), result.getTotal());
    }

    public List<SysUserVO> getList() {
        return DozerUtils.mapList(modelHandler.getList(), SysUserVO.class);
    }

    /**
     * 设置员工相关信息
     * @param models
     * @return
     */
    public List<SysUserVO> setRoleIds(List<SysUserModel> models){
        List<SysUserVO> list = new ArrayList<>();
        if (models.size() > 0) {
            //获取员工ID集合
            List<Long> userIds = new ArrayList<>();
            List<Long> orgIds = new ArrayList<>();

            for (SysUserModel model : models) {
                userIds.add(model.getId());
                if(model.getOrgId() != null && !orgIds.contains(model.getOrgId())){
                    orgIds.add(model.getOrgId());
                }
            }
            Map<Long, List<Long>> roleMaps = sysUserRoleHandler.getByUserIds(userIds);
            Map<Long, String> orgMaps = new HashMap<>();
            if(orgIds.size() > 0){
                orgMaps = sysOrgHandler.getOrgNameByIds(orgIds);
            }

            //赋值角色及组织
            list = DozerUtils.mapList(models, SysUserVO.class);
            for (SysUserVO vo : list) {
                if (roleMaps.containsKey(vo.getId())) {
                    vo.setRoleIdList(roleMaps.get(vo.getId()));
                }
                if(orgMaps.containsKey(vo.getOrgId())){
                    vo.setOrgName(orgMaps.get(vo.getOrgId()));
                }
            }
        }
        return list;
    }


    /**
     * 保存数据
     *
     * @param vo
     * @return
     */
    public long save(SysUserVO vo) {
        SysUserModel model = DozerUtils.map(vo, SysUserModel.class);
        model.setSuperAdmin(SuperAdminEnum.NO.getValue());

        // 判断用户名是否存在
        SysUserModel user = modelHandler.getByUsername(model.getUsername());
        if (user != null) {
            throw new ServerException("用户名已经存在");
        }

        // 判断手机号是否存在
        user = modelHandler.getByMobile(model.getMobile());
        if (user != null) {
            throw new ServerException("手机号已经存在");
        }

        //保存用户
        Long exCount = modelHandler.save(model);
        //用户角色关系
        sysUserRoleHandler.saveOrUpdate(model.getId(), vo.getRoleIdList());
        return exCount;
    }

    /**
     * 更新数据
     *
     * @param vo
     * @return
     */
    public long update(SysUserVO vo) {

        //判断用户名是否存在
        SysUserModel user = modelHandler.getByUsername(vo.getUsername());
        if (user != null && !user.getId().equals(vo.getId())) {
            throw new ServerException("用户名已经存在");
        }

        //判断手机号是否存在
        user = modelHandler.getByMobile(vo.getMobile());
        if (user != null && !user.getId().equals(vo.getId())) {
            throw new ServerException("手机号已经存在");
        }

        SysUserModel model = DozerUtils.map(vo, SysUserModel.class);
        //更新用户
        Long exCount = modelHandler.update(model);
        //更新用户角色关系
        sysUserRoleHandler.saveOrUpdate(model.getId(), vo.getRoleIdList());
        return exCount;
    }

    /**
     * 修改密码
     *
     * @param id
     * @param newPassword
     * @return
     */
    public long updatePassword(Long id, String newPassword) {
        SysUserModel model = modelHandler.getById(id);
        model.setPassword(newPassword);
        return modelHandler.updatePassword(model);
    }

    /**
     * 根据ID删除
     *
     * @param id
     * @return
     */
    public long delete(Long id) {
        //删除用户
        Long exCount = modelHandler.delete(id);

        //删除用户角色关系
        sysUserRoleHandler.deleteByUserIdsAndRoleId(id);
        return exCount;
    }


    /**
     * 根据ID获取用户信息
     * @param id
     * @return
     */
    public SysUserVO getById(Long id){
        SysUserModel model = modelHandler.getById(id);
        if(model != null){
            List<SysUserModel> list = new ArrayList<>();
            list.add(model);
            return setRoleIds(list).get(0);
        }
        return null;

    }
}
