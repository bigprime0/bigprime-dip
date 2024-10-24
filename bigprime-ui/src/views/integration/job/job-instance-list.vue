<template>
  <div :class="queryModel.type === 0 ? 'container-list' : ''">
    <Breadcrumb v-if="queryModel.type  === 0" :items="['menu.integration', 'menu.integration.instance']" />
    <div :class="queryModel.type  === 0 ? 'contain' : ''">
      <tiny-grid :data="jobLogGridData" ref="jobLogGridRef"
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
          </div>
        </template>
        <tiny-grid-column type="index" width="50"></tiny-grid-column>
        <tiny-grid-column field="engine" :title="$t('integration.job.column.engine')" width="80"></tiny-grid-column>
        <tiny-grid-column field="jobId" :title="$t('integration.job.column.jobId')" width="150"></tiny-grid-column>
        <tiny-grid-column field="jobName" :title="$t('integration.job.column.jobName')" width="150"></tiny-grid-column>
        <tiny-grid-column field="status" :title="$t('integration.job.column.status')" width="80">
          <template #default="data">
            <span style="color: #91a5e8" v-if="data.row.status == 'running'">运行中</span>
            <span style="color: red" v-if="data.row.status == 'error'">错误</span>
            <span style="color: green" v-if="data.row.status == 'success'">成功</span>
            <span style="color: darkred" v-if="data.row.status == 'exception'">异常</span>
          </template>
        </tiny-grid-column>
        <tiny-grid-column field="message" :title="$t('integration.job.column.message')" show-overflow>
          <template #default="{ row }">
            <json-viewer v-if="row.messageFmt != ''" :expanded="false" :expand-depth="0"
                         :value="row.messageFmt"></json-viewer>
            <span v-else>{{ row.message }}</span>
          </template>

        </tiny-grid-column>
        <tiny-grid-column field="startTime" :title="$t('integration.job.column.startTime')" width="120"
                          format-text="date"
                          :format-config="{ valueFormat: 'yyyy/MM/dd HH:mm:ss' }"></tiny-grid-column>
        <tiny-grid-column field="endTime" :title="$t('integration.job.column.endTime')" width="120" format-text="date"
                          :format-config="{ valueFormat: 'yyyy/MM/dd HH:mm:ss' }"></tiny-grid-column>
        <tiny-grid-column :title="$t('common.operations')" width="150" align="center">
          <template v-slot="data">
            <a @click="handleShowConfig(data.row)">
              {{ t('common.operations.show.config') }}
            </a>
            <a @click="handleShowLogs(data.row)">
              {{ t('common.operations.show.log') }}
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
          <suspense>
            <monacoEditor ref="buildRef"
                          v-model="logData"
                          overviewRulerBorder=false
                          cursorSmoothCaretAnimation=true
                          formatOnPaste=true
                          mouseWheelZoom=true
                          folding=true
                          automaticLayout=true
                          glyphMargin=true
                          codeLensFontSize="9"
                          :language="langType">
            </monacoEditor>
          </suspense>
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
import { IntegrationConfigService, type integrationJobLog } from '@/services/integration/integration-config'
import MonacoEditor from '@/components/monaco-editor/monaco-editor.vue'

const { t } = useI18n()
const searchValue = ref('')
const jobLogGridData = ref([])
const jobLogGridRef = ref()
const logData = ref('')
const langType = ref('text')
const queryModel = defineModel('queryModel', {
  default: {
    type: 0,
    jobName: ''
  }
})
//搜索查询
const search = async () => {
  formPage.value.limit = 15
  queryModel.value.jobName = searchValue.value
  const result: any = await IntegrationConfigService.getJobLogPageList({
    model:queryModel.value,
    page: formPage.value.page,
    limit: formPage.value.limit
  })
  jobLogGridData.value = result.list
  jobLogGridData.value.forEach((item: any) => {
    if (item.message != '') {
      try {
        item.messageFmt = JSON.parse(item.message)
      } catch (e) {
        item.messageFmt = ''
      }
    }
  })
  formPage.value.total = result.total

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

const handleShowLogs = (row: integrationJobLog) => {
  logData.value = ''
  logData.value = row.logs
  drawConfig.value.visible = true
  drawConfig.value.title = t('common.operations.show.log')
}

const handleShowConfig = (row: integrationJobLog) => {
  langType.value = 'json'
  logData.value = ''
  logData.value = row.content
  drawConfig.value.visible = true
  drawConfig.value.title = t('common.operations.show.config')
}

const handleDrawerClose = () => {
  drawConfig.value.visible = false
}


emitter.on('drawerCancel', handleDrawerClose)

emitter.on('masterSyncEmitter',() => {
  search()
})

onMounted(() => {
  search()
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

.codeEditBox {
  margin-top: 20px;
  height: calc(100vh - 130px);
}
</style>

