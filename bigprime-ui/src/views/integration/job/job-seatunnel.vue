<template>
  <div class="container-list">
    <div class="contain">
      <tiny-steps line :data="data" :active="active" @click="handleClick" flex="true"></tiny-steps>
      <keep-alive>
        <JobDefineForm v-if="stepValue == 'env'" :step="stepValue" :needFilter="needFilter"
                       :subcategories="subcategories" :stepConfigs="stepConfigs"
                       :JobDefineFormData="envFormData" :source-data="sourceData" :job="job"></JobDefineForm>
      </keep-alive>
      <keep-alive>
        <JobDefineForm v-if="stepValue == 'source'" :step="stepValue" :needFilter="needFilter"
                       :subcategories="subcategories" :stepConfigs="stepConfigs"
                       :JobDefineFormData="sourceFormData" :source-data="sourceData" :job="job"></JobDefineForm>
      </keep-alive>
      <keep-alive>
        <JobDefineForm v-if="stepValue == 'transform'" :step="stepValue" :needFilter="needFilter"
                       :subcategories="subcategories" :stepConfigs="stepConfigs"
                       :JobDefineFormData="transformFormData" :source-data="sourceData" :job="job"></JobDefineForm>
      </keep-alive>
      <keep-alive>
        <JobDefineForm v-if="stepValue == 'sink'" :step="stepValue" :needFilter="needFilter"
                       :subcategories="subcategories" :stepConfigs="stepConfigs"
                       :JobDefineFormData="sinkFormData" :source-data="sourceData" :job="job"></JobDefineForm>
      </keep-alive>
      <keep-alive>
        <monacoEditor ref="buildRef" v-if="stepValue == 'submit'"
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
            <tiny-button v-if="active < 4" type="primary" :icon="IconDoubleRight" @click="handleNext">
              {{ t('common.operations.step.next') }}
            </tiny-button>
            <tiny-button v-if="active == 4" type="primary" :icon="IconDone" @click="handleSave">
              {{ t('common.operations.save') }}
            </tiny-button>
          </tiny-row>
        </tiny-layout>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
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
  { name: t('seatunnel.step.env'), status: 'doing' },
  { name: t('seatunnel.step.source') },
  { name: t('seatunnel.step.transform') },
  { name: t('seatunnel.step.sink') },
  { name: t('seatunnel.step.build') }
])
const selectConfigs = defineModel('selectConfigs', { default: {} as integrationConfigDetail[] })
const stepConfigs = ref<integrationConfigDetail[]>([])
const envFormData = defineModel('envFormData', { default: {} as any })
const sourceFormData = defineModel('sourceFormData', { default: {} as any })
const transformFormData = defineModel('transformFormData', { default: {} as any })
const sinkFormData = defineModel('sinkFormData', { default: {} as any })
const buildJobData = defineModel('JobData', { default: {} as any })
const job = defineModel('job', { default: {} as integrationJob })
const sourceData = ref()

const _setStepConfig = () => {
  switch (active.value) {
    case 0 :
      stepValue.value = 'env'
      break
    case 1:
      stepValue.value = 'source'
      break
    case 2:
      stepValue.value = 'transform'
      break
    case 3:
      stepValue.value = 'sink'
      break
    case 4:
      stepValue.value = 'submit'
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
  active.value = active.value < 4 ? active.value + 1 : 4
  _setStepConfig()
  if (active.value === 4) {
    _buildJob()
  }
}

const handleClick = (index: number, node: any) => {
  active.value = index
  _setStepConfig()
  if (active.value === 4) {
    _buildJob()
  }
}

const _buildJob = () => {
  //构造任务
  let JobContext = {} as any
  //构建env
  let envPart = _buildEnvPart()
  JobContext.env = envPart

  //构建Source
  if (sourceFormData.value.plugin_name) {
    let sourcePart = _buildPart(sourceFormData, 'source')
    JobContext.source = []
    JobContext.source.push(sourcePart)
  }

  //构建transform
  if (transformFormData.value.plugin_name) {
    let transformPart = _buildPart(transformFormData, 'transform')
    JobContext.transform = []
    JobContext.transform.push(transformPart)
  }

  if (sinkFormData.value.plugin_name) {
    let sinkPart = _buildPart(sinkFormData, 'sink')
    JobContext.sink = []
    JobContext.sink.push(sinkPart)
  }
  buildJobData.value = JSON.stringify(JobContext, null, 2)
}

const _buildEnvPart = () => {
  let env = {} as any
  let envConfigs = selectConfigs.value.filter(t => t.category == 'env')
  forIn(envFormData.value, (value, key) => {
    if (key == 'job_name') {
      job.value.jobName = value
    }
    if (key == 'rest_api') {
      job.value.url = value
    }
    let find = envConfigs.find(s => s.configCode == key)
    if (find) {
      if (find.isVisible && find.isProperty && find.isEnable && value != null && value != '') {
        env[find.configKey] = value
      }
    }
  })
  return env
}

const _buildPart = (formData: any, category: string) => {
  let result = {} as any
  if (formData.value.plugin_name) {
    result.plugin_name = formData.value.plugin_name
    let partConfigs = selectConfigs.value.filter(t => t.category == category && t.subCategory == formData.value.plugin_name)
    forIn(formData.value, (value, key) => {
      let find = partConfigs.find(s => s.configCode == key)
      if (find) {
        if (find.isVisible && find.isProperty && find.isEnable && value != null && value != '') {
          if (find.compType == 'array') {
            if (value.indexOf(',') != -1) {
              value = value.split(',')
            } else if (value.indexOf('|') != -1) {
              value = value.split('|')
            } else {
              let valarr:any = []
              valarr.push(value)
              value = valarr
            }
          }
          if (find.compType == 'json') {
            value = JSON.parse(value)
          }
          result[find.configKey] = value
        }
      }
    })
  }
  return result
}

const handleSave = async () => {
  job.value.content = buildJobData.value
  let form_datas: any = {}
  form_datas.env = envFormData.value
  form_datas.source = sourceFormData.value
  form_datas.transform = transformFormData.value
  form_datas.sink = sinkFormData.value
  job.value.formDatas = JSON.stringify(form_datas)
  const result: any = await IntegrationConfigService.saveJob(job.value)
  if (result) {
    Modal.message({
      message: job.value.id > 0 ? t('common.prompt.update.success') : t('common.prompt.save.success'),
      status: 'success'
    })
    emitter.emit('handleQuery')
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
