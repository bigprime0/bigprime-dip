import request from '@/utils/request'

export interface integrationCategoryInfo {
  id: number,
  pid: number,
  label: string,
  route: string,
  isLeaf: number,
  isAdd: number,
  category: string,
  children: integrationCategoryInfo[],
}

export interface integrationGlobalConfig {
  category: string,
  configKey: string,
  configValue: string,
  description: string,
  compType: string,
  compConfig: string,
  defaultValue: string
}

export interface integrationConfigDetail {
  id: number,
  product: string,
  category: string,
  subCategory: string,
  configGroup: string,
  configCode: string,
  configKey: string,
  configTitle: string,
  version: string,
  seq: number,
  cnDescription: string,
  enDescription: string,
  isVisible: number,
  isProperty: number,
  isEnable: number,
  compType: string,
  compConfig: string,
  compConfigFmt: any,
  defaultValue: string
}

export interface integrationJob {
  id: number,
  engine: string,
  type:number,
  jobName: string,
  url?: string,
  formDatas: string,
  content: string,
  creator?: string,
  createTime?: Date,
  updater?: number,
  updateTime?: Date,
  writeType:string,
  writeSourceId:number,
  writeSourceDatabase:string,
  writeSourceTable:string
  readerType:string,
  readerSourceId: number,
  readerSourceDatabase:string,
  readerSourceTable:string,
  analyzeSql:string
}

export interface integrationJobLog {
  jobDefinedId: number,
  engine: string,
  jobId: string,
  jobName: string,
  formDatas: string,
  content: string,
  status: string,
  message: string,
  logs: string,
  startTime: Date,
  endTime: Date,
}

export namespace IntegrationConfigService {

  export const getTreeList = async () => {
    const res = await request.post('/bigprime-data/integration/config/category/tree')
    return res.data as integrationCategoryInfo[]
  }

  export const saveCategory = async (param: integrationCategoryInfo) => {
    const res = await request.post('/bigprime-data/integration/config/category/save', param)
    return res.data as number
  }

  export const deleteCategory = async (id: number) => {
    const res = await request.delete('/bigprime-data/integration/config/category/' + id)
    return res.data as number
  }

  export const getDetailPageList = async (params: any) => {
    const res = await request.post('/bigprime-data/integration/config/detail/page', params)
    return res.data as integrationConfigDetail[]
  }

  export const getDetailList = async () => {
    const res = await request.get('/bigprime-data/integration/config/detail/list')
    return res.data as integrationConfigDetail[]
  }

  export const saveDetail = async (param: integrationConfigDetail) => {
    const res = await request.post('/bigprime-data/integration/config/detail/save', param)
    return res.data as boolean
  }

  export const updateDetail = async (param: integrationConfigDetail) => {
    const res = await request.put('/bigprime-data/integration/config/detail/update', param)
    return res.data as boolean
  }

  export const deleteDetail = async (id: number) => {
    const res = await request.delete(`/bigprime-data/integration/config/detail/${id}`)
    return res.data as boolean
  }

  export const getGlobalConfigList = async () => {
    const res = await request.get('/bigprime-data/integration/config/global/list')
    return res.data as integrationGlobalConfig[]
  }

  export const saveGlobalConfig = async (param: integrationGlobalConfig[]) => {
    const res = await request.post('/bigprime-data/integration/global/config/save', param)
    return res.data as number
  }

  export const getJobPageList = async (params: any) => {
    const res = await request.post('/bigprime-data/integration/job/page', params)
    return res.data as integrationJob[]
  }

  export const saveJob = async (params: integrationJob) => {
    const res = await request.post('/bigprime-data/integration/job/save', params)
    return res.data as boolean
  }

  export const deleteJob = async (id: number) => {
    const res = await request.delete(`/bigprime-data/integration/job/${id}`)
    return res.data as boolean
  }

  export const submitJob = async (params: integrationJob) => {
    const res = await request.post('/bigprime-data/integration/job/submit', params)
    return res.data as any
  }

  export const getJobLogPageList = async (params: any) => {
    const res = await request.post('/bigprime-data/integration/job/log/page', params)
    return res.data as integrationJobLog[]
  }

  export const getMasterDataWriter = async (params: any) => {
    const res = await request.post('/bigprime-data/integration/master/writer', params)
    return res.data as {}
  }

  export const saveAndSubmitJob = async (params: integrationJob) => {
    const res = await request.post('/bigprime-data/integration/job/save-submit', params)
    return res.data as any
  }
}
