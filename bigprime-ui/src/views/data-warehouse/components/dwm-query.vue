<template>
  <DwmTree>
    <template #breadcrumb>
      <Breadcrumb :items="['menu.dwm', 'menu.dwm.dwm-query']" />
    </template>
    <template #tree-right="{tableData, databaseId, tableName}">
      <DsmOperateQuery :table-data="tableData" :data-base-id="databaseId" :table-name="tableName"></DsmOperateQuery>
    </template>
  </DwmTree>
</template>

<script setup lang="ts">
import DwmTree from '@/views/data-warehouse/components/dwm-tree.vue'
import DsmOperateQuery from '@/views/data-source/components/dsm-operate-query.vue'
</script>

