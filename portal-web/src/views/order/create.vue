<template>
  <div class="create-order-container">
    <el-card>
      <div slot="header">
        <span>下单寄件</span>
      </div>

      <el-steps :active="currentStep" finish-status="success" style="margin-bottom: 30px;">
        <el-step title="填写寄件信息"></el-step>
        <el-step title="填写收件信息"></el-step>
        <el-step title="填写货物信息"></el-step>
        <el-step title="确认提交"></el-step>
      </el-steps>

      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <div v-show="currentStep === 0">
          <el-form-item label="寄件人姓名" prop="senderName">
            <el-input v-model="form.senderName" placeholder="请输入寄件人姓名"></el-input>
          </el-form-item>
          <el-form-item label="寄件人电话" prop="senderPhone">
            <el-input v-model="form.senderPhone" placeholder="请输入寄件人电话"></el-input>
          </el-form-item>
          <el-form-item label="寄件人地址" prop="senderAddress">
            <el-input v-model="form.senderAddress" placeholder="请输入寄件人详细地址"></el-input>
          </el-form-item>
        </div>

        <div v-show="currentStep === 1">
          <el-form-item label="收件人姓名" prop="receiverName">
            <el-input v-model="form.receiverName" placeholder="请输入收件人姓名"></el-input>
          </el-form-item>
          <el-form-item label="收件人电话" prop="receiverPhone">
            <el-input v-model="form.receiverPhone" placeholder="请输入收件人电话"></el-input>
          </el-form-item>
          <el-form-item label="收件人地址" prop="receiverAddress">
            <el-input v-model="form.receiverAddress" placeholder="请输入收件人详细地址"></el-input>
          </el-form-item>
        </div>

        <div v-show="currentStep === 2">
          <el-form-item label="货物名称" prop="goodsName">
            <el-input v-model="form.goodsName" placeholder="请输入货物名称"></el-input>
          </el-form-item>
          <el-form-item label="温度要求" prop="temperatureRequirement">
            <el-select v-model="form.temperatureRequirement" placeholder="请选择温度要求" style="width: 100%;">
              <el-option label="-18℃以下（冷冻）" value="-18℃以下"></el-option>
              <el-option label="0~5℃（冷藏）" value="0~5℃"></el-option>
              <el-option label="2~8℃（冷藏）" value="2~8℃"></el-option>
              <el-option label="常温" value="常温"></el-option>
            </el-select>
          </el-form-item>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="重量(kg)" prop="goodsWeight">
                <el-input-number v-model="form.goodsWeight" :min="0" :precision="2" style="width: 100%;"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="体积(m³)" prop="goodsVolume">
                <el-input-number v-model="form.goodsVolume" :min="0" :precision="2" style="width: 100%;"></el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <div v-show="currentStep === 3">
          <el-card class="summary-card">
            <div slot="header">
              <span>订单信息确认</span>
            </div>
            <el-descriptions :column="2" border>
              <el-descriptions-item label="寄件人">{{ form.senderName }}</el-descriptions-item>
              <el-descriptions-item label="寄件人电话">{{ form.senderPhone }}</el-descriptions-item>
              <el-descriptions-item label="寄件人地址" :span="2">{{ form.senderAddress }}</el-descriptions-item>
              <el-descriptions-item label="收件人">{{ form.receiverName }}</el-descriptions-item>
              <el-descriptions-item label="收件人电话">{{ form.receiverPhone }}</el-descriptions-item>
              <el-descriptions-item label="收件人地址" :span="2">{{ form.receiverAddress }}</el-descriptions-item>
              <el-descriptions-item label="货物名称">{{ form.goodsName }}</el-descriptions-item>
              <el-descriptions-item label="温度要求">{{ form.temperatureRequirement }}</el-descriptions-item>
              <el-descriptions-item label="重量">{{ form.goodsWeight }} kg</el-descriptions-item>
              <el-descriptions-item label="体积">{{ form.goodsVolume }} m³</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>

        <el-form-item style="margin-top: 30px;">
          <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
          <el-button v-if="currentStep < 3" type="primary" @click="nextStep">下一步</el-button>
          <el-button v-if="currentStep === 3" type="primary" @click="handleSubmit" :loading="loading">
            提交订单
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { create } from '@/api/portal/order'

export default {
  name: 'CreateOrder',
  data() {
    return {
      currentStep: 0,
      loading: false,
      form: {
        senderName: '',
        senderPhone: '',
        senderAddress: '',
        receiverName: '',
        receiverPhone: '',
        receiverAddress: '',
        goodsName: '',
        goodsWeight: null,
        goodsVolume: null,
        temperatureRequirement: ''
      },
      rules: {
        senderName: [{ required: true, message: '请输入寄件人姓名', trigger: 'blur' }],
        senderPhone: [{ required: true, message: '请输入寄件人电话', trigger: 'blur' }],
        senderAddress: [{ required: true, message: '请输入寄件人地址', trigger: 'blur' }],
        receiverName: [{ required: true, message: '请输入收件人姓名', trigger: 'blur' }],
        receiverPhone: [{ required: true, message: '请输入收件人电话', trigger: 'blur' }],
        receiverAddress: [{ required: true, message: '请输入收件人地址', trigger: 'blur' }],
        goodsName: [{ required: true, message: '请输入货物名称', trigger: 'blur' }],
        temperatureRequirement: [{ required: true, message: '请选择温度要求', trigger: 'change' }],
        goodsWeight: [{ required: true, message: '请输入货物重量', trigger: 'blur' }],
        goodsVolume: [{ required: true, message: '请输入货物体积', trigger: 'blur' }]
      }
    }
  },
  methods: {
    nextStep() {
      let validateFields = []
      if (this.currentStep === 0) {
        validateFields = ['senderName', 'senderPhone', 'senderAddress']
      } else if (this.currentStep === 1) {
        validateFields = ['receiverName', 'receiverPhone', 'receiverAddress']
      } else if (this.currentStep === 2) {
        validateFields = ['goodsName', 'temperatureRequirement']
      }

      this.$refs.form.validateField(validateFields)
      setTimeout(() => {
        let hasError = false
        for (let field of validateFields) {
          const item = this.$refs.form.fields.find(f => f.prop === field)
          if (item && item.validateMessage) {
            hasError = true
            break
          }
        }
        if (!hasError) {
          this.currentStep++
        }
      }, 100)
    },
    prevStep() {
      this.currentStep--
    },
    handleSubmit() {
      this.loading = true
      this.form.userId = this.$store.state.userInfo?.userId

      create(this.form).then(() => {
        this.$message.success('订单提交成功！')
        this.$router.push('/order/list')
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.create-order-container {
  max-width: 800px;
  margin: 0 auto;

  .summary-card {
    margin-bottom: 20px;
  }
}
</style>
