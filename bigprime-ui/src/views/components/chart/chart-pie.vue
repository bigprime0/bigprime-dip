<template>
  <div>
    <div style="margin: 10px 0 0 0;display: flex;">
      <span v-if="!isEmpty(title)" style="margin-left: 20px;font-size: 15px;color: #97b3f2;">{{ title }}</span>
      <div style="margin:3px 0 0 20px;">
        <slot name="default"></slot>
      </div>

    </div>

    <tiny-chart-pie v-if="data.length > 0" theme-name="bg-pie" :options="options"></tiny-chart-pie>
    <tiny-chart-pie v-if="data.length === 0" theme-name="bg-line" :data-empty="true"></tiny-chart-pie>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ChartPie as TinyChartPie } from '@opentiny/vue'
import { isEmpty } from 'lodash-es'

const data = defineModel('data', { default: [] })
//默认circle, 可选值: rect, roundRect, triangle, diamond
const icon = defineModel('icon', { default: 'circle' })
const title = defineModel('title', {default: ''})

//const themes = ref(['light', 'cloud-light', 'hdesign-light', 'bpit-light'])
const options = ref({
  type: 'pie',
  theme:'cloud-light',
  legend: {
    show: true,
    // 可滚动的图例，生成切换按钮
    type: 'scroll',
    // 切换按钮在图例的位置
    pageButtonPosition: 'end',
    // 图例的宽度
    width: '80%',
    itemHeight: 20,
    icon: icon,
    position: {
      left: 'center',
      bottom: 15
    }
  },
  label: {
    show: true,
    type: 'percent'
  },
  data: data
})
</script>
