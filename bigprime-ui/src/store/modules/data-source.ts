import { defineStore } from 'pinia'
import { DataSourceService } from '@/services/warehouse/data-source'
import { isEmpty, cloneDeep } from 'lodash-es'
import type { SourceParam } from '@/services/spi/source'

export interface DataSourceInfo {
  id: number,   //数据源标识
  product: string, //产品
  productType: string, //类型
  name: string, //源名称
  summary: string,  //描述
  config: any,  //配置项
  source:SourceParam,
  headUrl:string,
  active:boolean
}

//数据源类型
export interface DsTypes {
  value: string, //类型，如JDBC,NoSql等
  describe: string //类型描述
}

//具体数据源信息
export interface DsSelectType {
  label: string, //产品描述，如mysql数据库
  value: string, //产品唯一标识，如Mysql
  ctg: string, //产品所属类型，如JDBC
  url: string //产品链接模板
}

//JDBC链接信息
export interface JdbcParam {
  url: string;   //url地址
  userName: string;   //用户名
  passWord: string;    //密码
  schema: string;   //schema名称
}

export const useDataSourceStore = defineStore('dataSource', {
  actions: {
    //获取数据源列表
    async getDsList(search: string) {
      if (isEmpty(search)) {
        search = ''
      }
      const res = await DataSourceService.getList({ search: search })
      return res.data as DataSourceInfo[]
    },

    //链接测试
    async testConnection(data: DataSourceInfo) {
      const res = await DataSourceService.testConnection(data)
      return res.data
    },

    //保存数据
    async save(data: DataSourceInfo) {
      const res = await DataSourceService.save(data)
      return res.data
    },

    //删除数据
    async deleteById(id: number) {
      const res = await DataSourceService.deleteById(id)
      return res.data
    }
  }
})

