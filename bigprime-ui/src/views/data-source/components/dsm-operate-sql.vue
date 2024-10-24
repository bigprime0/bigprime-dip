<template>
  <div class="operate-tabs">
    <Breadcrumb v-if="!isEmpty(tableName)" :class="tableData.type === 'DATABASE' ? 'svg-db':'svg-table'"
                :items="[tableName]"
                :IconLeft="tableData.type === 'DATABASE' ? IconDataSource : IconRichTextTable" />
    <tiny-link class="link-exec" :underline="false" :icon="iconDeltaRight()"
               :title="$t('datasource.execute.beg')"
               @click="searchData"> {{ $t('datasource.execute.run') }}
    </tiny-link>

    <tiny-link v-if="time > 0" class="link-time" :underline="false" :icon="iconTime()"
               :title="$t('datasource.execute.time')">
      {{ time }} ms
    </tiny-link>

    <monacoEditor style="margin-top: 10px" height="215px"
                  v-model="sql"
                  language="sql">
    </monacoEditor>

    <tiny-grid style="margin-top: 10px" :data="data" :fit="false" size="medium" auto-resize border
               column-width="150"
               :stripe="true"
               max-height="55%"
               show-header-overflow="tooltip" show-overflow="tooltip"
               highlight-hover-row>
      <tiny-grid-column v-for="item in columnHeaders" :key="item"
                        :field="item"
                        :title="item" />
    </tiny-grid>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { iconDataSource, iconDeltaRight, iconRichTextTable, iconTime } from '@opentiny/vue-icon'
import { DmlType, SourceService, type TableParam } from '@/services/spi/source'
import { includes, isEmpty, orderBy } from 'lodash-es'
import { Modal } from '@opentiny/vue'

const IconDataSource = iconDataSource()
const IconRichTextTable = iconRichTextTable()
const tableData = defineModel('tableData', { default: {} as TableParam })
const dataBaseId = defineModel('dataBaseId', { default: 0 })
const tableName = defineModel('tableName', { default: '' })

const columnHeaders = ref()
const sql = ref()
const data = ref()
const time = ref(0)
const columns = ref<any[]>([])

const searchData = async () => {
  let query: {}
  if (isEmpty(sql.value)) {
    query = {
      databaseId: dataBaseId.value,
      dmlConfig: {
        type: DmlType.select,
        tableName: tableName.value,
        limit: 10,
        offset: 1
      }
    }
  } else {
    query = {
      databaseId: dataBaseId.value,
      sql: sql.value
    }
  }
  const result = await SourceService.execute(query)
  columnHeaders.value = result.headers
  data.value = result.columns
  time.value = result.processor.elapsed
  if (isEmpty(sql.value)) {
    sql.value = result.content
  }
  if (!result.isSuccessful) {
    Modal.message({
      message: result.message,
      status: 'error'
    })
  }
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
  columns.value = []
  columnHeaders.value = []
  data.value = []
  sql.value = ''
  time.value = 0
}

const checkQuery = () => {
  return !isEmpty(tableName.value) && tableData.value.type !== 'DATABASE' && tableData.value.type !== 'OTHER'
}

watch(tableName, async () => {
  initDataBaseParam()
  if (checkQuery()) {
    await searchColumn()
  }
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
}

.svg-db {
  fill: #1ac456;
}

.svg-table {
  fill: #1a78c4;
}
</style>
