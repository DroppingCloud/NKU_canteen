<script setup>
import { ref, watch, onMounted } from 'vue'
import { User, Lock, Message } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { login, register } from '../api/api'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'close', 'login', 'register'])

// 内部可变状态
const dialogVisible = ref(false)

// 初始化时同步状态
onMounted(() => {
  console.log('LoginModal mounted, modelValue:', props.modelValue)
  dialogVisible.value = props.modelValue
})

// 监听props变化更新内部状态
watch(() => props.modelValue, (newVal) => {
  console.log('modelValue changed:', newVal)
  dialogVisible.value = newVal
}, { immediate: true })

// 当内部状态变化时，通知父组件更新
watch(dialogVisible, (newVal) => {
  console.log('dialogVisible changed:', newVal)
  emit('update:modelValue', newVal)
})

// 当前激活的标签
const activeTab = ref('login')

// 登录表单
const loginForm = ref({
  email: '',
  password: ''
})

// 登录表单校验规则
const loginRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 注册表单
const registerForm = ref({
  nickname: '',
  gender: 1, // 默认为男性
  email: '',
  password: '',
  confirmPassword: ''
})

// 注册表单校验规则
const registerRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在2到20个字符之间', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 表单加载状态
const loading = ref(false)

// 登录表单引用
const loginFormRef = ref(null)
// 注册表单引用
const registerFormRef = ref(null)

// 提交登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { email, password } = loginForm.value
        const res = await login({ email, password })
        
        if (res.code === 0) {
          // 登录成功，保存token和用户信息
          localStorage.setItem('token', res.data.token)
          localStorage.setItem('userInfo', JSON.stringify(res.data.user))
          
          // 触发自定义事件，通知组件localStorage已变化
          window.dispatchEvent(new Event('localStorageChanged'))
          
          ElMessage.success('登录成功')
          emit('login', res.data.user)
          dialogVisible.value = false
        } else {
          ElMessage.error(res.msg || '登录失败')
        }
      } catch (error) {
        console.error('登录错误:', error)
        // 尝试从不同位置获取错误消息
        const errorMsg = error.response?.data?.msg || error.msg || error.message || '登录失败，请检查网络连接'
        ElMessage.error(errorMsg)
      } finally {
        loading.value = false
      }
    }
  })
}

// 提交注册
const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 提取注册数据，排除confirmPassword
        const { nickname, gender, email, password } = registerForm.value
        
        // 直接使用表单中的性别值，不进行转换
        const userData = { 
          nickname, 
          gender, // 使用原始gender值，2表示女性
          email, 
          password 
        }
        
        const res = await register(userData)
        
        if (res.code === 0) {
          ElMessage.success('注册成功，请登录')
          // 清空表单
          registerForm.value = {
            nickname: '',
            gender: 1,
            email: '',
            password: '',
            confirmPassword: ''
          }
          // 切换到登录Tab
          activeTab.value = 'login'
        } else {
          ElMessage.error(res.msg || '注册失败')
        }
      } catch (error) {
        console.error('注册错误:', error)
        // 尝试从不同位置获取错误消息
        const errorMsg = error.response?.data?.msg || error.msg || error.message || '注册失败，请检查网络连接'
        ElMessage.error(errorMsg)
      } finally {
        loading.value = false
      }
    }
  })
}

// 忘记密码
const handleForgotPassword = () => {
  ElMessage.info('忘记密码功能暂未开放')
}

// 关闭弹窗
const handleClose = () => {
  dialogVisible.value = false
  emit('close')
}
</script>

<template>
  <el-dialog
    v-model="dialogVisible"
    title=""
    width="400px"
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="true"
    center
    class="login-dialog"
  >
    <div class="login-container">
      <!-- 关闭按钮 -->
      <div class="close-btn" @click="handleClose">×</div>
      
      <!-- 选项栏 -->
      <div class="tabs">
        <div 
          class="tab-item" 
          :class="{ active: activeTab === 'login' }"
          @click="activeTab = 'login'"
        >
          登录
        </div>
        <div 
          class="tab-item" 
          :class="{ active: activeTab === 'register' }"
          @click="activeTab = 'register'"
        >
          注册
        </div>
      </div>
      
      <!-- 登录表单 -->
      <div v-show="activeTab === 'login'" class="form-container">
        <el-form 
          ref="loginFormRef"
          :model="loginForm" 
          :rules="loginRules"
          label-position="top"
        >
          <el-form-item prop="email">
            <el-input 
              v-model="loginForm.email"
              placeholder="请输入邮箱"
              :prefix-icon="Message"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          
          <div class="action-links">
            <span class="forgot-password" @click="handleForgotPassword">忘记密码</span>
          </div>
          
          <el-button 
            type="primary" 
            class="submit-btn" 
            @click="handleLogin"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form>
      </div>
      
      <!-- 注册表单 -->
      <div v-show="activeTab === 'register'" class="form-container">
        <el-form 
          ref="registerFormRef"
          :model="registerForm" 
          :rules="registerRules"
          label-position="top"
        >
          <el-form-item prop="nickname">
            <el-input 
              v-model="registerForm.nickname"
              placeholder="请输入昵称"
              :prefix-icon="User"
            />
          </el-form-item>
          
          <el-form-item prop="gender" label="性别">
            <el-radio-group v-model="registerForm.gender">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">女</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item prop="email">
            <el-input 
              v-model="registerForm.email"
              placeholder="请输入邮箱"
              :prefix-icon="Message"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="registerForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          
          <el-form-item prop="confirmPassword">
            <el-input 
              v-model="registerForm.confirmPassword"
              type="password"
              placeholder="请确认密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          
          <el-button 
            type="primary" 
            class="submit-btn" 
            @click="handleRegister"
            :loading="loading"
          >
            注册
          </el-button>
        </el-form>
      </div>
    </div>
  </el-dialog>
</template>

<style scoped>
.login-dialog :deep(.el-dialog) {
  border-radius: 17px;
  overflow: hidden;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.2);
}

.login-dialog :deep(.el-dialog__body) {
  padding: 20px 30px 40px;
}

.login-container {
  position: relative;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
}

.close-btn {
  position: absolute;
  top: -29px;
  right: -13px;
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
}

.close-btn:hover {
  background-color: rgba(0, 0, 0, 0.05);
  color: #666;
}

.tabs {
  display: flex;
  border-bottom: 1px solid #eee;
  margin-bottom: 30px;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 15px 0;
  font-size: 16px;
  font-weight: bold;
  color: #666;
  cursor: pointer;
  position: relative;
  transition: all 0.3s;
}

.tab-item.active {
  color: #65558F;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 25%;
  width: 50%;
  height: 2px;
  background-color: #65558F;
}

.form-container {
  margin-top: 20px;
}

.submit-btn {
  width: 100%;
  height: 42px;
  margin-top: 20px;
  border-radius: 21px;
  font-size: 16px;
  font-weight: bold;
  background-color: #800080;
  border-color: #800080;
}

.submit-btn:hover {
  background-color: #590359;
  border-color: #590359;
}

.action-links {
  display: flex;
  justify-content: flex-end;
  margin-top: -10px;
  margin-bottom: 10px;
}

.forgot-password {
  color: #65558F;
  font-size: 14px;
  cursor: pointer;
}

.forgot-password:hover {
  text-decoration: underline;
}
</style> 