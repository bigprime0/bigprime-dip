<template>
  <div class="operate-tabs">
    <Breadcrumb v-if="!isEmpty(tableName)" :class="tableData.type === 'DATABASE' ? 'svg-db':'svg-table'"
                :items="[tableName]"
                :IconLeft="tableData.type === 'DATABASE' ? IconDataSource : IconRichTextTable" />
    <tiny-link class="link-exec" :underline="false" :icon="iconDeltaRight()"
               :title="$t('datasource.execute.beg')"
               :disabled="!checkQuery()"
               @click="searchData"> {{ $t('datasource.execute.run') }}
    </tiny-link>

    <tiny-link v-if="time > 0" class="link-time" :underline="false" :icon="iconTime()"
               :title="$t('datasource.execute.time')">
      {{ time }} ms
    </tiny-link>

    <tiny-link class="link-add" @click="addCondition" :underline="false"
               :icon="iconNode()" :title="$t('datasource.execute.add.condition')"></tiny-link>
    <div class="div-condition">
      <div v-for="item in filters" :key="item.id">
        <tiny-select style="width: 200px;margin-top: 5px;" v-model="item.column" clearable>
          <tiny-option v-for="item in tableData.columns" :key="item.column" :label="item.column" :value="item.column">
          </tiny-option>
        </tiny-select>

        <tiny-select style="width: 100px;margin-left: 10px;" v-model="item.operator" clearable>
          <tiny-option v-for="item in operatorData" :key="item.value" :label="item.label" :value="item.value">
          </tiny-option>
        </tiny-select>

        <tiny-input style="width: 200px;margin-left: 10px;" v-model="item.value" clearable></tiny-input>

        <tiny-link class="link-delete" @click="deleteCondition(item.id)" :underline="false"
                   :icon="iconNodeOpen()"></tiny-link>
        <br />
      </div>
    </div>

    <tiny-grid style="margin-top: 10px" :data="data" :fit="false" size="medium" auto-resize border
               column-width="150"
               :stripe="true"
               :min-height="gridHeight"
               :max-height="gridHeight"
               show-header-overflow="tooltip" show-overflow="tooltip"
               highlight-hover-row>
      <tiny-grid-column v-for="item in columnHeaders" :key="item"
                        :field="item"
                        :title="item" />
    </tiny-grid>
    <tiny-pager
      :current-page="searchPage.page"
      :page-size="searchPage.limit"
      :total="searchPage.total"
      :page-sizes="searchPage.pageSizes"
      @current-change="searchPageChange"
      @size-change="searchLimitChange"
      :align="searchPage.align"
      :layout="searchPage.layout"
    ></tiny-pager>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { iconDataSource, iconDeltaRight, iconNode, iconNodeOpen, iconRichTextTable, iconTime } from '@opentiny/vue-icon'
import { DmlType, operatorData, SourceService, type TableParam } from '@/services/spi/source'
import { includes, isEmpty, orderBy, trim } from 'lodash-es'
import { Modal } from '@opentiny/vue'

const IconDataSource = iconDataSource()
const IconRichTextTable = iconRichTextTable()
const tableData = defineModel('tableData', { default: {} as TableParam })
const cryptoId = defineModel('cryptoId', { default: 0 })
const dataBaseId = defineModel('dataBaseId', { default: 0 })
const tableName = defineModel('tableName', { default: '' })

const columnHeaders = ref()
const data = ref()
const time = ref(0)
const filters = ref<any[]>([{
  id: 0,
  column: '',
  value: '',
  operator: ''
}])
const id = ref(0)
const gridHeight = ref()
const searchPage = ref({
  total: 0,
  page: 1,
  limit: 5,
  pageSizes: [5, 10, 20],
  align: 'right',
  layout: 'total, prev, pager, next, jumper, sizes'
})
const columns = ref<any[]>([])

const setHeight = () => {
  const minHeight = 55
  const maxHeight = 78
  const add = 4 * filters.value.length
  const height = maxHeight - add
  if (minHeight > height) {
    gridHeight.value = '55%'
  } else if (height > maxHeight) {
    gridHeight.value = '78%'
  } else {
    gridHeight.value = height + '%'
  }
}

const addCondition = () => {
  id.value += 1
  filters.value.unshift({
    id: id.value,
    column: '',
    value: '',
    operator: ''
  })
  setHeight()
}

const deleteCondition = (id: any) => {
  filters.value = filters.value.filter(item => item.id !== id)
  setHeight()
}

const searchData = async () => {
  let filterData = filters.value.filter(item => trim(item.column).length > 0 && trim(item.operator).length > 0 && trim(item.value).length > 0)
  let query = {
    databaseId: dataBaseId.value,
    cryptoId: cryptoId.value,
    cryptoColumns: tableData.value.cryptoColumns,
    dmlConfig: {
      type: DmlType.select,
      tableName: tableName.value,
      where: filterData,
      limit: searchPage.value.limit,
      offset: searchPage.value.page
    }
  }

  const result = await SourceService.execute(query)
  columnHeaders.value = result.headers
  data.value = result.columns
  searchPage.value.total = result.totalRecords
  time.value = result.processor.elapsed
  if (!result.isSuccessful) {
    Modal.message({
      message: result.message,
      status: 'error'
    })
  }
}

const searchPageChange = (page: number) => {
  searchPage.value.page = page
  searchData()
}

const searchLimitChange = (limit: number) => {
  searchPage.value.limit = limit
  searchData()
}

const searchColumn = async () => {
  let result = await SourceService.getTable(dataBaseId.value, tableName.value)
  let columnData: any[] = []
  result?.columns.forEach((item: any) => {
    if (!isEmpty(tableData.value.limitColumns)) {
      if (includes(tableData.value.limitColumns, item.column)) {
        columnData.push(item)
      }
    } else {
      columnData.push(item)

    }
  })
  tableData.value.columns = orderBy(columnData, ['position'], ['asc'])
  await searchData()
}

const initDataBaseParam = () => {
  filters.value = [{
    id: 0,
    column: '',
    value: '',
    operator: ''
  }]
  columns.value = []
  columnHeaders.value = []
  data.value = []
  searchPage.value.total = 0
  time.value = 0
}

const checkQuery = () => {
  return !isEmpty(tableName.value) && tableData.value.type !== 'DATABASE' && tableData.value.type !== 'OTHER'
}

watch(tableName, async () => {
  initDataBaseParam()
  if (checkQuery()) {
    searchPage.value.page = 1
    await searchColumn()
  }
  setHeight()
})
</script>

<style scoped lang="less">
.operate-tabs {
  margin: 10px;

  :deep(.tiny-breadcrumb .tiny-breadcrumb__item:last-child .tiny-breadcrumb__inner) {
    font-weight: 500;
    color: #2c3e50;
    text-decoration: none;
  }

  :deep(.container-breadcrumb) {
    font-size: 13px;
    margin: 10px 0 5px 0;
  }

  .link-exec {

    :deep(.tiny-link-svg) {
      fill: #1ac456;
    }

    :deep(.tiny-link__inner) {
      margin-left: 2px;
      color: #1ac456;
    }
  }

  .link-sql {
    :deep(.tiny-link-svg) {
      margin-left: 10px;
      fill: #f69970;
    }

    :deep(.tiny-link__inner) {
      margin-left: 2px;
      color: #f69970;
    }
  }

  .link-time {
    :deep(.tiny-link-svg) {
      margin-left: 10px;
      fill: #830b53;
    }

    :deep(.tiny-link__inner) {
      margin-left: 2px;
      color: #830b53;
    }
  }

  .link-add {
    margin-left: 10px;

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1.2em;
      --ti-link-svg-height: 1.2em;
      fill: #1ac456;
      margin-right: 3px;
    }

    :deep(.tiny-link__inner) {
      font-size: 13px;
      color: #1ac456;
    }
  }

  .link-delete {
    margin-left: 10px;

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1.2em;
      --ti-link-svg-height: 1.2em;
      fill: #ee0808;
      margin-right: 3px;
    }

    :deep(.tiny-link__inner) {
      font-size: 13px;
      color: #ee0808;
    }
  }

  .div-condition {
    max-height: 180px;
    overflow: auto;
  }
}

.svg-db {
  fill: #1ac456;
}

.svg-table {
  fill: #1a78c4;
}
</style>
