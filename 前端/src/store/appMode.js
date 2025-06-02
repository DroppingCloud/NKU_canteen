import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppModeStore = defineStore('appMode', () => {
  // 是否处于管理员模式
  const isAdminMode = ref(false)
  
  // 切换模式
  function toggleAdminMode(value) {
    isAdminMode.value = value
  }
  
  return {
    isAdminMode,
    toggleAdminMode
  }
}) 