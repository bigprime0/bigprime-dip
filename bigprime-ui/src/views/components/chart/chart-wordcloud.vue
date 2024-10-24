<template>
  <div>
<!--    <div v-if="!isEmpty(title)" style="margin: 10px 0 -20px 0;">
      <span style="margin-left: 20px;font-size: 15px;color: #97b3f2;">{{ title }}</span>
    </div>-->
    <tiny-chart-word-cloud theme-name="bg-word-cloud" :options="options"></tiny-chart-word-cloud>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ChartWordcloud as TinyChartWordCloud } from '@opentiny/vue'
/*import { isEmpty } from 'lodash-es'*/


const data = defineModel('data', { default: [] })
const title = defineModel('title', {default: ''})

const options = ref({
  data: [
    {
      name: '金融',
      value: 1
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    },
    {
      name: '财务',
      value: 2
    }
  ]
})
</script>
