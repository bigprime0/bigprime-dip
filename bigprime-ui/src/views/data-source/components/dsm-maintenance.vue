<template>
  <div class="container-list">
    <Breadcrumb :items="['menu.dsm', 'menu.dsm.dsm-maintenance']" />
    <div v-if="!run" class="contain">
      <div class="grid-toolbar">
        <tiny-search @search="search" @clear="search" @change="search" v-model="searchValue"
                     :placeholder="$t('common.search.placeholder')"
                     is-enter-search clearable></tiny-search>

        <tiny-button type="primary" @click="handleForm(null, 'add')">{{ $t('common.operations.add') }}</tiny-button>
      </div>
      <div class="card-wrap" v-for="item in tableData" :key="item.id">
        <tiny-card size="mini">
          <template #title-left>
          </template>
          <template #title>
            <tiny-link v-if="item.source?.protocol" class="span-font" type="info" :underline="false"
                       :title="$t('datasource.field.protocol')"
                       :value="item.source?.protocol" />
            <tiny-link v-else class="span-font" type="info" :underline="false" value="Unknown" />
          </template>
          <template #title-right>
            <tiny-link v-if="item.source?.type" class="list-link" type="success" :underline="false"
                       :title="$t('datasource.field.version')"
                       :value="item.source?.version" />
            <tiny-link v-else class="list-link" type="success" :title="$t('datasource.field.version')"
                       :underline="false" value="Unknown" />
          </template>
          <template #default>
            <tiny-layout cols="3">
              <tiny-row>
                <tiny-col :span="1">
                  <tiny-user-head type="image"
                                  v-model="item.headUrl"
                                  :title="item.product"
                                  class="source"
                                  background-color="#ffffff" />
                </tiny-col>

                <tiny-col :span="2" class="source-text">
                  <tiny-popover trigger="hover" placement="bottom"
                                popper-class="popover-cls"
                                :title="item.name">
                    <span class="span-font">{{ item.summary }}</span>
                    <template #reference>
                      <tiny-link type="primary" :underline="false"
                                 :value="item.name" />
                    </template>
                  </tiny-popover>
                </tiny-col>
              </tiny-row>
            </tiny-layout>
          </template>
          <template #footer>
            <tiny-button v-if="item.active" type="success" :icon="iconHeartempty()" size="mini" circle
                         v-loading.lock.fullscreen="saveLoading"
                         tiny-loading__background="rgba(0, 0, 0, 0.08)"
                         tiny-loading__size="large"
                         @click="handleTest(item)"
                         :title="$t('datasource.active.success')" />
            <tiny-button v-else type="danger" :icon="iconHeartempty()" size="mini" circle
                         v-loading.lock.fullscreen="saveLoading"
                         tiny-loading__background="rgba(0, 0, 0, 0.08)"
                         tiny-loading__size="large"
                         @click="handleTest(item)"
                         :title="$t('datasource.active.failed')" />
            <tiny-button type="info" :icon="iconStartCircle()" size="mini" circle
                         :title="$t('datasource.active.run')"
                         :disabled="!item.active"
                         @click="handRun(item)" />

            <tiny-button type="primary" :icon="iconEdit()" size="mini" circle
                         :title="$t('common.operations.update')"
                         @click="handleForm(item, 'update')" />

            <tiny-button type="warning" :icon="iconDel()" circle size="mini"
                         :title="$t('common.operations.delete')"
                         @click="handleDelete(item.id)" />
          </template>
        </tiny-card>
      </div>
    </div>
    <div v-if="run" class="contain">
      <div class="run-grid-toolbar">
        <tiny-link style="float: left;margin: 0 0 5px 2px;font-size: 12px" type="success" :underline="false"
                   @click="handRunClose"
                   :value="$t('common.search.return')" />
      </div>
      <DsmQuery :data-base-id="runId" :head-url="headUrl" :show-breadcrumb=false></DsmQuery>
    </div>
  </div>
  <div>
    <tiny-drawer :title="drawTitle"
                 :visible="drawVisible"
                 @update:visible="drawVisible = $event"
                 :show-header="true"
                 :mask-closable="false"
                 width="70%"
                 @close="handleDrawerClose">
      <div>
        <suspense>
          <DsmMaintenanceForm ref="dsFormRef" :ds-form-data="dsFormData" :source-data="sourceData"></DsmMaintenanceForm>
        </suspense>
      </div>

    </tiny-drawer>
  </div>
</template>

<script setup lang="ts">
import { iconDel, iconEdit, iconHeartempty, iconStartCircle } from '@opentiny/vue-icon'
import { type DataSourceInfo, useDataSourceStore } from '@/store/modules/data-source'
import { useI18n } from 'vue-i18n'
import { onMounted, ref } from 'vue'
import { type SourceParam, SourceService } from '@/services/spi/source'
import { Modal, Notify } from '@opentiny/vue'
import emitter from '@/utils/evnetbus'
import DsmMaintenanceForm from '@/views/data-source/components/dsm-maintenance-form.vue'
import DsmQuery from '@/views/data-source/components/dsm-query.vue'

const run = ref(false)
const runId = ref(0)
const useDsStore = useDataSourceStore()
const { t } = useI18n()
const tableData = ref<DataSourceInfo[]>()
const searchValue = ref()
const drawVisible = ref(false)
const drawTitle = ref('')
const dsFormRef = ref()
const dsFormData = ref<DataSourceInfo>()
const sourceData = ref<SourceParam>()
const headUrl = ref()

//搜索查询
const search = async () => {
  let data = await useDsStore.getDsList(searchValue.value)
  data.forEach(o => {
    if (o.source?.configures) {
      o.source.configure = JSON.stringify(o.source?.configures)
    }
    if (o.source?.type) {
      o.headUrl = '/plugin/' + o.source?.type + '.png'
    } else {
      o.headUrl = '/plugin/Unknown.png'
    }

  })
  tableData.value = data
}

const handRun = (item: DataSourceInfo) => {
  runId.value = item.id
  run.value = true
  headUrl.value = item.headUrl
  dsFormData.value = item
}

const handRunClose = () => {
  run.value = false
  dsFormData.value = {} as DataSourceInfo
}

const handleForm = (row: any, typeValue: any) => {
  if (typeValue == 'add') {
    drawTitle.value = t('datasource.operation.draw.add')
    dsFormData.value = {
      config: '',
      id: 0,
      name: '',
      product: '',
      productType: '',
      source: {
        protocol: 'JDBC',
        type: 'MySQL'
      } as SourceParam,
      summary: '',
      headUrl: '',
      active: false
    }
    sourceData.value = dsFormData.value?.source
  } else {
    drawTitle.value = t('datasource.operation.draw.update')
    dsFormData.value = row
    sourceData.value = row.source
  }
  drawVisible.value = true
}

const handleDelete = (id: number) => {
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      const result = await useDsStore.deleteById(id)
      if (result) {
        Notify({
          type: 'success',
          message: t('common.prompt.delete.success'),
          position: 'top-right',
          duration: 1000
        })
        await search()
      } else {
        Notify({
          type: 'error',
          message: t('common.prompt.delete.fail'),
          position: 'top-right',
          duration: 1000
        })
      }
    }
  })
}

const saveLoading = ref(false)
const handleTest = async (data: DataSourceInfo) => {
  saveLoading.value = true
  const result = await SourceService.testConnection(data).catch(error => {
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
  await search()
}
const handleDrawerClose = () => {
  drawVisible.value = false
  search()
}

//监听抽屉子组件的取消
emitter.on('dsCancel', handleDrawerClose)

onMounted(() => {
  search()
})


</script>
<style scoped lang="less">
.card-wrap {
  float: left;
  margin: 10px 10px 0 0;

  :deep(.tiny-card.tiny-card--default) {
    border-color: var(--ti-card-default-border-color);
    margin-bottom: 15px;
  }

  :deep(.tiny-card .tiny-card__body .tiny-card--logo .tiny-card--logo__title) {
    display: flex;
    -webkit-box-align: start;
    -ms-flex-align: start;
    align-items: flex-start;
  }

  :deep(.tiny-card .tiny-card__body .tiny-card--logo .tiny-card--logo__title--p) {
    font-size: var(--ti-card-title-font-size);
    font-weight: var(--ti-card-title-font-weight);
    margin-right: 8px;
    margin-bottom: 1px;
    line-height: 20px;
    //color: hsla(160, 100%, 37%, 1);
  }

  :deep(.tiny-card .tiny-card__footer .tiny-card__footer-footer) {
    padding: 5px 10px 5px 5px;
    border-top: 1px solid #dfdfdf;
    text-align: right;
  }

  :deep(.tiny-button.tiny-button--info) {
    color: var(--ti-button-info-text-color);
    fill: var(--ti-button-info-text-color);
    border-color: #b3a3f8;
    background-color: #b3a3f8;
  }

  :deep(.tiny-button.tiny-button--danger) {
    color: var(--ti-button-info-text-color);
    fill: var(--ti-button-info-text-color);
    /*    border-color: #ed66ab;
        background-color: #ed66ab;*/
  }

  :deep(.tiny-button.tiny-button--primary) {
    color: var(--ti-button-info-text-color);
    fill: var(--ti-button-info-text-color);
    /*    border-color: #52c41a;
        background-color: #52c41a;*/
  }

  :deep(
    .tiny-button.is-disabled,
    .tiny-button.is-disabled:active,
    .tiny-button.is-disabled:focus,
    .tiny-button.is-disabled:hover) {
    color: var(--ti-button-primary-disabled-text-color);
    fill: var(--ti-button-primary-disabled-text-color);
    border-color: var(--ti-button-primary-disabled-border-color);
    background-color: var(--ti-button-primary-disabled-bg-color);
  }
}

.list-link {
  margin-left: 8px;
  font-size: 11px;
}

.span-font {
  font-size: 12px;
  color: #000000;
}

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
  height: calc(70vh - 100px);
  overflow: auto;
}

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

.run-grid-toolbar {
  display: flex;
  justify-content: left;
}

.source {
  margin-top: 10px;
  text-align: center;
}

.source-text {
  margin-top: 15%;
  padding-left: 15%;

  :deep(.tiny-link .tiny-link-svg) {
    margin-right: 3px;
    margin-left: 0px;
  }
}

.div-select-svg {
  float: left;
  width: 0.8em;
  height: 1.2em;
  vertical-align: middle;
  overflow: auto;
  display: inline-block;
  fill: #adb0b8;
}
</style>

<style>
.popover-cls {
}

.popover-cls .tiny-popover__title {
  font-size: 10px !important;
}
</style>

