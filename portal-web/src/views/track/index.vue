<template>
  <div class="track-container">
    <el-card>
      <div slot="header">
        <span>运单追踪</span>
      </div>

      <el-form :inline="true">
        <el-form-item label="订单号">
          <el-input v-model="orderNo" placeholder="请输入订单号" style="width: 300px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleTrack">追踪</el-button>
        </el-form-item>
      </el-form>

      <div v-if="orderData" class="track-result">
        <el-card class="order-info-card">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="订单号">{{ orderData.order.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(orderData.order.orderStatus)">
                {{ getStatusText(orderData.order.orderStatus) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="寄件人">{{ orderData.order.senderName }}</el-descriptions-item>
            <el-descriptions-item label="寄件人电话">{{ orderData.order.senderPhone }}</el-descriptions-item>
            <el-descriptions-item label="寄件人地址" :span="2">{{ orderData.order.senderAddress }}</el-descriptions-item>
            <el-descriptions-item label="收件人">{{ orderData.order.receiverName }}</el-descriptions-item>
            <el-descriptions-item label="收件人电话">{{ orderData.order.receiverPhone }}</el-descriptions-item>
            <el-descriptions-item label="收件人地址" :span="2">{{ orderData.order.receiverAddress }}</el-descriptions-item>
            <el-descriptions-item label="货物名称">{{ orderData.order.goodsName }}</el-descriptions-item>
            <el-descriptions-item label="温度要求">{{ orderData.order.temperatureRequirement }}</el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card class="timeline-card" style="margin-top: 20px;">
          <div slot="header">
            <span>物流轨迹</span>
          </div>
          <el-timeline>
            <el-timeline-item
              v-for="(item, index) in orderData.timeline"
              :key="index"
              :timestamp="item.time"
              placement="top"
              :type="index === 0 ? 'primary' : 'success'"
            >
              <el-card>
                <h4>{{ item.status }}</h4>
                <p>{{ item.description }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </el-card>

        <el-card v-if="temperatureData.length > 0" class="temperature-card" style="margin-top: 20px;">
          <div slot="header">
            <span>温度监控</span>
          </div>
          <div ref="tempChart" style="height: 300px;"></div>
        </el-card>
      </div>

      <div v-else-if="hasSearched" class="empty-result">
        <p>未找到相关订单</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { track } from '@/api/portal/track'
import { getTemperature } from '@/api/portal/track'

export default {
  name: 'Track',
  data() {
    return {
      orderNo: '',
      hasSearched: false,
      orderData: null,
      temperatureData: []
    }
  },
  mounted() {
    const queryOrderNo = this.$route.query.orderNo
    if (queryOrderNo) {
      this.orderNo = queryOrderNo
      this.handleTrack()
    }
  },
  beforeDestroy() {
    if (this.tempChart) {
      this.tempChart.dispose()
    }
  },
  methods: {
    handleTrack() {
      if (!this.orderNo) {
        this.$message.warning('请输入订单号')
        return
      }

      this.hasSearched = true
      track(this.orderNo).then(response => {
        this.orderData = response.data
        this.loadTemperature()
      }).catch(() => {
        this.orderData = null
      })
    },
    loadTemperature() {
      getTemperature(this.orderNo).then(response => {
        this.temperatureData = response.data
        this.$nextTick(() => {
          this.initTempChart()
        })
      })
    },
    initTempChart() {
      if (!this.$refs.tempChart || this.temperatureData.length === 0) return

      this.tempChart = echarts.init(this.$refs.tempChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.temperatureData.map(item => item.recordTime)
        },
        yAxis: {
          type: 'value',
          name: '温度(℃)'
        },
        series: [{
          data: this.temperatureData.map(item => item.temperature),
          type: 'line',
          smooth: true,
          areaStyle: {
            color: 'rgba(64, 158, 255, 0.2)'
          }
        }]
      }
      this.tempChart.setOption(option)
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
.track-container {
  .empty-result {
    text-align: center;
    padding: 60px 0;
    color: #999;
  }

  .track-result {
    margin-top: 20px;
  }
}
</style>
