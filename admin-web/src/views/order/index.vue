<template>
  <div class="order-container">
    <el-card>
      <div slot="header">
        <span>运单列表</span>
        <el-button type="primary" size="small" style="float: right;" @click="handleCreate">
          新建运单
        </el-button>
      </div>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.orderStatus" placeholder="请选择状态" clearable>
            <el-option label="待取件" :value="0"></el-option>
            <el-option label="运输中" :value="1"></el-option>
            <el-option label="派送中" :value="2"></el-option>
            <el-option label="已签收" :value="3"></el-option>
            <el-option label="已取消" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="orderNo" label="订单号" width="150"></el-table-column>
        <el-table-column label="寄件人" width="150">
          <template slot-scope="{row}">
            {{ row.senderName }}<br/>
            {{ row.senderPhone }}
          </template>
        </el-table-column>
        <el-table-column label="收件人" width="150">
          <template slot-scope="{row}">
            {{ row.receiverName }}<br/>
            {{ row.receiverPhone }}
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="货物名称" width="120"></el-table-column>
        <el-table-column prop="goodsWeight" label="重量(kg)" width="80"></el-table-column>
        <el-table-column prop="temperatureRequirement" label="温度要求" width="100"></el-table-column>
        <el-table-column prop="orderStatus" label="状态" width="80">
          <template slot-scope="{row}">
            <el-tag :type="getStatusType(row.orderStatus)">{{ getStatusText(row.orderStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="{row}">
            <el-button size="mini" @click="handleView(row)">查看</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="60%">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="寄件人姓名" prop="senderName">
              <el-input v-model="form.senderName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="寄件人电话" prop="senderPhone">
              <el-input v-model="form.senderPhone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="寄件人地址" prop="senderAddress">
          <el-input v-model="form.senderAddress"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="收件人姓名" prop="receiverName">
              <el-input v-model="form.receiverName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收件人电话" prop="receiverPhone">
              <el-input v-model="form.receiverPhone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="收件人地址" prop="receiverAddress">
          <el-input v-model="form.receiverAddress"></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="货物名称" prop="goodsName">
              <el-input v-model="form.goodsName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="温度要求" prop="temperatureRequirement">
              <el-input v-model="form.temperatureRequirement"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="重量(kg)" prop="goodsWeight">
              <el-input-number v-model="form.goodsWeight" :min="0" :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="体积(m³)" prop="goodsVolume">
              <el-input-number v-model="form.goodsVolume" :min="0" :precision="2"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getList, create, update, remove } from '@/api/admin/order'

export default {
  name: 'Order',
  data() {
    return {
      searchForm: {
        orderNo: '',
        orderStatus: null
      },
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新建运单',
      form: {
        id: null,
        senderName: '',
        senderPhone: '',
        senderAddress: '',
        receiverName: '',
        receiverPhone: '',
        receiverAddress: '',
        goodsName: '',
        goodsWeight: null,
        goodsVolume: null,
        temperatureRequirement: ''
      },
      rules: {
        senderName: [{ required: true, message: '请输入寄件人姓名', trigger: 'blur' }],
        senderPhone: [{ required: true, message: '请输入寄件人电话', trigger: 'blur' }],
        senderAddress: [{ required: true, message: '请输入寄件人地址', trigger: 'blur' }],
        receiverName: [{ required: true, message: '请输入收件人姓名', trigger: 'blur' }],
        receiverPhone: [{ required: true, message: '请输入收件人电话', trigger: 'blur' }],
        receiverAddress: [{ required: true, message: '请输入收件人地址', trigger: 'blur' }],
        goodsName: [{ required: true, message: '请输入货物名称', trigger: 'blur' }]
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
        orderNo: '',
        orderStatus: null
      }
      this.handleSearch()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.loadData()
    },
    handleCreate() {
      this.dialogTitle = '新建运单'
      this.form = {
        id: null,
        senderName: '',
        senderPhone: '',
        senderAddress: '',
        receiverName: '',
        receiverPhone: '',
        receiverAddress: '',
        goodsName: '',
        goodsWeight: null,
        goodsVolume: null,
        temperatureRequirement: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑运单'
      this.form = { ...row }
      this.dialogVisible = true
    },
    handleView(row) {
      this.$message.info('查看详情: ' + row.orderNo)
    },
    handleDelete(row) {
      this.$confirm('确认删除该运单吗？', '提示', {
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
              this.$message.success('创建成功')
              this.dialogVisible = false
              this.loadData()
            })
          }
        }
      })
    },
    getStatusText(status) {
      const map = {
        0: '待取件',
        1: '运输中',
        2: '派送中',
        3: '已签收',
        4: '已取消'
      }
      return map[status] || '未知'
    },
    getStatusType(status) {
      const map = {
        0: 'warning',
        1: 'primary',
        2: 'info',
        3: 'success',
        4: 'danger'
      }
      return map[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.order-container {
  padding: 20px;

  .search-form {
    margin-bottom: 20px;
  }
}
</style>
