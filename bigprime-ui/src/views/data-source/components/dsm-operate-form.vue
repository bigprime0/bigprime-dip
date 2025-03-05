<template>
  <div class="ds-form">
    <tiny-form overflow-title ref="datasourceColumnFormRef" :model="datasourceColumnFormData"
               :rules="datasourceColumnFormRules"
               label-width="100px">
      <tiny-divider content-position="left" content-color="#7693f5">{{ $t('common.field.divider') }}
      </tiny-divider>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :show-message="false" prop="column" :label="$t('datasource.structure.name')">
            <tiny-input v-model="datasourceColumnFormData.column" size="mini" :disabled="update"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :show-message="false" prop="dataType" :label="$t('datasource.structure.data.type')">
            <tiny-select v-model="datasourceColumnFormData.dataType" size="mini" clearable>
              <tiny-option v-for="key in columnType" :key="key" :label="key" :value="key">
              </tiny-option>
            </tiny-select>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('datasource.structure.precision')">
            <tiny-input type="number" v-model="datasourceColumnFormData.precision" size="mini"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('datasource.structure.scale')">
            <tiny-input type="number" v-model="datasourceColumnFormData.scale" size="mini"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('datasource.structure.is.key')">
            <tiny-switch v-model="datasourceColumnFormData.isKey" :true-value=true
                         :false-value=false></tiny-switch>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('datasource.structure.is.nullable')">
            <tiny-switch v-model="datasourceColumnFormData.isNullable" :true-value=true
                         :false-value=false></tiny-switch>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('datasource.structure.default.value')">
            <tiny-input v-model="datasourceColumnFormData.defaultValue" size="mini"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('datasource.structure.comment')">
            <tiny-input v-model="datasourceColumnFormData.comment" size="mini"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>


      <tiny-row style="text-align: center">
        <tiny-button plain type="primary" @click="closeDatasourceColumnForm(false)">
          {{ $t('common.operations.cancel')
          }}
        </tiny-button>
        <tiny-button :reset-time="500" type="primary"
                     v-loading.lock.fullscreen="saveLoading"
                     tiny-loading__background="rgba(0, 0, 0, 0.2)"
                     tiny-loading__size="large"
                     @click="saveDataQualityConfigData">
          {{ $t('common.operations.save') }}
        </tiny-button>
      </tiny-row>
    </tiny-form>
  </div>

</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import emitter from '@/utils/evnetbus'
import { useI18n } from 'vue-i18n'
import { columnType, SourceService, type TableParam } from '@/services/spi/source'
import { Modal } from '@opentiny/vue'

const { t } = useI18n()
const dataBaseId = defineModel('dataBaseId', { default: 0 })
const tableName = defineModel('tableName', { default: '' })
const tableData = defineModel('tableData', { default: {} as TableParam })
const update = defineModel('update', { default: false })
const datasourceColumnFormRef = ref()
const datasourceColumnFormData = defineModel('datasourceColumnFormData', { default: {} as any })
const datasourceColumnFormRules = computed(() => {
  let ruleList = {
    column: [{ required: true, message: t('common.prompt.required') }],
    type: [{ required: true, message: t('common.prompt.required') }]
  }
  return ruleList
})


const saveLoading = ref(false)
const saveDataQualityConfigData = () => {
  datasourceColumnFormRef.value
    .validate()
    .then(async () => {
      saveLoading.value = true
      let result;
      if (update.value === true) {
        result = await SourceService.alterColumn(dataBaseId.value, tableName.value,  datasourceColumnFormData.value).catch(error => {
          saveLoading.value = false
        })
      }else{
        result = await SourceService.createColumn(dataBaseId.value, tableName.value,  datasourceColumnFormData.value).catch(error => {
          saveLoading.value = false
        })
      }
      if (result.isSuccessful) {
        Modal.message({
          message: t('common.prompt.save.success'),
          status: 'success'
        })
        closeDatasourceColumnForm(true)
      } else {
        Modal.message({
          message: result.message,
          status: 'error'
        })
      }
      saveLoading.value = false
    })
}

const closeDatasourceColumnForm = (isUp: boolean) => {
  emitter.emit('closeDatasourceColumnForm', isUp)
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
