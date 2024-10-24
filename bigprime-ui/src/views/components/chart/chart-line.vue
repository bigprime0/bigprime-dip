<template>
  <div>
    <div v-if="!isEmpty(title)" style="margin: 10px 0 0 0;">
      <span style="margin-left: 20px;font-size: 15px;color: #97b3f2;">{{ title }}</span>
    </div>
    <tiny-chart-line v-if="data.length > 0" theme-name="bg-line" :options="options"></tiny-chart-line>
    <tiny-chart-line v-if="data.length === 0" theme-name="bg-line" :data-empty="true"></tiny-chart-line>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ChartLine as TinyChartLine } from '@opentiny/vue'
import { isEmpty } from 'lodash-es'

const data = defineModel('data', { default: [] })
const title = defineModel('title', {default: ''})
const options = ref({
  theme:'light',
  padding: [50, 30, 50, 20],
  smooth: true, // smooth属性配置曲线
  area:true,
  legend: {
    show: true,
    icon: 'line'
  },
  data: data
})
</script>
