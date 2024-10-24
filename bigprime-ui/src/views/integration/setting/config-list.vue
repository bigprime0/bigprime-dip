<template>
  <div class="container-list">
    <Breadcrumb v-if="pagePath != ''" class="svg-file" :items="[pagePath]" :IconLeft="IconFolderOpened" />
    <tiny-grid :data="detailGridData" ref="configGridRef"
               :fit="true"
               size="small"
               border
               :stripe="true"
               show-header-overflow="tooltip" show-overflow="tooltip"
               highlight-hover-row>
      <template #toolbar>
        <div class="grid-toolbar">
          <tiny-search @search="search" @clear="search" v-model="searchValue"
                       :placeholder="$t('common.search.placeholder')"
                       is-enter-search clearable></tiny-search>
          <tiny-button v-if="pagePath != ''" type="primary" @click="handleAddConfig">
            {{ $t('common.operations.add') }}
          </tiny-button>
        </div>
      </template>
      <tiny-grid-column type="index" width="50"></tiny-grid-column>
      <tiny-grid-column field="configKey" :title="$t('integration.config.detail.field.configKey')" show-overflow></tiny-grid-column>
      <tiny-grid-column field="configTitle" :title="$t('integration.config.detail.field.configTitle')" show-overflow></tiny-grid-column>
      <tiny-grid-column field="version" :title="$t('integration.config.detail.field.version')" align="center" width="100"></tiny-grid-column>
      <tiny-grid-column field="isVisible" :title="$t('integration.config.detail.field.isVisible')" align="center" width="70">
        <template #default="data">
          <span style="color: red" v-if="data.row.isVisible == 0">否</span>
          <span v-if="data.row.isVisible == 1">是</span>
        </template>
      </tiny-grid-column>
      <tiny-grid-column field="isProperty" :title="$t('integration.config.detail.field.isProperty')" align="center" width="80">
        <template #default="data">
          <span style="color: red" v-if="data.row.isProperty == 0">否</span>
          <span v-if="data.row.isProperty == 1">是</span>
        </template>
      </tiny-grid-column>
      <tiny-grid-column field="isEnable" :title="$t('integration.config.detail.field.isEnable')" align="center" width="70">
        <template #default="data">
          <span style="color: red" v-if="data.row.isEnable == 0">否</span>
          <span v-if="data.row.isEnable == 1">是</span>
        </template>
      </tiny-grid-column>
      <tiny-grid-column field="compType" :title="$t('integration.config.detail.field.compType')" align="center" width="80"
                        :format-config="{
          data: compTypeOptions,
          type: 'enum',
          label: 'label',
          value: 'value'
        }"
      ></tiny-grid-column>
      <tiny-grid-column field="defaultValue" :title="$t('integration.config.detail.field.defaultValue')" show-overflow></tiny-grid-column>

      <tiny-grid-column :title="$t('common.operations')" width="100" align="center">
        <template v-slot="data">
          <a @click="handleRowDelete(data.row.id)">
            {{ t('common.operations.delete') }}
          </a>
          <a @click="handleRowUpdate(data.row)">
            {{ t('common.operations.update') }}
          </a>
        </template>
      </tiny-grid-column>
    </tiny-grid>
    <tiny-pager
      :current-page="formPage.page"
      :page-size="formPage.limit"
      :total="formPage.total"
      :page-sizes="formPage.pageSizes"
      @current-change="pageChange"
      @size-change="limitChange"
      :align="formPage.align"
      :layout="formPage.layout"
    ></tiny-pager>

    <!--抽屉-->
    <tiny-drawer :title="drawConfig.title"
                 :visible="drawConfig.visible"
                 @update:visible="drawConfig.visible = $event"
                 :show-header="true"
                 :mask-closable="false"
                 :width="drawConfig.width"
                 @close="drawConfig.handleDrawerClose">
      <div>
        <suspense>
          <ConfigDetailForm :key="nanoid(8)" ref="detailFormRef" :detailFormData="detailFormData" :compTypeOptions="compTypeOptions" :selectNode="selectNode"></ConfigDetailForm>
        </suspense>
      </div>
    </tiny-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import emitter from '@/utils/evnetbus'
import { formPage } from '@/utils/tool'
import { Modal, Notify } from '@opentiny/vue'
import { iconFolderOpened } from '@opentiny/vue-icon'
import { type integrationCategoryInfo, IntegrationConfigService } from '@/services/integration/integration-config'
import { nanoid } from 'nanoid'
import ConfigDetailForm from '@/views/integration/setting/config-detail-form.vue'

const IconFolderOpened = iconFolderOpened()
const { t } = useI18n()
const selectNode = defineModel('selectNode', { default: {} as integrationCategoryInfo })
const searchValue = ref('')
const pagePath = ref('')
const detailFormRef = ref()
const detailGridData = ref([])
const detailFormData = ref({})
const compTypeOptions = ref([
  { label: '文本框', value: 'textInput' },
  { label: '文本域', value: 'textarea' },
  { label: '数值框', value: 'numberInput' },
  { label: '日期框', value: 'dateInput' },
  { label: '开关', value: 'switch' },
  { label: '下拉框', value: 'select' },
  { label: '数据源', value: 'datasource' },
  { label: '复选框', value: 'checkbox' },
  { label: '数组', value: 'array' },
  { label: 'Json串', value: 'json' },
  { label: '表格', value: 'grid' }
])

//搜索查询
const search = async () => {
  if (selectNode.value && selectNode.value.route) {
    let arr = selectNode.value.route.split('/')
    let pLabel = arr[arr.length - 2]
    const result: any = await IntegrationConfigService.getDetailPageList({
      parent: pLabel,
      category: selectNode.value.label,
      search: searchValue.value,
      page: formPage.value.page,
      limit: formPage.value.limit
    })
    detailGridData.value = result.list
    formPage.value.total = result.total
  }
}

const drawConfig = ref({
  title: '',
  visible: false,
  width: '55%',
  handleDrawerClose: () => {
    drawConfig.value.visible = false
  }
})

onMounted(() => {
  search()
})

const pageChange = (page: number) => {
  formPage.value.page = page
  search()
}

const limitChange = (limit: number) => {
  formPage.value.limit = limit
  search()
}

const handleAddConfig = () => {
  drawConfig.value.title = t('common.operations.add')
  drawConfig.value.visible = true
  if (selectNode.value.route) {
    let items = selectNode.value.route.split('/')
    let product, category, subCategory
    if (items.length === 3) {
      product = items[1]
      category = items[2]
    }
    if (items.length === 4) {
      product = items[1]
      category = items[2]
      subCategory = items[3]
    }
    if (product && category) {
      detailFormData.value = {
        id: 0,
        product: product,
        category: category,
        subCategory: subCategory,
        configGroup: '',
        configKey: '',
        configCode: '',
        configTitle: '',
        version: 'universal',
        seq: 0,
        cnDescription: '',
        enDescription: '',
        isVisible: 0,
        isProperty: 0,
        isEnable: 0,
        compType: 'textInput',
        compConfig: '',
        defaultValue: ''
      }
    }

  }
}

const handleRowDelete = async (id: number) => {
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      await IntegrationConfigService.deleteDetail(id)
      Notify({
        type: 'success',
        message: t('common.prompt.delete.success'),
        position: 'top-right',
        duration: 1000
      })
      await search()
    }
  })
}

const handleRowUpdate = (row: any) => {
  drawConfig.value.title = t('common.operations.update')
  drawConfig.value.visible = true
  detailFormData.value = row
}

const handleDrawerClose = () => {
  drawConfig.value.visible = false
}

const handleNodeClick = (node: any) => {
  //添加重复点击发生请求
  if (selectNode.value.id != node.id) {
    pagePath.value = node.route.replaceAll('/', ' / ')
    selectNode.value = node
  }
}

emitter.on('drawerCancel', handleDrawerClose)
emitter.on('handleQuery', search)
emitter.on('nodeClickHandle', handleNodeClick)
</script>

<style scoped lang="less">
.grid-toolbar {
  display: flex;
  justify-content: left;
  width: calc(25vh + 100px);
  margin: 12px 0;

  div {
    margin-right: 5px;
  }

  button {
    margin-right: 5px;
  }
}

.svg-file {
  fill: #1ac456;
}
</style>

