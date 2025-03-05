<template>
  <div v-if="checkQuery()" class="div-breadcrumb">
    <Breadcrumb class="svg-db" :items="[dataBaseName]" :IconLeft="IconDataSource" />
  </div>
  <div v-if="checkQuery()" class="operate-tabs">
    <tiny-link class="link-table" :underline="false" :icon="iconRichTextTable()"
               :title="$t('datasource.structure.name')"> {{ tableData.name }}
    </tiny-link>
    <tiny-link v-if="!isEmpty(tableData.comment)" class="link-table" :underline="false" :icon="iconDialog2()"
               :title="$t('datasource.structure.comment')"> {{ tableData.comment }}
    </tiny-link>

    <tiny-popover width="800"
                  trigger="click">
      <monacoEditor v-model="createSql" style="margin-top: 10px" height="500px"
                    language="sql">
      </monacoEditor>
      <template #reference>
        <tiny-link class="link-show" :underline="false" :icon="iconSearch()"
                   :title="$t('datasource.structure.show.sql')"
                   @click="showSql"> {{ $t('datasource.structure.show.sql') }}
        </tiny-link>
      </template>
    </tiny-popover>
    <br />

    <tiny-link v-if="!isEmpty(tableData.createTime)" class="link-table" :underline="false" :icon="iconTime()"
               :title="$t('datasource.structure.createTime')"> {{ tableData.createTime }}
    </tiny-link>
    <tiny-link v-if="!isEmpty(tableData.engine)" class="link-table" :underline="false" :icon="iconEditorTable()"
               :title="$t('datasource.structure.engine')"> {{ tableData.engine }}
    </tiny-link>

    <tiny-link v-if="!isEmpty(tableData.collation)" class="link-table" :underline="false" :icon="iconFontFamily()"
               :title="$t('datasource.structure.collation')"> {{ tableData.collation }}
    </tiny-link>
    <tiny-link v-if="tableData.avgRowLength" class="link-table" :underline="false" :icon="iconFontStyle()"
               :title="$t('datasource.structure.avgRowLength')"> {{ tableData.avgRowLength }}
    </tiny-link>
    <tiny-link v-if="tableData.dataLength" class="link-table" :underline="false" :icon="iconFontSize()"
               :title="$t('datasource.structure.dataLength')"> {{ tableData.dataLength }}
    </tiny-link>

    <tiny-link v-if="!isEmpty(tableData.format)" class="link-table" :underline="false" :icon="iconGroupTransfer()"
               :title="$t('datasource.structure.format')"> {{ tableData.format }}
    </tiny-link>
    <tiny-link v-if="tableData.indexLength" class="link-table" :underline="false" :icon="iconLineHeight()"
               :title="$t('datasource.structure.indexLength')"> {{ tableData.indexLength }}
    </tiny-link>
    <br />

    <tiny-grid style="margin-top: 10px;max-height: calc(100vh - 280px);overflow: auto;" :data="tableData.columns" :fit="true"
               size="medium" auto-resize border :stripe="true" show-header-overflow="tooltip" show-overflow="tooltip"
               highlight-hover-row>
      <tiny-grid-column field="column" :title="$t('datasource.structure.name')" />
      <tiny-grid-column field="dataType" :title="$t('datasource.structure.data.type')" />
      <tiny-grid-column field="precision" :title="$t('datasource.structure.precision')" />
      <tiny-grid-column field="scale" :title="$t('datasource.structure.scale')" />
      <tiny-grid-column field="isNullable" :title="$t('datasource.structure.is.nullable')">
        <template v-slot="data">
          <tiny-switch v-model="data.row.isNullable" :true-value=true :false-value=false disabled></tiny-switch>
        </template>
      </tiny-grid-column>
      <tiny-grid-column field="isKey" :title="$t('datasource.structure.is.key')">
        <template v-slot="data">
          <tiny-switch v-model="data.row.isKey" :true-value=true :false-value=false disabled></tiny-switch>
        </template>
      </tiny-grid-column>

      <tiny-grid-column field="defaultValue" :title="$t('datasource.structure.default.value')" />
      <tiny-grid-column field="comment" :title="$t('datasource.structure.comment')" />
      <tiny-grid-column field="extra" :title="$t('datasource.structure.extra')" />
    </tiny-grid>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import {
  iconDataSource,
  iconDialog2,
  iconEditorTable,
  iconFontFamily,
  iconFontSize,
  iconFontStyle,
  iconGroupTransfer,
  iconLineHeight,
  iconRichTextTable,
  iconSearch,
  iconTime
} from '@opentiny/vue-icon'
import { SourceService, type TableParam } from '@/services/spi/source'
import { includes, isEmpty, orderBy } from 'lodash-es'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const IconDataSource = iconDataSource()

const tableData = defineModel('tableData', { default: {} as TableParam })
const dataBaseId = defineModel('dataBaseId', { default: 0 })
const tableName = defineModel('tableName', { default: '' })
const dataBaseName = defineModel('dataBaseName', { default: '' })

const columns = ref<any[]>([])
const createSql = ref()
const showSql = async () => {
  createSql.value = await SourceService.getCreateStatement(dataBaseId.value, tableName.value)
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
}

const checkQuery = () => {
  return !isEmpty(tableName.value) && tableData.value.type !== 'DATABASE' && tableData.value.type !== 'OTHER'
}

watch(tableName, async () => {
  createSql.value = ''
  columns.value = []
  if (checkQuery()) {
    await searchColumn()
  }
})
</script>

<style scoped lang="less">
.operate-tabs {
  margin: 10px;

  .link-table {
    margin: 10px 25px 10px 0;

    :deep(.tiny-link__inner) {
      font-size: 13px;
    }

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1em;
      --ti-link-svg-height: 1em;
      margin-right: 5px;
    }
  }

  .link-show {
    margin: 10px 25px 10px 0;

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1em;
      --ti-link-svg-height: 1em;
      fill: #1ac456;
      margin-right: 5px;
    }

    :deep(.tiny-link__inner) {
      font-size: 13px;
      color: #1ac456;
    }
  }
}

.svg-db {
  fill: #1ac456;
}

.div-breadcrumb {
  :deep(.tiny-breadcrumb .tiny-breadcrumb__item:last-child .tiny-breadcrumb__inner) {
    font-weight: 500;
    color: #2c3e50;
    text-decoration: none;
  }

  :deep(.container-breadcrumb) {
    font-size: 13px;
    margin: 10px 0 0 10px;
  }
}
</style>
