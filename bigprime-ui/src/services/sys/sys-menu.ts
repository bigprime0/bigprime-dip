import request from '@/utils/request'

/**
 * 菜单字段信息
 */
export interface MenuInfo {
  id: number,//标识ID
  pid: number,//父级菜单标识ID
  name: string,//菜单名称标识
  nameDes: string,
  url: string,//菜单Url
  icon: string,//菜单icon
  authority: string,//菜单授权标识
  sort: number,//菜单排序
  children: MenuInfo[],//子集集合
  desConfig: {
    page: string,
    routing: string,
    multilingual: string
  }
}

/**
 * 菜单管理
 */
export namespace SysMenuService {
  /**
   * 菜单列表
   */
  export const getMenus = async () => {
    const res = await request.get('/bigprime-data/menu/get-menus')
    return res.data as MenuInfo[]
  }

  /**
   * 保存菜单
   * @param data
   */
  export const save = async (data: MenuInfo) => {
    if (data.id) {
      const result = await request.put('/bigprime-data/menu', data)
      return result.data as boolean
    } else {
      const result = await request.post('/bigprime-data/menu', data)
      return result.data as boolean
    }
  }

  /**
   * 删除菜单
   * @param id
   */
  export const deleteById = async (id: number) => {
    const result = await request.delete('/bigprime-data/menu/' + id)
    return result.data as boolean
  }
}




