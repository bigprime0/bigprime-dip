<template>
  <div class="container-list">
    <slot name="breadcrumb"></slot>
    <div class="contain">
      <div class="split-v-model">
        <tiny-split v-model="ratio" trigger-simple disabled>
          <template #left>
            <div class="demo-split-pane">
              <div class="div-icon">
                <div class="option-row">
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
                           @mouseleave="mouseoverEvent('')"
                >
                  <template #prefix="{node}">
                    <IconFolderOpened class="svg-other" @click.stop="iconClick(node)"
                                      v-if="node.data.type === 'OTHER'" />
                    <IconDataSource class="svg-db" @click.stop="iconClick(node)"
                                    v-else-if="node.data.type === 'DATABASE'" />
                    <IconRichTextTable class="svg-table" v-else />
                  </template>
                  <template #default="{node}">
                    <div class="div-default" @mouseover="mouseoverEvent(node.data.name)">{{ node.data.name }}</div>
                  </template>
                </tiny-tree>
              </div>
            </div>
          </template>
          <template #right>
            <div v-if="addLabelId > 0" v-show="showBind" class="div-select">
              <Breadcrumb class="svg-other" :items="[addName]" :IconLeft="IconFolderOpened" />
              <tiny-link type="success" :underline="false" :value="addName + '-' + t('warehouse.house.layer.bind')" />
              <tiny-select style="margin-left:10px;width: 300px" size="mini" @change="selectChange"
                           v-model="selectValue">
                <tiny-option v-for="item in sources" :key="item.id" :label="item.name" :value="item.id">
                </tiny-option>
              </tiny-select>
              <br />
              <tiny-link type="success" :underline="false"
                         :value="addName + '-' + t('warehouse.house.layer.bind.list')" />
              <div class="bind-list" v-for="item in bindList" :key="item.id">
                <IconDataSource v-if="item.pName === addName" class="svg-db" />
                <tiny-link v-if="item.pName === addName" type="success" :underline="false" :value="item.name" />
                <tiny-link v-if="item.pName === addName" @click="deleteNode(item.databaseId)"
                           :underline="false"
                           :icon="iconNodeOpen()"></tiny-link>
              </div>
            </div>
            <div v-else>
              <slot name="tree-right" :tableData="tableData" :databaseId="databaseId"
                    :tableName="tableName" :databaseName="databaseName"></slot>
            </div>
          </template>
        </tiny-split>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Modal, Split as TinySplit } from '@opentiny/vue'
import { iconDataSource, iconFolderOpened, iconNodeOpen, iconRichTextTable, iconSearch } from '@opentiny/vue-icon'
import emitter from '@/utils/evnetbus'
import { DataHouseLayerService } from '@/services/warehouse/data-house-layer'
import { find, includes, remove } from 'lodash-es'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const showBind = defineModel('showBind', { default: false })
const IconFolderOpened = iconFolderOpened()
const IconDataSource = iconDataSource()
const IconRichTextTable = iconRichTextTable()
const IconSearch = iconSearch()

const ratio = ref(0.2)
const treeRef = ref()
const treeData = ref()
const filterText = ref()
const tableData = ref()
const tableName = ref()
const databaseId = ref()
const databaseName = ref()

const name = ref()
const addLabelId = ref(0)
const addName = ref()
const selectValue = ref()
const sources = ref()
const bindList = ref()

const search = async () => {
  let data: any = await DataHouseLayerService.getHouseTree()
  let bindIds: any = []
  data.forEach((o: any) => {
    if (o.children) {
      o.children.forEach((c: any) => {
        c.pName = o.name
        bindIds.push(c)
        if(c.children){
          c.children.forEach((t: any) =>{
            t.databaseId = c.databaseId
          })
        }
      })
    }
  })
  treeData.value = data
  bindList.value = bindIds
}

const setSources = async () => {
  sources.value = await DataHouseLayerService.getSources()
}

const selectChange = async () => {
  let data = find(sources.value, { id: selectValue.value })
  await DataHouseLayerService.bindLayerSource({
    id: 0,
    parentId: addLabelId.value,
    databaseId: data.id,
    product: data.product
  })
  await setSources()
  selectValue.value = undefined
  search().then(r => {
  })
}

const iconClickData = ref<any[]>([])
const iconClick = (node: any) => {
  let name = node.data.name
  if (includes(iconClickData.value, name) === true) {
    node.expand()
    remove(iconClickData.value, function(item) {
      return item === name
    })
  } else {
    node.collapse()
    iconClickData.value.push(name)
  }

}

const mouseoverEvent = (value: any) => {
  name.value = value
}

const deleteNode = (id: any) => {
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      await DataHouseLayerService.deleteById(id)
      Modal.message({
        message: t('common.prompt.delete.success'),
        status: 'success'
      })
      await setSources()
      search().then(r => {
      })
    }
  })
}

const nodeClick = async (item: any) => {
  tableData.value = item
  if (item.type === 'OTHER') {
    await setSources()
    addLabelId.value = item.id
    addName.value = item.name
  } else {
    sources.value = []
    addLabelId.value = 0
    addName.value = ''
    let f: any = find(bindList.value, { databaseId: item.databaseId })
    if (f) {
      databaseName.value = f.name
    }
    databaseId.value = item.databaseId
    tableName.value = item.name
  }
}

const inputChange = () => {
  treeRef.value.filter(filterText.value)
}
const filterNodeMethod = (text: any, data: any) => {
  return data.name.includes(text)
}

onMounted(async () => {
  await search()
})

const createTableSuccess = async () => {
  await search()
}

emitter.on('createTableSuccess', createTableSuccess)
</script>

<style scoped lang="less">

.split-v-model {
  height: 100%;
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
  margin-bottom: 5px;
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

.svg-other {
  fill: #ffcc41;
}

.svg-db {
  fill: #1ac456;
}

.svg-table {
  fill: #1a78c4;
}

.div-select {
  margin: 10px;

  :deep(.tiny-link__inner) {
    font-size: 12px;
    color: #1ac456;
  }

  :deep(.tiny-breadcrumb .tiny-breadcrumb__item:last-child .tiny-breadcrumb__inner) {
    font-weight: 500;
    color: #2c3e50;
    text-decoration: none;
  }

  :deep(.container-breadcrumb) {
    font-size: 13px;
    margin: 10px 0 0 0;
  }
}

.link-delete {
  margin-left: 10px;

  :deep(.tiny-link-svg) {
    --ti-link-svg-width: 1em;
    --ti-link-svg-height: 1em;
    fill: #ee0808;
    margin-right: 3px;
  }

  :deep(.tiny-link__inner) {
    font-size: 13px;
    color: #ee0808;
  }
}

.bind-list {
  margin-left: 10px;

  :deep(.tiny-link-svg) {
    --ti-link-svg-width: 1em;
    --ti-link-svg-height: 1em;
    fill: #ee0808;
    margin-left: 5px;
    margin-top: -2px;
  }

  :deep(.tiny-svg) {
    width: 0.9em;
    height: 0.9em;
    vertical-align: middle;
    overflow: hidden;
    display: inline-block;
    margin-right: 3px;
  }

  :deep(.tiny-link__inner) {
    font-size: 13px;
  }
}

.container-list {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  overflow: auto;
}

.contain {
  flex: 1 1 auto;
  margin: 8px 10px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 0 8px 8px rgba(169, 174, 184, 0.05);
  padding: 10px;
}
</style>
