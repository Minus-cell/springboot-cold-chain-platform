<template>
  <div class="order-list-container">
    <el-card>
      <div slot="header">
        <span>我的订单</span>
      </div>

      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="全部" name=""></el-tab-pane>
        <el-tab-pane label="待取件" name="0"></el-tab-pane>
        <el-tab-pane label="运输中" name="1"></el-tab-pane>
        <el-tab-pane label="派送中" name="2"></el-tab-pane>
        <el-tab-pane label="已完成" name="3"></el-tab-pane>
        <el-tab-pane label="已取消" name="4"></el-tab-pane>
      </el-tabs>

      <div v-if="tableData.length === 0" class="empty-state">
        <p>暂无订单</p>
        <el-button type="primary" @click="$router.push('/order/create')">立即下单</el-button>
      </div>

      <div v-else class="order-list">
        <el-card v-for="order in tableData" :key="order.id" class="order-card">
          <div class="order-header">
            <span class="order-no">订单号：{{ order.orderNo }}</span>
            <el-tag :type="getStatusType(order.orderStatus)">{{ getStatusText(order.orderStatus) }}</el-tag>
          </div>
          <div class="order-body">
            <div class="order-info">
              <div class="info-item">
                <label>寄件人：</label>
                <span>{{ order.senderName }} {{ order.senderPhone }}</span>
              </div>
              <div class="info-item">
                <label>收件人：</label>
                <span>{{ order.receiverName }} {{ order.receiverPhone }}</span>
              </div>
              <div class="info-item">
                <label>货物：</label>
                <span>{{ order.goodsName }} ({{ order.goodsWeight }}kg)</span>
              </div>
              <div class="info-item">
                <label>温度要求：</label>
                <span>{{ order.temperatureRequirement }}</span>
              </div>
            </div>
            <div class="order-actions">
              <el-button size="mini" type="primary" @click="handleTrack(order)">追踪</el-button>
              <el-button v-if="order.orderStatus === 0" size="mini" type="danger" @click="handleCancel(order)">
                取消订单
              </el-button>
            </div>
          </div>
          <div class="order-footer">
            <span>下单时间：{{ order.createTime }}</span>
          </div>
        </el-card>
      </div>

      <el-pagination
        v-if="pagination.total > 0"
        @current-change="handlePageChange"
        :current-page="pagination.current"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, prev, pager, next"
        style="margin-top: 20px; text-align: right;">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import { getList, cancel } from '@/api/portal/order'

export default {
  name: 'OrderList',
  data() {
    return {
      activeTab: '',
      tableData: [],
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      const userId = this.$store.state.userInfo?.userId
      if (!userId) {
        this.$message.error('请先登录')
        this.$router.push('/login')
        return
      }

      const params = {
        userId,
        current: this.pagination.current,
        size: this.pagination.size,
        orderStatus: this.activeTab !== '' ? parseInt(this.activeTab) : null
      }

      getList(params).then(response => {
        this.tableData = response.data.records
        this.pagination.total = response.data.total
      })
    },
    handleTabClick() {
      this.pagination.current = 1
      this.loadData()
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.loadData()
    },
    handleTrack(order) {
      this.$router.push({ path: '/track/index', query: { orderNo: order.orderNo } })
    },
    handleCancel(order) {
      this.$confirm('确认取消该订单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancel(order.orderNo).then(() => {
          this.$message.success('订单已取消')
          this.loadData()
        })
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
.order-list-container {
  .empty-state {
    text-align: center;
    padding: 60px 0;

    p {
      color: #999;
      margin-bottom: 20px;
    }
  }

  .order-list {
    .order-card {
      margin-bottom: 15px;

      .order-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;

        .order-no {
          font-weight: bold;
          color: #333;
        }
      }

      .order-body {
        display: flex;
        justify-content: space-between;

        .order-info {
          flex: 1;

          .info-item {
            margin-bottom: 8px;

            label {
              color: #999;
              margin-right: 10px;
            }
          }
        }

        .order-actions {
          display: flex;
          flex-direction: column;
          gap: 10px;
        }
      }

      .order-footer {
        margin-top: 15px;
        padding-top: 15px;
        border-top: 1px solid #eee;
        font-size: 12px;
        color: #999;
      }
    }
  }
}
</style>
