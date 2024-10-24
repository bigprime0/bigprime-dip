import request from '@/utils/request'

export interface SourceParam {
    description: string,
    type: string,
    protocol: string,
    host: string,
    port: number,
    username: string,
    password: string,
    jdbcDriver: string,
    jdbcType: string,
    catalog: string,
    database: string,
    ssl: boolean,
    publish: boolean,
    usedConfig: boolean,
    version: string,
    available: boolean,
    message: string,
    configures: {},
    configure: string,
    schema: {},
    pipelines: []
}

export interface TableParam {
    name: string,
    description: string,
    type: string,
    engine: string,
    format: string,
    rows: number,
    createTime: string,
    updateTime: string,
    collation: string,
    comment: string,
    avgRowLength: number,
    dataLength: number,
    indexLength: number,
    autoIncrement: number,
    source: SourceParam,
    columns: ColumnParam[],
    limitColumns: [],
    cryptoColumns: [],
    cryptoId: number
}

export interface ColumnParam {
    column: string,
    description: string,
    type: string,
    comment: string,
    defaultValue: string,
    position: number,
    isNullable: boolean,
    maximumLength: number,
    collation: string,
    isKey: boolean,
    privileges: string,
    dataType: string,
    extra: string,
    precision: number,
    scale: number
}

export interface ResponseParam {
    headers: [],
    columns: [],
    isConnected: boolean,
    isSuccessful: boolean,
    message: string,
    content: string,
    totalRecords: number,
    processor: TimeParam
    connection: TimeParam
}

export interface PaginationParam {
    pageSize: number,
    currentPage: number,
    totalRecords: number
}

export interface TimeParam {
    elapsed: number,
    end: number,
    start: number
}

export interface DataBaseParam {
    type: string,
    name: string,
    children: [],
    databaseId: number,
    id: number
}


export interface DmlParams {
    databaseId: number,
    cryptoId: number,
    cryptoColumns: string[],
    dmlConfig: DmlConfig
}

export interface DmlConfig {
    type: DmlType,
    tableName: string,
    columns: [],
    orders: [],
    where: [],
    limit: number,
    offset: number
}

export enum DmlType {
    select = 'SELECT_STATEMENT'
}



export const operatorData = [
    {value: 'EQ', label: '='},
    {value: 'NEQ', label: '<>'},
    {value: 'GT', label: '>'},
    {value: 'GTE', label: '>='},
    {value: 'LT', label: '<'},
    {value: 'LTE', label: '<='},
    {value: 'LIKE', label: 'LIKE'},
    {value: 'NLIKE', label: 'NOT LIKE'},
    {value: 'NULL', label: 'IS NULL'},
    {value: 'NNULL', label: 'IS NOT NULL'}
]
/**
 * 源管理
 */
export namespace SourceService {

    export const getPlugin = async () => {
        const result = await request.get('/bigprime-data/source/get-plugin')
        return result.data
    }

    export const getList = async (query: any) => {
        const result = await request.post('/bigprime-data/source/get-list', query)
        return result.data
    }

    export const getSourceTree = async (id: number) => {
        const result = await request.get('/bigprime-data/source/get-source-tree/' + id)
        return result.data as DataBaseParam
    }

    export const testConnection = async (data: any) => {
        const result = await request.post('/bigprime-data/source/test-connection', data)
        return result.data as boolean
    }

    export const getTable = async (id: number, tableName: string) => {
        const result = await request.get('/bigprime-data/source/get-table/' + id + '/' + tableName)
        return result.data
    }

    export const execute = async (data: any) => {
        const result = await request.post('/bigprime-data/source/execute', data)
        return result.data as ResponseParam
    }
}
