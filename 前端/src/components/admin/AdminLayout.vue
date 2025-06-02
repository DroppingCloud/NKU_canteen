<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAppModeStore } from '../../store/appMode'

const router = useRouter()
const route = useRoute()
const appModeStore = useAppModeStore()

// 管理选项栏
const adminModules = ['食堂管理', '档口管理', '菜品管理']
const activeModule = ref('食堂管理')

// 处理模块切换
const handleModuleClick = (module) => {
  activeModule.value = module
  
  if (module === '食堂管理') {
    router.push('/admin/canteens')
  } else if (module === '档口管理') {
    router.push('/admin/stalls')
  } else if (module === '菜品管理') {
    router.push('/admin/dishes')
  }
}

// 当前路径决定活动模块
const setActiveModuleFromRoute = () => {
  const path = route.path
  if (path.includes('/admin/canteens')) {
    activeModule.value = '食堂管理'
  } else if (path.includes('/admin/stalls')) {
    activeModule.value = '档口管理'
  } else if (path.includes('/admin/dishes')) {
    activeModule.value = '菜品管理'
  }
}

// 初始化时设置当前模块
setActiveModuleFromRoute()
</script>

<template>
  <div class="admin-container">
    <!-- 管理选项栏 -->
    <div class="admin-modules">
      <div 
        v-for="module in adminModules" 
        :key="module"
        class="module-item"
        :class="{ active: activeModule === module }"
        @click="handleModuleClick(module)"
      >
        {{ module }}
      </div>
    </div>
    
    <!-- 管理内容区域 -->
    <div class="admin-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped>
.admin-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
}

.admin-modules {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
  background-color: #fff;
  padding: 0 20px;
}

.module-item {
  padding: 15px 30px;
  font-size: 16px;
  cursor: pointer;
  position: relative;
  color: #666;
  transition: all 0.3s;
}

.module-item:hover {
  color: #800080;
}

.module-item.active {
  color: #800080;
  font-weight: bold;
}

.module-item.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 20%;
  width: 60%;
  height: 2px;
  background-color: #800080;
}

.admin-content {
  flex: 1;
  padding: 20px;
  background-color: #f9f9f9;
  min-height: 500px;
}
</style> 