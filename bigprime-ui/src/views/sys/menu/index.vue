<template>
  <div class="container-list">
    <Breadcrumb :items="['menu.sys', 'menu.sys.menu']" />
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
        <tiny-grid-column field="nameDes" width="15%" :title="$t('sys.menu.field.name.des')" tree-node>
          <template v-slot="data">
            <component :is="Svgs[data.row.icon] && Svgs[data.row.icon]()" class="svgs-icon"></component>
            <span class="icon-span">{{ data.row.nameDes }}</span>
          </template>
        </tiny-grid-column>
        <tiny-grid-column field="name" width="15%" :title="$t('sys.menu.field.name')"></tiny-grid-column>
        <tiny-grid-column field="url" width="15%" :title="$t('sys.menu.field.url')"></tiny-grid-column>
        <tiny-grid-column field="sort" width="10%" :title="$t('sys.menu.field.sort')"></tiny-grid-column>
        <tiny-grid-column field="desConfig" :title="$t('sys.menu.field.config')">
          <template #default="{ row }">
            <json-viewer v-if="row.desConfig != ''" :expanded="false" :expand-depth="0"
                         :value="row.desConfig"></json-viewer>
            <span v-else></span>
          </template>
        </tiny-grid-column>
        <tiny-grid-column :title="$t('common.operations')" width="10%" align="center">
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
                 :width="drawWidth"
                 @close="handleDrawerClose">
      <div>
        <suspense>
          <MenuFormData :menuFormData="menuFormData"></MenuFormData>
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
import { type MenuInfo, SysMenuService } from '@/services/sys/sys-menu'
import Svgs from '@opentiny/vue-icon'
import { useRouterStore } from '@/store/modules/router'
import { isEmpty } from 'lodash-es'
import MenuFormData from '@/views/sys/menu/components/menu-form.vue'

const routeStore = useRouterStore()
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
const drawWidth = ref('50%')
const menuFormData = ref()

onMounted(async () => {
  await search()
})


//搜索查询
const search = async () => {
  const dataList = await SysMenuService.getMenus()
  dataList.forEach(data => {
    setDes(data)
  })
  if (!isEmpty(searchValue.value)) {
    let menus = [] as MenuInfo[]
    dataList.forEach(data => {
      const result = filterMenu(data)
      if (result) {
        menus.push(data)
      }
    })
    tableData.value = menus
  } else {
    tableData.value = dataList
  }
}

const setDes = (menu: MenuInfo) => {
  const des = routeStore.routerDes.find(r => r.id === menu.id)
  if (!isEmpty(des)) {
    menu.nameDes = t(des.nameDes)
    menu.desConfig = {
      page: isEmpty(des.urlDes) ? '' : `${des.urlDes}(${t('sys.menu.des.urlDes')})`,
      routing: isEmpty(des.pathDes) ? '' : `${des.pathDes}(${t('sys.menu.des.pathDes')})`,
      multilingual: isEmpty(des.nameDes) ? '' : `${des.nameDes}(${t('sys.menu.des.nameDes')})`
    }
  }
  if (!isEmpty(menu.children)) {
    menu.children.forEach(data => {
      setDes(data)
    })
  }
}

const filterMenu = (menu: MenuInfo) => {
  let isShow = false
  if (menu.nameDes.indexOf(searchValue.value) != -1
    || menu.name.indexOf(searchValue.value) != -1
    || (!isEmpty(menu.url) && menu.url.indexOf(searchValue.value) != -1)
  ) {
    isShow = true
  }
  let children = [] as MenuInfo[]
  if (!isEmpty(menu.children)) {
    menu.children.forEach(data => {
      const result = filterMenu(data)
      if (result) {
        children.push(data)
      }
    })
  }
  if (!isEmpty(children)) {
    isShow = true
  }
  menu.children = children
  return isShow
}

//打开form表单
const handleForm = (row: any, typeValue: any) => {
  drawWidth.value = '50%'
  if (typeValue == 'add') {
    drawTitle.value = t('common.text.add.data')
    menuFormData.value = {
      id: 0,
      pid: 0,
      name: '',
      url: '',
      icon: 'IconStatistics',
      sort: 0,
      desConfig: {
        page: '',
        routing: '',
        multilingual: ''
      }
    } as MenuInfo
  } else {
    drawTitle.value = t('common.text.update.data')
    menuFormData.value = row
  }
  drawVisible.value = true
}

//删除
const handleDelete = (id: number) => {
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      await routeStore.deleteMenu(id)
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
