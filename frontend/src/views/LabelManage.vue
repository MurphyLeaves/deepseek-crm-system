<template>
  <div style="padding: 20px">
    <h2>🏷️ 标签库管理</h2>

    <!-- 搜索和新增 -->
    <div style="margin: 15px 0; display: flex; gap: 10px; flex-wrap: wrap">
      <input v-model="keyword" placeholder="搜索标签名称或分类" @keyup.enter="loadData()" />
      <button @click="loadData()">搜索</button>
      <button
        @click="openAddDialog()"
        style="
          background: #409eff;
          color: white;
          border: none;
          padding: 6px 16px;
          border-radius: 4px;
          cursor: pointer;
        "
      >
        + 新增标签
      </button>
    </div>

    <!-- 标签表格 -->
    <table
      border="1"
      cellpadding="8"
      style="width: 100%; border-collapse: collapse; background: white"
    >
      <thead style="background: #f5f7fa">
        <tr>
          <th>ID</th>
          <th>标签名称</th>
          <th>分类</th>
          <th>颜色</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in tableData" :key="item.id">
          <td>{{ item.id }}</td>
          <td>
            <span
              :style="{
                backgroundColor: item.color,
                padding: '2px 10px',
                borderRadius: '4px',
                color: '#fff',
              }"
              >{{ item.name }}</span
            >
          </td>
          <td>{{ item.category }}</td>
          <td>{{ item.color }}</td>
          <td>
            <button
              @click="openEditDialog(item)"
              style="
                margin-right: 5px;
                background: #67c23a;
                color: white;
                border: none;
                padding: 4px 12px;
                border-radius: 4px;
                cursor: pointer;
              "
            >
              编辑
            </button>
            <button
              @click="handleDelete(item.id)"
              style="
                background: #f56c6c;
                color: white;
                border: none;
                padding: 4px 12px;
                border-radius: 4px;
                cursor: pointer;
              "
            >
              删除
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 分页 -->
    <div style="margin-top: 15px; display: flex; align-items: center; gap: 10px">
      <button @click="prevPage">上一页</button>
      <span>第 {{ page }} 页 / 共 {{ totalPages }} 页</span>
      <button @click="nextPage">下一页</button>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div
      v-if="dialogVisible"
      style="
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.4);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
      "
    >
      <div style="background: white; padding: 30px; border-radius: 8px; width: 400px">
        <h3>{{ dialogTitle }}</h3>
        <div style="margin-top: 15px">
          <div style="margin-bottom: 12px">
            <label style="display: inline-block; width: 80px">标签名称 *</label>
            <input
              v-model="form.name"
              placeholder="如：高意向"
              style="width: 200px; padding: 6px 10px"
            />
          </div>
          <div style="margin-bottom: 12px">
            <label style="display: inline-block; width: 80px">分类 *</label>
            <input
              v-model="form.category"
              placeholder="如：意向程度"
              style="width: 200px; padding: 6px 10px"
            />
          </div>
          <div style="margin-bottom: 12px">
            <label style="display: inline-block; width: 80px">颜色</label>
            <input
              v-model="form.color"
              type="color"
              style="width: 60px; padding: 0; border: none; cursor: pointer"
            />
            <span style="margin-left: 10px; font-size: 14px; color: #999">{{ form.color }}</span>
          </div>
        </div>
        <div style="margin-top: 20px; text-align: right">
          <button
            @click="dialogVisible = false"
            style="
              padding: 8px 20px;
              background: #909399;
              color: white;
              border: none;
              border-radius: 4px;
              cursor: pointer;
              margin-right: 10px;
            "
          >
            取消
          </button>
          <button
            @click="handleSave"
            style="
              padding: 8px 20px;
              background: #409eff;
              color: white;
              border: none;
              border-radius: 4px;
              cursor: pointer;
            "
          >
            保存
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getLabelPage, saveLabel, updateLabel, deleteLabel } from '../api/label'

export default {
  data() {
    return {
      page: 1,
      size: 10,
      keyword: '',
      tableData: [],
      total: 0,
      totalPages: 0,
      dialogVisible: false,
      dialogTitle: '新增标签',
      isEditMode: false,
      form: { id: null, name: '', category: '', color: '#409EFF' },
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const res = await getLabelPage({ page: this.page, size: this.size, keyword: this.keyword })
        if (res.data.code === 200) {
          this.tableData = res.data.data.records
          this.total = res.data.data.total
          this.totalPages = Math.ceil(this.total / this.size)
        }
      } catch (e) {
        alert('加载失败')
      }
    },
    openAddDialog() {
      this.isEditMode = false
      this.dialogTitle = '新增标签'
      this.form = { id: null, name: '', category: '', color: '#409EFF' }
      this.dialogVisible = true
    },
    openEditDialog(row) {
      this.isEditMode = true
      this.dialogTitle = '编辑标签'
      this.form = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      if (!this.form.name || !this.form.category) {
        alert('名称和分类不能为空')
        return
      }
      try {
        let res
        if (this.isEditMode) {
          res = await updateLabel(this.form.id, this.form)
        } else {
          res = await saveLabel(this.form)
        }
        if (res.data.code === 200) {
          alert('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          alert(res.data.message)
        }
      } catch (e) {
        alert('保存失败')
      }
    },
    async handleDelete(id) {
      if (!confirm('确定删除该标签吗？')) return
      try {
        const res = await deleteLabel(id)
        if (res.data.code === 200) {
          alert('删除成功')
          this.loadData()
        }
      } catch (e) {
        alert('删除失败')
      }
    },
    /**
     * 上一页
     * 判断当前页码是否大于 1，如果是则页码减 1 并重新加载数据
     */
    prevPage() {
      // 如果当前页码大于 1，才允许翻到上一页
      if (this.page > 1) {
        this.page-- // 页码减 1
        this.loadData() // 重新请求数据，刷新表格
      }
    },

    /**
     * 下一页
     * 判断当前页码是否小于总页数，如果是则页码加 1 并重新加载数据
     */
    nextPage() {
      // 如果当前页码小于总页数，才允许翻到下一页
      if (this.page < this.totalPages) {
        this.page++ // 页码加 1
        this.loadData() // 重新请求数据，刷新表格
      }
    },
  },
}
</script>
