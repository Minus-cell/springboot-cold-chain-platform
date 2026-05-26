<template>
  <div class="vehicle-container">
    <el-card>
      <div slot="header">
        <span>车辆列表</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleCreate">
          添加车辆
        </el-button>
      </div>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="车牌号">
          <el-input v-model="searchForm.vehicleNo" placeholder="请输入车牌号" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="空闲" :value="0"></el-option>
            <el-option label="运输中" :value="1"></el-option>
            <el-option label="维护中" :value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="vehicleNo" label="车牌号" width="120"></el-table-column>
        <el-table-column prop="vehicleType" label="车辆类型" width="100"></el-table-column>
        <el-table-column label="司机信息" width="150">
          <template slot-scope="{row}">
            {{ row.driverName }}<br/>
            {{ row.driverPhone }}
          </template>
        </el-table-column>
        <el-table-column prop="temperatureRange" label="温度范围" width="120"></el-table-column>
        <el-table-column prop="maxLoad" label="最大载重(kg)" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="{row}">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="当前位置" width="150">
          <template slot-scope="{row}">
            <span v-if="row.currentLat && row.currentLng">
              {{ row.currentLat }}, {{ row.currentLng }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template slot-scope="{row}">
            <el-button size="mini" type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @current-change="handlePageChange"
        :current-page="pagination.current"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, prev, pager, next"
        style="margin-top: 20px; text-align: right;">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="车牌号" prop="vehicleNo">
          <el-input v-model="form.vehicleNo"></el-input>
        </el-form-item>
        <el-form-item label="车辆类型" prop="vehicleType">
          <el-input v-model="form.vehicleType"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="司机姓名" prop="driverName">
              <el-input v-model="form.driverName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="司机电话" prop="driverPhone">
              <el-input v-model="form.driverPhone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="温度范围" prop="temperatureRange">
              <el-input v-model="form.temperatureRange"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大载重" prop="maxLoad">
              <el-input-number v-model="form.maxLoad" :min="0" :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">空闲</el-radio>
            <el-radio :label="1">运输中</el-radio>
            <el-radio :label="2">维护中</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getList, create, update, remove } from '@/api/admin/vehicle'

export default {
  name: 'Vehicle',
  data() {
    return {
      searchForm: {
        vehicleNo: '',
        status: null
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '添加车辆',
      form: {
        id: null,
        vehicleNo: '',
        vehicleType: '',
        driverName: '',
        driverPhone: '',
        temperatureRange: '',
        maxLoad: null,
        status: 0
      },
      rules: {
        vehicleNo: [{ required: true, message: '请输入车牌号', trigger: 'blur' }],
        driverName: [{ required: true, message: '请输入司机姓名', trigger: 'blur' }],
        driverPhone: [{ required: true, message: '请输入司机电话', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      const params = {
        current: this.pagination.current,
        size: this.pagination.size,
        ...this.searchForm
      }
      getList(params).then(response => {
        this.tableData = response.data.records
        this.pagination.total = response.data.total
      })
    },
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    handleReset() {
      this.searchForm = {
        vehicleNo: '',
        status: null
      }
      this.handleSearch()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.loadData()
    },
    handleCreate() {
      this.dialogTitle = '添加车辆'
      this.form = {
        id: null,
        vehicleNo: '',
        vehicleType: '',
        driverName: '',
        driverPhone: '',
        temperatureRange: '',
        maxLoad: null,
        status: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑车辆'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('确认删除该车辆吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        remove(row.id).then(() => {
          this.$message.success('删除成功')
          this.loadData()
        })
      })
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id) {
            update(this.form).then(() => {
              this.$message.success('更新成功')
              this.dialogVisible = false
              this.loadData()
            })
          } else {
            create(this.form).then(() => {
              this.$message.success('添加成功')
              this.dialogVisible = false
              this.loadData()
            })
          }
        }
      })
    },
    getStatusText(status) {
      const map = {
        0: '空闲',
        1: '运输中',
        2: '维护中'
      }
      return map[status] || '未知'
    },
    getStatusType(status) {
      const map = {
        0: 'success',
        1: 'warning',
        2: 'info'
      }
      return map[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.vehicle-container {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }
}
</style>
