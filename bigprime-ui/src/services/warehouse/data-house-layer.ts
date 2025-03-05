import request from '@/utils/request'

export interface StructValueInfo {
  tableStruct: any[],
  columnsStruct: any[],
  indexesStruct: any[]
}

export interface TableValueInfo {
  label: string,
  databaseId: number
}

/**
 * 数仓管理-数仓维护
 */
export namespace DataHouseLayerService {
  export const getHouseTree = async () => {
    const result = await request.get('/bigprime-data/layer/get-house-tree')
    return result.data
  }

  /**
   * 获取层级信息
   * @param param
   */
  export const getLayerTree = async () => {
    const result = await request.get('/bigprime-data/layer/get-layer-tree')
    return result.data
  }

  /**
   * 获取可用的数据库列表(过滤已绑定过的)
   * @param param
   */
  export const getSources = async () => {
    const result = await request.get('/bigprime-data/layer/get-sources')
    return result.data
  }

  /**
   * 绑定数据源
   * @param param
   */
  export const bindLayerSource = async (param: {}) => {
    const result = await request.post('/bigprime-data/layer/bind-layer-source', param)
    return result.data as any
  }

  /**
   * 根据绑定的数据库ID删除绑定关系
   * @param id
   */
  export const deleteById = async (id: number) => {
    const result = await request.get('/bigprime-data/layer/delete/' + id)
    return result.data as boolean
  }
}




