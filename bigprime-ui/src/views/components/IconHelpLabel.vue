<template>
  <span>{{ label }}</span>
  <tiny-tooltip effect="dark" :content="content" placement="top-start">
    <HelpCircle class="icon-help"></HelpCircle>
  </tiny-tooltip>
</template>

<script setup lang="ts">
import { iconHelpCircle } from '@opentiny/vue-icon'

const HelpCircle = iconHelpCircle()
const content = defineModel('content')
const label = defineModel('label')

</script>


<style scoped lang="less">
.icon-help {
  margin-left: 2px;
}
</style>
