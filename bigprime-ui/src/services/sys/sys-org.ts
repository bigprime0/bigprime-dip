import request from '@/utils/request'

/**
 * 组织信息
 */
export interface OrgInfo {
  id: number,//标识ID
  pid: number,//父级组织ID
  name: string,//组织名称
  sort: number,//排序
  parentName: string, //父级组织名称
  children: OrgInfo[]//子集集合
}

/**
 * 组织管理
 */
export namespace SysOrgService {
  /**
   * 组织列表
   */
  export const getList = async () => {
    const res = await request.get('/bigprime-data/org/get-list')
    return res.data as OrgInfo[]
  }

  export const getAll = async () => {
    const res = await request.get('/bigprime-data/org/get-all')
    return res.data as OrgInfo[]
  }

  /**
   * 保存组织
   * @param data
   */
  export const save = async (data: OrgInfo) => {
    if (data.id) {
      const result = await request.put('/bigprime-data/org', data)
      return result.data as boolean
    } else {
      const result = await request.post('/bigprime-data/org', data)
      return result.data as boolean
    }
  }

  /**
   * 删除组织
   * @param id
   */
  export const deleteById = async (id: number) => {
    const result = await request.delete('/bigprime-data/org/' + id)
    return result.data as boolean
  }
}




