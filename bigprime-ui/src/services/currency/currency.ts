import request from '@/utils/request'

/**
 * 通用接口管理
 */
export namespace CurrencyService {

    export const getHomeStatistic = async () => {
        const res = await request.get('/bigprime-data/currency/get-home-statistic')
        return res.data
    }
}


