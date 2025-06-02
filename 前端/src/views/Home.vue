<script setup>
import { ref, onMounted, reactive, watch } from 'vue'
import { useRouter } from 'vue-router'
import { getCanteenList } from '../api/api'
import { ElMessage } from 'element-plus'
import { useAppModeStore } from '../store/appMode'

// 获取应用模式store
const appModeStore = useAppModeStore()

const router = useRouter()

// 食堂数据
const canteenData = reactive([
  {
    campus: '八里台校区',
    canteens: []
  },
  {
    campus: '津南校区',
    canteens: []
  },
  {
    campus: '泰达校区',
    canteens: []
  }
])

// 加载状态
const loading = ref(false)

// 获取食堂列表数据
const fetchCanteenList = async () => {
  loading.value = true
  try {
    const response = await getCanteenList()
    if (response.code === 0) {
      // 处理食堂数据，将其按校区分类
      const canteens = response.data || []
      
      // 清空现有数据
      canteenData.forEach(campus => {
        campus.canteens = []
      })
      
      // 使用后端返回的campus字段进行分类
      canteens.forEach(canteen => {
        // 转换数据格式，适配前端展示
        const canteenItem = {
          id: canteen.canteen_id,
          name: canteen.name,
          location: canteen.location || '暂无位置信息',
          hours: canteen.open_time || '暂无营业时间信息',
          image: canteen.img || './assets/images/placeholder.jpg',
          campus: canteen.campus || '未知校区',
          // 保存原始数据用于详情页显示
          like_count: canteen.like_count,
          comment_count: canteen.comment_count
        }
        
        // 使用campus字段进行分类
        const campusName = canteen.campus || '未知校区'
        
        // 查找对应的校区索引
        let campusIndex = -1
        
        // 匹配校区名称（支持部分匹配）
        if (campusName.includes('八里台') || campusName === '八里台校区') {
          campusIndex = 0 // 八里台校区
        } else if (campusName.includes('津南') || campusName === '津南校区') {
          campusIndex = 1 // 津南校区
        } else if (campusName.includes('泰达') || campusName === '泰达校区') {
          campusIndex = 2 // 泰达校区
        }
        
        // 如果找到匹配的校区，添加到对应的数组中
        if (campusIndex >= 0 && campusIndex < canteenData.length) {
          canteenData[campusIndex].canteens.push(canteenItem)
        } else {
          // 如果没有匹配的校区，默认添加到八里台校区
          canteenData[0].canteens.push(canteenItem)
        }
      })
    } else {
      ElMessage.error(response.msg || '获取食堂数据失败')
      // 使用默认数据
      useDefaultData()
    }
  } catch (error) {
    console.error('获取食堂列表出错:', error)
    ElMessage.error('获取食堂数据失败，请检查网络连接')
    // 使用默认数据
    useDefaultData()
  } finally {
    loading.value = false
  }
}

// 使用默认数据（当API请求失败时）
const useDefaultData = () => {
  // 清空现有数据
  canteenData.forEach(campus => {
    campus.canteens = []
  })
  
  // 八里台校区默认数据
  canteenData[0].canteens = [
    {
      id: 1,
      name: '学一食堂',
      location: '南开大学八里台校区东北角',
      hours: '早6:30-9:00 午11:00-13:00 晚17:00-19:00',
      image: '/src/assets/images/placeholder.jpg',
      like_count: 0,
      comment_count: 0
    },
    {
      id: 2,
      name: '学二食堂',
      location: '南开大学八里台校区中心地带',
      hours: '早6:30-9:00 午11:00-13:00 晚17:00-19:00',
      image: '/src/assets/images/placeholder.jpg',
      like_count: 0,
      comment_count: 0
    }
  ]
  
  // 津南校区默认数据
  canteenData[1].canteens = [
    {
      id: 3,
      name: '津南第一食堂',
      location: '南开大学津南校区东区',
      hours: '早6:30-9:00 午11:00-13:00 晚17:00-19:00',
      image: '/src/assets/images/placeholder.jpg',
      like_count: 0,
      comment_count: 0
    }
  ]
  
  // 泰达校区默认数据
  canteenData[2].canteens = [
    {
      id: 4,
      name: '泰达食堂',
      location: '南开大学泰达校区主楼东侧',
      hours: '早6:30-9:00 午11:00-13:00 晚17:00-19:00',
      image: '/src/assets/images/placeholder.jpg',
      like_count: 0,
      comment_count: 0
    }
  ]
}

// 组件挂载时获取数据
onMounted(() => {
  fetchCanteenList()
  
  // 如果是管理模式，跳转到管理页面
  if (appModeStore.isAdminMode) {
    router.push('/admin')
  }
})

// 处理食堂点击
const handleCanteenClick = (canteen) => {
  router.push(`/canteen/${canteen.id}`)
}

// 修复图片路径处理函数
const getImageUrl = (imagePath) => {
  if (!imagePath) return './assets/images/placeholder.jpg'
  
  // 如果是相对路径且以/src开头，替换为./
  if (imagePath.startsWith('/src/')) {
    return imagePath.replace('/src/', './')
  }
  
  return imagePath
}

// 监听管理模式变化
watch(() => appModeStore.isAdminMode, (isAdminMode) => {
  if (isAdminMode) {
    router.push('/admin')
  }
})
</script>

<template>
  <div class="home-container" v-loading="loading">
    <div class="canteen-list">
      <div v-for="(campus, index) in canteenData" :key="index" class="campus-section">
        <!-- 仅当该校区有食堂时显示校区标题 -->
        <div v-if="campus.canteens.length > 0" class="campus-divider">
          <span class="campus-name">{{ campus.campus }}</span>
          <div class="divider-line"></div>
        </div>
        
        <div class="canteens-container">
          <div 
            v-for="canteen in campus.canteens" 
            :key="canteen.id"
            class="canteen-card"
            @click="handleCanteenClick(canteen)"
          >
            <div class="canteen-image" :style="{ backgroundImage: `url(${getImageUrl(canteen.image)})` }">
              <div class="canteen-info">
                <h3>{{ canteen.name }}</h3>
                <p class="location"><i class="location-icon"></i>{{ canteen.location }}</p>
                <p class="hours"><i class="time-icon"></i>{{ canteen.hours }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  padding: 0px 0px 0px 0px;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
  width: 100%;
  margin: 10px auto 0;
  /* border: 2px dashed red; */
  display: grid;
}

/* 校区块 */
.campus-section {
  margin-bottom: 50px;
}

.campus-divider {
  display: flex;
  align-items: center;
  margin: 25px 0px 35px;
  width: 100%;
}

.campus-name {
  font-size: 2.0rem;
  font-weight: bold;
  color: #333;
  margin-right: 25px;
}

.divider-line {
  flex: 1;
  height: 1.5px;
  background-color: #ddd;
}

.canteens-container {
  display: flex;
  flex-direction: column;
  gap: 40px;
  width: 100%;
  padding: 0 0px; 
}

.canteen-card {
  width: 100%;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  height: 350px;
}

.canteen-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2);
}

.canteen-image {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
}

.canteen-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0.3), transparent);
  padding: 20px 30px;
  color: white;
}

.canteen-info h3 {
  margin: 0 0 20px;
  font-size: 2.5rem;
}

.location, .hours {
  display: flex;
  align-items: center;
  margin: 12px 0;
  font-size: 1.4rem;
}

.location-icon, .time-icon {
  display: inline-block;
  width: 24px;
  height: 24px;
  margin-right: 15px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
}

.location-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z'/%3E%3C/svg%3E");
}

.time-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z'/%3E%3Cpath d='M12.5 7H11v6l5.25 3.15.75-1.23-4.5-2.67z'/%3E%3C/svg%3E");
}

/* 响应式设计 */
@media (max-width: 768px) {
  .home-container {
    padding: 140px 20px 20px;
  }
  
  .campus-section {
    margin-bottom: 40px;
  }
  
  .campus-divider {
    margin: 20px 0 25px;
  }
  
  .campus-name {
    font-size: 1.5rem;
  }
  
  .canteen-card {
    height: 250px;
    border-radius: 20px;
  }
  
  .canteen-info {
    padding: 25px 20px;
  }
  
  .canteen-info h3 {
    font-size: 1.8rem;
    margin-bottom: 12px;
  }
  
  .location, .hours {
    font-size: 1rem;
    margin: 8px 0;
  }
}

/* 添加管理面板样式 */
.admin-panel {
  margin-bottom: 40px;
  padding: 20px;
  border-radius: 15px;
  background-color: #f9f0fc;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.admin-title {
  font-size: 1.8rem;
  color: #800080;
  margin-bottom: 20px;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
}

.admin-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.admin-action {
  flex-basis: calc(33.333% - 10px);
}

@media (max-width: 768px) {
  .admin-action {
    flex-basis: calc(50% - 10px);
  }
}

@media (max-width: 480px) {
  .admin-action {
    flex-basis: 100%;
  }
}
</style> 