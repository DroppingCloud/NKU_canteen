<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCanteenList, addCanteen, updateCanteen, deleteCanteen } from '../../api/api'

// 食堂列表数据
const canteenList = ref([])
// 加载状态
const loading = ref(false)
// 对话框可见性
const dialogVisible = ref(false)
// 当前编辑的食堂
const currentCanteen = ref({
  name: '',
  location: '',
  open_time: '',
  img: '',
  campus: ''
})
// 对话框类型（添加/编辑）
const dialogType = ref('add')

// 校区选项
const campusOptions = [
  { label: '八里台', value: '八里台' },
  { label: '津南', value: '津南' },
  { label: '泰达', value: '泰达' }
]

// 表单规则
const rules = {
  name: [
    { required: true, message: '请输入食堂名称', trigger: 'blur' },
    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  location: [
    { required: true, message: '请输入食堂位置', trigger: 'blur' }
  ],
  open_time: [
    { required: true, message: '请输入营业时间', trigger: 'blur' }
  ],
  campus: [
    { required: true, message: '请选择所属校区', trigger: 'change' }
  ]
}

// 获取食堂列表
const fetchCanteenList = async () => {
  loading.value = true
  try {
    const res = await getCanteenList()
    if (res.code === 0) {
      canteenList.value = res.data
    } else {
      ElMessage.error(res.msg || '获取食堂列表失败')
    }
  } catch (error) {
    console.error('获取食堂列表错误:', error)
    ElMessage.error('获取食堂列表失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 打开添加对话框
const openAddDialog = () => {
  dialogType.value = 'add'
  currentCanteen.value = {
    name: '',
    location: '',
    open_time: '',
    img: '',
    campus: '八里台'
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogType.value = 'edit'
  currentCanteen.value = { ...row }
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
          res = await addCanteen(currentCanteen.value)
          if (res.code === 0) {
            ElMessage.success('添加成功')
            fetchCanteenList()
            dialogVisible.value = false
          } else {
            ElMessage.error(res.msg || '添加失败')
          }
        } else {
          res = await updateCanteen(currentCanteen.value)
          if (res.code === 0) {
            ElMessage.success('更新成功')
            fetchCanteenList()
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

// 删除食堂
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除食堂 "${row.name}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    loading.value = true
    try {
      const res = await deleteCanteen(row.canteen_id)
      if (res.code === 0) {
        ElMessage.success('删除成功')
        fetchCanteenList()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除食堂错误:', error)
      ElMessage.error('删除失败，请检查网络连接')
    } finally {
      loading.value = false
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 组件挂载时获取食堂列表
onMounted(() => {
  fetchCanteenList()
})
</script>

<template>
  <div class="canteen-management">
    <div class="page-header">
      <h2>食堂管理</h2>
      <el-button type="primary" @click="openAddDialog">添加食堂</el-button>
    </div>
    
    <el-table
      v-loading="loading"
      :data="canteenList"
      border
      style="width: 100%"
      row-key="canteen_id"
    >
      <el-table-column prop="canteen_id" label="ID" width="80" />
      <el-table-column prop="name" label="食堂名称" />
      <el-table-column prop="location" label="位置" />
      <el-table-column prop="campus" label="校区" />
      <el-table-column prop="open_time" label="营业时间" />
      <el-table-column label="图片" width="180">
        <template #default="scope">
          <el-image
            v-if="scope.row.img"
            :src="scope.row.img"
            :preview-src-list="[scope.row.img]"
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
    
    <!-- 添加/编辑食堂对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加食堂' : '编辑食堂'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="currentCanteen"
        :rules="rules"
        label-width="100px"
        class="canteen-form"
      >
        <el-form-item label="食堂名称" prop="name">
          <el-input v-model="currentCanteen.name" />
        </el-form-item>
        <el-form-item label="位置" prop="location">
          <el-input v-model="currentCanteen.location" />
        </el-form-item>
        <el-form-item label="校区" prop="campus">
          <el-select v-model="currentCanteen.campus" placeholder="请选择校区">
            <el-option
              v-for="item in campusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="营业时间" prop="open_time">
          <el-input v-model="currentCanteen.open_time" placeholder="例如: 7:00-21:00" />
        </el-form-item>
        <el-form-item label="图片URL" prop="img">
          <el-input v-model="currentCanteen.img" placeholder="图片链接地址" />
        </el-form-item>
        <el-form-item v-if="currentCanteen.img">
          <el-image
            :src="currentCanteen.img"
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
.canteen-management {
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

.canteen-form {
  max-width: 400px;
  margin: 0 auto;
}
</style> 