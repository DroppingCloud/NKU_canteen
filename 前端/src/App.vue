<script setup>
import AppHeader from './components/layout/AppHeader.vue'
import ModeSwitcher from './components/layout/ModeSwitcher.vue'
</script>

<template>
  <div class="app">
    <AppHeader />
    <ModeSwitcher />
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<style>
/* 导入Inria Serif字体 */
@import url('https://fonts.googleapis.com/css2?family=Inria+Serif:wght@400;700&display=swap');

/* 全局样式 */
* {
  box-sizing: border-box;   
  margin: 0;
  padding: 0;
}

html, body {
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
  background-color: #f5f5f5;
  color: #333;
  line-height: 1.6;
}

.app {
  display: flex;
  flex-direction: column;   /* 垂直布局 */
  min-height: 100vh;
  margin: 0 auto;
  /* border: 2px dashed green; */
}

.main-content {
  flex: 1;
  width: 1400px;
  margin: 200px 40px 0;
  /* border: 2px dashed blue; */
}


/* 页面过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
