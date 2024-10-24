<template>
  <div class="container-list">
    <Breadcrumb :items="['menu.integration', 'menu.integration.setting']" />
    <div class="contain">
      <tiny-split v-model="ratio" trigger-simple disabled>
        <template #left>
          <div class="demo-split-pane">
            <CategoryTree :selectNode="selectNode" />
          </div>
        </template>
        <template #right>
          <div class="demo-split-pane">
            <ConfigList />
          </div>
        </template>
      </tiny-split>
    </div>
  </div>
</template>
<script setup lang="ts">

import { useI18n } from 'vue-i18n'
import { ref } from 'vue'
import CategoryTree from '@/views/integration/setting/category-tree.vue'
import ConfigList from '@/views/integration/setting/config-list.vue'
import type { integrationCategoryInfo } from '@/services/integration/integration-config'

const { t } = useI18n()
const ratio = ref(0.2)

const selectNode = ref<integrationCategoryInfo>({
  id: 0,
  pid: 0,
  label: '',
  isLeaf: 0,
  isAdd: 0,
  route: '',
  category: '',
  children: []
})
</script>

<style scoped lang="less">
.container-list {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100%;
  overflow-x: hidden;
  overflow-y: auto;
}

.contain {
  flex: 1 1 auto;
  margin: 8px 10px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 0 8px 8px rgba(169, 174, 184, 0.05);
  padding: 10px;
}

.demo-split-pane {
  padding: 10px;
}
</style>
