<template>
  <div class="container-list">
    <Breadcrumb :items="['menu.sys', 'menu.sys.org']" />
    <div class="contain">
      <tiny-grid :data="tableData" :tree-config="treeConfig" :resizable="false"
                 show-header-overflow="tooltip" show-overflow="tooltip"
                 row-id="id">
        <template #toolbar>
          <div class="grid-toolbar">
            <tiny-search @search="search" @clear="search" v-model="searchValue"
                         :placeholder="$t('common.search.placeholder')"
                         is-enter-search clearable></tiny-search>
            <div>
              <tiny-button type="primary" @click="handleForm(null, 'add')">{{ $t('common.operations.add') }}
              </tiny-button>
            </div>
          </div>
        </template>
        <tiny-grid-column field="name" :title="$t('sys.org.field.name')" tree-node></tiny-grid-column>
        <tiny-grid-column field="parentName" :title="$t('sys.org.field.parent.name')"></tiny-grid-column>
        <tiny-grid-column field="sort" :title="$t('sys.org.field.sort')"></tiny-grid-column>
        <tiny-grid-column :title="$t('common.operations')" align="center">
          <template v-slot="data">
            <a @click="handleDelete(data.row.id)">
              {{ t('common.operations.delete') }}
            </a>
            <a @click="handleForm(data.row, 'update')">
              {{ t('common.operations.update') }}
            </a>
          </template>
        </tiny-grid-column>
      </tiny-grid>
    </div>
  </div>

  <div>
    <tiny-drawer :title="drawTitle"
                 :visible="drawVisible"
                 @update:visible="drawVisible = $event"
                 :show-header="true"
                 :mask-closable="false"
                 width="50%"
                 @close="handleDrawerClose">
      <div>
        <suspense>
          <FormData :formData="formData"></FormData>
        </suspense>
      </div>

    </tiny-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { Grid as TinyGrid, GridColumn as TinyGridColumn, Modal, Notify } from '@opentiny/vue'
import { useI18n } from 'vue-i18n'
import emitter from '@/utils/evnetbus'
import { type OrgInfo, SysOrgService } from '@/services/sys/sys-org'
import { isEmpty } from 'lodash-es'
import FormData from '@/views/sys/org/components/org-form.vue'

const { t } = useI18n()

const tableData = ref()
const treeConfig = ref({
  children: 'children',
  trigger: 'cell',
  indent: 12,
  expandAll: true
})


const searchValue = ref()
const drawVisible = ref(false)
const drawTitle = ref('')
const formData = ref()

onMounted(async () => {
  await search()
})


//搜索查询
const search = async () => {
  const dataList = await SysOrgService.getList()
  if (!isEmpty(searchValue.value)) {
    let infos = [] as OrgInfo[]
    dataList.forEach(data => {
      const result = filterTree(data)
      if (result) {
        infos.push(data)
      }
    })
    tableData.value = infos
  } else {
    tableData.value = dataList
  }
}

const filterTree = (info: OrgInfo) => {
  let isShow = false
  if (info.name.indexOf(searchValue.value) != -1
    || (!isEmpty(info.parentName) && info.parentName.indexOf(searchValue.value) != -1)
  ) {
    isShow = true
  }
  let children = [] as OrgInfo[]
  if (!isEmpty(info.children)) {
    info.children.forEach(data => {
      const result = filterTree(data)
      if (result) {
        children.push(data)
      }
    })
  }
  if (!isEmpty(children)) {
    isShow = true
  }
  info.children = children
  return isShow
}

//打开form表单
const handleForm = (row: any, typeValue: any) => {
  if (typeValue == 'add') {
    drawTitle.value = t('common.text.add.data')
    formData.value = {
      id: 0,
      pid: 0,
      name: '',
      sort: 0
    } as OrgInfo
  } else {
    drawTitle.value = t('common.text.update.data')
    formData.value = row
  }
  drawVisible.value = true
}

//删除
const handleDelete = (id: number) => {
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      await SysOrgService.deleteById(id)
      Notify({
        type: 'success',
        message: t('common.prompt.delete.success'),
        position: 'top-right',
        duration: 1000
      })
      await search()
    }
  })
}

//抽屉关闭操作
const handleDrawerClose = () => {
  drawVisible.value = false
  search()
}

//监听抽屉子组件的取消
emitter.on('drawerCancel', handleDrawerClose)

</script>

<style scoped lang="less">
.icon-span {
  padding-left: 8px;
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

</style>
