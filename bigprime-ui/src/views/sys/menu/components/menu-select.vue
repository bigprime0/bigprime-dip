<template>
  <tiny-select
    v-model="value"
    value-field="id"
    text-field="label"
    render-type="tree"
    clearable = "true"
    :multiple="multiple"
    :disabled="disabled"
    :tree-op="treeData"
  ></tiny-select>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { type MenuSelect, useRouterStore } from '@/store/modules/router'
import { cloneDeep, isEmpty } from 'lodash-es'

const routerStore = useRouterStore()
const { t } = useI18n()
const value = defineModel()
const multiple = defineModel('multiple', { default: false })
const disabled = defineModel('disabled', { default: false })

const treeData = ref()

onMounted(() => {
  const menus = cloneDeep(routerStore.menuSelects)
  menus.forEach(menu => {
    setMultilingual(menu)
  })
  treeData.value = { data: menus }
})

const setMultilingual = (menu: MenuSelect) => {
  menu.label = t(menu.label)
  if (!isEmpty(menu.children)) {
    menu.children.forEach(data => {
      setMultilingual(data)
    })
  }
}
</script>
