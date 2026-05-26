<template>
  <div class="login-container">
    <div class="login-box">
      <div class="header">
        <h2>智慧冷链物流</h2>
        <p>冷链物流服务平台</p>
      </div>

      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="el-icon-user"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="el-icon-lock"
            @keyup.enter.native="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%;" @click="handleLogin" :loading="loading">
            登录
          </el-button>
        </el-form-item>

        <div class="footer">
          <router-link to="/register">还没有账号？立即注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { login } from '@/api/portal/auth'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          login(this.loginForm).then(response => {
            this.$store.dispatch('login', response.data.token)
            this.$store.dispatch('setUserInfo', response.data)
            this.$message.success('登录成功')
            this.$router.push('/home')
          }).catch(() => {
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;

  .login-box {
    width: 400px;
    padding: 40px;
    background: white;
    border-radius: 10px;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);

    .header {
      text-align: center;
      margin-bottom: 30px;

      h2 {
        font-size: 28px;
        color: #333;
        margin: 0 0 10px;
      }

      p {
        color: #666;
        margin: 0;
        font-size: 14px;
      }
    }

    .login-form {
      .footer {
        text-align: center;
        margin-top: 10px;

        a {
          color: #409EFF;
          text-decoration: none;
        }
      }
    }
  }
}
</style>
