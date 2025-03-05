<template>
  <DwmTree :show-bind=true>
    <template #breadcrumb>
      <Breadcrumb :items="['menu.dwm', 'menu.dwm.dwm-modeling']" />
    </template>
    <template #tree-right="{tableData, databaseId, tableName, databaseName}">
      <DsmOperateCreate :table-data="tableData" :data-base-id="databaseId" :table-name="tableName"
                        :data-base-name="databaseName"></DsmOperateCreate>
    </template>
  </DwmTree>
</template>

<script setup lang="ts">
import DwmTree from '@/views/data-warehouse/components/dwm-tree.vue'
import DsmOperateCreate from '@/views/data-source/components/dsm-operate-create.vue'
</script>
