<template>
  <div class="div-top">
    <DsmSourceSelect ref="dsmSourceSelectRef"
                     :data-base-id="dataBaseId"
                     :query="querySource"
                     @change="sourceChange"></DsmSourceSelect>
    <div v-if="dataBaseId > 0" class="option-row">
      <tiny-input v-if="dataBaseId > 0" v-model="filterText"
                  :prefix-icon="IconSearch"
                  @input="inputChange"
                  clearable @clear="inputChange"
                  :placeholder="$t('common.search.placeholder')">

      </tiny-input>
    </div>
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
      <tiny-image v-if="!isEmpty(headUrl) && node.data.type === 'DATABASE'" class="svg-img" :src="headUrl"
                  fit="fit"></tiny-image>
      <IconDataSource v-else-if="node.data.type === 'DATABASE'" class="svg-db" />
      <IconRichTextTable class="svg-table" v-else />
    </template>
    <template #default="{node}">
      <div class="div-default">{{ node.data.name }}</div>
    </template>
  </tiny-tree>
</template>

<script setup lang="ts">
import { ref, watchEffect } from 'vue'
import { iconDataSource, iconRichTextTable, iconSearch } from '@opentiny/vue-icon'
import { SourceService } from '@/services/spi/source'
import { isEmpty } from 'lodash-es'
import DsmSourceSelect from '@/views/data-source/components/dsm-source-select.vue'
import emitter from '@/utils/evnetbus'

const IconDataSource = iconDataSource()
const IconRichTextTable = iconRichTextTable()
const IconSearch = iconSearch()
const headUrl = defineModel('headUrl', { default: '' })
const dataBaseId = defineModel('dataBaseId', { default: 0 })
const treeRef = ref()
const treeData = ref()
const filterText = ref()
const dsmSourceSelectRef = ref()
const querySource = {
  search: '',
  config: false,
  protocol: ''
}

const sourceChange = () => {
  dataBaseId.value = dsmSourceSelectRef.value.dataBaseId
  if (dsmSourceSelectRef.value.dbSource?.product) {
    headUrl.value = '/plugin/' + dsmSourceSelectRef.value.dbSource?.product + '.png'
  }
}
const nodeClick = async (item: any) => {
  emitter.emit('query-tree-data-click', {
    tableData: item,
    dataBaseId: dataBaseId.value
  })
}

const inputChange = () => {
  treeRef.value.filter(filterText.value)
}
const filterNodeMethod = (text: any, data: any) => {
  return data.name.includes(text)
}

watchEffect(async () => {
  if (dataBaseId.value > 0) {
    treeData.value = await SourceService.getSourceTree(dataBaseId.value)
  } else {
    treeData.value = []
  }
})
</script>

<style scoped lang="less">
.div-top{
  position: sticky;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #fff;
  z-index: 1;
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
</style>
