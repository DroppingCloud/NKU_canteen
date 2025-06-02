<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Search, Location } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import LoginModal from '../LoginModal.vue'
import UserProfileCard from '../UserProfileCard.vue'
import { logout } from '../../api/api'
import { useAppModeStore } from '../../store/appMode'

const router = useRouter()
const route = useRoute()
const appModeStore = useAppModeStore()

// 当前选中的选项
const activeModule = ref('食堂')
const modules = ['食堂', '档口', '菜品']

// 用户登录状态
const isLoggedIn = ref(false)
const userInfo = ref({
  name: '游客',
  avatar: 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/3d_4.png'
})

// 登录弹窗显示控制
const loginModalVisible = ref(false)
// 个人详情弹窗显示控制
const profileCardVisible = ref(false)

// 从LocalStorage中恢复用户登录状态
onMounted(() => {
  const storedToken = localStorage.getItem('token')
  const storedUserInfo = localStorage.getItem('userInfo')
  
  if (storedToken && storedUserInfo) {
    try {
      const parsedUserInfo = JSON.parse(storedUserInfo)
      console.log('恢复的用户信息:', parsedUserInfo)
      
      isLoggedIn.value = true
      userInfo.value = {
        name: parsedUserInfo.nickname || '用户',
        // 如果有头像地址则使用，否则使用默认头像
        avatar: parsedUserInfo.avatar && parsedUserInfo.avatar !== '' 
          ? parsedUserInfo.avatar 
          : 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/3d_4.png',
        id: parsedUserInfo.user_id
      }
      
      console.log('恢复的用户头像:', userInfo.value.avatar)
    } catch (error) {
      console.error('解析用户信息失败:', error)
    }
  }
})

// 根据路由自动设置当前选中的选项
watch(() => route.name, (newRouteName) => {
  if (newRouteName === 'Home') {
    activeModule.value = '食堂'
  } else if (newRouteName === 'Canteen') {
    activeModule.value = '档口'
  } else if (newRouteName === 'Booth') {
    activeModule.value = '菜品'
  } else if (newRouteName?.toString().startsWith('Admin')) {
    // 管理页面模式下，根据具体路径设置活动模块
    if (route.path.includes('/admin/canteens')) {
      activeModule.value = '食堂'
    } else if (route.path.includes('/admin/stalls')) {
      activeModule.value = '档口'
    } else if (route.path.includes('/admin/dishes')) {
      activeModule.value = '菜品'
    }
  }
}, { immediate: true })

// 监听管理模式变化
watch(() => appModeStore.isAdminMode, (isAdmin) => {
  // 当模式变化时更新路由
  setActiveModuleFromRoute()
})

// 导航路径
const breadcrumbs = computed(() => {
  const paths = [{ name: 'Home', path: '/', label: '首页' }]
  
  if (route.name === 'Canteen' && route.params.id) {
    paths.push({
      name: 'Canteen',
      path: `/canteen/${route.params.id}`,
      label: '食堂'
    })
  } else if (route.name === 'Booth' && route.params.id) {
    // 这里我们只有档口ID，不知道对应的食堂ID
    // 保留食堂层级的面包屑，但点击时需要从Booth信息中获取canteen_id
    paths.push({
      name: 'Canteen',
      path: `#`, // 先使用占位符，实际跳转由handleBreadcrumbClick处理
      label: '食堂' 
    })
    paths.push({
      name: 'Booth',
      path: `/booth/${route.params.id}`,
      label: '档口'
    })
  }
  
  return paths
})

// 搜索功能（预留）
const searchInput = ref('')
const handleSearch = () => {
  console.log('搜索:', searchInput.value)
  // 预留搜索逻辑
}

// 处理模块点击事件
const handleModuleClick = (module) => {
  // 获取当前所在的级别
  let currentLevel = 0
  if (route.name === 'Canteen') {
    currentLevel = 1
  } else if (route.name === 'Booth') {
    currentLevel = 2
  }
  
  // 获取目标级别
  const targetLevel = modules.indexOf(module)
  
  // 管理模式下允许任意切换，普通模式下只允许向上级切换
  if (appModeStore.isAdminMode) {
    activeModule.value = module
    
    // 管理模式下根据选择的模块导航到相应页面
    if (module === '食堂') {
      router.push('/admin/canteens')
    } else if (module === '档口') {
      router.push('/admin/stalls')
    } else if (module === '菜品') {
      router.push('/admin/dishes')
    }
    return
  }
  
  if (targetLevel < currentLevel) {
    activeModule.value = module
    
    // 普通用户模式
    if (module === '食堂') {
      router.push('/')
    } else if (module === '档口' && route.name === 'Booth') {
      // 需要从localstorage获取当前档口对应的食堂ID
      const boothStallId = route.params.id
      const canteenId = localStorage.getItem(`booth_${boothStallId}_canteenId`)
      if (canteenId) {
        router.push(`/canteen/${canteenId}`)
      } else {
        // 如果没有记录，返回首页
        router.push('/')
        ElMessage.warning('无法获取食堂信息，已返回首页')
      }
    }
  }
}

// 处理面包屑导航点击
const handleBreadcrumbClick = (path) => {
  if (path === '#' && route.name === 'Booth') {
    // 从当前档口获取食堂ID
    const boothStallId = route.params.id
    const canteenId = localStorage.getItem(`booth_${boothStallId}_canteenId`)
    if (canteenId) {
      router.push(`/canteen/${canteenId}`)
    } else {
      // 如果没有记录，返回首页
      router.push('/')
      ElMessage.warning('无法获取食堂信息，已返回首页')
    }
  } else {
    router.push(path)
  }
}

// 处理登录
const handleLogin = () => {
  console.log('点击登录按钮', isLoggedIn.value)
  if (!isLoggedIn.value) {
    console.log('打开登录弹窗')
    loginModalVisible.value = true
  }
}

// 处理注销
const handleLogout = async () => {
  if (isLoggedIn.value) {
    try {
      await logout()
      isLoggedIn.value = false
      userInfo.value = {
        name: '游客',
        avatar: 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/3d_4.png'
      }
      ElMessage.success('已退出登录')
      
      // 如果当前页面需要登录权限，则跳转到首页
      if (route.meta?.requiresAuth) {
        router.push('/')
      }
    } catch (error) {
      console.error('注销失败:', error)
      ElMessage.error('注销失败，请稍后重试')
    }
  }
}

// 处理查看个人详情
const handleViewProfile = () => {
  if (isLoggedIn.value) {
    profileCardVisible.value = true
  }
}

// 处理用户信息更新
const handleProfileUpdated = (updatedInfo) => {
  console.log('用户信息已更新:', updatedInfo)
  // 更新头部显示的用户名
  if (updatedInfo.nickname) {
    userInfo.value.name = updatedInfo.nickname
  }
  ElMessage.success('个人信息已更新')
}

// 处理登录提交
const onLoginSubmit = (userData) => {
  console.log('登录表单提交:', userData)
  isLoggedIn.value = true
  
  // 从后端返回数据中获取用户信息
  userInfo.value = {
    name: userData.nickname || '用户',
    // 如果有头像地址则使用，否则使用默认头像
    avatar: userData.avatar && userData.avatar !== '' 
      ? userData.avatar 
      : 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/3d_4.png',
    id: userData.user_id
  }
  
  console.log('用户头像地址:', userInfo.value.avatar)
  loginModalVisible.value = false
  ElMessage.success(`欢迎回来，${userInfo.value.name}！`)
}

// 处理注册提交
const onRegisterSubmit = (userData) => {
  console.log('注册成功，用户将进行登录')
  // 注册成功后会自动切换到登录页，不需要在这里做额外处理
}

// 关闭登录窗口
const closeLoginModal = () => {
  loginModalVisible.value = false
}

// 关闭个人信息卡片
const closeProfileCard = () => {
  profileCardVisible.value = false
}

// 获取当前路径决定活动模块
const setActiveModuleFromRoute = () => {
  if (route.name === 'Home') {
    activeModule.value = '食堂'
  } else if (route.name === 'Canteen') {
    activeModule.value = '档口'
  } else if (route.name === 'Booth') {
    activeModule.value = '菜品'
  } else if (route.name?.toString().startsWith('Admin')) {
    // 管理页面模式下，根据具体路径设置活动模块
    if (route.path.includes('/admin/canteens')) {
      activeModule.value = '食堂'
    } else if (route.path.includes('/admin/stalls')) {
      activeModule.value = '档口'
    } else if (route.path.includes('/admin/dishes')) {
      activeModule.value = '菜品'
    }
  }
}
</script>

<template>
  <header class="app-header">
    <div class="header-container">
      <!-- 用户头像 -->
      <div class="user-avatar-container">
        <div class="user-avatar">
          <el-avatar :size="80" :src="userInfo.avatar" />
        </div>
        <div class="avatar-menu">
          <div class="menu-item login" :class="{ disabled: isLoggedIn }" @click="handleLogin">
            <div class="menu-text">
              <span>登</span>
              <span>录</span>
            </div>
          </div>
          <div class="menu-item logout" :class="{ disabled: !isLoggedIn }" @click="handleLogout">
            <div class="menu-text">
              <span>注</span>
              <span>销</span>
            </div>
          </div>
          <div class="menu-item profile" :class="{ disabled: !isLoggedIn }" @click="handleViewProfile">
            <div class="menu-text">
              <span>个</span>
              <span>人</span>
              <span>详</span>
              <span>情</span>
            </div>
          </div>

          <div class="divider extra-line"></div> <!-- 第三条线 -->

          <!--坐标轴辅助线
          <div class="debug-x-axis"></div>
          <div class="debug-y-axis"></div>
          <div class="debug-circle-center"></div> -->
        </div>
      </div>

      <div class="main-content">
        <!-- 徽标 -->
        <div class="logo">
         <img src="https://www.nankai.edu.cn/_upload/article/images/77/1f/a7852bfa49c3bdea0f4564630e66/c658ec37-5315-4c7c-b5d5-aee3e3e570d3.jpg" 
             alt="南开大学徽标" 
             class="logo-image" />
        </div>
        
        <!-- 标题 + 面包屑 -->
        <div class="title-area">
          <!-- 网站标题 -->
          <div class="site-title">
          南开大学食堂信息管理系统
            <div class="site-subtitle">
              NANKAI UNIVERSITY CANTEEN MANAGEMENT SYSTEM
            </div>
          </div>

          <!-- 路径栏 -->
          <div class="breadcrumb">
            <el-icon class="home-icon"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-map-pin-house-icon lucide-map-pin-house"><path d="M15 22a1 1 0 0 1-1-1v-4a1 1 0 0 1 .445-.832l3-2a1 1 0 0 1 1.11 0l3 2A1 1 0 0 1 22 17v4a1 1 0 0 1-1 1z"/><path d="M18 10a8 8 0 0 0-16 0c0 4.993 5.539 10.193 7.399 11.799a1 1 0 0 0 .601.2"/><path d="M18 22v-3"/><circle cx="10" cy="10" r="3"/></svg></el-icon>
            <template v-for="(item, index) in breadcrumbs" :key="item.path">
              <span class="breadcrumb-item" @click="handleBreadcrumbClick(item.path)">
                {{ item.label }}
              </span>
              <span v-if="index < breadcrumbs.length - 1" class="separator"> > </span>
            </template>
          </div>
        </div>

        <div class="right-area">
          <!-- 主要功能区域 -->
          <div class="function-area">
            <!-- 搜索框 -->
            <div class="search-box">
              <el-input
                v-model="searchInput"
                placeholder="搜索食堂、档口、菜品..."
                :prefix-icon="Search"
                @keyup.enter="handleSearch"
              />
            </div>
            
            <!-- 选项栏 -->
            <div class="module-tabs">
              <div
                v-for="module in modules"
                :key="module"
                class="module-tab"
                :class="{ active: activeModule === module }"
                @click="handleModuleClick(module)"
              >
                {{ module }}
              </div>
              <div class="indicator" :style="{ left: `${modules.indexOf(activeModule) * 33.33}%` }"></div>
            </div>
            
            
          </div>
          
        </div>
      </div>
    </div>
    
    <!-- 登录弹窗 -->
    <LoginModal 
      v-model="loginModalVisible"
      @close="closeLoginModal"
      @login="onLoginSubmit"
      @register="onRegisterSubmit"
    />
    
    <!-- 个人信息卡片 -->
    <UserProfileCard
      v-model="profileCardVisible"
      @close="closeProfileCard"
      @profile-updated="handleProfileUpdated"
    />
  </header>
</template>

<style scoped>
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: #800080;
  color: white;
  border-radius: 0 0 6px 6px;
  z-index: 1000;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
  height: 200px;
}

.header-container {
  height: 100%;
  padding: 0;
  position: relative;
}

.main-content {
  height: 100%;
  max-width: 1400px;
  margin: 0 auto;
  margin-left: 50px;
  margin-right: auto;
  display: flex;
  align-items: center;
  padding: 0 ;
}

.logo {
  flex-shrink: 0;
  margin-right: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 30px 0 30px 0;
}

.logo-image {
  width: 150px;       /* 根据需要调整大小 */
  height: 150px;
  border-radius: 50%; /* 保持圆形外观 */
  object-fit: cover;
}

.logo-circle {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  background-color: white;
}

/* 标题 + 路径栏 */
.title-area {
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* ✅ 左对齐 */
  margin-right: 40px;
  gap: 25px;
}

.site-title {
  flex-shrink: 0;
  font-size: 3.0rem;
  font-weight: bold;
  white-space: nowrap;
  margin-right: 40px;
}

.site-subtitle {
  font-size: 1rem;
  font-weight: normal;
  color: rgba(255, 255, 255, 0.85);
  letter-spacing: 2px;
}

.breadcrumb {
  display: flex;
  align-items: center;
  font-size: 1.1rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  min-width: 200px;
  flex: 1;
}

.breadcrumb-item {
  cursor: pointer;
}

.breadcrumb-item:hover {
  text-decoration: underline;
}

.right-area {
  display: flex;
  align-items: center;
  gap: 100px;
  margin-left: auto;
}

/* 功能区 */
.function-area {
  flex: 1;
  display: flex;
  flex-direction: column;      /* 垂直排列 */
  align-items: center;         /* 横向居中 */
  justify-content: center;     /* 纵向居中 */
  gap: 50px;
  margin-top: 78px;
}

.search-box {
  width: 350px;
}

/** 选项栏 **/
.module-tabs {
  position: relative;
  display: flex;
  width: 350px;
  height: 40px;
  background-color: #FEF7FF;
  border-radius: 6px 6px 0 0;
  overflow: hidden;
}

.module-tab {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #333;
  cursor: pointer;
  position: relative;
  z-index: 1;
  font-weight: bold;
  transition: color 0.3s;
}

.module-tab::after {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  height: 60%;
  width: 1px;
  background-color: rgba(0, 0, 0, 0.2); /* 灰色线条，90%不透明 */
}

.module-tab:last-child::after {
  display: none;
}

.module-tab.active {
  color: #4a3d70;
  background-color: rgba(101, 85, 143, 0.15);
  border-radius: 4px;
}

.module-tab:hover {
  background-color: rgba(101, 85, 143, 0.05);
}

/** 滑动条 **/
.indicator {
  position: absolute;
  bottom: 0;
  height: 3px;
  width: 33.33%;
  background-color: #65558F;
  transition: left 0.3s ease;
  border-radius: 3px 3px 0 0;
}

.home-icon {
  margin-right: 10px;
  font-size: 1.2em;
}

.separator {
  margin: 0 8px;
}

/** 用户头像 **/
.user-avatar-container {
  position: absolute;
  top: 20px;     
  right: 20px;   
  z-index: 1100;
  width: 80px;
  height: 80px;
}

.user-avatar {
  position: relative;
  z-index: 2;
  transition: all 0.3s ease;
  width: 80px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.user-avatar-container:hover .user-avatar {
  transform: translate(-64px, 44px) scale(1.3);
}

.avatar-menu {
  position: absolute;
  top: 0;
  right: 0;
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background-color: #FEF7FF;
  opacity: 0;
  transform: scale(0);
  transform-origin: 75% 25%;
  transition: all 0.3s ease;
  z-index: 1;
}

.user-avatar-container:hover .avatar-menu {
  opacity: 1;
  transform: translate(-25px, 5px) scale(1.25);
}

.menu-item {
  position: absolute;
  display: block;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  color: #65558F;
  transition: all 0.2s ease;
  width: 100%;
  height: 100%;
  transform-origin: center buttom;
}

.menu-text {
  position: absolute;
  font-size: 14px;
  white-space: nowrap;
  pointer-events: none;  /* 允许点击穿透到父元素 */
  color: #65558F;
}

/* 登录文字 - 上方区域 */
.menu-item.login .menu-text span {
  position: absolute;
  transform-origin: center;
}

.menu-item.login .menu-text span:nth-child(1) { top: 25px; left: 110px; transform: rotate( 0deg); }
.menu-item.login .menu-text span:nth-child(2) { top: 50px; left: 120px; transform: rotate( 0deg); }

/* 注销文字 - 左上方区域 */
.menu-item.logout .menu-text span {
  position: absolute;
  transform-origin: center;
}

.menu-item.logout .menu-text span:nth-child(1) { top: 25px; left: 20px; transform: rotate(0deg); }
.menu-item.logout .menu-text span:nth-child(2) { top: 50px; left: 5px; transform: rotate(0deg); }

/* 个人详情文字 - 下方区域 */
.menu-item.profile .menu-text span {
  position: absolute;
  transform-origin: center;
}

.menu-item.profile .menu-text span:nth-child(1) { top: 100px; left: 30px; transform: rotate(45deg); }
.menu-item.profile .menu-text span:nth-child(2) { top: 112px; left: 50px; transform: rotate(20deg); }
.menu-item.profile .menu-text span:nth-child(3) { top: 112px; left: 80px; transform: rotate(-20deg); }
.menu-item.profile .menu-text span:nth-child(4) { top: 100px; left: 100px; transform: rotate(-45deg); }

/* 圆心辅助点 
.debug-circle-center {
  position: absolute;
  width: 6px;
  height: 6px;
  background-color: red;
  border-radius: 50%;
  top: calc(50% - 3px);   y轴居中
  left: calc(50% - 3px);  x轴居中
  z-index: 5;
}
  */


/* 水平参考线 
.debug-x-axis {
  position: absolute;
  top: 50%;
  left: 0;
  width: 100%;
  height: 1px;
  background-color: rgba(59, 228, 47, 0.87);
  z-index: 4;
}
  */

/* 垂直参考线 
.debug-y-axis {
  position: absolute;
  left: 50%;
  top: 0;
  width: 1px;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  z-index: 4;
}
  */

/* 登录 - 上方区域 */
.menu-item.login {
  clip-path: path("M 70 70 L 70 0 A 70 70 0 0 1 130.6 105 Z");
  /* background-color: rgba(223, 37, 77, 0.61);  红色半透明  */
}

/* 注销 - 左上方区域 */
.menu-item.logout {
  clip-path: path("M 70 70 L 9.4 105 A 70 70 0 0 1 70 0 Z");
  /* background-color: rgba(54, 163, 235, 0.92);  蓝色半透明  */
}

/* 个人详情 - 下方区域 */
.menu-item.profile {
  clip-path: path("M 70 70 L 130.6 105 A 70 70 0 0 1 9.4 105 Z");
  /* background-color: rgba(132, 75, 192, 0.72); 青绿色半透明 */
}

/* 鼠标悬停 - 所有区域 */
.menu-item:not(.disabled):hover {
  font-weight: bold;
  color: #4a3d70;
  background-color: rgba(101, 85, 143, 0.1);
}

/* 禁用状态 */
.menu-item.disabled {
  color: #bbbbbb;
  cursor: default;
  opacity: 0.7;
  background-color: rgba(200, 200, 200, 0.2);
}

.menu-item.disabled .menu-text span {
  color: #999999;
}

/* 添加分隔线 */
.avatar-menu::before,
.avatar-menu::after,
.avatar-menu .divider.extra-line {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 1px;
  height: 50%;
  background-color: rgba(0, 0, 0, 0.1);
  transform-origin: center bottom; /* 支点为下端，即圆心 */
}

.avatar-menu::before {
  transform: translateY(-100%) rotate(0deg);   /* 垂直线，向上延伸半径 */
}

.avatar-menu::after {
  transform: translateY(-100%) rotate(120deg);   /* 垂直线，向上延伸半径 */
}

/* 第三条线 - 顺时针 240° */
.avatar-menu .divider.extra-line {
  transform: translateY(-100%) rotate(240deg);
}

/* 媒体查询适配 */
@media (max-width: 1200px) {
  .app-header {
    height: auto;
  }
  
  .header-container {
    padding: 15px 20px;
  }
  
  .main-content {
    flex-wrap: wrap;
    gap: 15px;
  }
  
  .site-title {
    font-size: 1.8rem;
    margin-right: 20px;
  }

  .logo-circle {
    width: 70px;
    height: 70px;
  }
  
  .function-area {
    order: 3;
    width: 100%;
    flex-wrap: wrap;
  }
  
  .breadcrumb {
    order: 2;
  }
  
  .user-avatar-container {
    top: 15px;
    right: 15px;
  }
}

@media (max-width: 768px) {
  .header-container {
    padding: 10px 15px;
  }
  
  .main-content {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .logo {
    display: flex;
    align-items: center;
    width: 100%;
    margin-bottom: 10px;
  }
  
  .logo-circle {
    width: 50px;
    height: 50px;
  }
  
  .site-title {
    font-size: 1.5rem;
  }
  
  .function-area {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .search-box, .module-tabs, .breadcrumb {
    width: 100%;
  }
  
  .user-avatar-container {
    top: 10px;
    right: 15px;
    width: 50px;
    height: 50px;
  }
}
</style> 