<template>
  <tiny-select
    v-model="value"
    value-field="id"
    text-field="name"
    render-type="tree"
    :multiple="multiple"
    :tree-op="treeData"
  ></tiny-select>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { SysRoleService } from '@/services/sys/sys-role'
const value = defineModel()
const multiple = defineModel('multiple', { default: false })

const treeData = ref()

onMounted(async () => {
  const data = await SysRoleService.all()
  treeData.value = { data: data }
})
</script>
