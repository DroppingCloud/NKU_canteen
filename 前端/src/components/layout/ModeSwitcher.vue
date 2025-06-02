<script setup>
import { ref, onMounted, computed, watch, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useAppModeStore } from '../../store/appMode'
import { useRouter } from 'vue-router'

// 获取应用模式store
const appModeStore = useAppModeStore()
const router = useRouter()

// 检查用户是否是管理员
const isAdmin = ref(false)

// 从LocalStorage中获取用户信息并检查是否为管理员
onMounted(() => {
  checkIsAdmin()
  
  // 添加localStorage事件监听
  window.addEventListener('storage', handleStorageChange)
  
  // 模拟一个自定义事件，用于监听程序内部的localStorage变化
  window.addEventListener('localStorageChanged', checkIsAdmin)
})

// 组件销毁时移除事件监听
onUnmounted(() => {
  window.removeEventListener('storage', handleStorageChange)
  window.removeEventListener('localStorageChanged', checkIsAdmin)
})

// 处理localStorage变化事件
const handleStorageChange = (event) => {
  if (event.key === 'userInfo' || event.key === 'token') {
    checkIsAdmin()
  }
}

// 检查当前用户是否为管理员
const checkIsAdmin = () => {
  const storedUserInfo = localStorage.getItem('userInfo')
  const token = localStorage.getItem('token')
  
  // 如果没有token，肯定不是管理员
  if (!token) {
    isAdmin.value = false
    
    // 如果当前是管理模式，则自动切换回用户模式
    if (appModeStore.isAdminMode) {
      appModeStore.toggleAdminMode(false)
    }
    return
  }
  
  if (storedUserInfo) {
    try {
      const userInfo = JSON.parse(storedUserInfo)
      // 根据user_type字段判断是否为管理员
      isAdmin.value = userInfo.user_type === 'admin' || userInfo.user_type === 1
      
      // 如果用户不是管理员但当前是管理模式，则自动切换回用户模式
      if (!isAdmin.value && appModeStore.isAdminMode) {
        appModeStore.toggleAdminMode(false)
      }
    } catch (error) {
      console.error('解析用户信息失败:', error)
      isAdmin.value = false
      
      // 出错时也切换回用户模式
      if (appModeStore.isAdminMode) {
        appModeStore.toggleAdminMode(false)
      }
    }
  } else {
    isAdmin.value = false
    
    // 如果当前是管理模式，则自动切换回用户模式
    if (appModeStore.isAdminMode) {
      appModeStore.toggleAdminMode(false)
    }
  }
}

// 拉动吊牌
const toggleMode = () => {
  // 再次检查是否为管理员，防止状态不同步
  checkIsAdmin()
  
  // 检查是否为管理员
  if (!isAdmin.value) {
    ElMessage.warning('您没有管理员权限')
    return
  }
  
  // 切换前的状态
  const wasAdminMode = appModeStore.isAdminMode
  
  // 使用store更新状态
  appModeStore.toggleAdminMode(!wasAdminMode)
  
  // 根据新状态导航到对应页面
  if (!wasAdminMode) {
    // 从用户模式切换到管理模式，导航到管理页面
    router.push('/admin')
  } else {
    // 从管理模式切换到用户模式，导航到首页
    router.push('/')
  }
  
  ElMessage.success(`已切换至${appModeStore.isAdminMode ? '管理' : '用户'}模式`)
}

// 显示的文本内容
const modeText = computed(() => {
  return appModeStore.isAdminMode ? '管理模式' : '用户模式'
})
</script>

<template>
  <div class="mode-switcher">
    <div class="rope-top"></div>
    <div class="tag-container" :class="{ 'admin-mode': appModeStore.isAdminMode }">
      <div class="tag" @click="toggleMode">
        <div class="tag-text">{{ modeText }}</div>
        <div class="rope">
          <div class="rope-end"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.mode-switcher {
  position: fixed;
  right: 40px;
  top: 50px; /* 顶栏高度 */
  z-index: 999;
  height: 200px; /* 顶栏高度 */
  pointer-events: none; /* 让鼠标事件穿透到下层元素 */
}

/* 顶部拉绳 */
.rope-top {
  position: absolute;
  bottom: 0; /* 位于顶栏底部 */
  left: 50%;
  transform: translateX(-50%);
  width: 1px;
  height: 100%; /* 等于顶栏高度 */
  background-color: #bd9cc9;
}

.tag-container {
  position: absolute;
  top: 200px; /* 定位在顶栏底部 */
  left: 50%;
  transform: translateX(-50%);
  transition: transform 0.3s ease;
  transform-origin: top center;
  pointer-events: auto; /* 恢复鼠标事件 */
}

.tag-container:hover {
  transform: translateX(-50%) scale(1.05);
}

.tag {
  position: relative;
  width: 60px;
  height: 25px;
  background-color: #f0e6f5;
  border-radius: 4px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #d8c7e0;
}

.tag-text {
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
  font-size: 12px;
  font-weight: bold;
  color: #800080;
  text-align: center;
  line-height: 25px;
  width: 100%;
}

.rope {
  position: absolute;
  top: -40px;
  left: 50%;
  margin-left: -0.5px;
  width: 1px;
  height:40px;
  background-color: #bd9cc9;
  z-index: 1;
}

.rope-end {
  position: absolute;
  bottom: 0px;
  left: -2px;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background-color: #bd9cc9;
}

.tag-container.admin-mode .tag {
  background-color: #800080;
  border-color: #590359;
}

.tag-container.admin-mode .tag-text {
  color: #fff;
}

/* 拉动效果 */
.tag-container:active {
  transform: translateX(-50%) translateY(10px);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .mode-switcher {
    right: 10px;
  }
  
  .tag {
    width: 50px;
    height: 20px;
  }
  
  .tag-text {
    font-size: 10px;
    line-height: 20px;
  }
}
</style> 