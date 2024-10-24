<template>
  <div class="div-icon">
    <tiny-tree
      ref="treeRef"
      node-key="id"
      :data="treeData"
      :show-line=true
      indent="12px"
      size="medium"
      :default-expanded-keys="expandKeys"
      current-node-key="0"
      expand-on-click-node="false"
      @node-click="nodeClick"
      @mouseleave="mouseoverEvent(-1)"
      @add-node="saveEvent"
      @edit-node="editEvent"
    >
      <template #prefix="{node}">
        <IconFolderOpened class="svg-dir" v-if="!node.data.isLeaf" />
        <IconFiletext class="svg-file" v-else />
      </template>

      <template #default="{node}">
        <div class="div-default" @mouseover="mouseoverEvent(node.data.id)">{{ node.data.label }}</div>
      </template>

      <template #operation="{node}">
        <div v-if="showId === node.data.id">
          <span v-if="node.data.isLeaf == 1" @click.stop="editNode(node)"
                :title="$t('common.operations.update')">
            <IconWriting></IconWriting>
          </span>

          <span v-if="canAddNodeKeys.indexOf(node.data.label) > -1" @click.stop="addNode(node)"
                :title="$t('common.operations.add')">
            <IconPlusSquare></IconPlusSquare>
          </span>

          <span v-if="node.data.isLeaf == 1"
                @click.stop="deleteNode(node)"
                :title="$t('common.operations.delete')">
            <IconMinusSquare></IconMinusSquare>
          </span>
        </div>
      </template>

    </tiny-tree>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Modal, Tree as TinyTree } from '@opentiny/vue'
import { iconFiletext, iconFolderOpened, iconMinusSquare, iconPlusSquare, iconWriting } from '@opentiny/vue-icon'
import { useI18n } from 'vue-i18n'
import { type integrationCategoryInfo, IntegrationConfigService } from '@/services/integration/integration-config'
import emitter from '@/utils/evnetbus'

const IconFolderOpened = iconFolderOpened()
const IconFiletext = iconFiletext()
const IconPlusSquare = iconPlusSquare()
const IconMinusSquare = iconMinusSquare()
const IconWriting = iconWriting()

const showId = ref(-1)
const { t } = useI18n()
const treeData = ref<integrationCategoryInfo[]>()
const treeRef = ref()
const editId = ref(-2)
const canAddNodeKeys = ref('sink,transform,source,reader,writer')
const expandKeys = ref<number[]>([0])
const selectNode = defineModel('selectNode', { default: {} as integrationCategoryInfo })


onMounted(() => {
  initTree()
})

const initTree = async () => {
  const result = await IntegrationConfigService.getTreeList()
  getExpandKeys(result)
  treeData.value = result
}

const getExpandKeys = (result: integrationCategoryInfo[]) => {
  result.forEach(item => {
    expandKeys.value.push(item.id)
    if (item.children.length >= 0) {
      item.children.forEach(child => {
        expandKeys.value.push(child.id)
      })
    }
  })

}

const parent = ref<integrationCategoryInfo>({} as integrationCategoryInfo)

const addNode = (node: any) => {
  treeRef.value.saveNode()
  editId.value = 0
  parent.value = node.data
  treeRef.value.addNode(node)
}
const editNode = (node: any) => {
  // if (node.data.id !== editId.value) {
  //   treeRef.value.saveNode()
  // }
  // editId.value = node.data.id
  // node.data.label = node.data.name
  treeRef.value.editNode(node)
}
const deleteNode = (node: any) => {
  if (node.data.id !== editId.value) {
    treeRef.value.saveNode()
  }
  editId.value = -2
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      const result = await IntegrationConfigService.deleteCategory(node.data.id)
      if (result) {
        if (result === 500001) {
          Modal.message({
            status: 'warning',
            message: t('integration.config.msg.500001'),
            duration: '500'
          })
        }
        treeRef.value.remove(node)
      }
    }
  })
}

const saveEvent = async (node: any) => {
  node.data.pid = parent.value.id
  node.data.children = []
  node.data.isLeaf = 1
  node.data.route = parent.value.route + '/' + node.data.label
  //清除掉临时id,使用后台生成的id更新
  node.data.id = 0
  const id = await IntegrationConfigService.saveCategory(node.data)
  if (id == -10002) {
    Modal.alert(t('integration.config.alert.same'))
  } else {
    treeRef.value.saveNode()
    node.data.id = id
    //修改父节点的是否叶子节点
    if (parent.value.isLeaf === 1) {
      parent.value.isLeaf = 0
    }
    treeRef.value.setCurrentKey(id)
  }
}

const editEvent = async (node: any) => {
  node.data.configCode = node.data.label
  node.data.configName = node.data.label
  await IntegrationConfigService.saveCategory(node.data)
}

const mouseoverEvent = (id: any) => {
  showId.value = id
}

const nodeClick = (data: integrationCategoryInfo) => {
  if (data.isLeaf) {
    selectNode.value = data
    emitter.emit('nodeClickHandle', data)
    emitter.emit('handleQuery')
  }
}


</script>

<style scoped lang="less">
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

  :deep(.tiny-tree-node__expand-icon.is-leaf) {
    visibility: hidden;
    margin-left: -50px;
  }

  :deep(.tiny-tree-node .tiny-tree-node__content-left svg) {
    font-size: var(--ti-tree-node-operate-icon-font-size);
    outline: 2px solid transparent;
    outline-offset: 2px;
  }

  :deep(.tiny-tree-node .tiny-tree-node__content-left .tree-node-icon) {
    visibility: hidden;
  }

  :deep(.tiny-tree-node .tiny-tree-node__content-left .tree-node-icon svg) {
    margin-left: -60px;
  }

}

.svg-dir {
  fill: #ffcc41;
}

.svg-file {
  fill: #1ac456;
}
</style>
