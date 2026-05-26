<template>
  <div class="profile-container">
    <el-card>
      <div slot="header">
        <span>个人信息</span>
      </div>

      <el-form :model="form" :rules="rules" ref="form" label-width="120px" style="max-width: 600px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdate" :loading="loading">保存修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card style="margin-top: 20px;">
      <div slot="header">
        <span>修改密码</span>
      </div>

      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordForm" label-width="120px" style="max-width: 600px;">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdatePassword" :loading="passwordLoading">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getInfo, updateInfo, updatePassword } from '@/api/portal/user'

export default {
  name: 'Profile',
  data() {
    return {
      form: {
        id: null,
        username: '',
        realName: '',
        phone: '',
        email: ''
      },
      rules: {
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }]
      },
      passwordForm: {
        oldPassword: '',
        newPassword: ''
      },
      passwordRules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ]
      },
      loading: false,
      passwordLoading: false
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

      getInfo({ userId }).then(response => {
        this.form = response.data
      })
    },
    handleUpdate() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          updateInfo(this.form).then(() => {
            this.$message.success('修改成功')
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        }
      })
    },
    handleUpdatePassword() {
      this.$refs.passwordForm.validate(valid => {
        if (valid) {
          this.passwordLoading = true
          const userId = this.$store.state.userInfo?.userId
          updatePassword({
            userId,
            oldPassword: this.passwordForm.oldPassword,
            newPassword: this.passwordForm.newPassword
          }).then(() => {
            this.$message.success('密码修改成功')
            this.passwordForm = {
              oldPassword: '',
              newPassword: ''
            }
            this.passwordLoading = false
          }).catch(() => {
            this.passwordLoading = false
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.profile-container {
  max-width: 800px;
  margin: 0 auto;
}
</style>
