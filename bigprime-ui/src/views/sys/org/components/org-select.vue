<template>
  <tiny-select
    v-model="pid"
    value-field="id"
    text-field="name"
    render-type="tree"
    :multiple="multiple"
    :tree-op="treeData"
  ></tiny-select>
</template>

<script setup lang="ts">
import { SysOrgService } from '@/services/sys/sys-org'
import { onMounted, ref } from 'vue'

const pid = defineModel()
const multiple = defineModel('multiple', { default: false })

const treeData = ref()

onMounted(async () => {
  const list = await SysOrgService.getList()
  treeData.value = {
    data: list
  }
})

</script>

