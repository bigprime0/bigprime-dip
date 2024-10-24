import { ref } from 'vue'
import { find, isEmpty } from 'lodash-es'
import { v4 as uuidv4 } from 'uuid'

/**
 * 分页配置
 */
export const formPage = ref({
  total: 0,
  page: 1,
  limit: 10,
  pageSizes: [10, 15, 20, 50, 100],
  align: 'right',// 可选值：['left', 'center', 'right']
  layout: 'total, prev, pager, next, jumper, sizes'
})

/**
 * 列表选项
 */
export interface SelectModel {
  value: string,
  describe: string
}

export interface BaseModel {
  id: number,
  creator: number,
  creatorName: string,
  createTime: Date,
  updater: number,
  updaterName: string,
  updateTime: Date
}

export const checkNum = (value: any, minLimit: any, maxLimit: any) => {
  // 检查必须为整数
  value = Math.floor(value)
  if (value < minLimit) {
    value = minLimit
  } else if (value > maxLimit) {
    value = maxLimit
  }
  return value
}

export const findDescribe = (data: any, value: any) => {
  if(data && value){
    const e = find(data, function(o) {
      return o.value === value
    })
    if (!isEmpty(e)) {
      return e.describe
    }
  }
  return ''
}

export const validForm = (formRef: any) => {
  return new Promise((resolve) => {
    formRef.value.validate((valid: any) => {
      resolve(valid)
    })
  })
}

export const createUuid = () => {
  return uuidv4()
}

export const findValue = (data: any, value: any, valueKey: any, describeKey: any) => {
  const e = find(data, function(o : any) {
    return o[valueKey] === value
  })
  if (!isEmpty(e)) {
    return e[describeKey]
  }
  return ''
}

