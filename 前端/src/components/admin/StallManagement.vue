<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getStallList, addStall, updateStall, deleteStall, getCanteenList } from '../../api/api'

// 档口列表数据
const stallList = ref([])
// 食堂列表（用于选择）
const canteenOptions = ref([])
// 加载状态
const loading = ref(false)
// 对话框可见性
const dialogVisible = ref(false)
// 当前编辑的档口
const currentStall = ref({
  name: '',
  floor: '',
  img: '',
  intro: '',
  canteen_id: null
})
// 对话框类型（添加/编辑）
const dialogType = ref('add')
// 选择的食堂ID（用于筛选）
const selectedCanteenId = ref(null)

// 表单规则
const rules = {
  // 
  // name: [
  //   { required: true, message: '请输入档口名称', trigger: 'blur' },
  //   { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
  // ],
  // floor: [
  //   { required: true, message: '请输入所在楼层', trigger: 'blur' }
  // ],
  // canteen_id: [
  //   { required: true, message: '请选择所属食堂', trigger: 'change' }
  // ]
}

// 获取档口列表
const fetchStallList = async () => {
  loading.value = true
  try {
    let res
    if (selectedCanteenId.value) {
      res = await getStallList(selectedCanteenId.value)
    } else {
      // 如果没有选择食堂，获取所有档口
      // 为了简单处理，如果后端没有该接口，可以只在选择食堂后显示档口
      if (canteenOptions.value.length > 0) {
        selectedCanteenId.value = canteenOptions.value[0].value
        res = await getStallList(selectedCanteenId.value)
      } else {
        stallList.value = []
        loading.value = false
        return
      }
    }
    
    if (res.code === 0) {
      stallList.value = res.data
    } else {
      ElMessage.error(res.msg || '获取档口列表失败')
    }
  } catch (error) {
    console.error('获取档口列表错误:', error)
    ElMessage.error('获取档口列表失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

// 获取食堂列表（用于选择）
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
        fetchStallList()
      }
    }
  } catch (error) {
    console.error('获取食堂选项错误:', error)
  }
}

// 处理食堂选择变化
const handleCanteenChange = () => {
  fetchStallList()
}

// 打开添加对话框
const openAddDialog = () => {
  dialogType.value = 'add'
  currentStall.value = {
    name: '',
    floor: '',
    img: '',
    intro: '',
    canteen_id: selectedCanteenId.value
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogType.value = 'edit'
  currentStall.value = { ...row }
  dialogVisible.value = true
}

// 表单引用
const formRef = ref(null)

// 提交表单
const submitForm = async () => {
  // 直接提交，不进行验证
  loading.value = true
  try {
    let res
    if (dialogType.value === 'add') {
      res = await addStall(currentStall.value)
      if (res.code === 0) {
        ElMessage.success('添加成功')
        fetchStallList()
        dialogVisible.value = false
      } else {
        ElMessage.error(res.msg || '添加失败')
      }
    } else {
      res = await updateStall(currentStall.value)
      if (res.code === 0) {
        ElMessage.success('更新成功')
        fetchStallList()
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
  
  // 原表单验证框架
  // if (!formRef.value) return
  
  // await formRef.value.validate(async (valid) => {
  //   if (valid) {
  //     loading.value = true
  //     try {
  //       let res
  //       if (dialogType.value === 'add') {
  //         res = await addStall(currentStall.value)
  //         if (res.code === 0) {
  //           ElMessage.success('添加成功')
  //           fetchStallList()
  //           dialogVisible.value = false
  //         } else {
  //           ElMessage.error(res.msg || '添加失败')
  //         }
  //       } else {
  //         res = await updateStall(currentStall.value)
  //         if (res.code === 0) {
  //           ElMessage.success('更新成功')
  //           fetchStallList()
  //           dialogVisible.value = false
  //         } else {
  //           ElMessage.error(res.msg || '更新失败')
  //         }
  //       }
  //     } catch (error) {
  //       console.error('操作失败:', error)
  //       ElMessage.error('操作失败，请检查网络连接')
  //     } finally {
  //       loading.value = false
  //     }
  //   }
  // })
}

// 删除档口
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除档口 "${row.name}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    loading.value = true
    try {
      const res = await deleteStall(row.stall_id)
      if (res.code === 0) {
        ElMessage.success('删除成功')
        fetchStallList()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      console.error('删除档口错误:', error)
      ElMessage.error('删除失败，请检查网络连接')
    } finally {
      loading.value = false
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 获取食堂名称
const getCanteenName = (canteenId) => {
  const canteen = canteenOptions.value.find(item => item.value === canteenId)
  return canteen ? canteen.label : '未知食堂'
}

// 组件挂载时获取食堂选项和档口列表
onMounted(() => {
  fetchCanteenOptions()
})
</script>

<template>
  <div class="stall-management">
    <div class="page-header">
      <h2>档口管理</h2>
      <div class="header-actions">
        <el-select 
          v-model="selectedCanteenId" 
          placeholder="选择食堂" 
          @change="handleCanteenChange"
          class="canteen-select"
        >
          <el-option
            v-for="item in canteenOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-button type="primary" @click="openAddDialog">添加档口</el-button>
      </div>
    </div>
    
    <el-table
      v-loading="loading"
      :data="stallList"
      border
      style="width: 100%"
      row-key="stall_id"
    >
      <el-table-column prop="stall_id" label="ID" width="80" />
      <el-table-column prop="name" label="档口名称" />
      <el-table-column prop="floor" label="所在楼层" width="100" />
      <el-table-column prop="intro" label="简介" />
      <el-table-column label="所属食堂" width="150">
        <template #default="scope">
          {{ getCanteenName(scope.row.canteen_id) }}
        </template>
      </el-table-column>
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
    
    <!-- 添加/编辑档口对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加档口' : '编辑档口'"
      width="500px"
    >
      <el-form
        ref="formRef"
        :model="currentStall"
        label-width="100px"
        class="stall-form"
      >
        <el-form-item label="档口名称" prop="name">
          <el-input v-model="currentStall.name" />
        </el-form-item>
        <el-form-item label="所属食堂" prop="canteen_id">
          <el-select v-model="currentStall.canteen_id" placeholder="请选择食堂">
            <el-option
              v-for="item in canteenOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所在楼层" prop="floor">
          <el-input v-model="currentStall.floor" placeholder="例如: 1F、2F" />
        </el-form-item>
        <el-form-item label="简介" prop="intro">
          <el-input v-model="currentStall.intro" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="图片URL" prop="img">
          <el-input v-model="currentStall.img" placeholder="图片链接地址" />
        </el-form-item>
        <el-form-item v-if="currentStall.img">
          <el-image
            :src="currentStall.img"
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
.stall-management {
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

.canteen-select {
  width: 200px;
}

.stall-form {
  max-width: 400px;
  margin: 0 auto;
}
</style> 