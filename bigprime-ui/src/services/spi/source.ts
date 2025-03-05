import request from '@/utils/request'
import { includes, isEmpty, split } from 'lodash-es'
import { ref } from 'vue'

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
    select = 'SELECT_STATEMENT',
    insert = 'INSERT_STATEMENT',
    update = 'UPDATE_STATEMENT',
    delete = 'DELETE_STATEMENT',
    truncate = 'TRUNCATE_STATEMENT'
}

export const columnType = [
    'varchar',
    'bigint',
    'int',
    'date',
    'tinyint',
    'double',
    'smallint',
    'decimal',
    'time',
    'timestamp'
]

export const operatorData = [
    { value: 'EQ', label: '=' },
    { value: 'NEQ', label: '<>' },
    { value: 'GT', label: '>' },
    { value: 'GTE', label: '>=' },
    { value: 'LT', label: '<' },
    { value: 'LTE', label: '<=' },
    { value: 'LIKE', label: 'LIKE' },
    { value: 'NLIKE', label: 'NOT LIKE' },
    { value: 'NULL', label: 'IS NULL' },
    { value: 'NNULL', label: 'IS NOT NULL' }
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

    export const getTables = async (id: any, isCascade:Boolean) => {
        return executeDdl({ type: 'getTables', databaseId: id, isCascade: isCascade })
    }

    export const getTable = async (id: number, tableName: string) => {
        return executeDdl({ type: 'getTable', databaseId: id, name: tableName })
    }

    export const execute = async (data: any) => {
        const result = await request.post('/bigprime-data/source/execute', data)
        return result.data as ResponseParam
    }

    export const getDatabases = async (id: any) => {
        return executeDdl({ type: 'getDatabases', databaseId: id })
    }

    export const getCreateStatement = async (id: any, tableName: string) => {
        return executeDdl({ type: 'getCreateStatement', databaseId: id, name: tableName })
    }

    export const getViews = async (id: any) => {
        return executeDdl({ type: 'getViews', databaseId: id })
    }

    export const getView = async (id: any, viewName: string) => {
        return executeDdl({ type: 'getView', databaseId: id, name: viewName })
    }

    export const getFunctions = async (id: any) => {
        return executeDdl({ type: 'getFunctions', databaseId: id })
    }

    export const getFunction = async (id: any, functionName: string) => {
        return executeDdl({ type: 'getFunction', databaseId: id, name: functionName })
    }

    export const createTable = async (id: any, tableModel: {}) => {
        return executeDdl({ type: 'createTable', databaseId: id, model: tableModel })
    }

    export const alterTable = async (id: any, tableModel: {}) => {
        return executeDdl({ type: 'alterTable', databaseId: id, model: tableModel })
    }

    export const createColumn = async (id: any, tableName: string, columnModel: {}) => {
        return executeDdl({ type: 'createColumn', databaseId: id, name: tableName, columnModel: columnModel })
    }

    export const alterColumn = async (id: any, tableName: string, columnModel: {}) => {
        return executeDdl({ type: 'alterColumn', databaseId: id, name: tableName, columnModel: columnModel })
    }

    export const dropTable = async (id: any, tableName: string) => {
        return executeDdl({ type: 'dropTable', databaseId: id, name: tableName })
    }
    export const dropColumn = async (id: any, tableName: string, columnName: string) => {
        return executeDdl({ type: 'dropColumn', databaseId: id, name: tableName, columnName:columnName })
    }

    export const executeDdl = async (data: any) => {
        const result = await request.post('/bigprime-data/source/execute-ddl', data)
        return result.data
    }
}
