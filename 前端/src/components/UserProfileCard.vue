<script setup>
import { ref, watch, onMounted, computed, nextTick, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { updateUserProfile, getUserProfile } from '../api/api'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'close', 'profileUpdated'])

// 内部可变状态
const dialogVisible = ref(false)

// 用户信息
const userProfile = ref({
  user_id: null,
  nickname: '',
  gender: 1,
  avatar: '',
  user_type: '',
  register_time: 0,
  email: ''
})

// 编辑状态的用户信息
const editingProfile = ref({})

// 是否处于编辑状态
const isEditing = ref(false)

// 加载状态
const loading = ref(false)

// 粒子动画相关
const canvasRef = ref(null)
let animationFrameId = null
let particles = [] 

// 格式化日期
const formattedRegisterTime = computed(() => {
  if (!userProfile.value.register_time) return ''
  const date = new Date(userProfile.value.register_time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
})

// 粒子类
class Particle {
  constructor(canvas, side = 0, progress = 0) {
    this.canvas = canvas
    this.side = side     // 粒子位置状态：0-顶部, 1-右侧, 2-底部, 3-左侧
    this.progress = progress  // 位置百分比（0-1）
    this.speed = 0.002 + Math.random() * 0.001  // 移动速度
    this.size = 10  // 主粒子大小
    this.trail = []  // 拖尾历史位置
    this.trailLength = 140  // 拖尾长度
    this.x = 0
    this.y = 0
    this.childParticles = []  // 子粒子数组
    this.lastChildTime = 0  // 上次生成子粒子的时间
    this.updatePosition()
  }
  
  updatePosition() {
    const w = this.canvas.width
    const h = this.canvas.height
    
    // 根据当前边和进度计算位置
    switch (this.side) {
      case 0: // 顶部边，从左到右
        this.x = w * this.progress
        this.y = 0
        break
      case 1: // 右侧边，从上到下
        this.x = w
        this.y = h * this.progress
        break
      case 2: // 底部边，从右到左
        this.x = w - (w * this.progress)
        this.y = h
        break
      case 3: // 左侧边，从下到上
        this.x = 0
        this.y = h - (h * this.progress)
        break
    }
  }
  
  // 创建一个子粒子
  createChildParticle() {
    const now = Date.now()
    // 每80ms创建一个子粒子
    if (now - this.lastChildTime > 80) {
      this.lastChildTime = now
      // 随机角度
      const angle = Math.random() * Math.PI * 2
      // 随机初始速度，增大速度范围
      const speed = 0.6 + Math.random() * 1.2
      
      this.childParticles.push({
        x: this.x,
        y: this.y,
        vx: Math.cos(angle) * speed,
        vy: Math.sin(angle) * speed,
        size: 2 + Math.random() * 3,  // 增大子粒子尺寸
        alpha: 0.9,
        life: 1.0 // 生命值，会逐渐减少
      })
    }
    
    // 更新子粒子
    for (let i = this.childParticles.length - 1; i >= 0; i--) {
      const child = this.childParticles[i]
      
      // 移动
      child.x += child.vx
      child.y += child.vy
      
      // 减小速度（降低阻尼使粒子移动更远）
      child.vx *= 0.98
      child.vy *= 0.98
      
      // 减小生命值（减缓消失速度）
      child.life -= 0.015
      child.alpha = child.life * 0.9
      
      // 移除死亡的粒子
      if (child.life <= 0) {
        this.childParticles.splice(i, 1)
      }
    }
  }
  
  update() {
    // 更新位置百分比
    this.progress += this.speed
    
    if (this.progress > 1) {
      // 切换到下一条边
      this.progress = 0
      this.side = (this.side + 1) % 4
    }
    
    this.updatePosition()
    
    // 更新拖尾
    this.trail.push({ x: this.x, y: this.y })
    if (this.trail.length > this.trailLength) {
      this.trail.shift()
    }
    
    // 创建和更新子粒子
    this.createChildParticle()
  }
  
  draw(ctx) {
    // 绘制子粒子
    this.childParticles.forEach(child => {
      // 创建淡金色渐变（从金色渐变到透明）
      const childGradient = ctx.createRadialGradient(
        child.x, child.y, 0,
        child.x, child.y, child.size
      )
      childGradient.addColorStop(0, `rgba(255, 223, 0, ${child.alpha})`)
      childGradient.addColorStop(0.5, `rgba(255, 236, 139, ${child.alpha * 0.7})`)
      childGradient.addColorStop(1, `rgba(255, 248, 220, ${child.alpha * 0.3})`)
      
      ctx.beginPath()
      ctx.arc(child.x, child.y, child.size, 0, Math.PI * 2)
      ctx.fillStyle = childGradient
      ctx.fill()
    })
    
    // 绘制拖尾
    for (let i = 0; i < this.trail.length; i++) {
      const point = this.trail[i]
      const alpha = i / this.trail.length
      const size = this.size * (i / this.trail.length) * 0.9
      
      // 金白色渐变的拖尾
      const gradient = ctx.createRadialGradient(
        point.x, point.y, 0,
        point.x, point.y, size
      )
      gradient.addColorStop(0, `rgba(255, 240, 200, ${alpha * 0.9})`)
      gradient.addColorStop(0.5, `rgba(255, 215, 0, ${alpha * 0.6})`)
      gradient.addColorStop(1, `rgba(255, 255, 255, ${alpha * 0.3})`)
      
      ctx.beginPath()
      ctx.arc(point.x, point.y, size, 0, Math.PI * 2)
      ctx.fillStyle = gradient
      ctx.fill()
    }
    
    // 绘制主粒子（最亮的金色带白光）
    const mainGradient = ctx.createRadialGradient(
      this.x, this.y, 0,
      this.x, this.y, this.size
    )
    mainGradient.addColorStop(0, 'rgba(255, 255, 255, 1.0)')  // 中心为白色
    mainGradient.addColorStop(0.3, 'rgba(255, 250, 205, 0.9)') // 浅金色
    mainGradient.addColorStop(0.6, 'rgba(255, 215, 0, 0.7)')  // 金色
    mainGradient.addColorStop(1, 'rgba(255, 255, 255, 0)')    // 边缘透明
    
    // 绘制发光效果，增加发光范围
    ctx.shadowColor = 'rgba(255, 255, 255, 0.9)'
    ctx.shadowBlur = 20
    
    ctx.beginPath()
    ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2)
    ctx.fillStyle = mainGradient
    ctx.fill()
    
    // 重置阴影
    ctx.shadowBlur = 0
  }
}

// 停止粒子动画
const stopParticleAnimation = () => {
  if (animationFrameId) {
    cancelAnimationFrame(animationFrameId)
    animationFrameId = null
  }
}

// 启动粒子动画
const startParticleAnimation = () => {
  if (!canvasRef.value) return
  
  const canvas = canvasRef.value
  const ctx = canvas.getContext('2d')
  
  // 设置Canvas大小
  const updateCanvasSize = () => {
    const parent = canvas.parentElement
    if (parent) {
      const rect = parent.getBoundingClientRect()
      canvas.width = rect.width
      canvas.height = rect.height
    }
  }
  
  // 初始化设置
  updateCanvasSize()
  
  // 创建粒子 - 只创建一个粒子
  particles = []
  particles.push(new Particle(canvas, 0, 0))
  
  // 停止之前的动画（如果有）
  stopParticleAnimation()
  
  // 动画循环
  const animate = () => {
    if (!ctx || !canvas) return
    
    // 清空画布
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    
    // 更新并绘制粒子
    particles.forEach(particle => {
      particle.update()
      particle.draw(ctx)
    })
    
    // 继续下一帧
    animationFrameId = requestAnimationFrame(animate)
  }
  
  // 开始动画
  animate()
  
  // 监听窗口大小变化
  window.addEventListener('resize', updateCanvasSize)
}

// 重置编辑状态
const resetEditingProfile = () => {
  editingProfile.value = {
    user_id: userProfile.value.user_id,
    nickname: userProfile.value.nickname,
    gender: userProfile.value.gender
  }
}

// 获取用户信息
const fetchUserProfile = async () => {
  loading.value = true
  try {
    if (!localStorage.getItem('token')) {
      ElMessage.error('用户未登录')
      dialogVisible.value = false
      return
    }
    
    const response = await getUserProfile()
    
    if (response.code === 0) {
      // 获取用户数据
      const userData = response.data
      userProfile.value = userData
      resetEditingProfile()
    } else {
      ElMessage.error(response.msg || '获取用户信息失败')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 监听props变化更新内部状态
watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal
  
  if (newVal) {
    // 当对话框打开时获取用户信息
    fetchUserProfile()
    
    // 在下一个渲染周期启动粒子动画
    nextTick(() => {
      startParticleAnimation()
    })
  } else {
    // 当对话框关闭时停止粒子动画
    stopParticleAnimation()
  }
}, { immediate: true })

// 当内部状态变化时，通知父组件更新
watch(dialogVisible, (newVal) => {
  emit('update:modelValue', newVal)
})

// 进入编辑模式
const startEditing = () => {
  isEditing.value = true
  resetEditingProfile()
}

// 取消编辑
const cancelEditing = () => {
  isEditing.value = false
}

// 提交编辑
const submitEditing = async () => {
  loading.value = true
  try {
    if (!localStorage.getItem('token')) {
      ElMessage.error('用户未登录')
      return
    }
    
    // 准备要提交的数据
    const dataToSubmit = {
      ...editingProfile.value
    }
    
    const response = await updateUserProfile(dataToSubmit)
    
    if (response.code === 0) {
      ElMessage.success('修改成功')
      
      // 更新本地用户信息
      userProfile.value = {
        ...userProfile.value,
        nickname: editingProfile.value.nickname,
        gender: editingProfile.value.gender
      }
      
      // 更新本地存储的用户信息
      const storedUserInfo = localStorage.getItem('userInfo')
      if (storedUserInfo) {
        try {
          const parsedUserInfo = JSON.parse(storedUserInfo)
          parsedUserInfo.nickname = editingProfile.value.nickname
          // 更新性别时同样使用前端表示
          parsedUserInfo.gender = editingProfile.value.gender
          localStorage.setItem('userInfo', JSON.stringify(parsedUserInfo))
          
          // 通知父组件用户信息已更新
          emit('profileUpdated', {
            nickname: editingProfile.value.nickname,
            gender: editingProfile.value.gender
          })
        } catch (error) {
          console.error('更新本地存储用户信息失败:', error)
        }
      }
      
      isEditing.value = false
    } else {
      ElMessage.error(response.msg || '修改失败')
    }
  } catch (error) {
    console.error('修改用户信息失败:', error)
    ElMessage.error('修改用户信息失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 关闭弹窗
const handleClose = () => {
  dialogVisible.value = false
  isEditing.value = false
  emit('close')
}

// 组件卸载时清理资源
onBeforeUnmount(() => {
  stopParticleAnimation()
})
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    title=""
    width="600px"
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="true"
    center
    class="profile-dialog"
  >
    <div class="profile-card" v-loading="loading">
      <!-- 粒子动画Canvas -->
      <canvas ref="canvasRef" class="particle-canvas"></canvas>
      
      <!-- 关闭按钮 -->
      <div class="close-btn" @click="handleClose">×</div>
      
      <div class="profile-content">
        <!-- 用户头像 -->
        <div class="avatar-section">
          <el-avatar :size="120" :src="userProfile.avatar || 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/3d_4.png'" />
        </div>
        
        <!-- 用户信息 -->
        <div class="info-section">
          <!-- 用户昵称 -->
          <div class="info-item nickname">
            <div class="item-label">昵称：</div>
            <div class="item-value" v-if="!isEditing" @click="startEditing">
              {{ userProfile.nickname }}
              <el-icon class="edit-icon"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" fill="currentColor"><path d="M884.6 180.6l-41.2-41.2a64 64 0 0 0-90.5 0L271.3 621.1 126.9 886.9a8 8 0 0 0 10.2 10.2l265.8-144.4L884.6 271a64 64 0 0 0 0-90.4zM200.5 828.2l42.4-123.7 60.6-11.3 31.9 31.9-11.3 60.6-123.6 42.5zm651.4-600.3L530.6 549.2l-56.4-56.4 321.3-321.3 56.4 56.4z"></path></svg></el-icon>
            </div>
            <el-input v-else v-model="editingProfile.nickname" placeholder="请输入昵称" size="small" />
          </div>
          
          <!-- 用户邮箱 -->
          <div class="info-item">
            <div class="item-label">邮箱：</div>
            <div class="item-value email">{{ userProfile.email }}</div>
          </div>
          
          <!-- 用户性别 -->
          <div class="info-item gender">
            <div class="item-label">性别：</div>
            <div class="item-value" v-if="!isEditing" @click="startEditing">
              {{ userProfile.gender === 1 ? '男' : '女' }}
              <el-icon class="edit-icon"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" fill="currentColor"><path d="M884.6 180.6l-41.2-41.2a64 64 0 0 0-90.5 0L271.3 621.1 126.9 886.9a8 8 0 0 0 10.2 10.2l265.8-144.4L884.6 271a64 64 0 0 0 0-90.4zM200.5 828.2l42.4-123.7 60.6-11.3 31.9 31.9-11.3 60.6-123.6 42.5zm651.4-600.3L530.6 549.2l-56.4-56.4 321.3-321.3 56.4 56.4z"></path></svg></el-icon>
            </div>
            <el-radio-group v-else v-model="editingProfile.gender" size="small">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">女</el-radio>
            </el-radio-group>
          </div>
          
          <!-- 注册时间 -->
          <div class="info-item">
            <div class="item-label">注册时间：</div>
            <div class="item-value">{{ formattedRegisterTime }}</div>
          </div>
        </div>
      </div>
      
      <!-- 编辑模式下的按钮 -->
      <div class="action-buttons" v-if="isEditing">
        <el-button @click="cancelEditing" size="small">取消</el-button>
        <el-button type="primary" @click="submitEditing" size="small" :loading="loading">确认</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
.profile-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
}

.profile-dialog :deep(.el-dialog__body) {
  padding: 0;
}

.profile-card {
  position: relative;
  padding: 40px;
  background-color: #FEF7FF;
  border-radius: 16px;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
  overflow: hidden;
}

/* 粒子动画Canvas */
.particle-canvas {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none; /* 确保点击可以穿透到下面的内容 */
}

.close-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  transition: all 0.2s;
  z-index: 10;
}

.close-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
  color: #666;
}

.profile-content {
  display: flex;
  gap: 40px;
  position: relative;
  z-index: 2;
}

.avatar-section {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.info-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.info-item {
  display: flex;
  align-items: center;
}

.item-label {
  font-weight: bold;
  color: #333;
  min-width: 80px;
}

.item-value {
  color: #666;
  display: flex;
  align-items: center;
}

.item-value.email {
  color: #999; /* 邮箱显示为暗色，表示不可修改 */
}

.nickname .item-value,
.gender .item-value {
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.nickname .item-value:hover,
.gender .item-value:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.edit-icon {
  margin-left: 8px;
  font-size: 14px;
  color: #800080;
}

.action-buttons {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  position: relative;
  z-index: 2;
}

.action-buttons .el-button {
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
}
</style> 