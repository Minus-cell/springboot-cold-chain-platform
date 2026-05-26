<template>
  <div class="app-wrapper">
    <div class="sidebar-container">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        mode="vertical"
      >
        <div class="logo">
          <h1>冷链物流</h1>
        </div>
        <el-menu-item index="/dashboard" @click="$router.push('/dashboard')">
          <i class="el-icon-data-line"></i>
          <span slot="title">仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/order/index" @click="$router.push('/order/index')">
          <i class="el-icon-document"></i>
          <span slot="title">运单管理</span>
        </el-menu-item>
        <el-menu-item index="/vehicle/index" @click="$router.push('/vehicle/index')">
          <i class="el-icon-truck"></i>
          <span slot="title">车辆管理</span>
        </el-menu-item>
        <el-menu-item index="/temperature/index" @click="$router.push('/temperature/index')">
          <i class="el-icon-data-analysis"></i>
          <span slot="title">温控管理</span>
        </el-menu-item>
        <el-submenu index="/system">
          <template slot="title">
            <i class="el-icon-setting"></i>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user" @click="$router.push('/system/user')">用户管理</el-menu-item>
          <el-menu-item index="/system/role" @click="$router.push('/system/role')">角色管理</el-menu-item>
        </el-submenu>
      </el-menu>
    </div>

    <div class="main-container">
      <div class="navbar">
        <div class="page-title">{{ pageTitle }}</div>
        <div class="right-menu">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              {{ username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <div class="content-container">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import { logout } from '@/api/admin/auth'

export default {
  name: 'Layout',
  computed: {
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    pageTitle() {
      return this.$route.meta && this.$route.meta.title ? this.$route.meta.title : '后台管理'
    },
    username() {
      return this.$store.state.userInfo && this.$store.state.userInfo.username
        ? this.$store.state.userInfo.username
        : '管理员'
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        logout().then(() => {
          this.$store.dispatch('logout')
          this.$router.push('/login')
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-wrapper {
  display: flex;
  height: 100vh;

  .sidebar-container {
    width: 200px;
    background-color: #304156;
    height: 100vh;
    overflow-y: auto;

    .logo {
      height: 60px;
      line-height: 60px;
      text-align: center;
      background: #2b3a4a;

      h1 {
        color: #fff;
        font-size: 20px;
        margin: 0;
      }
    }

    :deep(.el-menu) {
      border: none;
      height: calc(100vh - 60px);
    }
  }

  .main-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .content-container {
      flex: 1;
      overflow: auto;
      background: #f5f7fa;
    }

    .navbar {
      height: 60px;
      display: flex;
      align-items: center;
      background: #fff;
      box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
      padding: 0 20px;

      .page-title {
        flex: 1;
        font-size: 18px;
        font-weight: 600;
        color: #333;
      }

      .right-menu {
        .el-dropdown-link {
          cursor: pointer;
          color: #333;
        }
      }
    }
  }
}
</style>
