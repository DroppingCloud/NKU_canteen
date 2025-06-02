<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCanteenDetail } from '../api/api'
import { getStallList } from '../api/api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const canteenId = computed(() => route.params.id)

// 档口数据，按楼层分组
const boothData = reactive([])

// 食堂信息
const canteenInfo = ref({
  id: '',
  name: '',
  location: '',
  open_time: '',
  img: '',
  // 不再显示评价信息，但仍然保留字段以防后续需要
  like_count: 0,
  comment_count: 0,
  comments: []
})

// 加载状态
const loading = ref(false)

// 获取食堂详情
const fetchCanteenDetail = async () => {
  loading.value = true
  try {
    const response = await getCanteenDetail(canteenId.value)
    if (response.code === 0) {
      canteenInfo.value = response.data
    } else {
      ElMessage.error(response.msg || '获取食堂详情失败')
      // 使用默认数据
      useDefaultCanteenData()
    }
  } catch (error) {
    console.error('获取食堂详情出错:', error)
    ElMessage.error('获取食堂详情失败，请检查网络连接')
    // 使用默认数据
    useDefaultCanteenData()
  } finally {
    loading.value = false
  }
}

// 楼层格式化函数，将数字格式转为中文格式
const formatFloor = (floor) => {
  if (!floor) return '未知楼层'
  
  // 如果已经是中文格式（如"一楼"），则直接返回
  if (/^[一二三四五六七八九十]+楼$/.test(floor)) {
    return floor
  }
  
  // 尝试转换数字格式（如"1F"、"1"、"一层"等）
  const floorNumber = parseInt(floor.toString().replace(/[^0-9]/g, ''))
  if (isNaN(floorNumber)) return floor // 如果无法解析数字，返回原值
  
  // 数字转中文
  const chineseNumbers = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十']
  let chineseFloor
  
  if (floorNumber <= 10) {
    chineseFloor = chineseNumbers[floorNumber]
  } else if (floorNumber < 20) {
    chineseFloor = '十' + (floorNumber % 10 > 0 ? chineseNumbers[floorNumber % 10] : '')
  } else {
    chineseFloor = chineseNumbers[Math.floor(floorNumber / 10)] + '十' + (floorNumber % 10 > 0 ? chineseNumbers[floorNumber % 10] : '')
  }
  
  return chineseFloor + '楼'
}

// 获取楼层的数字值，用于排序
const getFloorNumber = (floor) => {
  // 处理中文楼层
  const chineseNumbers = { '零': 0, '一': 1, '二': 2, '三': 3, '四': 4, '五': 5, '六': 6, '七': 7, '八': 8, '九': 9, '十': 10 }
  
  if (/^[一二三四五六七八九十]+楼$/.test(floor)) {
    if (floor === '十楼') return 10
    if (floor.startsWith('十')) {
      // 处理十几楼
      const secondChar = floor.charAt(1)
      return secondChar ? 10 + chineseNumbers[secondChar] : 10
    }
    if (floor.includes('十')) {
      // 处理二十楼等
      const parts = floor.split('十')
      const tens = chineseNumbers[parts[0]] * 10
      const ones = parts[1] && parts[1] !== '楼' ? chineseNumbers[parts[1]] : 0
      return tens + ones
    }
    // 单个数字
    return chineseNumbers[floor.charAt(0)]
  }
  
  // 处理数字格式
  const match = floor.match(/\d+/)
  return match ? parseInt(match[0]) : 999 // 如果无法解析，设为一个大数，排在最后
}

// 获取档口列表
const fetchStallList = async () => {
  loading.value = true
  try {
    const response = await getStallList(canteenId.value)
    if (response.code === 0) {
      const stalls = response.data || []
      
      // 清空现有数据
      boothData.length = 0
      
      // 按楼层分组
      const floorGroups = {}
      
      stalls.forEach(stall => {
        const floor = stall.floor || '未知楼层'
        if (!floorGroups[floor]) {
          floorGroups[floor] = []
        }
        
        // 转换数据格式，适配前端展示
        floorGroups[floor].push({
          id: stall.stall_id,
          name: stall.name,
          description: stall.intro || '暂无简介',
          image: stall.img || '/src/assets/images/placeholder.jpg',
          // 暂时保留评分模拟数据，但不显示
          rating: 4.0 + Math.random(),
          canteen_id: stall.canteen_id
        })
      })
      
      // 将分组后的数据添加到boothData，并进行楼层格式化和排序
      Object.keys(floorGroups).forEach(floor => {
        boothData.push({
          originalFloor: floor,
          floor: formatFloor(floor),
          floorNumber: getFloorNumber(floor),
          booths: floorGroups[floor]
        })
      })
      
      // 按楼层数字从小到大排序
      boothData.sort((a, b) => {
        return a.floorNumber - b.floorNumber
      })
      
      // 如果没有数据，显示默认数据
      if (boothData.length === 0) {
        useDefaultBoothData()
      }
    } else {
      ElMessage.error(response.msg || '获取档口列表失败')
      // 使用默认数据
      useDefaultBoothData()
    }
  } catch (error) {
    console.error('获取档口列表出错:', error)
    ElMessage.error('获取档口列表失败，请检查网络连接')
    // 使用默认数据
    useDefaultBoothData()
  } finally {
    loading.value = false
  }
}

// 使用默认食堂数据（当API请求失败时）
const useDefaultCanteenData = () => {
  canteenInfo.value = {
    canteen_id: canteenId.value,
    name: `食堂 ${canteenId.value}`,
    location: '南开大学校区内',
    open_time: '早6:30-9:00 午11:00-13:00 晚17:00-19:00',
    img: './assets/images/placeholder.jpg',
    like_count: 0,
    comment_count: 0,
    comments: []
  }
}

// 使用默认档口数据（当API请求失败时）
const useDefaultBoothData = () => {
  // 清空现有数据
  boothData.length = 0
  
  // 添加默认数据
  boothData.push({
    originalFloor: '1',
    floor: '一楼',
    floorNumber: 1,
    booths: [
      { id: 1, name: '米饭窗口', description: '各类盖饭、米饭、炒饭', image: './assets/images/placeholder.jpg', rating: 4.5 },
      { id: 2, name: '面食窗口', description: '各种拉面、刀削面、炸酱面', image: './assets/images/placeholder.jpg', rating: 3.8 }
    ]
  })
  
  boothData.push({
    originalFloor: '2',
    floor: '二楼',
    floorNumber: 2,
    booths: [
      { id: 3, name: '川菜窗口', description: '麻辣香锅、水煮鱼片、回锅肉', image: './assets/images/placeholder.jpg', rating: 4.7 }
    ]
  })
}

// 组件挂载时获取数据
onMounted(() => {
  fetchCanteenDetail()
  fetchStallList()
})

// 处理档口点击
const handleBoothClick = (booth) => {
  // 在跳转前，将档口ID和对应的食堂ID存储到localStorage
  localStorage.setItem(`booth_${booth.id}_canteenId`, canteenId.value)
  router.push(`/booth/${booth.id}`)
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
</script>

<template>
  <div class="canteen-container" v-loading="loading">
    <!-- 食堂信息头部 -->
    <div class="canteen-header" :style="{ backgroundImage: `url(${getImageUrl(canteenInfo.img)})` }">
      <div class="canteen-header-content">
        <h1>{{ canteenInfo.name }}</h1>
        <div class="canteen-details">
          <p class="canteen-time"><i class="time-icon"></i>{{ canteenInfo.open_time }}</p>
          <p class="canteen-location"><i class="location-icon"></i>{{ canteenInfo.location }}</p>
          <!-- 移除点赞和评论统计
          <div class="canteen-stats">
            <span class="likes"><i class="like-icon"></i>{{ canteenInfo.like_count || 0 }}</span>
            <span class="comments"><i class="comment-icon"></i>{{ canteenInfo.comment_count || 0 }}</span>
          </div>
          -->
        </div>
        
        <!-- 移除评论显示
        <div v-if="canteenInfo.comments && canteenInfo.comments.length > 0" class="canteen-comments">
          <div class="comment-title">最新评论</div>
          <div class="comment-item" v-for="comment in canteenInfo.comments.slice(0, 2)" :key="comment.comment_id">
            <div class="comment-user">{{ comment.user?.nickname || '匿名用户' }}</div>
            <div class="comment-content">{{ comment.content }}</div>
          </div>
        </div>
        -->
      </div>
    </div>
    
    <!-- 档口列表 -->
    <div class="booth-list">
      <div v-for="(floor, index) in boothData" :key="index" class="booth-category">
        <div class="category-divider">
          <span class="category-name">{{ floor.floor }}</span>
          <div class="divider-line"></div>
        </div>
        
        <div class="booths-grid">
          <div 
            v-for="booth in floor.booths" 
            :key="booth.id"
            class="booth-card"
            @click="handleBoothClick(booth)"
          >
            <div class="booth-image" :style="{ backgroundImage: `url(${getImageUrl(booth.image)})` }">
              <div class="booth-info">
                <div class="booth-header">
                  <h3>{{ booth.name }}</h3>
                  <!-- 移除评分星级
                  <div class="booth-rating">
                    <div class="stars">
                      <span v-for="i in 5" :key="i" class="star" :class="{ 'filled': i <= Math.floor(booth.rating), 'half': i === Math.ceil(booth.rating) && !Number.isInteger(booth.rating) }"></span>
                    </div>
                  </div>
                  <div class="booth-likes">
                    <span>{{ booth.likes }}人赞过</span>
                    <i class="like-icon"></i>
                  </div>
                  -->
                </div>
                <p>{{ booth.description }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.canteen-container {
  padding-top: 0px;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
  max-width: 1400px;
  margin: 0 auto;
}

.canteen-header {
  height: 300px;
  background-size: cover;
  background-position: center;
  position: relative;
  border-radius: 0 0 17px 17px;
  overflow: hidden;
}

.canteen-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.7));
}

.canteen-header-content {
  position: relative;
  padding: 30px;
  color: white;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.canteen-header-content h1 {
  margin: 0 0 10px;
  font-size: 2rem;
}

.canteen-details {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: center;
}

.canteen-time, .canteen-location {
  display: flex;
  align-items: center;
  margin: 0;
  font-size: 0.9rem;
}

.time-icon, .location-icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-right: 5px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
}

.time-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M11.99 2C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z'/%3E%3Cpath d='M12.5 7H11v6l5.25 3.15.75-1.23-4.5-2.67z'/%3E%3C/svg%3E");
}

.location-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z'/%3E%3C/svg%3E");
}

/* 移除点赞和评论图标
.like-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z'/%3E%3C/svg%3E");
}

.comment-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M21.99 4c0-1.1-.89-2-1.99-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18zM18 14H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z'/%3E%3C/svg%3E");
}

.canteen-stats {
  display: flex;
  align-items: center;
  gap: 10px;
}

.likes, .comments {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
}

.canteen-comments {
  margin-top: 15px;
}

.comment-title {
  font-size: 0.9rem;
  margin-bottom: 5px;
  opacity: 0.9;
}

.comment-item {
  background-color: rgba(255, 255, 255, 0.1);
  padding: 8px 12px;
  border-radius: 8px;
  margin-bottom: 5px;
  font-size: 0.85rem;
}

.comment-user {
  font-weight: bold;
  margin-bottom: 3px;
}

.comment-content {
  opacity: 0.9;
}
*/

.booth-list {
  padding: 20px;
}

.booth-category {
  margin-bottom: 30px;
}

.category-divider {
  display: flex;
  align-items: center;
  margin: 20px 0;
}

.category-name {
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
  margin-right: 15px;
}

.divider-line {
  flex: 1;
  height: 1px;
  background-color: #ddd;
}

.booths-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.booth-card {
  border-radius: 17px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  height: 280px;
  position: relative;
}

.booth-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.booth-image {
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  position: relative;
}

.booth-info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.2));
  padding: 10px 15px 15px 15px;
  color: white;
  height: 35%;
  transition: height 0.3s ease, background 0.3s ease;
  overflow-y: auto;
}

.booth-card:hover .booth-info {
  height: 70%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.4));
}

.booth-header {
  display: flex;
  flex-direction: column;
  margin-bottom: 5px;
  position: relative;
}

/* 移除评分样式
.booth-rating {
  display: flex;
  align-items: center;
  margin-top: 6px;
}

.stars {
  display: flex;
}

.star {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-right: 2px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23cccccc' viewBox='0 0 24 24'%3E%3Cpath d='M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z'/%3E%3C/svg%3E");
  background-size: contain;
  background-repeat: no-repeat;
}

.star.filled {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23ffc107' viewBox='0 0 24 24'%3E%3Cpath d='M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z'/%3E%3C/svg%3E");
}

.star.half {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cdefs%3E%3ClinearGradient id='grad' x1='0%25' y1='0%25' x2='100%25' y2='0%25'%3E%3Cstop offset='50%25' style='stop-color:%23ffc107;stop-opacity:1' /%3E%3Cstop offset='50%25' style='stop-color:%23cccccc;stop-opacity:1' /%3E%3C/linearGradient%3E%3C/defs%3E%3Cpath fill='url(%23grad)' d='M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z'/%3E%3C/svg%3E");
}

.booth-likes {
  position: absolute;
  top: 8px;
  right: -10px;
  font-size: 0.8rem;
  color: rgba(255, 255, 255, 0.7);
  display: flex;
  align-items: center;
}
*/

.booth-info h3 {
  margin: 0;
  font-size: 1.3rem;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.booth-info p {
  margin: 0;
  font-size: 0.9rem;
  line-height: 1.4;
  word-wrap: break-word;
  overflow-wrap: break-word;
}

.booth-info::-webkit-scrollbar {
  width: 0;
  display: none;
}

.booth-info {
  -ms-overflow-style: none;  /* IE and Edge */
  scrollbar-width: none;  /* Firefox */
}

/* 响应式设计 */
@media (max-width: 768px) {
  .canteen-header {
    height: 220px;
  }
  
  .booths-grid {
    grid-template-columns: 1fr;
  }
  
  /* 移除评论相关样式
  .canteen-comments {
    margin-top: 10px;
  }
  
  .comment-item {
    padding: 6px 10px;
  }
  */
}
</style> 