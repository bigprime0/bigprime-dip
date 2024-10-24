<template>
  <tiny-select
    v-model="value"
    value-field="id"
    text-field="username"
    render-type="tree"
    :multiple="multiple"
    :tree-op="treeData"
  ></tiny-select>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { SysUserService } from '@/services/sys/sys-user'
const value = defineModel()
const multiple = defineModel('multiple', { default: false })

const treeData = ref()

onMounted(async () => {
  const data = await SysUserService.getList()
  treeData.value = { data: data }
})
</script>
