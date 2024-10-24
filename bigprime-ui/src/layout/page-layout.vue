<template>
  <router-view v-slot="{ Component, route }">
    <component :is="Component" :key="route.fullPath" />
    <DgpHome />
  </router-view>
</template>

<script lang="ts" setup>
import DgpHome from '@/layout/home.vue'
</script>

<style lang="less"></style>
