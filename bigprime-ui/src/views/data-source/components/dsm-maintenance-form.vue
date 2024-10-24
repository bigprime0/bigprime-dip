<template>
  <div class="container-split">
    <tiny-split v-model="ratioData" trigger-simple disabled>
      <template #left>
        <div class="ds-choose">
          <tiny-collapse v-model="active">
            <tiny-collapse-item v-for="(item, index) in plugin" :title="item.key" :key="item.key" :name="item.key">
              <div class="ds-list">
                <template v-for="type in item.value">
                  <div class="card-div" v-if="sourceData.protocol === item.key && sourceData.type === type">
                    <tiny-card type="image" size="mini" :src="`/plugin/${type}.png`"
                               @click="handleChooseDs(item.key, type)"
                               status="success"></tiny-card>
                    <tiny-link class="link-span" :underline="false"
                               @click="handleChooseDs(item.key, type)"
                               type="success"> {{ type }}
                    </tiny-link>
                  </div>
                  <div class="card-div" v-else>
                    <tiny-card type="image" size="mini" :src="`/plugin/${type}.png`"
                               @click="handleChooseDs(item.key, type)" :disabled="dsFormData.id > 0"></tiny-card>
                    <tiny-link class="link-span" :underline="false"
                               @click="handleChooseDs(item.key, type)" :disabled="dsFormData.id > 0"> {{ type }}
                    </tiny-link>
                  </div>
                </template>
              </div>
            </tiny-collapse-item>
          </tiny-collapse>
        </div>
      </template>
      <template #right>
        <div class="ds-form" v-if="showFields.length > 0">
          <tiny-form ref="ruleFormRef" overflow-title :model="dsFormData" :rules="formRules" label-width="100px">
            <tiny-divider content-position="left" content-color="#7693f5">{{ $t('datasource.field.divider') }}
            </tiny-divider>
            <tiny-row>
              <tiny-col :span="6">
                <tiny-form-item :label="$t('datasource.field.protocol')">
                  <tiny-input v-model="sourceData.protocol" :disabled="true"></tiny-input>
                </tiny-form-item>
              </tiny-col>
              <tiny-col :span="6">
                <tiny-form-item :label="$t('datasource.field.type')">
                  <tiny-input v-model="sourceData.type" :disabled="true"></tiny-input>
                </tiny-form-item>
              </tiny-col>
            </tiny-row>

            <tiny-row>
              <tiny-col :span="12">
                <tiny-form-item :label="$t('common.name')" :show-message="false" prop="name">
                  <tiny-input v-model="dsFormData.name"></tiny-input>
                </tiny-form-item>
              </tiny-col>
            </tiny-row>

            <tiny-row>
              <tiny-col :span="12">
                <tiny-form-item :label="$t('common.summary')">
                  <tiny-input type="textarea" v-model="dsFormData.summary" :rows=3 :maxlength="300"
                              :autosize="{ minRows: 2, maxRows: 5 }" show-word-limit></tiny-input>
                </tiny-form-item>
              </tiny-col>
            </tiny-row>
          </tiny-form>

          <tiny-form ref="sourceDataRef" overflow-title :model="sourceData" :rules="formRules" label-width="100px">
            <tiny-divider content-position="left" content-color="#7693f5">{{ $t('datasource.config.divider') }}
            </tiny-divider>
            <tiny-row>
              <tiny-col :span="6" v-if="includes(showFields, 'host')">
                <tiny-form-item :label="$t('datasource.config.host')" :show-message="false" prop="host">
                  <tiny-input v-model="sourceData.host"></tiny-input>
                </tiny-form-item>
              </tiny-col>
              <tiny-col :span="6" v-if="includes(showFields, 'port')">
                <tiny-form-item :label="$t('datasource.config.port')" :show-message="false" prop="port">
                  <tiny-input type="number" v-model="sourceData.port"></tiny-input>
                </tiny-form-item>
              </tiny-col>
            </tiny-row>

            <tiny-row>
              <tiny-col :span="6" v-if="includes(showFields, 'schema')">
                <tiny-form-item label="schema" :show-message="false" prop="schema">
                  <tiny-input v-model="sourceData.schema"></tiny-input>
                </tiny-form-item>
              </tiny-col>
            </tiny-row>

            <tiny-row>
              <tiny-col :span="6" v-if="includes(showFields, 'database')">
                <tiny-form-item :label="$t('datasource.advanced.database')" :show-message="false" prop="database">
                  <tiny-input v-model="sourceData.database"></tiny-input>
                </tiny-form-item>
              </tiny-col>
              <tiny-col :span="6" v-if="includes(showFields, 'ssl')" :show-message="false" prop="ssl">
                <tiny-form-item :label="$t('datasource.authorize.ssl')">
                  <tiny-switch v-model="sourceData.ssl"></tiny-switch>
                </tiny-form-item>
              </tiny-col>
            </tiny-row>

            <tiny-row>
              <tiny-col :span="6" v-if="includes(showFields, 'username')">
                <tiny-form-item :label="$t('datasource.authorize.username')" :show-message="false" prop="username">
                  <tiny-input v-model="sourceData.username"></tiny-input>
                </tiny-form-item>
              </tiny-col>
              <tiny-col :span="6" v-if="includes(showFields, 'password')">
                <tiny-form-item :label="$t('datasource.authorize.password')" :show-message="false" prop="password">
                  <tiny-input type="password" show-password v-model="sourceData.password"></tiny-input>
                </tiny-form-item>
              </tiny-col>
            </tiny-row>

            <tiny-divider content-position="left" content-color="#7693f5">{{ $t('datasource.custom.divider') }}
            </tiny-divider>
            <tiny-link class="link-add" @click="addCondition('','')" :underline="false" :icon="iconNode()"></tiny-link>
            <div style="max-height: 150px;overflow: auto;">
              <div style="display: flex; margin: 10px 0 0 30px;" v-for="item in configureData" :key="item.id">
                <span class="span-font">{{ $t('datasource.custom.attribute') }}</span>
                <tiny-input style="width: 40%;margin-left: 5px;" v-model="item.key"></tiny-input>
                <span class="span-font">{{ $t('datasource.custom.value') }}</span>
                <tiny-input style="width: 30%;margin-left: 5px;" v-model="item.value"></tiny-input>
                <tiny-link class="link-delete" @click="deleteCondition(item.id)" :underline="false"
                           :icon="iconNodeOpen()"></tiny-link>
                <br />
              </div>
            </div>


            <br />
            <tiny-form-item style="text-align: center">
              <tiny-button plain type="primary" @click="handleCancel"> {{ $t('common.operations.cancel') }}
              </tiny-button>
              <tiny-button plain :reset-time="500" type="primary"
                           v-loading.lock.fullscreen="saveLoading"
                           tiny-loading__background="rgba(0, 0, 0, 0.2)"
                           tiny-loading__size="large"
                           @click="handleTest">
                {{ $t('datasource.operation.button.test') }}
              </tiny-button>
              <tiny-button :reset-time="500" type="primary"
                           v-loading.lock.fullscreen="saveLoading"
                           tiny-loading__background="rgba(0, 0, 0, 0.2)"
                           tiny-loading__size="large"
                           @click="handleSave"> {{ $t('common.operations.save') }}
              </tiny-button>
            </tiny-form-item>
          </tiny-form>

        </div>
      </template>
    </tiny-split>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watchEffect } from 'vue'
import emitter from '@/utils/evnetbus'
import { type DataSourceInfo, useDataSourceStore } from '@/store/modules/data-source'
import { useI18n } from 'vue-i18n'
import { Modal } from '@opentiny/vue'
import { SourceService } from '@/services/spi/source'
import { find, includes, some, trim } from 'lodash-es'
import { iconNode, iconNodeOpen } from '@opentiny/vue-icon'
import { validForm } from '@/utils/tool'

const { t } = useI18n()
const ruleFormRef = ref()
const sourceDataRef = ref()
const ratioData = ref(0.4)
const dsFormData = defineModel<DataSourceInfo>('dsFormData', { default: {} as DataSourceInfo })
const sourceData = defineModel('sourceData', { default: {} as any })
const useDsStore = useDataSourceStore()
const plugin = ref()
const active = ref()
const command = ref()
const configureData = ref<any[]>([])
const id = ref(0)
const showFields = ref([] as string[])
const ruleFields = ref([] as string[])

const formRules = computed(() => {
  let rules: any = {
    name: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  if (includes(ruleFields.value, 'host')) {
    rules.host = [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  if (includes(ruleFields.value, 'port')) {
    rules.port = [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  if (includes(ruleFields.value, 'database')) {
    rules.database = [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  if (includes(ruleFields.value, 'username')) {
    rules.username = [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  if (includes(ruleFields.value, 'password')) {
    rules.password = [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  if (includes(ruleFields.value, 'ssl')) {
    rules.ssl = [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  return rules
})

const addCondition = (key: any, value: any) => {
  const hasKey = some(configureData.value, (config) => config.key === key)
  if (!hasKey || key === '') {
    id.value += 1
    configureData.value.unshift({
      id: id.value,
      key: key,
      value: value
    })
  }
}
const deleteCondition = (id: any) => {
  configureData.value = configureData.value.filter(item => item.id !== id)
}

const convertConfigures = () => {
  let json: any = {}
  if (configureData.value) {
    configureData.value.forEach(o => {
      if (trim(o.key).length > 0 && trim(o.value).length > 0) {
        json[o.key] = o.value
      }
    })
  }
  sourceData.value.configures = json
}

const saveLoading = ref(false)
const handleSave = () => {
  Promise.all([validForm(ruleFormRef), validForm(sourceDataRef)]).then(async (res) => {
    const [valid1, valid2] = res
    if (valid1 && valid2) {
      saveLoading.value = true
      convertConfigures()
      const result = await useDsStore.save(dsFormData.value).catch(error => {
        saveLoading.value = false
      })
      saveLoading.value = false
      if (result) {
        Modal.message({
          message: t('common.prompt.save.success'),
          status: 'success'
        })
        handleCancel()
      }
    }
  })
}

const handleTest = () => {
  Promise.all([validForm(ruleFormRef), validForm(sourceDataRef)]).then(async (res) => {
    const [valid1, valid2] = res
    if (valid1 && valid2) {
      saveLoading.value = true
      convertConfigures()
      const result = await SourceService.testConnection(dsFormData.value).catch(error => {
        saveLoading.value = false
      })
      saveLoading.value = false
      if (result) {
        Modal.message({
          message: t('common.prompt.test.success'),
          status: 'success'
        })
      } else {
        Modal.message({
          message: t('common.prompt.test.fail'),
          status: 'error'
        })
      }
    }
  })
}

const handleCancel = () => {
  emitter.emit('dsCancel')
}

const handleShow = (protocol: any, type: any) => {
  if (dsFormData.value.id === 0) {
    sourceData.value.host = ''
    sourceData.value.port = 0
    sourceData.value.username = ''
    sourceData.value.password = ''
    sourceData.value.database = ''
    sourceData.value.ssl = false
    configureData.value = []
  }
  let shows: any = []
  let rules: any = []
  let commands = find(command.value, { key: type })
  if (commands) {
    for (let index in commands.value) {
      let model: any = commands.value[index]
      if (model.required) {
        rules.push(model.key)
        shows.push(model.key)
      }
      if (model.show) {
        shows.push(model.key)
      }
      if (dsFormData.value.id === 0) {
        if (model.key === 'configures') {
          if (model.commandCustomModels) {
            let id = -1
            let data: any = []
            for (let customIndex in model.commandCustomModels) {
              data.push(model.commandCustomModels[customIndex])
              data[id] = id
              id -= 1
            }
            configureData.value = data
          }

        } else if (model.defaultValue) {
          sourceData.value[model.key] = model.defaultValue
        }
      }
    }
  }
  showFields.value = shows
  ruleFields.value = rules
}

const handleChooseDs = (protocol: any, type: any) => {
  if (dsFormData.value.id > 0) {
    return
  }
  dsFormData.value.source.protocol = protocol
  dsFormData.value.source.type = type
  dsFormData.value.product = type
  handleShow(protocol, type)
}


watchEffect(() => {
  if (dsFormData.value.id >= 0) {
    handleShow(sourceData.value.protocol, sourceData.value.type)
  }
})

watchEffect(() => {
  if (sourceData.value.configure) {
    let entries = Object.entries(JSON.parse(sourceData.value.configure))
    let id = 0
    let data: any = []
    for (const [key, value] of entries) {
      id -= 1
      data.unshift({
        id: id,
        key: key,
        value: value
      })
    }
    configureData.value = data
  }
})

onMounted(async () => {
  let data = await SourceService.getPlugin()
  let properties: any = []
  let actives: any = []
  let commands: any = []
  for (let key in data) {
    let source: any = data[key]
    let sourceProperties: any = []
    for (let k in source) {
      sourceProperties.push(k)
      commands.push({ key: k, value: source[k] as any[] })
    }
    properties.push({ key, value: sourceProperties })
    actives.push(key)
  }
  plugin.value = properties
  active.value = actives
  command.value = commands
})

</script>

<style scoped lang="less">
.container-split {
  height: calc(100vh - 110px);
  border: 1px solid #d9d9d9;

  :deep(.tiny-split-pane.left-pane) {
    top: 0;
    bottom: 0;
    overflow: auto;
  }

  :deep(.tiny-split-pane.right-pane) {
    top: 0;
    bottom: 0;
    overflow: auto;
  }
}

.ds-choose {
  width: 100%;

  :deep(.tiny-collapse) {
    padding: 0 10px 10px 10px;
    border-top: 0;
    border-bottom: 0;
  }
}

.ds-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: normal;
  text-align: center;

  :deep(.tiny-card) {
    width: 64px;
    height: 64px;
    overflow: hidden;
  }

  :deep(.tiny-card.tiny-card--default) {
    cursor: pointer;
    border: 2px solid #ffffff;
  }

  :deep(.tiny-card.tiny-card--success) {
    cursor: pointer;
    border: 2px solid #02be7f;
  }

  :deep(img) {
    text-align: center;
    width: 60px;
    height: 60px;
    background-color: #ffffff;
  }

  :deep(.tiny-card .tiny-card__body .tiny-card--logo.tiny-card--mini-padding) {
    padding: 0px;
  }
}

.card-div {
  margin: 15px;
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

.link-add {
  margin-left: 50px;

  :deep(.tiny-link-svg) {
    --ti-link-svg-width: 1.4em;
    --ti-link-svg-height: 1.4em;
    fill: #1ac456;
  }
}

.link-delete {
  margin-left: 5px;

  :deep(.tiny-link-svg) {
    --ti-link-svg-width: 1.4em;
    --ti-link-svg-height: 1.4em;
    fill: #ee0808;
  }
}

.span-font {
  font-size: 13px;
  color: #2c3e50;
  margin-top: 2px;
  margin-left: 20px;
}

.link-span {
  width: 100%;

  :deep(.tiny-link__inner) {
    margin-top: 5px;
    font-size: 12px;
  }
}
</style>
