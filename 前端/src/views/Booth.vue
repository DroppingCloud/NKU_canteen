<script setup>
import { ref, computed, onMounted, reactive, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getStallDetail, getDishList } from '../api/api'
import { ElMessage } from 'element-plus'
import DishDetail from '../components/DishDetail.vue'

const route = useRoute()
const router = useRouter()
const boothId = computed(() => route.params.id)

// 档口信息
const boothInfo = ref({
  stall_id: '',
  name: '',
  intro: '',
  floor: '',
  img: '',
  // 不再显示评价信息，但仍然保留字段以便后续需要
  like_count: 0,
  comment_count: 0,
  comments: []
})

// 菜品数据，按分类
const dishData = reactive([])

// 加载状态
const loading = ref(false)
// 是否出现错误
const hasError = ref(false)

// 监听路由变化，当路由参数改变时重新获取数据
watch(() => route.params.id, (newId, oldId) => {
  if (newId && newId !== oldId) {
    fetchData()
  }
}, { immediate: false })

// 统一的数据获取函数
const fetchData = async () => {
  hasError.value = false
  await Promise.all([fetchStallDetail(), fetchDishList()])
}

// 获取档口详情
const fetchStallDetail = async () => {
  loading.value = true
  try {
    const response = await getStallDetail(boothId.value)
    if (response.code === 0) {
      boothInfo.value = response.data
      // 获取成功后，确保更新档口对应的食堂ID信息
      if (boothInfo.value.canteen_id) {
        localStorage.setItem(`booth_${boothId.value}_canteenId`, boothInfo.value.canteen_id)
      }
    } else {
      ElMessage.error(response.msg || '获取档口详情失败')
      // 使用默认数据
      useDefaultBoothData()
      hasError.value = true
    }
  } catch (error) {
    console.error('获取档口详情出错:', error)
    ElMessage.error('获取档口详情失败，请检查网络连接')
    // 使用默认数据
    useDefaultBoothData()
    hasError.value = true
  } finally {
    loading.value = false
  }
}

// 获取菜品列表
const fetchDishList = async () => {
  loading.value = true
  try {
    const response = await getDishList(boothId.value)
    if (response.code === 0) {
      const dishes = response.data || []
      
      // 清空现有数据
      dishData.length = 0
      
      // 模拟分类，实际接口可能不支持菜品分类
      // 这里简单地根据价格进行分类
      const categories = {
        '招牌菜品': [],
        '热销菜品': [],
        '特色菜品': []
      }
      
      dishes.forEach(dish => {
        // 将菜品转换为前端显示格式
        const dishItem = {
          id: dish.dish_id,
          name: dish.name,
          price: dish.price,
          description: dish.intro || '暂无描述',
          image: dish.image_url || '/src/assets/images/placeholder.jpg',
          // 不再显示评价信息，但保留评分数据用于分类
          rating: 4.0 + Math.random(),
          comments: dish.comments || []
        }
        
        // 简单的分类逻辑
        if (dish.like_count > 50) {
          categories['招牌菜品'].push(dishItem)
        } else if (dish.price > 15) {
          categories['特色菜品'].push(dishItem)
        } else {
          categories['热销菜品'].push(dishItem)
        }
      })
      
      // 将分类后的数据添加到dishData
      Object.keys(categories).forEach(category => {
        if (categories[category].length > 0) {
          dishData.push({
            category,
            dishes: categories[category]
          })
        }
      })
      
      // 如果没有数据，显示默认数据
      if (dishData.length === 0) {
        useDefaultDishData()
      }
    } else {
      ElMessage.error(response.msg || '获取菜品列表失败')
      // 使用默认数据
      useDefaultDishData()
      hasError.value = true
    }
  } catch (error) {
    console.error('获取菜品列表出错:', error)
    ElMessage.error('获取菜品列表失败，请检查网络连接')
    // 使用默认数据
    useDefaultDishData()
    hasError.value = true
  } finally {
    loading.value = false
  }
}

// 使用默认档口数据（当API请求失败时）
const useDefaultBoothData = () => {
  boothInfo.value = {
    stall_id: boothId.value,
    name: `档口 ${boothId.value}`,
    intro: '提供各种美食，品种丰富，满足各种口味需求。',
    floor: '一楼',
    img: './assets/images/placeholder.jpg',
    like_count: 0,
    comment_count: 0,
    comments: []
  }
}

// 使用默认菜品数据（当API请求失败时）
const useDefaultDishData = () => {
  // 清空现有数据
  dishData.length = 0
  
  // 添加默认数据
  dishData.push({
    category: '招牌菜品',
    dishes: [
      { id: 1, name: '红烧排骨', price: 15, description: '精选五花肉，红烧工艺，肥而不腻', image: './assets/images/placeholder.jpg', rating: 4.7 },
      { id: 2, name: '麻婆豆腐', price: 12, description: '正宗川味，麻辣鲜香，口感丰富', image: './assets/images/placeholder.jpg', rating: 4.5 }
    ]
  })
  
  dishData.push({
    category: '热销菜品',
    dishes: [
      { id: 4, name: '宫保鸡丁', price: 14, description: '鸡肉鲜嫩，搭配花生，口感香脆', image: './assets/images/placeholder.jpg', rating: 4.6 }
    ]
  })
}

// 组件挂载时获取数据
onMounted(() => {
  fetchData()
})

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

// 格式化后的楼层显示
const formattedFloor = computed(() => {
  return formatFloor(boothInfo.value.floor)
})

// 菜品详情弹窗
const selectedDish = ref(null);
const isDetailModalVisible = ref(false);

// 打开菜品详情
const openDishDetail = (dish) => {
  selectedDish.value = dish;
  isDetailModalVisible.value = true;
};

// 关闭菜品详情
const closeDishDetail = () => {
  isDetailModalVisible.value = false;
};

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
  <div class="booth-container" v-loading="loading">
    <!-- 错误提示 -->
    <div v-if="hasError" class="error-message">
      <p>加载档口信息出现问题，显示的是默认数据。</p>
      <el-button type="primary" @click="fetchData">重新加载</el-button>
    </div>
    
    <!-- 档口信息头部 -->
    <div class="booth-header" :style="{ backgroundImage: `url(${getImageUrl(boothInfo.img)})` }">
      <div class="booth-header-content">
        <h1>{{ boothInfo.name }}</h1>
        <p class="booth-description">{{ boothInfo.intro }}</p>
        <div class="booth-details">
          <p class="booth-floor"><i class="floor-icon"></i>{{ formattedFloor }}</p>
          <!-- 移除评分显示
          <p class="booth-rating">
            <span class="rating-stars">
              <span v-for="(star, i) in stars.full" :key="`full-${i}`" class="star full">{{ star }}</span>
              <span v-for="(star, i) in stars.half" :key="`half-${i}`" class="star half">{{ star }}</span>
              <span v-for="(star, i) in stars.empty" :key="`empty-${i}`" class="star empty">{{ star }}</span>
            </span>
            <span class="rating-number">{{ boothRating }}</span>
          </p>
          <div class="booth-stats">
            <span class="likes"><i class="like-icon-header"></i>{{ boothInfo.like_count || 0 }}</span>
            <span class="comments"><i class="comment-icon"></i>{{ boothInfo.comment_count || 0 }}</span>
          </div>
          -->
        </div>
        
        <!-- 移除评论显示
        <div v-if="boothInfo.comments && boothInfo.comments.length > 0" class="booth-comments">
          <div class="comment-title">最新评论</div>
          <div class="comment-item" v-for="comment in boothInfo.comments.slice(0, 2)" :key="comment.comment_id">
            <div class="comment-user">{{ comment.user?.nickname || '匿名用户' }}</div>
            <div class="comment-content">{{ comment.content }}</div>
          </div>
        </div>
        -->
      </div>
    </div>
    
    <!-- 菜品列表 -->
    <div class="dish-list">
      <div v-for="(category, index) in dishData" :key="index" class="dish-category">
        <div class="category-divider">
          <span class="category-name">{{ category.category }}</span>
          <div class="divider-line"></div>
        </div>
        
        <div class="dishes-grid">
          <div 
            v-for="dish in category.dishes" 
            :key="dish.id"
            class="dish-card"
            @click="openDishDetail(dish)"
          >
            <div class="dish-image" :style="{ backgroundImage: `url(${getImageUrl(dish.image)})` }"></div>
            <div class="dish-info">
              <div class="dish-name-price">
                <h3>{{ dish.name }}</h3>
                <span class="dish-price">¥{{ dish.price }}</span>
              </div>
              <p class="dish-description">{{ dish.description }}</p>
              <!-- 移除菜品评分和点赞显示
              <div class="dish-footer">
                <div class="dish-rating">
                  <span v-for="i in getDishStars(dish.rating).full" :key="`full-${i}`" class="dish-star filled"></span>
                  <span v-if="getDishStars(dish.rating).half" class="dish-star half"></span>
                  <span v-for="i in getDishStars(dish.rating).empty" :key="`empty-${i}`" class="dish-star"></span>
                </div>
                <div class="dish-likes">
                  {{ dish.likes }}人赞过
                  <i class="like-icon"></i>
                </div>
              </div>
              -->
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 菜品详情弹窗 -->
    <DishDetail
      :dish="selectedDish || {}"
      :visible="isDetailModalVisible"
      @close="closeDishDetail"
    />
  </div>
</template>

<style scoped>
.error-message {
  background-color: #fef0f0;
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.error-message p {
  margin: 0;
  color: #f56c6c;
}

/* 其他现有样式保持不变 */
.booth-container {
  padding-top: 0px 0px;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
  max-width: 1400px;
  margin: 0 auto;
}

.booth-header {
  height: 300px;
  background-size: cover;
  background-position: center;
  position: relative;
  border-radius: 0 0 17px 17px;
  overflow: hidden;
}

.booth-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.7));
}

.booth-header-content {
  position: relative;
  padding: 30px;
  color: white;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.booth-header-content h1 {
  margin: 0 0 10px;
  font-size: 2rem;
}

.booth-description {
  margin: 0 0 15px;
  font-size: 1rem;
  max-width: 80%;
}

.booth-details {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: center;
}

.booth-floor {
  display: flex;
  align-items: center;
  margin: 0;
  font-size: 0.9rem;
}

/* 移除评分相关样式
.booth-rating {
  display: flex;
  align-items: center;
  margin: 0;
  font-size: 0.9rem;
}

.booth-stats {
  display: flex;
  align-items: center;
  gap: 10px;
}

.likes, .comments {
  display: flex;
  align-items: center;
  font-size: 0.9rem;
}

.rating-stars {
  display: flex;
  margin-right: 5px;
}

.star {
  color: gold;
  font-size: 1rem;
}

.star.empty {
  color: rgba(255, 255, 255, 0.5);
}

.rating-number {
  font-weight: bold;
}
*/

.floor-icon /*, .like-icon-header, .comment-icon*/ {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-right: 5px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
}

.floor-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M4 10v7h3v-7H4zm6 0v7h3v-7h-3zM2 22h19v-3H2v3zm14-12v7h3v-7h-3zm-4.5-9L2 6v2h19V6l-9.5-5z'/%3E%3C/svg%3E");
}

/* 移除点赞和评论图标
.like-icon-header {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z'/%3E%3C/svg%3E");
}

.comment-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='white' viewBox='0 0 24 24'%3E%3Cpath d='M21.99 4c0-1.1-.89-2-2-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14l4 4-.01-18zM18 14H6v-2h12v2zm0-3H6V9h12v2zm0-3H6V6h12v2z'/%3E%3C/svg%3E");
}

.booth-comments {
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

.dish-list {
  padding: 20px;
}

.dish-category {
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

.dishes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.dish-card {
  border-radius: 17px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
  display: flex;
  height: 120px;
}

.dish-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.dish-image {
  width: 120px;
  height: 120px;
  background-size: cover;
  background-position: center;
  flex-shrink: 0;
}

.dish-info {
  flex: 1;
  padding: 15px;
  display: flex;
  flex-direction: column;
}

.dish-name-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.dish-name-price h3 {
  margin: 0;
  font-size: 1.1rem;
}

.dish-price {
  color: #E74C3C;
  font-weight: bold;
}

.dish-description {
  margin: 0;
  font-size: 0.9rem;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  min-height: 40px;
}

/* 移除菜品评分和点赞相关样式
.dish-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.dish-rating {
  display: flex;
  align-items: center;
}

.dish-star {
  display: inline-block;
  width: 14px;
  height: 14px;
  margin-right: 2px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23cccccc' viewBox='0 0 24 24'%3E%3Cpath d='M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z'/%3E%3C/svg%3E");
  background-size: contain;
  background-repeat: no-repeat;
}

.dish-star.filled {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='%23ffc107' viewBox='0 0 24 24'%3E%3Cpath d='M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z'/%3E%3C/svg%3E");
}

.dish-star.half {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cdefs%3E%3ClinearGradient id='grad' x1='0%25' y1='0%25' x2='100%25' y2='0%25'%3E%3Cstop offset='50%25' style='stop-color:%23ffc107;stop-opacity:1' /%3E%3Cstop offset='50%25' style='stop-color:%23cccccc;stop-opacity:1' /%3E%3C/linearGradient%3E%3C/defs%3E%3Cpath fill='url(%23grad)' d='M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z'/%3E%3C/svg%3E");
}

.dish-likes {
  font-size: 0.8rem;
  color: #999;
  display: flex;
  align-items: center;
}

.like-icon {
  display: inline-block;
  width: 14px;
  height: 14px;
  margin-left: 5px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='rgba(230, 21, 21, 0.74)' viewBox='0 0 24 24'%3E%3Cpath d='M1 21h4V9H1v12zm22-11c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.44-1.06L14.17 1 7.59 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2h9c.83 0 1.54-.5 1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-2z'/%3E%3C/svg%3E");
}
*/

/* 响应式设计 */
@media (max-width: 768px) {
  .booth-header {
    height: 220px;
  }
  
  .booth-description {
    max-width: 100%;
  }
  
  .dishes-grid {
    grid-template-columns: 1fr;
  }
  
  /* 移除评论相关样式
  .booth-comments {
    margin-top: 10px;
  }
  
  .comment-item {
    padding: 6px 10px;
  }
  */
}
</style> 