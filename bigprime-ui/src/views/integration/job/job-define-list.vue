<template>
  <div :class="queryModel.type === 0 ? 'container-list' : ''">
    <Breadcrumb v-if="queryModel.type  === 0" :items="['menu.integration', 'menu.integration.define']" />
    <div :class="queryModel.type  === 0 ? 'contain' : ''" v-loading="isLoading">
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
            <div>
              <tiny-button v-if="queryModel.type === 0" type="primary" @click="handleAddSeatunnelJob">
                {{ $t('integration.job.add.seatunnel') }}
              </tiny-button>
              <tiny-button v-if="!(queryModel.type === 1 && isEmpty(queryModel.writeSourceTable))" type="primary" @click="handleAddAddaxJob">
                {{ $t('integration.job.add.addax') }}
              </tiny-button>
            </div>
          </div>
        </template>
        <tiny-grid-column type="index" width="60"></tiny-grid-column>
        <tiny-grid-column field="engine" :title="$t('integration.job.column.engine')" show-overflow></tiny-grid-column>
        <tiny-grid-column field="jobName" :title="$t('integration.job.column.jobName')"
                          show-overflow></tiny-grid-column>
        <tiny-grid-column v-if="queryModel.type === 1" field="writeSourceTable" :title="$t('integration.job.column.tableName')"
                          show-overflow></tiny-grid-column>
        <tiny-grid-column field="creator" :title="$t('common.createUser')"
                          show-overflow></tiny-grid-column>
        <tiny-grid-column field="createTime" :title="$t('common.createDate')"
                          show-overflow></tiny-grid-column>
        <tiny-grid-column field="updater" :title="$t('common.editor')" show-overflow></tiny-grid-column>
        <tiny-grid-column field="updateTime" :title="$t('common.lastupdated')"></tiny-grid-column>

        <tiny-grid-column :title="$t('common.operations')" width="150" align="center">
          <template v-slot="data">
            <a @click="handleRowSubmit(data.row)">
              {{ t('common.operations.execute') }}
            </a>
            <a @click="handleRowDelete(data.row.id)">
              {{ t('common.operations.delete') }}
            </a>
            <a @click="handleRowUpdate(data.row)">
              {{ t('common.operations.update') }}
            </a>
          </template>
        </tiny-grid-column>
      </tiny-grid>
      <!--分页-->
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
          <JobSeatunnel :key="nanoid(8)" v-if="selectProduct == 'seatunnel'" :job="job" :selectConfigs="selectConfigs"
                        :envFormData="envFormData" :sourceFormData="sourceFormData"
                        :transformFormData="transformFormData" :sinkFormData="sinkFormData"></JobSeatunnel>
          <JobAddax :key="nanoid(8)" v-if="selectProduct == 'addax'" :job="job" :selectConfigs="selectConfigs"
                    :envFormData="envFormData" :sourceFormData="sourceFormData" :transformFormData="transformFormData"
                    :sinkFormData="sinkFormData" :query-model="queryModel"></JobAddax>
        </div>
      </tiny-drawer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import emitter from '@/utils/evnetbus'
import { formPage } from '@/utils/tool'
import { Modal, Notify } from '@opentiny/vue'
import { nanoid } from 'nanoid'
import JobSeatunnel from '@/views/integration/job/job-seatunnel.vue'
import JobAddax from '@/views/integration/job/job-addax.vue'
import {
  type integrationConfigDetail,
  IntegrationConfigService,
  type integrationJob
} from '@/services/integration/integration-config'
import { isEmpty } from 'lodash-es'

const { t } = useI18n()
const isLoading = ref(false)
const searchValue = ref('')
const detailGridData = ref([])
const envFormData = ref<any>({})
const sourceFormData = ref<any>({})
const transformFormData = ref<any>({})
const sinkFormData = ref<any>({})
const JobData = ref<any>({})
const job = ref<integrationJob>({
  id: 0,
  engine: '',
  jobName: '',
  type: 0,
  formDatas: '',
  content: '',
  analyzeSql: '',
  readerSourceDatabase: '',
  readerSourceId: 0,
  readerSourceTable: '',
  readerType: '',
  writeSourceDatabase: '',
  writeSourceId: 0,
  writeSourceTable: '',
  writeType: ''
})

const configsData = ref<integrationConfigDetail[]>([])
const selectProduct = ref('')
const selectConfigs = ref<integrationConfigDetail[]>([])
const queryModel = defineModel('queryModel', {
  default: {
    type: 0,
    jobName: '',
    writeSourceTable: ''
  }
})

//搜索查询
const search = async () => {
  queryModel.value.jobName = searchValue.value
  const result: any = await IntegrationConfigService.getJobPageList({
    model: queryModel.value,
    page: formPage.value.page,
    limit: formPage.value.limit
  })
  detailGridData.value = result.list
  formPage.value.total = result.total
}

const loadConfig = async () => {
  const result: any = await IntegrationConfigService.getDetailList()
  configsData.value = result
}

const drawConfig = ref({
  title: '',
  visible: false,
  width: '60%',
  handleDrawerClose: () => {
    drawConfig.value.visible = false
  }
})

const pageChange = (page: number) => {
  formPage.value.page = page
  search()
}

const limitChange = (limit: number) => {
  formPage.value.limit = limit
  search()
}

const handleAddSeatunnelJob = () => {
  drawConfig.value.title = t('integration.job.add.seatunnel')
  selectProduct.value = 'seatunnel'
  selectConfigs.value = configsData.value.filter(item => item.product === selectProduct.value)
  job.value = {
    id: 0,
    engine: '',
    jobName: '',
    type: queryModel.value.type,
    formDatas: '',
    content: '',
    analyzeSql: '',
    readerSourceDatabase: '',
    readerSourceId: 0,
    readerSourceTable: '',
    readerType: '',
    writeSourceDatabase: '',
    writeSourceId: 0,
    writeSourceTable: '',
    writeType: ''
  }
  drawConfig.value.visible = true
}

const handleAddAddaxJob = () => {
  drawConfig.value.title = t('integration.job.add.addax')
  selectProduct.value = 'addax'
  selectConfigs.value = configsData.value.filter(item => item.product === selectProduct.value)
  drawConfig.value.visible = true
}

const handleRowDelete = async (id: number) => {
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      let result = await IntegrationConfigService.deleteJob(id)
      if (result) {
        Notify({
          type: 'success',
          message: t('common.prompt.delete.success'),
          position: 'top-right',
          duration: 1000
        })
        await search()
      }
    }
  })
}

const handleRowUpdate = (row: integrationJob) => {
  job.value = row
  selectProduct.value = row.engine
  selectConfigs.value = configsData.value.filter(item => item.product === selectProduct.value)
  drawConfig.value.title = t('common.operations.update')
  drawConfig.value.visible = true
  if (row.formDatas) {
    if (selectProduct.value === 'seatunnel') {
      let formInfo = JSON.parse(row.formDatas)
      envFormData.value = formInfo.env
      sourceFormData.value = formInfo.source
      if (sourceFormData.value.plugin_name) {
        sourceFormData.value.subCategory = sourceFormData.value.plugin_name
      }
      transformFormData.value = formInfo.transform
      if (transformFormData.value.plugin_name) {
        transformFormData.value.subCategory = transformFormData.value.plugin_name
      }
      sinkFormData.value = formInfo.sink
      if (sinkFormData.value.plugin_name) {
        sinkFormData.value.subCategory = sinkFormData.value.plugin_name
      }
    }
    if (selectProduct.value === 'addax') {
      let formInfo = JSON.parse(row.formDatas)
      envFormData.value = formInfo.env
      sourceFormData.value = formInfo.source
      if (sourceFormData.value.plugin_name) {
        sourceFormData.value.subCategory = sourceFormData.value.plugin_name
      }
      sinkFormData.value = formInfo.sink
      if (sinkFormData.value.plugin_name) {
        sinkFormData.value.subCategory = sinkFormData.value.plugin_name
      }
    }

  }
  if (row.content) {
    JobData.value = row.content
  }
}

const handleRowSubmit = async (row: integrationJob) => {
  isLoading.value = true
  const rst = await IntegrationConfigService.submitJob(row)
  if (rst.status == 'success') {
    isLoading.value = false
    Modal.message({
      message: rst.message,
      status: 'success'
    })
    if(queryModel.value.type === 1){
      emitter.emit('masterSyncEmitter')
    }

  } else {
    isLoading.value = false
    Modal.message({
      message: rst.message,
      status: 'error'
    })
  }
}

const handleDrawerClose = () => {
  drawConfig.value.visible = false
  search()
}

emitter.on('drawerCancel', handleDrawerClose)
emitter.on('handleQuery', search)

onMounted(() => {
  search()
  loadConfig()
})

</script>

<style scoped lang="less">

.container-list {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  overflow-x: hidden;
  overflow-y: auto;
}

.contain {
  flex: 1 1 auto;
  margin: 8px 10px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 0 8px 8px rgba(169, 174, 184, 0.05);
  padding: 10px;
}

.grid-toolbar {
  display: flex;
  justify-content: left;
  width: calc(40vh + 200px);
  margin: 12px 0;

  .tiny-search {
    width: 256px;
  }

  div {
    margin-right: 5px;
  }

  .tiny-button-group {
    margin-right: 5px;
  }
}
</style>

