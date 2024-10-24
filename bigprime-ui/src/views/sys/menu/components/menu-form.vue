<template>
  <div class="ds-form">
    <tiny-form ref="menuFormRef" overflow-title :model="menuFormData" :rules="formRules" label-width="100px">
      <tiny-divider content-position="left" content-color="#7693f5">{{ $t('common.field.divider') }}
      </tiny-divider>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.menu.field.parent.name')">
            <tiny-input v-model="menuFormData.id" v-show="false"></tiny-input>
            <MenuSelect v-model="menuFormData.pid" />
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.menu.field.icon')">
            <component :is="Svgs[menuFormData.icon] && Svgs[menuFormData.icon]()" class="svgs-icon"></component>
            <span class="span-font">（{{ menuFormData.icon }}）</span>
            <tiny-button type="text" :icon="TinyIconSearch" text="选择图标" @click="handleIcon"></tiny-button>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.menu.field.name')" :show-message="false" prop="name">
            <tiny-input v-model="menuFormData.name"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.menu.field.name.des')">
            <tiny-input :disabled="true" :placeholder="$t(`${menuFormData.desConfig?.multilingual}`)"></tiny-input>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

      <tiny-row>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.menu.field.url')" :show-message="false" prop="url">
            <tiny-input v-model="menuFormData.url"></tiny-input>
          </tiny-form-item>
        </tiny-col>
        <tiny-col :span="6">
          <tiny-form-item :label="$t('sys.menu.field.sort')">
            <tiny-numeric type="number" v-model="menuFormData.sort" mouse-wheel="true" min="0"
                          precision="0"></tiny-numeric>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>

<!--      <tiny-row v-if="showAuthority === true">-->
<!--        <tiny-col :span="6">-->
<!--          <tiny-form-item :label="$t('sys.menu.field.authority')" :show-message="false" prop="url">-->
<!--            <tiny-input v-model="menuFormData.authority"></tiny-input>-->
<!--          </tiny-form-item>-->
<!--        </tiny-col>-->
<!--      </tiny-row>-->


      <tiny-divider content-position="left" content-color="#7693f5">{{ $t('sys.menu.field.config') }}
      </tiny-divider>

      <tiny-row>
        <tiny-col :span="12">
          <tiny-form-item :label="$t('sys.menu.des.urlDes')">
            <span class="span-font">-> </span>
            <span class="span-des">{{ menuFormData.desConfig?.page }}</span>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>
      <tiny-row>
        <tiny-col :span="12">
          <tiny-form-item :label="$t('sys.menu.des.pathDes')">
            <span class="span-font">-> </span>
            <span class="span-des">{{ menuFormData.desConfig?.routing }}</span>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>
      <tiny-row>
        <tiny-col :span="12">
          <tiny-form-item :label="$t('sys.menu.des.nameDes')">
            <span class="span-font">-> </span>
            <span class="span-des">{{ menuFormData.desConfig?.multilingual }}</span>
          </tiny-form-item>
        </tiny-col>
      </tiny-row>


      <br />
      <tiny-row style="text-align: center">
        <tiny-button plain type="primary" @click="handleCancel"> {{ $t('common.operations.cancel') }}
        </tiny-button>
        <tiny-button :reset-time="500" type="primary" @click="handleSave"> {{ $t('common.operations.save') }}
        </tiny-button>
      </tiny-row>
    </tiny-form>
  </div>

  <div>
    <tiny-drawer :title="$t('sys.menu.field.icon')"
                 :visible="drawVisible"
                 @update:visible="drawVisible = $event"
                 :show-header="true"
                 :mask-closable="false"
                 width="50%"
                 @close="iconDrawerClose">
      <div>
        <suspense>
          <MenuIcon ref="iconRef" v-model="menuFormData.icon" />
        </suspense>
      </div>

    </tiny-drawer>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch, watchEffect } from 'vue'
import emitter from '@/utils/evnetbus'
import { useI18n } from 'vue-i18n'
import { Modal } from '@opentiny/vue'
import { type MenuInfo } from '@/services/sys/sys-menu'
import { useRouterStore } from '@/store/modules/router'
import MenuSelect from '@/views/sys/menu/components/menu-select.vue'
import MenuIcon from '@/views/sys/menu/components/menu-icon.vue'
import Svgs from '@opentiny/vue-icon'
import { iconSearch } from '@opentiny/vue-icon'
import { isEmpty, toLower, upperFirst } from 'lodash-es'

const TinyIconSearch = iconSearch()
const routerStore = useRouterStore()
const { t } = useI18n()

const menuFormRef = ref()
const menuFormData = defineModel<MenuInfo>('menuFormData', { default: {} })

const drawVisible = ref(false)
const iconRef = ref()
// const showAuthority = ref(false)

watchEffect(() => {
  menuFormData.value.name = upperFirst(menuFormData.value?.name)
  const name = menuFormData.value.name
  const des = routerStore.routerDes?.find(f => f.id === menuFormData.value.pid)
  if (!isEmpty(des)) {
    menuFormData.value.desConfig.routing = `${des.name}${isEmpty(name) ? '' : `/${name}`}`
    menuFormData.value.desConfig.multilingual = `${des.nameDes}${isEmpty(name) ? '' : `.${toLower(name)}`}`
  } else if (!isEmpty(name)) {
    menuFormData.value.desConfig.routing = name
    menuFormData.value.desConfig.multilingual = `menu.${toLower(name)}`
  } else {
    if (!isEmpty(menuFormData.value.desConfig?.routing)) {
      menuFormData.value.desConfig.routing = ''
    }
    if (!isEmpty(menuFormData.value.desConfig?.multilingual)) {
      menuFormData.value.desConfig.multilingual = ''
    }
  }

  if (!isEmpty(menuFormData.value.url)) {
    // showAuthority.value = true
    menuFormData.value.desConfig.page = `@/views/${menuFormData.value.url}.vue`
  } else {
    if (!isEmpty(menuFormData.value.desConfig?.page)) {
      menuFormData.value.desConfig.page = ''
    }
    // showAuthority.value = false
  }

  if (menuFormData.value.id === 0) {
    const prentMenu = routerStore.routerDes.filter(f => f.pid === menuFormData.value.pid)
    if (!isEmpty(prentMenu)) {
      menuFormData.value.sort = prentMenu.length + 1
    } else {
      menuFormData.value.sort = 1
    }
  }
})

const formRules = computed(() => {
  let ruleList = {
    name: [{ required: true, message: t('common.prompt.required'), trigger: 'blur' }]
  }
  return ruleList
})

const handleIcon = () => {
  drawVisible.value = true
}

const iconDrawerClose = () => {
  drawVisible.value = false
}
emitter.on('iconCancel', iconDrawerClose)

const handleSave = () => {
  menuFormRef.value
    .validate()
    .then(async () => {
      if (menuFormData.value.sort === 0) {
        menuFormData.value.sort = 1
      }
      await routerStore.saveMenu(menuFormData.value)
      Modal.message({
        message: t('common.prompt.save.success'),
        status: 'success'
      })
      handleCancel()
    })
}
const handleCancel = () => {
  emitter.emit('drawerCancel')
}


</script>

<style scoped lang="less">
.span-font {
  font-size: 13px;
  color: #000000;
}

.span-des {
  font-size: 13px;
  color: #2f5bea;
}

.container-split {
  height: calc(100vh - 110px);
  border: 1px solid #d9d9d9;
}

.ds-choose {
  :deep(.tiny-collapse) {
    padding: 0 10px;
    border-top: 0;
    border-bottom: 0;
  }
}

.ds-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: normal;

  :deep(.tiny-card) {
    width: 100px;
  }

  :deep(.tiny-card.tiny-card--default) {
    margin: 3px;
    cursor: pointer;
  }

  :deep(.tiny-card.tiny-card--success) {
    border: 2px solid #3ac295;
  }

  :deep(img) {
    height: 50px;
  }

  :deep(.tiny-card .tiny-card__body .tiny-card--logo.tiny-card--mini-padding) {
    padding: 0px;
  }
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
