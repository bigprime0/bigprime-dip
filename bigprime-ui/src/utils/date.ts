import dayjs from 'dayjs'
import { isEmpty } from 'lodash-es'

export const addTimeToDate = (date:Date,hours: number, minutes: number, seconds: number) => {
  const millisecondsToAdd = (hours * 60 * 60 * 1000) + (minutes * 60 * 1000) + (seconds * 1000);
  return dayjs(date).add(millisecondsToAdd)
}

export const getNowDateTime = () => {
  return dayjs().format('YYYY-MM-DD HH:mm:ss')
}

export const getNowDate = () => {
  return dayjs().format('YYYY-MM-DD')
}

export const formatDateTime = (date: any) => {
  if(date){
    return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
  }
  return ''
}

export const formatDate = (date: any) => {
  if(date){
    return dayjs(date).format('YYYY-MM-DD')
  }
  return ''
}


export const getDateYear = (date:Date) => {
  return dayjs(date).year()
}
