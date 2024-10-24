<template>
  <div>
    <tiny-tree-menu
      ref="tree"
      accordion
      :data="treeDataFilter"
      :show-filter="false"
      node-key="id"
      :indent="10"
      ellipsis
      :default-expanded-keys="[expandedArr]"
      @current-change="currentChange"
      :show-title=false
      :default-expand-all=false
    >
      <template #default="slotScope">
        <template v-for="(item, index) in routerStore.routerDes" :key="index">
          <span v-if="slotScope.label === item.nameDes">
            <component :is="Svgs[item.icon] && Svgs[item.icon]()"></component>
            <span class="span-font" :title="$t(item.nameDes)">{{ $t(item.nameDes) }}</span>
          </span>
        </template>
      </template>
    </tiny-tree-menu>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref, watch } from 'vue'
import type { RouteRecordNormalized } from 'vue-router'
import Svgs from '@opentiny/vue-icon'
import { TreeMenu as tinyTreeMenu } from '@opentiny/vue'
import router from '@/router'
import { useRouterStore } from '@/store/modules/router'

const routerStore = useRouterStore()

// 获取路由数据
const appRoute = computed(() => {
  return router
    .getRoutes()
    .find((el) => el.name === 'home') as RouteRecordNormalized
})
const treeDataFilter = JSON.parse(JSON.stringify(appRoute.value.children))
// 菜单按路由定义的order顺序显示
treeDataFilter.sort((a: any, b: any) => {
  return (a.meta.order || 0) - (b.meta.order || 0)
})

const tree = ref()
const expandedArr = ref()

watch(
  () => router.currentRoute.value.path,
  (newValue) => {
    let data = newValue.split('/')
    let result = data[data.length - 1]
    const characters = [...result]
    characters[0] = characters[0]?.toUpperCase()
    expandedArr.value = characters.join('')
    setTimeout(() => {
      tree?.value?.$refs?.tree?.setCurrentNode({ id: expandedArr.value })
    }, 0)
  },
  { immediate: true }
)

const currentChange = (data: any) => {
  if (routerStore.filter.indexOf(data.id) === -1 && data.children.length === 0) {
    router.push({ name: data.id })
  }
}
</script>

<style lang="less" scoped>
.span-font{
  font-size: 13px;
}


:deep(.tiny-tree-node__content:hover) {
  //background-color: var(--ti-tree-node-content-hover-bg-color) !important;
  background: #e9edfa !important;
}

:deep(.tiny-tree-menu .tiny-tree .tiny-tree-node.is-current>.tiny-tree-node__content .tree-node-body) {
  color: #526ecc;
}

:deep(.tiny-tree-menu .tiny-tree .tiny-tree-node.is-current>.tiny-tree-node__content .tiny-tree-node__content-left::before) {
  left: 10px;
  border-left: 2px solid #526ecc;
}

:deep(.tiny-tree-node__wrapper > .is-expanded > .tiny-tree-node__children
  .tiny-tree-node__wrapper .is-current .tiny-tree-node__content .tiny-tree-node__content-left:before) {
  left: 50px;
  border-left: 2px solid #526ecc;
}

:deep(.tiny-tree-node > .tiny-tree-node__content) {
  margin-left: 0 !important;
}

.tiny-tree-menu {
  width: 250px;
}


.tiny-tree-menu
.tiny-tree
.tiny-tree-node.is-current
> .tiny-tree-node__content
.tree-node-name
.tiny-svg {
  //fill: var(--ti-base-icon-color);
}
</style>
