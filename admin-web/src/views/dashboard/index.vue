<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="stat-card blue">
          <div class="stat-icon">
            <i class="el-icon-document"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.todayOrderCount }}</div>
            <div class="stat-label">今日订单</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="stat-icon">
            <i class="el-icon-truck"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.inTransitCount }}</div>
            <div class="stat-label">运输中</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card red">
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.alarmCount }}</div>
            <div class="stat-label">告警数量</div>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-icon">
            <i class="el-icon-location"></i>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.onlineVehicleCount }}</div>
            <div class="stat-label">在线车辆</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="16">
        <el-card class="chart-card">
          <div slot="header">
            <span>订单趋势</span>
          </div>
          <div ref="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <div slot="header">
            <span>实时温控</span>
          </div>
          <el-table :data="realtimeData" max-height="300">
            <el-table-column prop="location" label="位置" width="100"></el-table-column>
            <el-table-column prop="temperature" label="温度(℃)" width="80"></el-table-column>
            <el-table-column prop="recordTime" label="时间" :formatter="formatTime"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getStats, getTrend, getRealtime } from '@/api/admin/dashboard'

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        todayOrderCount: 0,
        inTransitCount: 0,
        alarmCount: 0,
        onlineVehicleCount: 0
      },
      trendData: [],
      realtimeData: [],
      trendChart: null
    }
  },
  mounted() {
    this.loadData()
  },
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    loadData() {
      getStats().then(response => {
        this.stats = response.data
      })

      getTrend().then(response => {
        this.trendData = response.data
        this.initTrendChart()
      })

      getRealtime().then(response => {
        this.realtimeData = response.data
      })
    },
    initTrendChart() {
      if (!this.$refs.trendChart) return

      this.trendChart = echarts.init(this.$refs.trendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.trendData.map(item => item.date)
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: this.trendData.map(item => item.count),
          type: 'line',
          smooth: true,
          areaStyle: {
            color: 'rgba(64, 158, 255, 0.2)'
          }
        }]
      }
      this.trendChart.setOption(option)
    },
    formatTime(row) {
      return row.recordTime ? row.recordTime.slice(11, 16) : ''
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  .stat-card {
    display: flex;
    align-items: center;
    padding: 20px;
    border-radius: 8px;
    color: white;

    .stat-icon {
      font-size: 48px;
      margin-right: 20px;
    }

    .stat-content {
      .stat-value {
        font-size: 32px;
        font-weight: bold;
      }
      .stat-label {
        font-size: 14px;
        margin-top: 5px;
      }
    }

    &.blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
    &.green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
    &.red { background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%); }
    &.orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
  }

  .chart-card {
    :deep(.el-card__header) {
      font-weight: bold;
    }
  }
}
</style>
