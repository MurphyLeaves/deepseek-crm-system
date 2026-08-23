<template>
  <div
    style="
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      width: 100vw;
      background: #7f96c8;
      margin: 0;
      padding: 20px;
      box-sizing: border-box;
      position: fixed;
      top: 0;
      left: 0;
    "
  >
    <a-card
      style="
        width: 100%;
        max-width: 1400px;
        border-radius: 12px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
      "
    >
      <div
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 16px;
        "
      >
        <h2 style="margin: 0">📋 客户管理</h2>
        <a-button type="warning" size="middle" @click="openPromptDialog"> ⚙️ 提示词配置 </a-button>
      </div>

      <!-- 搜索栏 -->
      <a-space style="margin-bottom: 16px; flex-wrap: wrap">
        <a-input
          v-model:value="query.keyword"
          placeholder="输入企业名称或联系人"
          allow-clear
          style="width: 260px"
        />
        <a-button type="primary" @click="handleSearch">搜索</a-button>
        <a-button type="primary" @click="openAddDialog">+ 新增客户</a-button>
        <!-- 在已有按钮后面追加 -->
        <a-button type="primary" @click="handleDownloadTemplate">📥 下载模板</a-button>
        <a-button type="primary" @click="handleImport">📤 导入Excel</a-button>
        <a-button type="primary" @click="handleExport">📤 导出数据</a-button>
      </a-space>

      <!-- 表格 -->
      <a-table
        :columns="columns"
        :data-source="tableData"
        :row-key="(record) => record.id"
        :scroll="{ y: 'calc(100vh - 380px)' }"
        :pagination="false"
        bordered
      >
        <template #bodyCell="{ column, record }">
          <!-- 标签列 -->
          <template v-if="column.key === 'labels'">
            <a-space wrap>
              <a-tag v-for="label in record.labels || []" :key="label.id" :color="label.color">
                {{ label.name }}
              </a-tag>
              <span v-if="!record.labels || record.labels.length === 0" style="color: #ccc"
                >无</span
              >
            </a-space>
          </template>

          <!-- AI 评估列 -->
          <template v-else-if="column.key === 'aiEval'">
            <div v-if="record.aiPriorityLevel">
              <div style="display: flex; align-items: center; gap: 6px; flex-wrap: wrap">
                <a-tag
                  :color="
                    record.aiPriorityLevel === 'A'
                      ? 'red'
                      : record.aiPriorityLevel === 'B'
                        ? 'orange'
                        : 'blue'
                  "
                >
                  {{ record.aiPriorityLevel }}级
                </a-tag>
                <span style="font-weight: bold">{{ record.aiPriorityScore }}分</span>
              </div>
              <!-- ✅ 评估理由：自动换行，无截断 -->
              <div
                style="
                  font-size: 12px;
                  color: #555;
                  margin-top: 4px;
                  background: #f5f7fa;
                  padding: 4px 8px;
                  border-radius: 4px;
                  line-height: 1.6;
                  word-wrap: break-word;
                  white-space: normal;
                "
              >
                💡 {{ record.aiReason || '暂无理由' }}
              </div>
              <div style="font-size: 11px; color: #bbb; margin-top: 2px">
                {{ record.aiEvalTime ? formatDate(record.aiEvalTime) : '' }}
              </div>
            </div>
            <span v-else style="color: #ccc">未评估</span>
          </template>

          <!-- 操作列 -->
          <template v-else-if="column.key === 'action'">
            <a-space wrap>
              <a-button type="link" size="small" @click="openEditDialog(record)">编辑</a-button>
              <a-button type="link" size="small" danger @click="handleDelete(record.id)"
                >删除</a-button
              >
              <a-button type="link" size="small" @click="openLabelDialog(record)">标签</a-button>
              <a-button
                type="link"
                size="small"
                :style="{
                  color: record.aiPriorityLevel ? '#faad14' : '#52c41a',
                }"
                :loading="loadingMap[record.id]"
                @click="handleAIEvaluate(record.id, !!record.aiPriorityLevel)"
              >
                {{ record.aiPriorityLevel ? '🔄 重新生成' : '🤖 AI评估' }}
              </a-button>
            </a-space>
          </template>
        </template>
      </a-table>

      <!-- 分页 -->
      <div style="margin-top: 16px; display: flex; justify-content: flex-end">
        <a-pagination
          v-model:current="query.page"
          v-model:pageSize="query.size"
          :total="total"
          show-size-changer
          :page-size-options="['10', '50', '100']"
          @change="loadData"
          @showSizeChange="handleSizeChange"
        />
      </div>

      <!-- 新增/编辑弹窗 -->
      <a-modal
        v-model:open="dialogVisible"
        :title="dialogTitle"
        @ok="handleSave"
        @cancel="dialogVisible = false"
      >
        <a-form :model="form" layout="vertical">
          <a-form-item
            label="企业名称"
            name="companyName"
            :rules="[{ required: true, message: '请输入企业名称' }]"
          >
            <a-input v-model:value="form.companyName" placeholder="请输入企业名称" />
          </a-form-item>
          <a-form-item label="联系人">
            <a-input v-model:value="form.contactPerson" placeholder="请输入联系人姓名" />
          </a-form-item>
          <a-form-item label="电话">
            <a-input v-model:value="form.phone" placeholder="请输入联系电话" />
          </a-form-item>
          <a-form-item label="邮箱">
            <a-input v-model:value="form.email" placeholder="请输入邮箱" />
          </a-form-item>
          <a-form-item label="地址">
            <a-input v-model:value="form.address" placeholder="请输入企业地址" />
          </a-form-item>
          <a-form-item label="行业">
            <a-input v-model:value="form.industry" placeholder="请输入行业" />
          </a-form-item>
          <a-form-item label="企业规模">
            <a-select v-model:value="form.employeeScale" placeholder="请选择企业规模">
              <a-select-option value="10人以下">10人以下</a-select-option>
              <a-select-option value="10-50人">10-50人</a-select-option>
              <a-select-option value="50-200人">50-200人</a-select-option>
              <a-select-option value="200-500人">200-500人</a-select-option>
              <a-select-option value="500人以上">500人以上</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="客户来源">
            <a-input v-model:value="form.source" placeholder="请输入来源" />
          </a-form-item>
          <a-form-item label="备注">
            <a-textarea v-model:value="form.remark" placeholder="请输入备注" :rows="3" />
          </a-form-item>
        </a-form>
      </a-modal>

      <!-- 标签管理弹窗 -->
      <a-modal
        v-model:open="labelDialogVisible"
        :title="`管理标签 - ${currentCustomer?.companyName}`"
        @ok="handleSaveLabels"
        @cancel="labelDialogVisible = false"
      >
        <div v-for="(group, category) in groupedLabels" :key="category" style="margin-bottom: 16px">
          <div
            style="
              font-weight: 600;
              font-size: 14px;
              color: #409eff;
              border-bottom: 1px solid #eee;
              padding-bottom: 4px;
              margin-bottom: 8px;
            "
          >
            {{ category }}
          </div>
          <div
            v-for="label in group"
            :key="label.id"
            style="margin-bottom: 6px; display: flex; align-items: center"
          >
            <a-checkbox
              :checked="selectedLabelIds.includes(label.id)"
              @change="toggleLabel(label.id, category)"
            />
            <a-tag :color="label.color" style="margin-left: 10px">
              {{ label.name }}
            </a-tag>
            <span style="margin-left: 10px; color: #999; font-size: 12px">
              {{ label.category }}
            </span>
          </div>
        </div>
      </a-modal>

      <!-- 提示词配置弹窗 -->
      <a-modal
        v-model:open="promptDialogVisible"
        title="⚙️ AI 评估提示词配置"
        width="65%"
        @ok="handleSavePrompt"
        @cancel="promptDialogVisible = false"
      >
        <a-alert
          message="修改后将立即生效，后续所有「AI评估」和「重新生成」将使用新提示词"
          type="info"
          show-icon
          closable
          style="margin-bottom: 16px"
        />
        <a-textarea
          v-model:value="promptContent"
          :rows="14"
          placeholder="请输入评估提示词，使用 {companyName}、{industry} 等作为占位符..."
          style="font-size: 14px"
        />
        <div style="margin-top: 8px; font-size: 12px; color: #999">
          💡 支持的占位符：{companyName}、{industry}、{employeeScale}、{source}、{remark}
        </div>
      </a-modal>
    </a-card>
  </div>
</template>

<script>
import { message } from 'ant-design-vue'
import {
  getCustomerPage,
  getCustomerById,
  saveCustomer,
  updateCustomer,
  deleteCustomer,
} from '../api/customer'
import {
  getAllLabels,
  getCustomerLabels,
  addLabelsToCustomer,
  removeLabelFromCustomer,
} from '../api/label'
import { evaluateCustomer } from '../api/evaluation'
import { getPrompt, updatePrompt } from '../api/config'
import { downloadTemplate, importCustomers, exportCustomers } from '../api/importExport'

export default {
  data() {
    return {
      query: { page: 1, size: 10, keyword: '' },
      tableData: [],
      total: 0,
      loadingMap: {}, // 控制每行 AI 评估按钮的加载状态

      dialogVisible: false,
      dialogTitle: '新增客户',
      isEditMode: false,
      form: {
        id: null,
        companyName: '',
        contactPerson: '',
        phone: '',
        email: '',
        address: '',
        industry: '',
        employeeScale: '',
        source: '',
        remark: '',
      },

      labelDialogVisible: false,
      currentCustomer: null,
      allLabels: [],
      customerLabels: [],
      selectedLabelIds: [],
      groupedLabels: {},

      promptDialogVisible: false,
      promptContent: '',
      promptSaving: false,
    }
  },
  computed: {
    columns() {
      return [
        { title: 'ID', dataIndex: 'id', key: 'id', width: 80 },
        { title: '企业名称', dataIndex: 'companyName', key: 'companyName' },
        { title: '联系人', dataIndex: 'contactPerson', key: 'contactPerson' },
        { title: '电话', dataIndex: 'phone', key: 'phone' },
        { title: '行业', dataIndex: 'industry', key: 'industry' },
        { title: '规模', dataIndex: 'employeeScale', key: 'employeeScale' },
        { title: '标签', key: 'labels', width: 160 },
        // ========== 新增：AI 评级列 ==========
        { title: 'AI评级', key: 'aiEval', width: 300 },
        { title: '操作', key: 'action', width: 280 },
      ]
    },
  },
  mounted() {
    this.loadData()
  },
  methods: {
    // ===================== 数据加载 =====================
    async loadData() {
      try {
        const res = await getCustomerPage(this.query)
        if (res.data.code === 200) {
          const records = res.data.data.records || []
          records.sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
          this.tableData = records
          this.total = res.data.data.total
        }
      } catch (e) {
        message.error('加载数据失败，请检查后端是否启动')
        console.error(e)
      }
    },

    handleSizeChange() {
      this.query.page = 1
      this.loadData()
    },

    handleSearch() {
      this.query.page = 1
      this.loadData()
    },

    // ===================== AI 评估 =====================
    async handleAIEvaluate(customerId, isRegenerate) {
      this.loadingMap[customerId] = true
      try {
        const res = await evaluateCustomer(customerId, isRegenerate)
        if (res.data.code === 200) {
          message.success('AI评估完成！')
          await this.loadData()
        } else {
          message.error('评估失败：' + res.data.message)
        }
      } catch (e) {
        message.error('请求失败，请检查后端服务')
        console.error(e)
      } finally {
        this.loadingMap[customerId] = false
      }
    },

    // ===================== 提示词配置 =====================
    async openPromptDialog() {
      this.promptDialogVisible = true
      try {
        const res = await getPrompt()
        this.promptContent = res.data.data || ''
      } catch (e) {
        message.error('加载提示词失败')
        console.error(e)
      }
    },

    async handleSavePrompt() {
      if (!this.promptContent.trim()) {
        message.warning('提示词不能为空')
        return
      }
      this.promptSaving = true
      try {
        await updatePrompt(this.promptContent)
        message.success('提示词更新成功！')
        this.promptDialogVisible = false
      } catch (e) {
        message.error('保存失败')
        console.error(e)
      } finally {
        this.promptSaving = false
      }
    },

    // ===================== 客户 CRUD =====================
    openAddDialog() {
      this.isEditMode = false
      this.dialogTitle = '新增客户'
      this.form = {
        id: null,
        companyName: '',
        contactPerson: '',
        phone: '',
        email: '',
        address: '',
        industry: '',
        employeeScale: '',
        source: '',
        remark: '',
      }
      this.dialogVisible = true
    },

    async openEditDialog(row) {
      try {
        const res = await getCustomerById(row.id)
        if (res.data.code === 200) {
          const data = res.data.data
          this.isEditMode = true
          this.dialogTitle = '编辑客户'
          this.form = { ...data }
          this.dialogVisible = true
        } else {
          message.error('获取客户详情失败')
        }
      } catch (e) {
        message.error('获取客户详情失败，请检查后端')
        console.error(e)
      }
    },

    async handleSave() {
      if (!this.form.companyName) {
        message.warning('企业名称不能为空')
        return
      }
      try {
        let res
        if (this.isEditMode) {
          res = await updateCustomer(this.form.id, this.form)
        } else {
          res = await saveCustomer(this.form)
        }
        if (res.data.code === 200) {
          message.success(this.isEditMode ? '更新成功' : '新增成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          message.error(res.data.message || '操作失败')
        }
      } catch (e) {
        message.error('保存失败，请检查网络')
        console.error(e)
      }
    },

    async handleDelete(id) {
      if (!confirm('确定要删除该客户吗？此操作不可恢复！')) return
      try {
        const res = await deleteCustomer(id)
        if (res.data.code === 200) {
          message.success('删除成功')
          this.loadData()
        } else {
          message.error(res.data.message || '删除失败')
        }
      } catch (e) {
        message.error('删除失败，请检查后端')
        console.error(e)
      }
    },

    // ===================== 标签管理 =====================
    async loadAllLabels() {
      const res = await getAllLabels()
      if (res.data.code === 200) {
        this.allLabels = res.data.data
        this.groupedLabels = this.allLabels.reduce((acc, label) => {
          const category = label.category || '其他'
          if (!acc[category]) acc[category] = []
          acc[category].push(label)
          return acc
        }, {})
      }
    },

    async loadCustomerLabels(customerId) {
      const res = await getCustomerLabels(customerId)
      if (res.data.code === 200) {
        this.customerLabels = res.data.data
        this.selectedLabelIds = res.data.data.map((l) => l.id)
      }
    },

    async openLabelDialog(customer) {
      this.currentCustomer = customer
      await this.loadAllLabels()
      await this.loadCustomerLabels(customer.id)
      this.labelDialogVisible = true
    },

    toggleLabel(labelId, category) {
      const isSelected = this.selectedLabelIds.includes(labelId)
      if (category === '行业分类') {
        if (isSelected) {
          this.selectedLabelIds = this.selectedLabelIds.filter((id) => id !== labelId)
        } else {
          this.selectedLabelIds = [...this.selectedLabelIds, labelId]
        }
        return
      }
      if (isSelected) {
        this.selectedLabelIds = this.selectedLabelIds.filter((id) => id !== labelId)
      } else {
        const groupIds = (this.groupedLabels[category] || []).map((l) => l.id)
        this.selectedLabelIds = this.selectedLabelIds.filter((id) => !groupIds.includes(id))
        this.selectedLabelIds = [...this.selectedLabelIds, labelId]
      }
    },

    async handleSaveLabels() {
      const existingIds = this.customerLabels.map((l) => l.id)
      const toAdd = this.selectedLabelIds.filter((id) => !existingIds.includes(id))
      const toRemove = existingIds.filter((id) => !this.selectedLabelIds.includes(id))
      try {
        if (toAdd.length > 0) {
          await addLabelsToCustomer(this.currentCustomer.id, toAdd)
        }
        for (const labelId of toRemove) {
          await removeLabelFromCustomer(this.currentCustomer.id, labelId)
        }
        message.success('标签更新成功')
        this.labelDialogVisible = false
        this.loadData()
      } catch (e) {
        message.error('更新标签失败')
      }
    },

    // 下载导入模板
    async handleDownloadTemplate() {
      try {
        const res = await downloadTemplate()
        // 创建下载链接
        const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '客户导入模板.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        message.success('模板下载成功')
      } catch (e) {
        message.error('下载模板失败')
        console.error(e)
      }
    },

// 导入Excel
    handleImport() {
      // 创建文件选择器
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return

        try {
          const res = await importCustomers(file)
          if (res.data.code === 200) {
            const data = res.data.data
            message.success(
              `导入完成：成功 ${data.successCount} 条，失败 ${data.failCount} 条`
            )
            if (data.errors && data.errors.length > 0) {
              console.warn('导入错误详情：', data.errors)
              // 显示详细错误（简化版）
              const errorMsg = data.errors.map(e =>
                `第${e.rowNum}行（${e.companyName || '未填写'}）：${e.reason}`
              ).join('\n')
              alert(`导入失败详情：\n${errorMsg}`)
            }
            this.loadData()  // 刷新列表
          } else {
            message.error('导入失败：' + res.data.message)
          }
        } catch (e) {
          message.error('导入请求失败')
          console.error(e)
        }
      }
      input.click()
    },

// 导出客户数据
    async handleExport() {
      try {
        const params = {
          keyword: this.query.keyword || undefined,
          industry: this.query.industry || undefined,
          employeeScale: this.query.employeeScale || undefined
        }
        const res = await exportCustomers(params)
        const blob = new Blob([res.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '客户数据导出.xlsx'
        link.click()
        URL.revokeObjectURL(link.href)
        message.success('导出成功')
      } catch (e) {
        message.error('导出失败')
        console.error(e)
      }
    },

    // ===================== 工具方法 =====================
    formatDate(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(
        date.getDate(),
      ).padStart(2, '0')}`
    },
  },
}
</script>

<style scoped>
/* 表格滚动条样式 */
</style>
