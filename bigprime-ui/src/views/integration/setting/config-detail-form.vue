<template>
  <div class="ds-form">
    <tiny-form ref="detailFormRef" overflow-title :model="detailFormData" :rules="formRules" label-width="120px">

      <tiny-row v-show="false">
        <tiny-col :span="12">
          <tiny-form-item label="id" :show-message="false" prop="id">
            <tiny-input v-model="detailFormData.id" disabled="true"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>
      <tiny-row>
        <tiny-col :span="4">
          <tiny-form-item :label="$t('integration.config.detail.field.product')" :show-message="false" prop="product">
            <tiny-input v-model="detailFormData.product" disabled="true"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="4">
          <tiny-form-item :label="$t('integration.config.detail.field.category')" :show-message="false" prop="category">
            <tiny-input v-model="detailFormData.category" disabled="true"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="4">
          <tiny-form-item :label="$t('integration.config.detail.field.subCategory')" :show-message="false" prop="subCategory">
            <tiny-input v-model="detailFormData.subCategory" disabled="true"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('integration.config.detail.field.configGroup')" :show-message="false" prop="configGroup">
            <tiny-input v-model="detailFormData.configGroup"></tiny-input>
          </tiny-form-item>
        </tiny-col>

        <tiny-col :span="6">
          <tiny-form-item :label="$t('integration.config.detail.field.version')" :show-message="false" prop="version">
            <tiny-input v-model="detailFormData.version"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('integration.config.detail.field.configKey')" :show-message="false" prop="configKey">
            <tiny-input v-model="detailFormData.configKey"></tiny-input>
          </tiny-form-item>
        </tiny-col>

        <tiny-col :span="6">
          <tiny-form-item :label="$t('integration.config.detail.field.configTitle')" :show-message="false" prop="configTitle">
            <tiny-input v-model="detailFormData.configTitle"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="4">
          <tiny-form-item :label="$t('integration.config.detail.field.isVisible')" :show-message="false" prop="isVisible">
            <tiny-switch v-model="detailFormData.isVisible" :true-value=1 :false-value=0></tiny-switch>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="4">
          <tiny-form-item :label="$t('integration.config.detail.field.isProperty')" :show-message="false" prop="isProperty">
            <tiny-switch v-model="detailFormData.isProperty" :true-value=1 :false-value=0></tiny-switch>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="4">
          <tiny-form-item :label="$t('integration.config.detail.field.isEnable')" :show-message="false" prop="isEnable">
            <tiny-switch v-model="detailFormData.isEnable" :true-value=1 :false-value=0></tiny-switch>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('integration.config.detail.field.compType')" :show-message="false" prop="compType">
            <tiny-select v-model="detailFormData.compType">
              <tiny-option v-for="item in compTypeOptions" :key="item.value" :label="item.label" :value="item.value">
              </tiny-option>
            </tiny-select>
          </tiny-form-item>
        </tiny-col>

        <tiny-col :span="6">
          <tiny-form-item :label="$t('integration.config.detail.field.defaultValue')" :show-message="false" prop="defaultValue">
            <tiny-input v-model="detailFormData.defaultValue"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="12">
          <tiny-form-item :label="$t('integration.config.detail.field.compConfig')" :show-message="false" prop="compConfig">
            <tiny-input type="textarea" v-model="detailFormData.compConfig" :rows=5 :maxlength="900"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="12">
          <tiny-form-item :label="$t('integration.config.detail.field.cnDescription')" :show-message="false" prop="cnDescription">
            <tiny-input type="textarea" v-model="detailFormData.cnDescription" :rows=2 :maxlength="500"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="12">
          <tiny-form-item :label="$t('integration.config.detail.field.enDescription')" :show-message="false" prop="enDescription">
            <tiny-input type="textarea" v-model="detailFormData.enDescription" :rows=2 :maxlength="500"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-divider content-position="left" content-color="#7693f5"></tiny-divider>
      <tiny-form-item style="text-align: center">
        <tiny-button plain type="primary" @click="handleCancel"> {{ $t('common.operations.cancel') }}</tiny-button>
        <tiny-button :reset-time="500" type="primary" @click="handleSave"> {{ $t('common.operations.save') }}</tiny-button>
      </tiny-form-item>

    </tiny-form>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { type integrationConfigDetail, IntegrationConfigService } from '@/services/integration/integration-config'
import { useI18n } from 'vue-i18n'
import emitter from '@/utils/evnetbus'
import { Modal } from '@opentiny/vue'

const { t } = useI18n()
const detailFormRef = ref()
const detailFormData = defineModel('detailFormData', { default: {} as integrationConfigDetail })
const compTypeOptions = defineModel('compTypeOptions', { default: {} as any })
const formRules = computed(() => {
  let ruleList = {
    configKey: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  return ruleList
})

const handleCancel = () => {
  emitter.emit('drawerCancel')
}

const handleSave = () => {
  detailFormRef.value
    .validate()
    .then(async () => {
      const result = detailFormData.value.id > 0 ? await IntegrationConfigService.updateDetail(detailFormData.value) : await IntegrationConfigService.saveDetail(detailFormData.value)
      if (result) {
        Modal.message({
          message: detailFormData.value.id > 0 ? t('common.prompt.update.success') : t('common.prompt.save.success'),
          status: 'success'
        })
        emitter.emit('handleQuery')
        handleCancel()
      } else {
        Modal.message({
          message: detailFormData.value.id > 0 ? t('common.prompt.update.fail') : t('common.prompt.save.fail'),
          status: 'error'
        })
      }
    })
}


</script>

<style scoped lang="less">
.span-font {
  font-size: 12px;
  color: #000000;
}

.ds-form {
  padding: 10px;

  :deep(.container-form) {
    margin-top: 0;
    padding: 20px 15px 15px 15px;
    border-radius: 0;
    box-shadow: 0 0 2px 2px #eee;
    height: 100vh - 80px;
    height: calc(100vh - 130px);
  }
}
</style>
