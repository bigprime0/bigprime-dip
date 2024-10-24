import request from '@/utils/request'


/**
 * 角色信息
 */
export interface RoleInfo {
  id: number,
  name: string,
  remark: string,
  menuIdList: number[]
}

/**
 * 角色管理
 */
export namespace SysRoleService {
  /**
   * 角色列表
   */
  export const getPage = async (param: {}) => {
    const res = await request.post('/bigprime-data/role/page', param)
    return { total: res.data?.total, list: res.data?.list }
  }

  /**
   * 保存角色
   * @param data
   */
  export const save = async (data: RoleInfo) => {
    if (data.id) {
      const result = await request.put('/bigprime-data/role', data)
      return result.data as boolean
    } else {
      const result = await request.post('/bigprime-data/role', data)
      return result.data as boolean
    }
  }

  /**
   * 删除角色
   * @param id
   */
  export const deleteById = async (id: number) => {
    const result = await request.delete('/bigprime-data/role/' + id)
    return result.data as boolean
  }

  export const all = async () => {
    const res = await request.get('/bigprime-data/role/all')
    return res.data as RoleInfo[]
  }
}




