<template>
  <div class="user-manage">
    <el-card>
      <div slot="header" class="clearfix">
        <span>用户管理</span>
        <el-button style="float: right;" type="primary" icon="el-icon-plus" size="small" @click="handleAdd">
          新增用户
        </el-button>
      </div>

      <el-table :data="userList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名" width="120"></el-table-column>
        <el-table-column prop="realName" label="真实姓名" width="100"></el-table-column>
        <el-table-column prop="roleNames" label="角色" width="150">
          <template slot-scope="scope">
            <el-tag v-for="role in (scope.row.roleNames || '').split(',')" :key="role" size="small" style="margin-right: 5px;">
              {{ role }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="手机号" width="130"></el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="160"></el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        style="margin-top: 20px; text-align: right;">
      </el-pagination>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="userForm" :rules="userRules" ref="userForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="!!userForm.id" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="userForm.roleIds" multiple placeholder="请选择角色" style="width: 100%;">
            <el-option v-for="role in roleList" :key="role.id" :label="role.roleName" :value="role.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!userForm.id">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码（默认admin123）"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getUserList, addUser, updateUser, deleteUser } from '@/api/admin/user'
import { getRoleList } from '@/api/admin/role'

export default {
  name: 'SystemUser',
  data() {
    return {
      loading: false,
      submitLoading: false,
      userList: [],
      roleList: [],
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增用户',
      userForm: {
        id: null,
        username: '',
        realName: '',
        roleIds: [],
        phone: '',
        email: '',
        password: '',
        status: 1
      },
      userRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
        phone: [{ pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }],
        email: [{ type: 'email', message: '请输入正确的邮箱', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.fetchUserList()
    this.fetchRoleList()
  },
  methods: {
    fetchUserList() {
      this.loading = true
      getUserList({
        page: this.pagination.page,
        size: this.pagination.size
      }).then(res => {
        this.userList = res.data.records || res.data || []
        this.pagination.total = res.data.total || this.userList.length
      }).catch(() => {
        this.userList = []
      }).finally(() => {
        this.loading = false
      })
    },
    fetchRoleList() {
      getRoleList().then(res => {
        this.roleList = res.data || []
      }).catch(() => {
        this.roleList = []
      })
    },
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.userForm = {
        id: null,
        username: '',
        realName: '',
        roleIds: [],
        phone: '',
        email: '',
        password: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.userForm = {
        id: row.id,
        username: row.username,
        realName: row.realName,
        roleIds: row.roleIds ? row.roleIds.split(',').map(Number) : [],
        phone: row.phone,
        email: row.email,
        status: row.status
      }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确定要删除用户 "${row.username}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser(row.id).then(() => {
          this.$message.success('删除成功')
          this.fetchUserList()
        })
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.userForm.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const action = this.userForm.id ? updateUser : addUser
          action(this.userForm).then(() => {
            this.$message.success(this.userForm.id ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.fetchUserList()
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    },
    handleSizeChange(val) {
      this.pagination.size = val
      this.fetchUserList()
    },
    handleCurrentChange(val) {
      this.pagination.page = val
      this.fetchUserList()
    }
  }
}
</script>

<style lang="scss" scoped>
.user-manage {
  padding: 20px;
}
</style>