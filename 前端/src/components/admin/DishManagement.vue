<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDishList, addDish, updateDish, deleteDish, getCanteenList, getStallList } from '../../api/api'

// 菜品列表数据
const dishList = ref([])
// 食堂列表（用于选择）
const canteenOptions = ref([])
// 档口列表（用于选择）
const stallOptions = ref([])
// 加载状态
const loading = ref(false)
// 对话框可见性
const dialogVisible = ref(false)
// 当前编辑的菜品
const currentDish = ref({
  name: '',
  intro: '',
  price: 0,
  image_url: '',
  stall_id: null
})
// 对话框类型（添加/编辑）
const dialogType = ref('add')
// 选择的食堂ID（用于筛选档口）
const selectedCanteenId = ref(null)
// 选择的档口ID（用于筛选菜品）
const selectedStallId = ref(null)

// 表单规则
const rules = {
  name: [
    { required: true, message: '请输入菜品名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' },
    { type: 'number', message: '价格必须为数字', trigger: 'blur' }
  ],
  stall_id: [
    { required: true, message: '请选择所属档口', trigger: 'change' }
  ]
}

// 获取菜品列表
const fetchDishList = async () => {
  if (!selectedStallId.value) return
  
  loading.value = true
  try {
    const res = await getDishList(selectedStallId.value)
    if (res.code === 0) {
      dishList.value = res.data
    } else {
      ElMessage.error(res.msg || '获取菜品列表失败')
    }
  } catch (error) {
    console.error('获取菜品列表错误:', error)
    ElMessage.error('获取菜品列表失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 获取食堂列表
const fetchCanteenOptions = async () => {
  try {
    const res = await getCanteenList()
    if (res.code === 0) {
      canteenOptions.value = res.data.map(item => ({
        label: item.name,
        value: item.canteen_id
      }))
      
      // 如果有食堂选项，默认选择第一个
      if (canteenOptions.value.length > 0 && !selectedCanteenId.value) {
        selectedCanteenId.value = canteenOptions.value[0].value
        fetchStallOptions()
      }
    }
  } catch (error) {
    console.error('获取食堂选项错误:', error)
  }
}

// 获取档口列表
const fetchStallOptions = async () => {
  if (!selectedCanteenId.value) return
  
  try {
    const res = await getStallList(selectedCanteenId.value)
    if (res.code === 0) {
      stallOptions.value = res.data.map(item => ({
        label: item.name,
        value: item.stall_id
      }))
      
      // 如果有档口选项，默认选择第一个
      if (stallOptions.value.length > 0) {
        if (!selectedStallId.value || !stallOptions.value.some(item => item.value === selectedStallId.value)) {
          selectedStallId.value = stallOptions.value[0].value
          fetchDishList()
        }
      } else {
        selectedStallId.value = null
        dishList.value = []
      }
    }
  } catch (error) {
    console.error('获取档口选项错误:', error)
  }
}

// 处理食堂选择变化
const handleCanteenChange = () => {
  selectedStallId.value = null
  stallOptions.value = []
  dishList.value = []
  fetchStallOptions()
}

// 处理档口选择变化
const handleStallChange = () => {
  fetchDishList()
}

// 打开添加对话框
const openAddDialog = () => {
  if (!selectedStallId.value) {
    ElMessage.warning('请先选择档口')
    return
  }
  
  dialogType.value = 'add'
  currentDish.value = {
    name: '',
    intro: '',
    price: 0,
    image_url: '',
    stall_id: selectedStallId.value
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogType.value = 'edit'
  // 确保price是数字类型
  const price = typeof row.price === 'string' ? parseFloat(row.price) : row.price
  currentDish.value = { 
    ...row,
    price: price
  }
  dialogVisible.value = true
}

// 表单引用
const formRef = ref(null)

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        let res
        if (dialogType.value === 'add') {
          res = await addDish(currentDish.value)
          if (res.code === 0) {
            ElMessage.success('添加成功')
            fetchDishList()
            dialogVisible.value = false
          } else {
            ElMessage.error(res.msg || '添加失败')
          }
        } else {
          res = await updateDish(currentDish.value)
          if (res.code === 0) {
            ElMessage.success('更新成功')
            fetchDishList()
            dialogVisible.value = false
          } else {
            ElMessage.error(res.msg || '更新失败')
          }
        }
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败，请检查网络连接')
      } finally {
        loading.value = false
      }
    }
  })
}

// 删除菜品
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除菜品 "${row.name}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    loading.value = true
    try {
      const res = await deleteDish(row.dish_id)
      if (res.code === 0) {
        ElMessage.success('删除成功')
        fetchDishList()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除菜品错误:', error)
      ElMessage.error('删除失败，请检查网络连接')
    } finally {
      loading.value = false
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 获取档口名称
const getStallName = (stallId) => {
  const stall = stallOptions.value.find(item => item.value === stallId)
  return stall ? stall.label : '未知档口'
}

// 格式化价格
const formatPrice = (price) => {
  return `¥${parseFloat(price).toFixed(2)}`
}

// 组件挂载时获取初始数据
onMounted(() => {
  fetchCanteenOptions()
})
</script>

<template>
  <div class="dish-management">
    <div class="page-header">
      <h2>菜品管理</h2>
      <div class="header-actions">
        <el-select 
          v-model="selectedCanteenId" 
          placeholder="选择食堂" 
          @change="handleCanteenChange"
          class="select-item"
        >
          <el-option
            v-for="item in canteenOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-select 
          v-model="selectedStallId" 
          placeholder="选择档口" 
          @change="handleStallChange"
          class="select-item"
          :disabled="!selectedCanteenId"
        >
          <el-option
            v-for="item in stallOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-button type="primary" @click="openAddDialog" :disabled="!selectedStallId">添加菜品</el-button>
      </div>
    </div>
    
    <el-table
      v-loading="loading"
      :data="dishList"
      border
      style="width: 100%"
      row-key="dish_id"
    >
      <el-table-column prop="dish_id" label="ID" width="80" />
      <el-table-column prop="name" label="菜品名称" />
      <el-table-column prop="intro" label="简介" />
      <el-table-column label="价格" width="100">
        <template #default="scope">
          {{ formatPrice(scope.row.price) }}
        </template>
      </el-table-column>
      <el-table-column label="所属档口" width="150">
        <template #default="scope">
          {{ getStallName(scope.row.stall_id) }}
        </template>
      </el-table-column>
      <el-table-column label="图片" width="180">
        <template #default="scope">
          <el-image
            v-if="scope.row.image_url"
            :src="scope.row.image_url"
            :preview-src-list="[scope.row.image_url]"
            fit="cover"
            style="width: 100px; height: 60px;"
          />
          <span v-else>无图片</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="openEditDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 添加/编辑菜品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加菜品' : '编辑菜品'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="currentDish"
        :rules="rules"
        label-width="100px"
        class="dish-form"
      >
        <el-form-item label="菜品名称" prop="name">
          <el-input v-model="currentDish.name" />
        </el-form-item>
        <el-form-item label="所属档口" prop="stall_id">
          <el-select v-model="currentDish.stall_id" placeholder="请选择档口">
            <el-option
              v-for="item in stallOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input-number 
            v-model="currentDish.price" 
            :precision="2" 
            :step="0.1" 
            style="width: 200px;"
          />
        </el-form-item>
        <el-form-item label="简介" prop="intro">
          <el-input v-model="currentDish.intro" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="图片URL" prop="image_url">
          <el-input v-model="currentDish.image_url" placeholder="图片链接地址" />
        </el-form-item>
        <el-form-item v-if="currentDish.image_url">
          <el-image
            :src="currentDish.image_url"
            fit="cover"
            style="width: 200px; height: 120px;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.dish-management {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.select-item {
  width: 150px;
}

.dish-form {
  max-width: 400px;
  margin: 0 auto;
}
</style> 