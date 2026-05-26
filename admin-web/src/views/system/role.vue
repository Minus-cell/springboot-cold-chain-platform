<template>
  <div class="role-manage">
    <el-card>
      <div slot="header" class="clearfix">
        <span>角色管理</span>
        <el-button style="float: right;" type="primary" icon="el-icon-plus" size="small" @click="handleAdd">
          新增角色
        </el-button>
      </div>

      <el-table :data="roleList" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="roleName" label="角色名称" width="150"></el-table-column>
        <el-table-column prop="roleCode" label="角色编码" width="150"></el-table-column>
        <el-table-column prop="description" label="描述" min-width="200"></el-table-column>
        <el-table-column label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" size="small">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C;">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="roleForm" :rules="roleRules" ref="roleForm" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码（如：ROLE_ADMIN）"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" :rows="3" placeholder="请输入描述"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
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
import { getRoleList, addRole, updateRole, deleteRole } from '@/api/admin/role'

export default {
  name: 'SystemRole',
  data() {
    return {
      loading: false,
      submitLoading: false,
      roleList: [],
      dialogVisible: false,
      dialogTitle: '新增角色',
      roleForm: {
        id: null,
        roleName: '',
        roleCode: '',
        description: '',
        status: 1
      },
      roleRules: {
        roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
          { pattern: /^ROLE_\w+$/, message: '角色编码必须以ROLE_开头', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.fetchRoleList()
  },
  methods: {
    fetchRoleList() {
      this.loading = true
      getRoleList().then(res => {
        this.roleList = res.data || []
      }).catch(() => {
        this.roleList = []
      }).finally(() => {
        this.loading = false
      })
    },
    handleAdd() {
      this.dialogTitle = '新增角色'
      this.roleForm = {
        id: null,
        roleName: '',
        roleCode: '',
        description: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑角色'
      this.roleForm = {
        id: row.id,
        roleName: row.roleName,
        roleCode: row.roleCode,
        description: row.description,
        status: row.status
      }
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm(`确定要删除角色 "${row.roleName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRole(row.id).then(() => {
          this.$message.success('删除成功')
          this.fetchRoleList()
        })
      }).catch(() => {})
    },
    handleSubmit() {
      this.$refs.roleForm.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const action = this.roleForm.id ? updateRole : addRole
          action(this.roleForm).then(() => {
            this.$message.success(this.roleForm.id ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.fetchRoleList()
          }).finally(() => {
            this.submitLoading = false
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.role-manage {
  padding: 20px;
}
</style>