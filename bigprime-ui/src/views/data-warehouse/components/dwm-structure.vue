<template>
  <DwmTree>
    <template #breadcrumb>
      <Breadcrumb :items="['menu.dwm', 'menu.dwm.dwm-structure']" />
    </template>
    <template #tree-right="{tableData, databaseId, tableName, databaseName}">
      <DsmOperateStructure :table-data="tableData" :data-base-id="databaseId" :table-name="tableName"
                        :data-base-name="databaseName"></DsmOperateStructure>
    </template>
  </DwmTree>
</template>

<script setup lang="ts">
import DwmTree from '@/views/data-warehouse/components/dwm-tree.vue'
import DsmOperateStructure from '@/views/data-source/components/dsm-operate-structure.vue'
</script>
