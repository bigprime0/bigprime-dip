<!--
* 根据传入的配置数据集生成动态配置表单
-->
<template>
  <div class="container-list">
    <div class="contain">
      <tiny-form
          ref="dynamicFormRef"
          :model="JobDefineFormData"
          :rules="dynamicFormRules"
          validate-type="text"
          label-width="140px"
          :disabled="formDisabled"
      >
        <template v-if="needFilter">
          <tiny-row>
            <tiny-col :span="12">
              <tiny-form-item :label="$t('seatunnel.step.form.selectType')">
                <tiny-select
                    v-model="subCategory"
                    @change="handleChooseSubCategory"
                    :show-alloption="false"
                    clearable
                >
                  <tiny-option
                      v-for="item in subCategorys"
                      :key="item"
                      :label="item"
                      :value="item"
                  ></tiny-option>
                </tiny-select>
              </tiny-form-item>
            </tiny-col>
          </tiny-row>
        </template>

        <template v-for="item in currentConfigs" :key="item.configKey">
          <tiny-row v-if="item.isVisible == 1">
            <tiny-col :span="12">
              <tiny-form-item :label="item.configTitle">
                <template #label v-if="isEmpty(item.cnDescription) != true">
                  <IconHelpLabel
                      :label="item.configTitle"
                      :content="item.cnDescription"
                  ></IconHelpLabel>
                </template>
                <tiny-input
                    v-if="item.compType == 'textInput' || item.compType == 'array'"
                    v-model="JobDefineFormData[item.configCode]"
                ></tiny-input>
                <tiny-input
                    v-if="item.compType == 'textarea'"
                    type="textarea"
                    v-model="JobDefineFormData[item.configCode]"
                    show-word-limit
                    :autosize="{ minRows: 2, maxRows: 3 }"
                ></tiny-input>
                <tiny-input
                    v-if="item.compType == 'json'"
                    type="textarea"
                    v-model="JobDefineFormData[item.configCode]"
                    show-word-limit
                    :autosize="{ minRows: 10, maxRows: 20 }"
                ></tiny-input>
                <tiny-input
                    v-if="item.compType == 'numberInput'"
                    type="number"
                    v-model="JobDefineFormData[item.configCode]"
                ></tiny-input>
                <tiny-switch
                    v-if="item.compType == 'switch'"
                    v-model="JobDefineFormData[item.configCode]"
                ></tiny-switch>
                <tiny-select
                    v-if="item.compType == 'select'"
                    v-model="JobDefineFormData[item.configCode]"
                >
                  <tiny-option
                      v-for="option in item.compConfigFmt"
                      :key="option.value"
                      :label="option.label"
                      :value="option.value"
                  ></tiny-option>
                </tiny-select>
                <tiny-select
                    v-if="item.compType == 'datasource'"
                    v-model="JobDefineFormData[item.configCode]"
                    filterable
                    @change="dsChangeHandle"
                >
                  <tiny-option
                      v-for="option in sourceData"
                      :key="option.id"
                      :label="option.name"
                      :value="option.id"
                  ></tiny-option>
                </tiny-select>
                <tiny-checkbox-group
                    v-if="item.compType == 'checkbox'"
                    v-model="JobDefineFormData[item.configCode]"
                    type="checkbox"
                >
                  <tiny-checkbox
                      v-for="option in item.compConfigFmt"
                      :key="option.value"
                      :label="option.value"
                      :text="option.label"
                  ></tiny-checkbox>
                </tiny-checkbox-group>
                <tiny-date-picker
                    v-if="item.compType == 'dateInput'"
                    v-model="JobDefineFormData[item.configCode]"
                ></tiny-date-picker>
              </tiny-form-item>
            </tiny-col>
          </tiny-row>
        </template>
      </tiny-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue'
import type {integrationConfigDetail, integrationJob} from '@/services/integration/integration-config'
import {find, isEmpty} from 'lodash-es'
import IconHelpLabel from '@/views/components/IconHelpLabel.vue'

const dynamicFormRef = ref()
const dynamicFormRules = ref({})
const JobDefineFormData = defineModel<any>('JobDefineFormData', {})
const stepConfigs = defineModel('stepConfigs', {default: {} as integrationConfigDetail[]})
const currentConfigs = ref<integrationConfigDetail[]>([])
const subCategorys = defineModel('subcategories', {default: {} as string[]})
const needFilter = defineModel<boolean>('needFilter')
const formDisabled = defineModel("formDisabled", {default: false})
const subCategory = ref('')
const sourceData = defineModel("sourceData", {default: [] as any})
const step = defineModel("step", {default: ''})
const job = defineModel('job', {default: {} as integrationJob})

const _init = () => {
  if (JobDefineFormData.value['plugin_name']) {
    subCategory.value = JobDefineFormData.value['plugin_name']
    handleChooseSubCategory()
  }
  if (stepConfigs.value && stepConfigs.value.length > 0) {
    //切换配置
    stepConfigs.value.forEach((item) => {
      if (item.defaultValue) {
        if (item.compType == 'numberInput') {
          JobDefineFormData.value[item.configCode] = parseInt(item.defaultValue)
        } else {
          JobDefineFormData.value[item.configCode] = item.defaultValue
        }
      }
      let options: any = []
      if (item.compType == 'select' || item.compType == 'checkbox') {
        if (item.compConfig.indexOf('|') != -1) {
          let arr = item.compConfig.split('|')
          arr.forEach((aItem) => {
            if (aItem.indexOf(':') != -1) {
              let itemArr = aItem.split(':')
              options.push({value: itemArr[0], label: itemArr[1]})
            } else {
              options.push({value: aItem, label: aItem})
            }
          })
        }
      }
      item.compConfigFmt = options
    })
  }
  if (!needFilter.value) {
    currentConfigs.value = stepConfigs.value
  }
}

const handleChooseSubCategory = () => {
  currentConfigs.value = stepConfigs.value.filter((s) => s.subCategory == subCategory.value)
  JobDefineFormData.value['plugin_name'] = subCategory.value
}

const dsChangeHandle = (value: any) => {
  let data: any = find(sourceData.value, {id: value})
  if (data) {
    if (subCategory.value == 'Jdbc') {
      JobDefineFormData.value.url = data.source.url
      JobDefineFormData.value.user = data.source.username
      JobDefineFormData.value.password = data.source.password
      JobDefineFormData.value.driver = data.source.jdbcDriver
    }
    if (subCategory.value.indexOf('rdbms') != -1) {
      JobDefineFormData.value.jdbcUrl = data.source.url
      JobDefineFormData.value.username = data.source.username
      JobDefineFormData.value.password = data.source.password
      JobDefineFormData.value.driver = data.source.jdbcDriver
    }
    if (step.value === 'reader' || step.value === 'source') {
      job.value.readerSourceId = data.id
      job.value.readerSourceDatabase = data.source.database
    }
    if (step.value === 'writer' || step.value === 'sink') {
      job.value.writeSourceId = data.id
      job.value.writeSourceTable = data.source.database
    }
  }
}

onMounted(() => {
  _init()
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
  margin: 30px 50px 0px 0px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 0 8px 8px rgba(169, 174, 184, 0.05);
  padding: 10px;
}
</style>
