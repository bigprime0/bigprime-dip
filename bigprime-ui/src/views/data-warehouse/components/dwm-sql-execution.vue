<template>
  <DwmTree>
    <template #breadcrumb>
      <Breadcrumb :items="['menu.dwm', 'menu.dwm.dwm-sql-execution']" />
    </template>
    <template #tree-right="{tableData, databaseId, tableName, databaseName}">
      <DsmOperateSql :table-data="tableData" :data-base-id="databaseId" :table-name="tableName"></DsmOperateSql>
    </template>
  </DwmTree>
</template>

<script setup lang="ts">
import DwmTree from '@/views/data-warehouse/components/dwm-tree.vue'
import DsmOperateSql from '@/views/data-source/components/dsm-operate-sql.vue'
</script>

