import request from '@/utils/request'

/**
 * 数据源服务
 */
export namespace DataSourceService {
  /**
   * 获取数据源类型
   */
  export const getTypes = async () => request.get('/bigprime-data/data-source/types')

  /**
   * 获取列表数据
   * @param param
   */
  export const getList = async (param: {}) => request.post('/bigprime-data/data-source/list', param)


  /**
   * 保存数据源
   * @param param
   */
  export const save = async (param: {}) => request.post('/bigprime-data/data-source/save', param)

  /**
   * 测试连接
   * @param param
   */
  export const testConnection = async (param: {}) => request.post('/bigprime-data/data-source/test-connection', param)

  /**
   * 删除数据源
   * @param id
   */
  export const deleteById = async (id: number) => request.get('/bigprime-data/data-source/delete/' + id)

}

/**
 * 数据源ddl管理
 */
export namespace DataSourceDDLService {

  /**
   * 根据数据库ID获取数据库表列表信息
   * @param id 数据库ID
   */
  export const getSourceTables = async (id: number) => {
    const result = await request.get('/bigprime-data/data-source/get-source-tables/' + id)
    return result.data;
  }

  /**
   * 根据数据库ID及表名获取表详情信息
   * @param id 数据库ID
   * @param tableName 表名
   */
  export const getSourceTableDetails = async (id: number, tableName: string) => {
    const result = await request.get('/bigprime-data/data-source/get-source-table-details/' + id + '/' + tableName)
    return result.data?.tableInfo
  }

  /**
   * 创建表
   * @param data 表信息
   */
  export const createTable = async (data: any) => {
    const result = await request.post('/bigprime-data/data-source/create-table', data)
    return result.data as boolean
  }

  /**
   * 保存表(包含列及索引的增删改)
   * @param data 表信息
   */
  export const saveTable = async (data: any) => {
    const result = await request.post('/bigprime-data/data-source/save-table', data)
    return result.data as boolean
  }

  export const getSourceTableStruct = async (id: number) => {
    const result = await request.get('/bigprime-data/data-source/get-source-table-struct/' + id)
    return result.data;
  }
}

/**
 * 数据源dml管理
 */
export namespace DataSourceDMLService {

  /**
   * 根据数据库ID及表名获取表Top100信息
   * @param id 数据库ID
   * @param tableName 表名
   */
  export const getTableData = async (id: number, tableName: string) => {
    const result = await request.get('/bigprime-data/data-source/get-table-data/' + id + '/' + tableName)
    return result.data
  }


  /**
   * 根据数据库ID及sql获取表Top100信息
   * @param data
   */
  export const getTableDataBySql = async (data: any) => {
    const result = await request.post('/bigprime-data/data-source/get-table-data-sql', data)
    return result.data
  }

  /**
   * 根据数据库ID及表名获取表分页信息
   * @param data
   */
  export const getTableDataByPage = async (data: any) => {
    const result = await request.post('/bigprime-data/data-source/get-table-data-page', data)
    return { total: result.data?.total, list: result.data?.list }
  }
}
