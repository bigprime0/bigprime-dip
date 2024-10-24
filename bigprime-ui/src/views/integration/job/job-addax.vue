<template>
  <div class="container-list">
    <div class="contain">
      <tiny-steps line :data="data" :active="active" @click="handleClick" flex="true"></tiny-steps>
      <keep-alive>
        <JobDefineForm v-if="stepValue == 'setting'" :step="stepValue" :needFilter="needFilter"
                       :subcategories="subcategories" :stepConfigs="stepConfigs"
                       :JobDefineFormData="envFormData" :source-data="sourceData" :job="job"></JobDefineForm>
      </keep-alive>
      <keep-alive>
        <JobDefineForm v-if="stepValue == 'reader'" :step="stepValue" :needFilter="needFilter"
                       :subcategories="subcategories" :stepConfigs="stepConfigs"
                       :JobDefineFormData="sourceFormData" :source-data="sourceData" :job="job"></JobDefineForm>
      </keep-alive>
      <keep-alive>
        <JobDefineForm v-if="stepValue == 'writer'" :step="stepValue" :needFilter="needFilter"
                       :subcategories="subcategories" :stepConfigs="stepConfigs"
                       :JobDefineFormData="sinkFormData" :source-data="sourceData" :job="job"
                       :form-disabled="queryModel.type === 1"></JobDefineForm>
      </keep-alive>
      <keep-alive>
        <monacoEditor ref="buildRef" v-if="stepValue == 'build'"
                      v-model="buildJobData"
                      overviewRulerBorder=false
                      cursorSmoothCaretAnimation=true
                      formatOnPaste=true
                      mouseWheelZoom=true
                      folding=true
                      automaticLayout=true
                      glyphMargin=true
                      language="json">
        </monacoEditor>
      </keep-alive>
      <div class="step-button">
        <tiny-layout>
          <tiny-row>
            <tiny-button v-if="active > 0" type="primary" :icon="IconDoubleLeft" @click="handlePrev">
              {{ t('common.operations.step.prev') }}
            </tiny-button>
            <tiny-button v-if="active < 3" type="primary" :icon="IconDoubleRight" @click="handleNext">
              {{ t('common.operations.step.next') }}
            </tiny-button>
            <tiny-button v-if="active == 3" type="primary" :icon="IconDone" @click="handleSave">
              {{ t('common.operations.save') }}
            </tiny-button>
          </tiny-row>
        </tiny-layout>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, watchEffect } from 'vue'
import { useI18n } from 'vue-i18n'
import {
  type integrationConfigDetail,
  IntegrationConfigService,
  type integrationJob
} from '@/services/integration/integration-config'
import JobDefineForm from '@/views/integration/job/job-define-form.vue'
import { forIn, indexOf, isEmpty } from 'lodash-es'
import { iconDone, iconDoubleLeft, iconDoubleRight } from '@opentiny/vue-icon'
import { Modal } from '@opentiny/vue'
import emitter from '@/utils/evnetbus'
import MonacoEditor from '@/components/monaco-editor/monaco-editor.vue'
import { SourceService } from '@/services/spi/source'

const IconDoubleLeft = iconDoubleLeft()
const IconDoubleRight = iconDoubleRight()
const IconDone = iconDone()

const { t } = useI18n()
const active = ref(0)
const stepValue = ref('')
const needFilter = ref(false)
const subcategories = ref<string[]>([])
const data = reactive([
  { name: t('addax.step.setting'), status: 'doing' },
  { name: t('addax.step.reader') },
  { name: t('addax.step.writer') },
  { name: t('addax.step.build') }
])
const selectConfigs = defineModel('selectConfigs', { default: {} as integrationConfigDetail[] })
const stepConfigs = ref<integrationConfigDetail[]>([])
const envFormData = defineModel('envFormData', { default: {} as any })
const sourceFormData = defineModel('sourceFormData', { default: {} as any })
const sinkFormData = defineModel('sinkFormData', { default: {} as any })
const buildJobData = defineModel('JobData', { default: {} as any })
const job = defineModel('job', { default: {} as integrationJob })
const queryModel = defineModel('queryModel', { default: {} as any })
const sourceData = ref()

const _setStepConfig = () => {
  switch (active.value) {
    case 0 :
      stepValue.value = 'setting'
      break
    case 1:
      stepValue.value = 'reader'
      break
    case 2:
      stepValue.value = 'writer'
      break
    case 3:
      stepValue.value = 'build'
      break
    default:
      stepValue.value = ''
      break
  }
  if (stepValue.value) {
    stepConfigs.value = selectConfigs.value.filter(item => item.category == stepValue.value || item.subCategory == stepValue.value)
    subcategories.value = []
    stepConfigs.value.forEach(item => {
      if (!isEmpty(item.subCategory)) {
        if (indexOf(subcategories.value, item.subCategory) == -1) {
          subcategories.value.push(item.subCategory)
        }
      }
    })
    needFilter.value = subcategories.value.length > 0
  }
}

const handlePrev = () => {
  active.value = active.value > 0 ? active.value - 1 : 0
  _setStepConfig()
}

const handleNext = () => {
  active.value = active.value < 3 ? active.value + 1 : 3
  _setStepConfig()
  if (active.value === 3) {
    _buildJob()
  }
}

const handleClick = (index: number, node: any) => {
  active.value = index
  _setStepConfig()
  if (active.value === 3) {
    _buildJob()
  }
}

const _buildJob = () => {
  //构造任务
  let JobContext = {} as any
  JobContext.job = {}
  JobContext.job.content = {}
  JobContext.job.setting = {}
  //构建env
  let envPart = _buildEnvPart()
  JobContext.job.setting = envPart

  //构建Source
  if (sourceFormData.value.plugin_name) {
    let sourcePart = _buildPart(sourceFormData, 'reader')
    JobContext.job.content.reader = sourcePart
  }

  if (sinkFormData.value.plugin_name) {
    let sinkPart = _buildPart(sinkFormData, 'writer')
    JobContext.job.content.writer = sinkPart
  }

  let writerTables: any = []
  if (JobContext.job.content.writer) {
    if(isEmpty(job.value.writeType)){
      job.value.writeType = JobContext.job.content.writer.name
    }
    if (!isEmpty(JobContext.job.content.writer.parameter?.connection?.table)) {
      writerTables = JobContext.job.content.writer.parameter.connection.table
    }else{
      writerTables.push(JobContext.job.content.writer.name)
    }
    job.value.writeSourceTable = JobContext.job.content.writer.parameter.connection.table.join('')
  }

  let readerColumn = ['*']
  let readerTables: any = []
  let querySql = ''
  if (JobContext.job.content.reader) {
    job.value.readerType = JobContext.job.content.reader.name
    job.value.readerSourceTable = JobContext.job.content.reader.name
    if (JobContext.job.content.reader.parameter) {
      if(!isEmpty(JobContext.job.content.reader.parameter.querySql)){
        querySql = JobContext.job.content.reader.parameter.querySql
      }else{
        if (!isEmpty(JobContext.job.content.reader.parameter.column)) {
          readerColumn = JobContext.job.content.reader.parameter.column
        }
        if (JobContext.job.content.reader.parameter.connection) {
          if (!isEmpty(JobContext.job.content.reader.parameter.connection.table)) {
            readerTables = JobContext.job.content.reader.parameter.connection.table
          }
        }
      }
    }
  }
  let sql:any = []
  let column = readerColumn.join(',')
  let writeSourceDatabase = !isEmpty(job.value.writeSourceDatabase) ? `${job.value.writeSourceDatabase}.` : ''
  let readerSourceDatabase = !isEmpty(job.value.readerSourceDatabase) ? `${job.value.readerSourceDatabase}.` : ''
  if(querySql !== ''){
    writerTables.forEach((writerTables:any) => {
      sql.push(`INSERT INTO ${writeSourceDatabase}${writerTables} ${querySql}`)
    })
  }else{
    readerTables.forEach((table: any) => {
      writerTables.forEach((writerTables:any) => {
        sql.push(`INSERT INTO ${writeSourceDatabase}${writerTables} SELECT ${column} FROM ${readerSourceDatabase}${table}`)
      })
    })
  }
  job.value.analyzeSql = sql.join(';')

  buildJobData.value = JSON.stringify(JobContext, null, 2)
}

const _buildEnvPart = () => {
  let env = {} as any
  let envConfigs = selectConfigs.value.filter(t => t.category == 'setting')
  forIn(envFormData.value, (value, key) => {
    if (key == 'job_name') {
      job.value.jobName = value
    }
    if (key == 'addax_home') {
      job.value.url = value
    }
    let find = envConfigs.find(s => s.configCode == key)
    if (find) {
      if (find.isVisible && find.isProperty && find.isEnable && value != null && value != '') {
        if (find.configGroup) {
          if (!env[find.configGroup]) {
            env[find.configGroup] = {}
          }
          env[find.configGroup][find.configKey] = value
        } else {
          env[find.configKey] = value
        }
      }
    }
  })
  return env
}

const _buildPart = (formData: any, category: string) => {
  let result = {} as any
  result.parameter = {}
  if (formData.value.plugin_name) {
    if (!result.name) {
      result.name = formData.value.plugin_name
    }
    let partConfigs = selectConfigs.value.filter(t => t.category == category && t.subCategory == formData.value.plugin_name)
    forIn(formData.value, (value, key) => {
      let find = partConfigs.find(s => s.configCode == key && s.configCode != 'name')
      if (find) {
        if (find.isVisible && find.isProperty && find.isEnable && value != null && value != '') {
          if (find.compType == 'array') {
            if (value.indexOf(',') != -1) {
              value = value.split(',')
            } else if (value.indexOf('|') != -1) {
              value = value.split('|')
            } else {
              let valarr: any = []
              valarr.push(value)
              value = valarr
            }
          }
          if (find.compType == 'json') {
            value = JSON.parse(value)
          }
          if (find.configGroup) {
            if (!result.parameter[find.configGroup]) {
              result.parameter[find.configGroup] = {}
            }
            result.parameter[find.configGroup][find.configKey] = value
          } else {
            result.parameter[find.configKey] = value
          }
        }
      }
    })
  }
  return result
}

const handleSave = async () => {
  job.value.engine = 'addax'
  job.value.content = buildJobData.value
  let form_datas: any = {}
  form_datas.env = envFormData.value
  form_datas.source = sourceFormData.value
  form_datas.sink = sinkFormData.value
  job.value.formDatas = JSON.stringify(form_datas)
  const result: any = await IntegrationConfigService.saveJob(job.value)
  if (result) {
    Modal.message({
      message: job.value.id > 0 ? t('common.prompt.update.success') : t('common.prompt.save.success'),
      status: 'success'
    })
    emitter.emit('drawerCancel')
  } else {
    Modal.message({
      message: job.value.id > 0 ? t('common.prompt.update.fail') : t('common.prompt.save.fail'),
      status: 'error'
    })
  }
}

const queryDatasource = async () => {
  sourceData.value = await SourceService.getList({
    search: '',
    config: true
  })
}

watchEffect(async () => {
  if (queryModel.value.type === 1 && queryModel.value.writeSourceTable) {
    let writer = await IntegrationConfigService.getMasterDataWriter({ table: queryModel.value.writeSourceTable })
    job.value.writeSourceId = -1;
    job.value.writeSourceDatabase = 'MasterLibrary';
    job.value.writeType = 'rdbmswriter';
    job.value.writeSourceTable = 'queryModel.value.writeSourceTable';
    sinkFormData.value = writer
    sinkFormData.value.plugin_name = 'rdbmswriter'
  }
})

onMounted(() => {
  queryDatasource()
  if (selectConfigs.value) {
    _setStepConfig()
  }
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

  .step-button {
    margin: 0;
    text-align: center;
    padding-top: 20px;
  }
}

.codeEditBox {
  margin-top: 20px;
  height: calc(100vh - 230px);
}

</style>
