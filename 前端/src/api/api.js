import axios from 'axios'

const BASE_URL = 'https://akjqgjgkcqvn.sealoshzh.site/api'

/**
 * 获取食堂列表
 * @returns {Promise} 返回食堂列表数据
 */
export function getCanteenList() {
  return axios.get(`${BASE_URL}/canteen/list`)
    .then(response => response.data)
}

/**
 * 获取食堂详情
 * @param {number} canteenId 食堂ID
 * @returns {Promise} 返回食堂详情数据
 */
export function getCanteenDetail(canteenId) {
  return axios.get(`${BASE_URL}/canteen/detail`, {
    params: { canteen_id: canteenId }
  }).then(response => response.data)
}

/**
 * 添加食堂（管理员功能）
 * @param {Object} canteenData 食堂数据
 * @returns {Promise}
 */
export function addCanteen(canteenData) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/canteen/add`, canteenData, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

/**
 * 更新食堂信息（管理员功能）
 * @param {Object} canteenData 食堂数据
 * @returns {Promise}
 */
export function updateCanteen(canteenData) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/canteen/update`, canteenData, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

/**
 * 删除食堂（管理员功能）
 * @param {number} canteenId 食堂ID
 * @returns {Promise}
 */
export function deleteCanteen(canteenId) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/canteen/delete`, {}, {
    params: { canteen_id: canteenId },
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

/**
 * 获取档口列表
 * @param {Number} canteenId 食堂ID
 * @returns {Promise} 返回档口列表数据
 */
export function getStallList(canteenId) {
  return axios.get(`${BASE_URL}/stall/list`, {
    params: { canteen_id: canteenId }
  }).then(response => response.data)
}

/**
 * 获取档口详情
 * @param {Number} stallId 档口ID
 * @returns {Promise} 返回档口详情数据
 */
export function getStallDetail(stallId) {
  return axios.get(`${BASE_URL}/stall/detail`, {
    params: { stall_id: stallId }
  }).then(response => response.data)
}

/**
 * 添加档口（管理员功能）
 * @param {Object} stallData 档口数据
 * @returns {Promise}
 */
export function addStall(stallData) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/stall/add`, stallData, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

/**
 * 更新档口信息（管理员功能）
 * @param {Object} stallData 档口数据
 * @returns {Promise}
 */
export function updateStall(stallData) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/stall/update`, stallData, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

/**
 * 删除档口（管理员功能）
 * @param {Number} stallId 档口ID
 * @returns {Promise}
 */
export function deleteStall(stallId) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/stall/delete`, {}, {
    params: { stall_id: stallId },
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

/**
 * 获取菜品列表
 * @param {Number} stallId 档口ID
 * @returns {Promise} 返回菜品列表数据
 */
export function getDishList(stallId) {
  return axios.get(`${BASE_URL}/dish/list`, {
    params: { stall_id: stallId }
  }).then(response => response.data)
}

/**
 * 获取菜品详情
 * @param {Number} dishId 菜品ID
 * @returns {Promise} 返回菜品详情数据
 */
export function getDishDetail(dishId) {
  return axios.get(`${BASE_URL}/dish/detail`, {
    params: { dish_id: dishId }
  }).then(response => response.data)
}

/**
 * 添加菜品（管理员功能）
 * @param {Object} dishData 菜品数据
 * @returns {Promise}
 */
export function addDish(dishData) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/dish/add`, dishData, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

/**
 * 更新菜品信息（管理员功能）
 * @param {Object} dishData 菜品数据
 * @returns {Promise}
 */
export function updateDish(dishData) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/dish/update`, dishData, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

/**
 * 删除菜品（管理员功能）
 * @param {Number} dishId 菜品ID
 * @returns {Promise}
 */
export function deleteDish(dishId) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/dish/delete`, {}, {
    params: { dish_id: dishId },
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

// 用户相关API接口
// 登录
export function login(data) {
  return axios.post(`${BASE_URL}/user/login`, data)
    .then(response => response.data)
}

// 注册
export function register(data) {
  return axios.post(`${BASE_URL}/user/register`, data)
    .then(response => response.data)
}

// 登出
export function logout() {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/user/logout`, {}, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

// 获取用户信息
export function getUserProfile() {
  const token = localStorage.getItem('token')
  return axios.get(`${BASE_URL}/user/profile`, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

// 更新用户信息
export function updateUserProfile(data) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/user/update`, data, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
}

// 评分功能API接口（预留）
export function rateTarget(targetType, targetId, score) {
  const token = localStorage.getItem('token')
  return axios.post(`${BASE_URL}/rating/rate`, {
    target_type: targetType,
    target_id: targetId,
    score: score
  }, {
    headers: {
      Authorization: token ? `Bearer ${token}` : ''
    }
  }).then(response => response.data)
} 