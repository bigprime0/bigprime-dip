<template>
  <div :class="showBreadcrumb ? 'container-list' : ''">
    <Breadcrumb v-if="showBreadcrumb" :items="['menu.dsm', 'menu.dsm.dsm-query']" />
    <div :class="showBreadcrumb ? 'contain' : ''">
      <div class="split-v-model">
        <tiny-split v-model="ratio" trigger-simple disabled>
          <template #left>
            <div class="demo-split-pane">
              <div class="div-icon">
                <DsmSourceSelect v-show="showBreadcrumb" ref="dsmSourceSelectRef"
                                 :data-base-id="dataBaseId"
                                 :query="querySource"
                                 @change="sourceChange"></DsmSourceSelect>
                <div v-if="dataBaseId > 0" class="option-row">
                  <tiny-input v-model="filterText"
                              :prefix-icon="IconSearch"
                              @input="inputChange"
                              clearable @clear="inputChange"
                              :placeholder="$t('common.search.placeholder')">

                  </tiny-input>
                </div>
                <tiny-tree ref="treeRef"
                           node-key="name"
                           :data="treeData"
                           :default-expand-all="true"
                           :expand-on-click-node="false"
                           :show-line=true
                           indent="10px"
                           :filter-node-method="filterNodeMethod"
                           @node-click="nodeClick"
                >
                  <template #prefix="{node}">
                    <tiny-image v-if="!isEmpty(headUrl) && node.data.type === 'DATABASE'" class="svg-img" :src="headUrl" fit="fit"></tiny-image>
                    <IconDataSource v-else-if="node.data.type === 'DATABASE'" class="svg-db"/>
                    <IconRichTextTable class="svg-table" v-else />
                  </template>
                  <template #default="{node}">
                    <div class="div-default">{{ node.data.name }}</div>
                  </template>
                </tiny-tree>
              </div>
            </div>
          </template>
          <template #right>
            <DsmOperateSql v-if="tableData?.type === 'DATABASE'" :table-data="tableData" :data-base-id="dataBaseId" :table-name="tableName"></DsmOperateSql>
            <DsmOperateQuery v-else :table-data="tableData" :data-base-id="dataBaseId" :table-name="tableName"></DsmOperateQuery>
          </template>
        </tiny-split>
      </div>
    </div>
  </div>

</template>

<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import { Split as TinySplit } from '@opentiny/vue'
import { iconDataSource, iconRichTextTable, iconSearch } from '@opentiny/vue-icon'
import { SourceService } from '@/services/spi/source'
import emitter from '@/utils/evnetbus'
import { isEmpty } from 'lodash-es'
import DsmOperateQuery from '@/views/data-source/components/dsm-operate-query.vue'
import DsmSourceSelect from '@/views/data-source/components/dsm-source-select.vue'
import DsmOperateSql from "@/views/data-source/components/dsm-operate-sql.vue";

const IconDataSource = iconDataSource()
const IconRichTextTable = iconRichTextTable()
const IconSearch = iconSearch()
const ratio = ref(0.2)
const showBreadcrumb = defineModel('showBreadcrumb', { default: true })
const headUrl = defineModel('headUrl', { default: '' })
const dataBaseId = defineModel('dataBaseId', { default: 0 })
const treeParameters = defineModel('treeParameters', { default: [] })
const treeRef = ref()
const treeData = ref()
const filterText = ref()
const tableData = ref()
const tableName = ref()
const dsmSourceSelectRef= ref()
const querySource = {
  search: '',
  config: false,
  protocol: ''
}

const sourceChange = () => {
  dataBaseId.value = dsmSourceSelectRef.value.dataBaseId
  tableName.value = ''
  if(dsmSourceSelectRef.value.dbSource?.product){
    headUrl.value = '/plugin/' + dsmSourceSelectRef.value.dbSource?.product + '.png'
  }

}
const nodeClick = async (item: any) => {
  tableData.value = item
  tableName.value = item.name
}

const inputChange = () => {
  treeRef.value.filter(filterText.value)
}
const filterNodeMethod = (text: any, data: any) => {
  return data.name.includes(text)
}

watchEffect(async () => {
  tableName.value = ''
  if (dataBaseId.value > 0) {
    if (!isEmpty(treeParameters.value)) {
      treeData.value = treeParameters.value
    } else {
      treeData.value = await SourceService.getSourceTree(dataBaseId.value)
    }
  } else {
    treeData.value = []
  }
})

const createTableSuccess = async () => {
  treeData.value = await SourceService.getSourceTree(dataBaseId.value)
}

emitter.on('createTableSuccess', createTableSuccess)
</script>

<style scoped lang="less">

.split-v-model {
  height: calc(85vh - 100px);
  border: 1px solid #d9d9d9;

  :deep(.tiny-split-pane.left-pane, .tiny-split-pane.right-pane) {
    top: 0;
    bottom: 0;
    overflow: auto;
  }
}

.demo-split-pane {
  padding: 10px;
}

.option-row {
  display: flex;
  align-items: center;
  margin: 5px 0;
}

.div-default {
  width: 100%;
  font-size: var(--ti-tree-small-node-font-size);
}

.div-icon {

  :deep(.tiny-tree-node .tiny-tree-node__content-right) {
    display: flex;
    -webkit-box-align: center;
    align-items: baseline;
    height: 100%;
    text-align: right;
  }

  :deep(.tiny-tree-node .tiny-tree-node__content-right svg) {
    fill: #5e7ce0;
    font-size: var(--ti-tree-node-operate-icon-font-size);
    margin-right: var(--ti-tree-node-operate-icon-margin-left);
    outline: 2px solid transparent;
    outline-offset: 2px;
  }

  :deep(.tiny-tree-node .tiny-tree-node__content-right svg:hover) {
    fill: #52c41a;
    font-size: var(--ti-tree-node-operate-icon-font-size);
    margin-right: var(--ti-tree-node-operate-icon-margin-left);
    outline: 2px solid transparent;
    outline-offset: 2px;
  }

  :deep(.tiny-tree-node .tiny-tree-node__content-left .tree-node-icon) {
    visibility: hidden;
  }

  :deep(.tiny-tree-node .tiny-tree-node__content-left .tree-node-icon svg) {
    margin-left: -60px;
  }

  :deep(.tiny-tree-node__expand-icon.is-leaf) {
    visibility: hidden;
    margin-left: -50px;
  }

  :deep(.tiny-tree-node .tiny-tree-node__content-left svg) {
    font-size: var(--ti-tree-node-operate-icon-font-size);
    outline: 2px solid transparent;
    outline-offset: 2px;
  }
}

.svg-db {
  fill: #1ac456;
}

.svg-img {
  width: 22px;
  height: 22px;

  :deep(img) {
    width: 22px;
    height: 22px;
    background-color: #ffffff;
  }
}

.svg-table {
  fill: #1a78c4;
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

  :deep(.tiny-button.tiny-button--info) {
    color: var(--ti-button-info-text-color);
    fill: var(--ti-button-info-text-color);
    border-color: #06cc4d;
    background-color: #06cc4d;
  }

  :deep(.tiny-button.tiny-button--waring) {
    color: var(--ti-button-info-text-color);
    fill: var(--ti-button-info-text-color);
    border-color: rgba(238, 8, 8, 0.44);
    background-color: rgba(238, 8, 8, 0.44);
  }

}
</style>
