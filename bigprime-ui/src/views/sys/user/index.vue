<template>
  <div class="container-list">
    <Breadcrumb :items="['menu.sys', 'menu.sys.user']" />
    <div class="contain">
      <tiny-grid :data="tableData" :fit="true" size="medium" auto-resize border :stripe="true"
                 show-header-overflow="tooltip" show-overflow="tooltip"
                 highlight-hover-row max-height="85%">
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

        <tiny-grid-column width="50">
          <template v-slot="data">
            <tiny-user-head v-if="!isEmpty(data.row.avatarUrl)" v-model="data.row.avatarUrl" type="image" round min></tiny-user-head>
            <tiny-user-head v-else v-model="data.row.realName" class="head-item" background-color="#3ac295" color="#fff" type="label" round min></tiny-user-head>
          </template>
        </tiny-grid-column>
        <tiny-grid-column field="realName" :title="$t('sys.user.field.realName')"></tiny-grid-column>
        <tiny-grid-column field="username" :title="$t('sys.user.field.username')"></tiny-grid-column>
        <tiny-grid-column field="orgName" :title="$t('sys.user.field.orgName')"></tiny-grid-column>
        <tiny-grid-column :title="$t('sys.user.field.gender')">
          <template v-slot="data">
            <span v-if="data.row.gender === 0">{{$t('common.field.sex.man')}}</span>
            <span v-else>{{ $t('common.field.sex.woman') }}</span>
          </template>
        </tiny-grid-column>

        <tiny-grid-column field="email" :title="$t('sys.user.field.email')"></tiny-grid-column>
        <tiny-grid-column field="mobile" :title="$t('sys.user.field.mobile')"></tiny-grid-column>
        <tiny-grid-column :title="$t('sys.user.field.status')">
          <template v-slot="data">
            <tiny-switch v-model="data.row.status" :disabled="true"></tiny-switch>
          </template>
        </tiny-grid-column>


        <tiny-grid-column :title="$t('common.operations')" width="100" align="center">
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
      <tiny-pager
        :current-page="formPage.page"
        :page-size="formPage.limit"
        :total="formPage.total"
        :page-sizes="formPage.pageSizes"
        @current-change="pageChange"
        @size-change="limitChange"
        :align="formPage.align"
        :layout="formPage.layout"
      ></tiny-pager>
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
          <DataForm ref="formRef" :formData="formData"></DataForm>
        </suspense>
      </div>
    </tiny-drawer>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import emitter from '@/utils/evnetbus'
import DataForm from '@/views/sys/user/components/user-form.vue'
import { Modal, Notify } from '@opentiny/vue'
import { SysUserService } from '@/services/sys/sys-user'
import { isEmpty } from 'lodash-es'
import { formPage } from '@/utils/tool'

const { t } = useI18n()
const tableData = ref()
const searchValue = ref('')
const drawVisible = ref(false)
const drawTitle = ref('')
const formRef = ref()
const formData = ref()

onMounted(() => {
  search()
})

const pageChange = (page: number) => {
  formPage.value.page = page
  search()
}

const limitChange = (limit: number) => {
  formPage.value.limit = limit
  search()
}

//搜索查询
const search = async () => {
  const result = await SysUserService.getPage({
    search: searchValue.value,
    page: formPage.value.page,
    limit: formPage.value.limit
  })
  tableData.value = result.list
  formPage.value.total = result.total
}

//打开form表单
const handleForm = (row: any, typeValue: any) => {
  if (typeValue == 'add') {
    drawTitle.value = t('common.text.add.data')
    formData.value = { id: 0, status: 1, gender: 0, orgId:0, password:'123456',roleIdList:[],superAdmin:0 }
  } else {
    drawTitle.value = t('common.text.update.data')
    if(isEmpty(row.roleIdList)){
      row.roleIdList = []
    }
    formData.value = row
  }
  drawVisible.value = true
}

const handleDelete = (id: number) => {
  Modal.confirm(t('common.prompt.delete')).then(async (res: string) => {
    if (res === 'confirm') {
      await SysUserService.deleteById(id)
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
const handleDrawerClose = () => {
  drawVisible.value = false
  search()
}

//监听抽屉子组件的取消
emitter.on('drawerCancel', handleDrawerClose)

</script>

<style scoped lang="less">
.span-font{
  color: #3ac295;
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
