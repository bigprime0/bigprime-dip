<template>
  <div class="div-breadcrumb">
    <Breadcrumb v-if="!isEmpty(tableName)" class="svg-db" :items="[dataBaseName]" :IconLeft="IconDataSource" />
  </div>
  <div v-if="!isEmpty(tableName) && tableData.type !== 'DATABASE' && tableData.type !== 'OTHER'" class="operate-tabs">
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

    <tiny-link class="link-show" :underline="false" :icon="iconNode()"
               :title="$t('datasource.structure.add.column')"
               @click="editColumn('add', null)">{{ $t('datasource.structure.add.column') }}
    </tiny-link>
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
      <tiny-grid-column :title="$t('common.operations')" width="200"
                        align="center">
        <template v-slot="data">
          <tiny-link class="link-btn-edit" :underline="false" :icon="iconEdit()"
                     :title="$t('common.operations.update')"
                     @click="editColumn('update',data.row)">{{ $t('common.operations.update') }}
          </tiny-link>
          <tiny-link class="link-btn-del" :underline="false" :icon="iconNodeOpen()"
                     :title="$t('common.operations.delete')"
                     @click="deleteColumn(data.row)">{{ $t('common.operations.delete') }}
          </tiny-link>

        </template>
      </tiny-grid-column>
    </tiny-grid>
  </div>
  <div v-if="!isEmpty(tableName) && tableData.type === 'DATABASE'" class="operate-tabs">
    <tiny-form class="div-form" overflow-title ref="tableBodyRef" :model="tableBody" :rules="tableBodyRules">
      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :show-message="false" prop="name">
            <template #label>
              <tiny-link :underline="false" :icon="iconRichTextTable()"
                         :title="$t('datasource.structure.name')">
                {{ $t('datasource.structure.name') }}
              </tiny-link>
            </template>
            <tiny-input size="mini"
                        v-model="tableBody.name"
                        placeholder="bigprime_test"></tiny-input>

          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item>
            <template #label>
              <tiny-link :underline="false" :icon="iconDialog2()"
                         :title="$t('datasource.structure.comment')">
                {{ $t('datasource.structure.comment') }}
              </tiny-link>
            </template>
            <tiny-input size="mini"
                        v-model="tableBody.comment"
                        placeholder="enter a description"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>
    </tiny-form>

    <tiny-link v-if="false" class="link-custom-label" :underline="false" :icon="iconSettingData()"
               :title="$t('datasource.custom.divider')"> {{ $t('datasource.custom.divider') }}
    </tiny-link>
    <tiny-link v-if="false" class="link-custom-add" @click="addTableCustom('','')" :underline="false"
               :icon="iconNode()">
    </tiny-link>
    <div v-if="false" style="max-height: 150px;overflow: auto;">
      <div style="display: flex; margin: 5px 0 0 0;" v-for="item in tableCustomData" :key="item.id">
        <span class="custom-span-font">{{ $t('datasource.custom.attribute') }}</span>
        <tiny-input style="width: 20%;margin-left: 5px;" v-model="item.key"></tiny-input>
        <span class="custom-span-font">{{ $t('datasource.custom.value') }}</span>
        <tiny-input style="width: 30%;margin-left: 5px;" v-model="item.value"></tiny-input>
        <tiny-link class="link-custom-delete" @click="deleteTableCustom(item.id)" :underline="false"
                   :icon="iconNodeOpen()"></tiny-link>
        <br />
      </div>
    </div>
    <br v-if="false" />

    <tiny-popover :content="$t('datasource.create.grid.add')"
                  trigger="manual" v-model="gridVisible">
      <template #reference>
        <tiny-link class="link-add" style="margin-left: 0px;" :underline="false" :icon="iconNode()"
                   :title="$t('common.operations.add')"
                   :disabled="isDisabled"
                   @click="columnsEvent('add')">{{ $t('common.operations.add') }}
        </tiny-link>
      </template>
    </tiny-popover>
    <tiny-link class="link-delete" :disabled="isDisabled" :underline="false" :icon="iconNodeOpen()"
               :title="$t('common.operations.delete')"
               @click="columnsEvent('delete')">{{ $t('common.operations.delete') }}
    </tiny-link>
    <tiny-link class="link-save" :disabled="isDisabled" :underline="false" :icon="iconSave()"
               :title="$t('common.operations.save')"
               @click="save"> {{ $t('common.operations.save') }}
    </tiny-link>
    <br />

    <tiny-grid style="margin-top: 10px;max-height: calc(100vh - 280px);overflow: auto;"
               ref="columnRef"
               :data="columns"
               :fit="true" size="medium" auto-resize border :stripe="true"
               :edit-config="{ trigger: 'click', mode: 'cell', showStatus: true }"
               :edit-rules="columnRules"
               show-header-overflow="tooltip"
               highlight-hover-row>
      <tiny-grid-column type="selection" width="40"></tiny-grid-column>

      <tiny-grid-column field="column" :editor="{}" :title="$t('datasource.structure.name')">
        <template #edit="data">
          <tiny-input v-model="data.row.column" size="mini" placeholder="id"></tiny-input>
        </template>
        <template v-slot="data">
          <tiny-input v-model="data.row.column" size="mini" placeholder="id"></tiny-input>
        </template>
      </tiny-grid-column>

      <tiny-grid-column field="dataType" :editor="{}" :title="$t('datasource.structure.data.type')">
        <template #edit="data">
          <tiny-select v-model="data.row.dataType" size="mini" @change="dataTypeChange(data.row)" clearable>
            <tiny-option v-for="key in columnType" :key="key" :label="key" :value="key">
            </tiny-option>
          </tiny-select>
        </template>
        <template v-slot="data">
          <tiny-select v-model="data.row.dataType" size="mini" clearable>
            <tiny-option v-for="key in columnType" :key="key" :label="key" :value="key">
            </tiny-option>
          </tiny-select>
        </template>
      </tiny-grid-column>

      <tiny-grid-column field="precision" :editor="{}" :title="$t('datasource.structure.precision')">
        <template #edit="data">
          <tiny-input type="number" v-model="data.row.precision" size="mini"></tiny-input>
        </template>
        <template v-slot="data">
          <tiny-input type="number" v-model="data.row.precision" size="mini"></tiny-input>
        </template>
      </tiny-grid-column>

      <tiny-grid-column field="scale" :editor="{}" :title="$t('datasource.structure.scale')">
        <template #edit="data">
          <tiny-input type="number" v-model="data.row.scale" size="mini"></tiny-input>
        </template>
        <template v-slot="data">
          <tiny-input type="number" v-model="data.row.scale" size="mini"></tiny-input>
        </template>
      </tiny-grid-column>

      <tiny-grid-column field="isNullable" :editor="{}" :title="$t('datasource.structure.is.nullable')">
        <template #edit="data">
          <tiny-switch v-model="data.row.isNullable" :true-value=true :false-value=false></tiny-switch>
        </template>
        <template v-slot="data">
          <tiny-switch v-model="data.row.isNullable" :true-value=true :false-value=false></tiny-switch>
        </template>
      </tiny-grid-column>

      <tiny-grid-column field="isKey" :editor="{}" :title="$t('datasource.structure.is.key')">
        <template #edit="data">
          <tiny-switch v-model="data.row.isKey" :true-value=true :false-value=false></tiny-switch>
        </template>
        <template v-slot="data">
          <tiny-switch v-model="data.row.isKey" :true-value=true :false-value=false></tiny-switch>
        </template>
      </tiny-grid-column>

      <tiny-grid-column field="defaultValue" :editor="{}" :title="$t('datasource.structure.default.value')">
        <template #edit="data">
          <tiny-input v-model="data.row.defaultValue" size="mini" placeholder="1"></tiny-input>
        </template>
        <template v-slot="data">
          <tiny-input v-model="data.row.defaultValue" size="mini" placeholder="1"></tiny-input>
        </template>
      </tiny-grid-column>

      <tiny-grid-column field="comment" :editor="{}" :title="$t('datasource.structure.comment')">
        <template #edit="data">
          <tiny-input v-model="data.row.comment" size="mini" placeholder="ID"></tiny-input>
        </template>
        <template v-slot="data">
          <tiny-input v-model="data.row.comment" size="mini" placeholder="ID"></tiny-input>
        </template>
      </tiny-grid-column>
    </tiny-grid>
  </div>
  <div>
    <tiny-drawer :title="datasourceColumnFormTitle"
                 :visible="datasourceColumnFormDrawer"
                 @update:visible="datasourceColumnFormDrawer = $event"
                 :show-header="true"
                 :mask-closable="false"
                 width="50%"
                 @close="closeDatasourceColumnForm(false)">
      <div>
        <suspense>
          <DsmOperateForm :table-data="tableData" :datasource-column-form-data="datasourceColumnFormData"
                          :update="update" :table-name="tableName" :data-base-id="dataBaseId"></DsmOperateForm>
        </suspense>
      </div>
    </tiny-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import {
  iconDataSource,
  iconDialog2,
  iconEdit,
  iconEditorTable,
  iconFontFamily,
  iconFontSize,
  iconFontStyle,
  iconGroupTransfer,
  iconLineHeight,
  iconNode,
  iconNodeOpen,
  iconRichTextTable,
  iconSave,
  iconSearch,
  iconSettingData,
  iconTime
} from '@opentiny/vue-icon'
import { columnType, SourceService, type TableParam } from '@/services/spi/source'
import { assign, cloneDeep, includes, isEmpty, orderBy, some } from 'lodash-es'
import { useI18n } from 'vue-i18n'
import { validForm } from '@/utils/tool'
import emitter from '@/utils/evnetbus'
import { Modal } from '@opentiny/vue'
import DsmOperateForm from '@/views/data-source/components/dsm-operate-form.vue'

const { t } = useI18n()
const IconDataSource = iconDataSource()

const tableData = defineModel('tableData', { default: {} as TableParam })
const dataBaseId = defineModel('dataBaseId', { default: 0 })
const tableName = defineModel('tableName', { default: '' })
const dataBaseName = defineModel('dataBaseName', { default: '' })

const id = ref(0)
const isDisabled = ref(true)
const tableBodyRef = ref()
const tableBody = ref<any>({})
const tableBodyRules = computed(() => {
  let rules: any = {
    name: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  return rules
})

const gridVisible = ref(false)
const columnRef = ref()
const columns = ref<any[]>([])
const columnRules = {
  column: [{ required: true, message: t('common.prompt.required') }],
  dataType: [{ required: true, message: t('common.prompt.required') }]
}

const datasourceColumnFormDrawer = ref(false)
const datasourceColumnFormTitle = ref('')
const datasourceColumnFormData = ref()
const datasourceColumnFormCopy = ref()
const update = ref(false)

const tableCustomData = ref<any[]>([])
const addTableCustom = (key: any, value: any) => {
  const hasKey = some(tableCustomData.value, (config) => config.key === key)
  if (!hasKey || key === '') {
    id.value += 1
    tableCustomData.value.unshift({
      id: id.value,
      key: key,
      value: value
    })
  }
}
const deleteTableCustom = (id: any) => {
  tableCustomData.value = tableCustomData.value.filter(item => item.id !== id)
}

const editColumn = (typeValue: any, row: any) => {
  if (typeValue == 'add') {
    update.value = false
    datasourceColumnFormTitle.value = t('common.text.add.data')
    datasourceColumnFormData.value = {
      column: '',
      dataType: '',
      precision: 0,
      scale: 0,
      comment: '',
      defaultValue: '',
      isKey: false,
      isNullable: true,
      disabled: true
    }
  } else {
    update.value = true
    datasourceColumnFormTitle.value = t('common.text.update.data')
    datasourceColumnFormData.value = row
    datasourceColumnFormCopy.value = cloneDeep(row)
  }
  datasourceColumnFormDrawer.value = true
}
const columnsEvent = (code: string) => {
  switch (code) {
    case 'add':
      gridVisible.value = false
      columnRef.value.insert({
        column: '',
        dataType: 'varchar',
        precision: 25,
        scale: 0,
        comment: '',
        defaultValue: '',
        isKey: false,
        isNullable: true,
        disabled: true
      })
      break
    case 'delete': {
      const data = columnRef.value.getSelectRecords()
      if (data.length > 0) {
        columnRef.value.removeSelecteds()
      }
      break
    }
  }
}

const dataTypeChange = (row: any) => {
  if (row.dataType == 'varchar') {
    if (row.precision <= 0) {
      row.precision = 25
    }
    row.scale = 0
  } else if (row.dataType == 'double') {
    if (row.precision <= 0) {
      row.precision = 0
    }
    if (row.scale <= 0) {
      row.scale = 0
    }
  } else if (row.dataType == 'decimal') {
    if (row.precision <= 0) {
      row.precision = 10
    }
    if (row.scale <= 0) {
      row.scale = 0
    }
  } else {
    row.precision = 0
    row.scale = 0
  }
}

const save = () => {
  Promise.all([validForm(tableBodyRef), validForm(columnRef)]).then(async (res) => {
    const [valid1, valid2] = res
    if (valid1 && valid2) {
      let addColumns = columnRef.value.getRecordset().insertRecords
      if (!isEmpty(addColumns)) {
        addColumns.forEach((column: any) => {
          dataTypeChange(column)
        })
        tableBody.value.columns = addColumns
        const result = await SourceService.createTable(dataBaseId.value, tableBody.value)

        if (result.isSuccessful) {
          Modal.message({
            message: t('common.prompt.save.success'),
            status: 'success'
          })
          columns.value = []
          tableBody.value = {}
          emitter.emit('createTableSuccess')
        } else {
          Modal.message({
            message: result.message,
            status: 'error'
          })
        }
      } else {
        gridVisible.value = true
        setTimeout(() => {
          gridVisible.value = false
        }, 2000)
      }

    }
  })
}

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

const deleteColumn = async (row: any) => {
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      const result = await SourceService.dropColumn(dataBaseId.value, tableName.value, row.column)
      if (result.isSuccessful) {
        Modal.message({
          message: t('common.prompt.delete.success'),
          status: 'success'
        })
        await searchColumn()
      } else {
        Modal.message({
          message: result.message,
          status: 'error'
        })
      }
    }
  })
}
const closeDatasourceColumnForm = async (isUp: any) => {
  if (isUp) {
    await searchColumn()
  } else {
    //还原数据
    assign(datasourceColumnFormData.value, datasourceColumnFormCopy.value)
  }
  datasourceColumnFormDrawer.value = false
}

emitter.on('closeDatasourceColumnForm', (isUp) => {
  closeDatasourceColumnForm(isUp)
})

watch(tableName, async () => {
  createSql.value = ''
  gridVisible.value = false
  tableBody.value = {}
  columns.value = []
  isDisabled.value = true
  if (!isEmpty(tableName.value)) {
    if (tableData.value.type === 'DATABASE') {
      isDisabled.value = false
    } else if (tableData.value.type !== 'OTHER') {
      isDisabled.value = false
      await searchColumn()
    }
  }
})
</script>

<style scoped lang="less">
.operate-tabs {
  margin: 10px;

  .div-form {

    :deep(.tiny-form-item__label) {
      text-align: left;
      vertical-align: middle;
      float: left;
      font-size: var(--ti-form-item-label-font-size);
      color: var(--ti-form-item-label-text-color);
      height: var(--ti-form-item-label-line-height);
      line-height: var(--ti-form-item-label-line-height);
      padding-right: var(--ti-form-item-label-padding-right);
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      margin-top: 5px;
    }

    :deep(.tiny-link__inner) {
      font-size: 13px;
      margin-left: 3px;
    }

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1.2em;
      --ti-link-svg-height: 1.2em;
    }

    :deep(.tiny-form-item__content) {
      position: relative;
      font-size: var(--ti-form-item-label-font-size);
      line-height: var(--ti-form-item-label-line-height);
      width: 50%;
      margin-top: 5px;
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

  .link-save {
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

  .link-btn-edit {
    margin: 0px 10px 0px 0px;

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1em;
      --ti-link-svg-height: 1em;
      fill: #1ac456;
      margin-right: 2px;
    }

    :deep(.tiny-link__inner) {
      font-size: 13px;
      color: #1ac456;
    }
  }

  .link-btn-del {
    margin: 0px 10px 0px 0px;

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1em;
      --ti-link-svg-height: 1em;
      fill: #ee0808;
      margin-right: 2px;
    }

    :deep(.tiny-link__inner) {
      font-size: 13px;
      color: #ee0808;
    }
  }

  .link-custom-label {
    font-size: 13px;
    color: #2c3e50;
    margin-left: 20px;

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1.2em;
      --ti-link-svg-height: 1.2em;
      margin: 0 3px 2px 0;

    }
  }

  .link-custom-add {
    margin-left: 10px;

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1.1em;
      --ti-link-svg-height: 1.1em;
      fill: #1ac456;
      margin-top: -3px;
    }
  }

  .link-custom-delete {
    margin-left: 10px;

    :deep(.tiny-link-svg) {
      --ti-link-svg-width: 1.1em;
      --ti-link-svg-height: 1.1em;
      fill: #ee0808;
    }
  }

  .custom-span-font {
    font-size: 13px;
    color: #2c3e50;
    margin-top: 2px;
    margin-left: 20px;
  }

  .svg-table {
    fill: #1a78c4;
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
