<template>
  <tiny-select
    v-model="dataBaseId"
    :placeholder="$t('datasource.select.data.source')"
    clearable
    filterable
  >
    <tiny-option v-for="item in data" :key="item.id" :label="item.name" :value="item.id">
    </tiny-option>
  </tiny-select>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watchEffect } from 'vue'
import { find, isEmpty } from 'lodash-es'
import { SourceService } from '@/services/spi/source'

const dataBaseId = defineModel('dataBaseId')
const query = defineModel('query', {
  default: {
    search: '',
    config: false,
    protocol: 'JDBC'
  }
})
const data = ref()

const dbSource = computed(() => {
  return find(data.value, { 'id': dataBaseId.value })
})



onMounted(async () => {
  data.value = await SourceService.getList(query.value)
})

watchEffect(() => {
  if (dataBaseId.value === 0) {
    dataBaseId.value = undefined
  }
})

defineExpose({
  dataBaseId,
  dbSource
})
</script>
