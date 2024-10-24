import request from '@/utils/request'


// 数据库信息api
// 获取表名
export function getTables(params: {}) {
  return request({
    url: '/api/metadata/getTables',
    method: 'get',
    params
  })
}

// 获取schema
export function getTableSchema(params: {}) {
  return request({
    url: '/api/metadata/getDBSchema',
    method: 'get',
    params
  })
}

// 获取字段
export function getColumns(params: {}) {
  return request({
    url: '/api/metadata/getColumns',
    method: 'get',
    params
  })
}

// 根据sql获取字段
export function getColumnsByQuerySql(params: {}) {
  return request({
    url: '/api/metadata/getColumnsByQuerySql',
    method: 'get',
    params
  })
}

// 根据datasourceID、tablename创建表【目标端】
export function createTable(params: {}) {
  return request({
    url: '/api/metadata/createTable',
    method: 'post',
    params
  })
}

// 判断字段是否存在，存在，即更新值，否则添加字段
export function updateColumnsValue(query: {}) {
  return request({
    url: '/api/metadata/updateColumnsValue',
    method: 'post',
    data: query
  })
}

// datax插件api

export function list(params: {}) {
  return request({
    url: '/api/jobJdbcDatasource',
    method: 'get',
    params
  })
}

export function fetched(params: {}) {
  return request({
    url: '/api/jobJdbcDatasource/' + params,
    method: 'get'
  })
}

export function updated(data: {}) {
  return request({
    url: '/api/jobJdbcDatasource',
    method: 'put',
    data
  })
}

export function created(data: {}) {
  return request({
    url: '/api/jobJdbcDatasource',
    method: 'post',
    data
  })
}

export function deleted(data: {}) {
  return request({
    url: '/api/jobJdbcDatasource',
    method: 'delete',
    params: data
  })
}

export function test(data: {}) {
  return request({
    url: '/api/jobJdbcDatasource/test',
    method: 'post',
    data
  })
}

export function getDataSourceList(params: {}) {
  return request({
    url: '/api/jobJdbcDatasource/all',
    method: 'get',
    params
  })
}

