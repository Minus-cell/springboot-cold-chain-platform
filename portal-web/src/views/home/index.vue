<template>
  <div class="home-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="welcome-card">
          <div class="welcome">
            <h2>欢迎回来，{{ username }}！</h2>
            <p>智慧冷链物流，为您的冷链物流保驾护航</p>
          </div>
        </el-card>

        <el-row :gutter="20" class="stats-row">
          <el-col :span="8">
            <div class="stat-card blue">
              <div class="stat-value">{{ stats.totalCount }}</div>
              <div class="stat-label">总订单数</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card green">
              <div class="stat-value">{{ stats.inTransitCount }}</div>
              <div class="stat-label">运输中</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-card orange">
              <div class="stat-value">{{ stats.completedCount }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </el-col>
        </el-row>

        <el-card class="quick-actions">
          <div slot="header">
            <span>快捷服务</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <router-link to="/order/create">
                <div class="action-item">
                  <i class="el-icon-plus"></i>
                  <span>快速下单</span>
                </div>
              </router-link>
            </el-col>
            <el-col :span="6">
              <router-link to="/track/index">
                <div class="action-item">
                  <i class="el-icon-location"></i>
                  <span>运单追踪</span>
                </div>
              </router-link>
            </el-col>
            <el-col :span="6">
              <router-link to="/order/list">
                <div class="action-item">
                  <i class="el-icon-document"></i>
                  <span>我的订单</span>
                </div>
              </router-link>
            </el-col>
            <el-col :span="6">
              <router-link to="/user/profile">
                <div class="action-item">
                  <i class="el-icon-user"></i>
                  <span>个人中心</span>
                </div>
              </router-link>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card>
          <div slot="header">
            <span>最新公告</span>
          </div>
          <div class="news-list">
            <div class="news-item">
              <h4>冷链物流系统上线通知</h4>
              <p>智慧冷链物流系统正式上线，为您提供全方位的冷链物流服务。</p>
              <span class="news-date">2024-01-01</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStats } from '@/api/portal/index'

export default {
  name: 'Home',
  data() {
    return {
      username: '',
      stats: {
        totalCount: 0,
        inTransitCount: 0,
        completedCount: 0
      }
    }
  },
  mounted() {
    this.username = this.$store.state.userInfo?.username || '用户'
    this.loadStats()
  },
  methods: {
    loadStats() {
      const userId = this.$store.state.userInfo?.userId
      if (userId) {
        getStats({ userId }).then(response => {
          this.stats = response.data
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.home-container {
  .welcome-card {
    margin-bottom: 20px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;

    .welcome {
      h2 {
        margin: 0 0 10px;
        font-size: 24px;
      }

      p {
        margin: 0;
        opacity: 0.9;
      }
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      padding: 20px;
      border-radius: 8px;
      text-align: center;
      color: white;

      &.blue { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
      &.green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
      &.orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }

      .stat-value {
        font-size: 36px;
        font-weight: bold;
      }

      .stat-label {
        margin-top: 10px;
        font-size: 14px;
      }
    }
  }

  .quick-actions {
    .action-item {
      text-align: center;
      padding: 30px 0;
      border: 1px solid #eee;
      border-radius: 8px;
      transition: all 0.3s;
      cursor: pointer;

      i {
        font-size: 36px;
        color: #409EFF;
        display: block;
        margin-bottom: 10px;
      }

      span {
        color: #333;
        font-size: 14px;
      }

      &:hover {
        border-color: #409EFF;
        box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
      }
    }
  }

  .news-list {
    .news-item {
      padding: 15px 0;
      border-bottom: 1px solid #eee;

      &:last-child {
        border-bottom: none;
      }

      h4 {
        margin: 0 0 10px;
        font-size: 16px;
        color: #333;
      }

      p {
        margin: 0 0 10px;
        font-size: 14px;
        color: #666;
        line-height: 1.6;
      }

      .news-date {
        font-size: 12px;
        color: #999;
      }
    }
  }
}
</style>
