<template>
  <div class="app-wrapper">
    <div class="navbar">
      <div class="navbar-content">
        <div class="logo">
          <router-link to="/home">智慧冷链物流</router-link>
        </div>
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          router
          class="navbar-menu"
        >
          <el-menu-item index="/home">首页</el-menu-item>
          <el-menu-item index="/order/create">下单寄件</el-menu-item>
          <el-menu-item index="/order/list">我的订单</el-menu-item>
          <el-menu-item index="/track/index">运单追踪</el-menu-item>
        </el-menu>
        <div class="user-info">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              {{ username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人信息</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <div class="main-container">
      <router-view />
    </div>
  </div>
</template>

<script>
import { logout } from '@/api/portal/auth'

export default {
  name: 'Layout',
  computed: {
    username() {
      return this.$store.state.userInfo?.username || '用户'
    },
    activeMenu() {
      const route = this.$route
      return route.path
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        logout().then(() => {
          this.$store.dispatch('logout')
          this.$router.push('/login')
        })
      } else if (command === 'profile') {
        this.$router.push('/user/profile')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  min-height: 100vh;

  .navbar {
    background: white;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    position: sticky;
    top: 0;
    z-index: 1000;

    .navbar-content {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      align-items: center;
      padding: 0 20px;
      height: 60px;

      .logo {
        font-size: 20px;
        font-weight: bold;
        margin-right: 50px;

        a {
          color: #333;
          text-decoration: none;
        }
      }

      .navbar-menu {
        flex: 1;
        border-bottom: none;
      }

      .user-info {
        margin-left: auto;

        .el-dropdown-link {
          cursor: pointer;
          color: #333;
        }
      }
    }
  }

  .main-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }
}
</style>
