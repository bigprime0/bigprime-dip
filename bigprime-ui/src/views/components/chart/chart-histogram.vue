<template>
  <div>
    <div v-if="!isEmpty(title)" style="margin: 10px 0 0 0;">
      <span style="margin-left: 20px;font-size: 15px;color: #97b3f2;">{{ title }}</span>
    </div>
    <tiny-chart-histogram v-if="data.length > 0" theme-name="bg-histogram" :options="options"></tiny-chart-histogram>
    <tiny-chart-histogram v-if="data.length === 0" theme-name="bg-histogram" :data-empty="true"></tiny-chart-histogram>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ChartHistogram as TinyChartHistogram } from '@opentiny/vue'
import { isEmpty } from 'lodash-es'

const data = defineModel('data', { default: [] })
const title = defineModel('title', {default: ''})

//const themes = ref(['light', 'cloud-light', 'hdesign-light', 'bpit-light'])
const options = ref({
  theme:'bpit-light',
  data: data
})
</script>
