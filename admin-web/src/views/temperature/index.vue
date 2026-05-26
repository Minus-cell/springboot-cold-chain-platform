<template>
  <div class="temperature-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card>
          <div slot="header">
            <span>温度曲线</span>
            <el-select v-model="selectedOrder" placeholder="请选择运单" style="float: right; width: 200px;" @change="loadChart">
              <el-option v-for="order in orders" :key="order.id" :label="order.orderNo" :value="order.id"></el-option>
            </el-select>
          </div>
          <div ref="chart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>最新告警</span>
          </div>
          <el-table :data="alarms" max-height="400">
            <el-table-column prop="temperature" label="温度(℃)" width="80"></el-table-column>
            <el-table-column prop="location" label="位置" width="100"></el-table-column>
            <el-table-column prop="recordTime" label="时间"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px;">
      <div slot="header">
        <span>温度记录列表</span>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="orderId" label="运单ID" width="100"></el-table-column>
        <el-table-column prop="vehicleId" label="车辆ID" width="100"></el-table-column>
        <el-table-column prop="temperature" label="温度(℃)" width="100"></el-table-column>
        <el-table-column prop="humidity" label="湿度(%)" width="100"></el-table-column>
        <el-table-column prop="location" label="位置"></el-table-column>
        <el-table-column prop="recordTime" label="记录时间" width="180"></el-table-column>
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
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getList, getChart, getAlarms, getLatest } from '@/api/admin/temperature'
import { getList as getOrderList } from '@/api/admin/order'

export default {
  name: 'Temperature',
  data() {
    return {
      tableData: [],
      alarms: [],
      orders: [],
      selectedOrder: null,
      chart: null,
      pagination: {
        current: 1,
        size: 10,
        total: 0
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadOrders()
    this.loadAlarms()
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    loadData() {
      const params = {
        current: this.pagination.current,
        size: this.pagination.size
      }
      getList(params).then(response => {
        this.tableData = response.data.records
        this.pagination.total = response.data.total
      })
    },
    loadOrders() {
      getOrderList({ current: 1, size: 100 }).then(response => {
        this.orders = response.data.records
      })
    },
    loadAlarms() {
      getAlarms().then(response => {
        this.alarms = response.data
      })
    },
    loadChart(orderId) {
      if (!orderId) return

      getChart(orderId).then(response => {
        const data = response.data
        this.initChart(data.dates, data.temperatures, data.humidities)
      })
    },
    initChart(dates, temperatures, humidities) {
      if (!this.$refs.chart) return

      this.chart = echarts.init(this.$refs.chart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['温度', '湿度']
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: [
          {
            type: 'value',
            name: '温度(℃)',
            axisLabel: {
              formatter: '{value} °C'
            }
          },
          {
            type: 'value',
            name: '湿度(%)',
            axisLabel: {
              formatter: '{value} %'
            }
          }
        ],
        series: [
          {
            name: '温度',
            type: 'line',
            data: temperatures,
            smooth: true,
            markLine: {
              data: [
                { yAxis: -18, name: '下限' },
                { yAxis: -25, name: '告警' }
              ]
            }
          },
          {
            name: '湿度',
            type: 'line',
            yAxisIndex: 1,
            data: humidities,
            smooth: true
          }
        ]
      }
      this.chart.setOption(option)
    },
    handlePageChange(page) {
      this.pagination.current = page
      this.loadData()
    }
  }
}
</script>

<style lang="scss" scoped>
.temperature-container {
  padding: 20px;
}
</style>
