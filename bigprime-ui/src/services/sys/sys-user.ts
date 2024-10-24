import request from '@/utils/request'


/**
 * 用户信息
 */
export interface UserInfo {
  id: number,
  username: string,
  realName: string,
  avatar: string,
  avatarUrl: string,
  gender: number,
  email: string,
  mobile: string,
  orgId: number,
  superAdmin: number,
  status: number,
  roleIdList: number[],
  orgName: string,
  createTime: string,
  dinkyUrl: string
}

export interface UserPassword {
  password: string,
  newPassword: string
}

/**
 * 用户管理
 */
export namespace SysUserService {
  /**
   * 组织列表
   */
  export const getPage = async (param: {}) => {
    const res = await request.post('/bigprime-data/user/page', param)
    return { total: res.data?.total, list: res.data?.list }
  }

  export const getList = async () => {
    const result = await request.get('/bigprime-data/user/list')
    return result.data as UserInfo[]
  }

  /**
   * 保存组织
   * @param data
   */
  export const save = async (data: UserInfo) => {
    if (data.id) {
      const result = await request.put('/bigprime-data/user', data)
      return result.data as boolean
    } else {
      const result = await request.post('/bigprime-data/user', data)
      return result.data as boolean
    }
  }
  /**
   * 修改密码
   * @param data
   */
  export const password = async (data: UserPassword) => {
    const result = await request.put('/bigprime-data/user/password', data)
    return result.data as boolean
  }


  /**
   * 删除组织
   * @param id
   */
  export const deleteById = async (id: number) => {
    const result = await request.delete('/bigprime-data/user/' + id)
    return result.data as boolean
  }

  /**
   * 根据ID获取员工数据
   * @param id
   */
  export const getById = async (id: number) => {
    const result = await request.get('/bigprime-data/user/' + id)
    return result.data as UserInfo
  }

  /**
   * 登录
   * @param id
   */
  export const login = async (param: {}) => {
    return request.post('/bigprime-data/auth/login', param)
  }
}




