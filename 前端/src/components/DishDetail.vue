<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
  dish: {
    type: Object,
    required: true
  },
  visible: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['close']);

// 关闭窗口
const closeModal = () => {
  emit('close');
};

// 获取星级
const getDishStars = (rating) => {
  const fullStars = Math.floor(rating);
  const hasHalfStar = rating % 1 >= 0.5;
  const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);
  
  return {
    full: fullStars,
    half: hasHalfStar ? 1 : 0,
    empty: emptyStars
  };
};

// 模拟评论数据
const comments = ref([
  {
    id: 1,
    user: {
      name: '张同学',
      avatar: 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/memo_9.png'
    },
    content: '这道菜真的太好吃了，下次还会再来点！味道很正宗，分量也很足，强烈推荐给大家。',
    time: '2023-05-15 12:30',
    likes: 24
  },
  {
    id: 2,
    user: {
      name: '李同学',
      avatar: 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/memo_8.png'
    },
    content: '价格实惠，服务态度也很好，就是等的时间有点长。',
    time: '2023-05-14 18:45',
    likes: 16
  },
  {
    id: 3,
    user: {
      name: '王老师',
      avatar: 'https://cdn.jsdelivr.net/gh/alohe/avatars/png/memo_13.png'
    },
    content: '作为常客，我觉得这家店的水平一直很稳定，这道菜是我的最爱。',
    time: '2023-05-13 19:20',
    likes: 38
  }
]);

// 推荐菜品
const recommendations = ref([
  { id: 101, name: '宫保鸡丁', image: './assets/images/placeholder.jpg' },
  { id: 102, name: '糖醋里脊', image: './assets/images/placeholder.jpg' }
]);

// 用户评论
const userComment = ref('');

// 提交评论
const submitComment = () => {
  if (userComment.value.trim() === '') return;
  
  comments.value.unshift({
    id: Date.now(),
    user: {
      name: '我',
      avatar: './assets/images/user-avatar.jpg'
    },
    content: userComment.value,
    time: new Date().toLocaleString(),
    likes: 0
  });
  
  userComment.value = '';
};
</script>

<template>
  <div v-if="visible && dish" class="modal-overlay" @click="closeModal">
    <div class="modal-container" @click.stop>
      <button class="close-button" @click="closeModal">×</button>
      
      <!-- 上半部分：菜品基本信息 -->
      <div class="dish-info-section">
        <div class="dish-image">
          <img :src="dish.image ? dish.image.replace('/src', '.') : './assets/images/placeholder.jpg'" alt="菜品图片">
        </div>
        <div class="dish-details">
          <h2>{{ dish.name }}</h2>
          <div class="dish-price">¥{{ dish.price }}</div>
          <div class="dish-description-container">
            <p class="dish-description">{{ dish.description }}</p>
          </div>
          <div class="dish-footer">
            <div class="dish-rating">
              <span v-for="i in getDishStars(dish.rating).full" :key="`full-${i}`" class="dish-star filled"></span>
              <span v-if="getDishStars(dish.rating).half" class="dish-star half"></span>
              <span v-for="i in getDishStars(dish.rating).empty" :key="`empty-${i}`" class="dish-star"></span>
              <span class="rating-number">{{ dish.rating }}</span>
            </div>
            <div class="dish-likes">
              {{ dish.likes }}人赞过
              <i class="like-icon"></i>
            </div>
          </div>
        </div>
      </div>
      
      <div class="divider"></div>
      
      <!-- 下半部分：评论和推荐 -->
      <div class="bottom-section">
        <!-- 评论区 -->
        <div class="comments-section">
          <h3>用户评论</h3>
          <div class="comments-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-avatar">
                <img :src="comment.user.avatar ? comment.user.avatar.replace('/src', '.') : './assets/images/placeholder.jpg'" :alt="comment.user.name">
              </div>
              <div class="comment-content">
                <div class="comment-user">{{ comment.user.name }}</div>
                <div class="comment-text">{{ comment.content }}</div>
                <div class="comment-footer">
                  <span class="comment-time">{{ comment.time }}</span>
                  <div class="comment-likes">
                    <span>{{ comment.likes }}</span>
                    <i class="like-icon-small"></i>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 评论输入框 -->
          <div class="comment-input">
            <div class="user-avatar">
              <img src="https://cdn.jsdelivr.net/gh/alohe/avatars/png/memo_6.png" alt="用户头像">
            </div>
            <div class="input-container">
              <textarea 
                v-model="userComment" 
                placeholder="写下你的评论..." 
                @keyup.enter="submitComment"
              ></textarea>
              <button @click="submitComment">发送</button>
            </div>
          </div>
        </div>
        
        <!-- 猜你想吃区 -->
        <div class="recommendations-section">
          <h3>猜你想吃</h3>
          <div class="recommendations-list">
            <div v-for="rec in recommendations" :key="rec.id" class="recommendation-item">
              <img :src="rec.image ? rec.image.replace('/src', '.') : './assets/images/placeholder.jpg'" :alt="rec.name">
              <div class="recommendation-name">{{ rec.name }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  width: 80%;
  max-width: 900px;
  max-height: 80vh;
  height: 1400px;
  background-color: white;
  border-radius: 17px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  font-family: "Inria Serif", "楷体", KaiTi, "KaiTi_GB2312", serif;
}

.close-button {
  position: absolute;
  top: 15px;
  right: 15px;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
  z-index: 10;
}

/* 上半部分：菜品基本信息 */
.dish-info-section {
  display: flex;
  padding: 30px 30px 20px;
}

.dish-image {
  width: 40%;
  height: 250px;
  overflow: hidden;
  border-radius: 12px;
  margin-right: 30px;
}

.dish-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.dish-details {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.dish-details h2 {
  margin: 0 0 10px;
  font-size: 1.8rem;
  color: #333;
}

.dish-price {
  font-size: 1.4rem;
  color: #E74C3C;
  font-weight: bold;
  margin-bottom: 15px;
}

.dish-description-container {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 15px;
}

.dish-description {
  font-size: 1rem;
  color: #666;
  line-height: 1.6;
  margin: 0;
}

.dish-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dish-rating {
  display: flex;
  align-items: center;
}

.dish-star {
  display: inline-block;
  width: 18px;
  height: 18px;
  margin-right: 3px;
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

.rating-number {
  margin-left: 5px;
  font-weight: bold;
  color: #ffc107;
}

.dish-likes {
  font-size: 0.9rem;
  color: #777;
  display: flex;
  align-items: center;
}

.like-icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-left: 5px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='rgba(230, 21, 21, 0.74)' viewBox='0 0 24 24'%3E%3Cpath d='M1 21h4V9H1v12zm22-11c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.44-1.06L14.17 1 7.59 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2h9c.83 0 1.54-.5 1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-2z'/%3E%3C/svg%3E");
}

/* 分割线 */
.divider {
  height: 1px;
  background-color: #eee;
  margin: 0 30px;
}

/* 下半部分：评论和推荐 */
.bottom-section {
  display: flex;
  padding: 0px 30px 30px;
  max-height: 400px;
}

/* 评论区 */
.comments-section {
  flex: 3;
  margin-right: 30px;
}

.comments-section h3, .recommendations-section h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.2rem;
  color: #333;
}

.comments-list {
  max-height: 280px;
  overflow-y: auto;
}

.comment-item {
  display: flex;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.comment-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.comment-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.comment-user {
  font-weight: bold;
  margin-bottom: 5px;
  color: #333;
}

.comment-text {
  margin-bottom: 8px;
  line-height: 1.4;
  color: #555;
}

.comment-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.8rem;
}

.comment-time {
  color: #999;
}

.comment-likes {
  display: flex;
  align-items: center;
  color: #999;
}

.like-icon-small {
  display: inline-block;
  width: 12px;
  height: 12px;
  margin-left: 3px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='rgba(230, 21, 21, 0.74)' viewBox='0 0 24 24'%3E%3Cpath d='M1 21h4V9H1v12zm22-11c0-1.1-.9-2-2-2h-6.31l.95-4.57.03-.32c0-.41-.17-.79-.44-1.06L14.17 1 7.59 7.59C7.22 7.95 7 8.45 7 9v10c0 1.1.9 2 2 2h9c.83 0 1.54-.5 1.84-1.22l3.02-7.05c.09-.23.14-.47.14-.73v-2z'/%3E%3C/svg%3E");
}

/* 评论输入框 */
.comment-input {
  display: flex;
  margin-top: 15px;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 15px;
  flex-shrink: 0;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.input-container {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.input-container textarea {
  width: 100%;
  height: 60px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
  resize: none;
  margin-bottom: 10px;
  font-family: inherit;
}

.input-container button {
  align-self: flex-end;
  padding: 6px 15px;
  background-color: #E74C3C;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* 猜你想吃区 */
.recommendations-section {
  flex: 1;
}

.recommendations-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.recommendation-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-radius: 8px;
  background-color: #f9f9f9;
  cursor: pointer;
  transition: background-color 0.2s;
}

.recommendation-item:hover {
  background-color: #f0f0f0;
}

.recommendation-item img {
  width: 50px;
  height: 50px;
  border-radius: 6px;
  object-fit: cover;
  margin-right: 10px;
}

.recommendation-name {
  font-weight: bold;
  color: #333;
}

/* 滚动条样式 */
.dish-description-container::-webkit-scrollbar,
.comments-list::-webkit-scrollbar {
  width: 4px;
}

.dish-description-container::-webkit-scrollbar-track,
.comments-list::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.dish-description-container::-webkit-scrollbar-thumb,
.comments-list::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 2px;
}

.dish-description-container::-webkit-scrollbar-thumb:hover,
.comments-list::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .modal-container {
    width: 95%;
  }
  
  .dish-info-section {
    flex-direction: column;
  }
  
  .dish-image {
    width: 100%;
    margin-right: 0;
    margin-bottom: 20px;
  }
  
  .bottom-section {
    flex-direction: column;
  }
  
  .comments-section {
    margin-right: 0;
    margin-bottom: 20px;
  }
}
</style> 