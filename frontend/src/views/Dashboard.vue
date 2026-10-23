<template>
  <div class="dashboard-container">
    <!-- 头部 -->
    <div class="header">
      <div style="display: flex; align-items: center; gap: 16px">
        <button class="back-btn" @click="$router.push('/home')">← 返回首页</button>
        <h2>📊 数据可视化看板</h2>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-row">
      <div class="stat-card">
        <div class="stat-number">{{ stats.totalCustomers || 0 }}</div>
        <div class="stat-label">总客户数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.monthNewCustomers || 0 }}</div>
        <div class="stat-label">本月新增</div>
      </div>
      <div class="stat-card">
        <div class="stat-number" style="color: #f5222d">{{ stats.levelACount || 0 }}</div>
        <div class="stat-label">A级客户数</div>
      </div>
    </div>

    <!-- 趋势图和优先级图 -->
    <div class="charts-row">
      <div class="chart-box">
        <div ref="trendChartRef" class="chart"></div>
      </div>
      <div class="chart-box">
        <div ref="priorityChartRef" class="chart"></div>
      </div>
    </div>

    <!-- 标签分布 -->
    <div class="pie-section">
      <h3 class="pie-section-title">📊 标签客户占比（按分类）</h3>
      <div class="pie-cards-wrapper">
        <div v-for="(category, index) in tagCategories" :key="index" class="pie-card">
          <div class="pie-card-title">{{ category }}</div>
          <div
            :ref="
              (el) => {
                if (el) tagPieRefs[index] = el
              }
            "
            class="pie-chart"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDashboardStats } from '../api/dashboard'

export default {
  data() {
    return {
      stats: {},
      tagCategories: [],
      tagPieCharts: [],
      tagPieDataMap: {},
      tagPieRefs: [],
      resizeHandlers: [],
    }
  },
  mounted() {
    this.loadData()
    // 窗口变化时额外触发布局刷新
    window.addEventListener('resize', this.handleResize)
  },
  beforeUnmount() {
    if (this.trendChart) {
      this.trendChart.dispose()
      this.trendChart = null
    }
    if (this.priorityChart) {
      this.priorityChart.dispose()
      this.priorityChart = null
    }
    this.tagPieCharts.forEach((chart) => {
      if (chart) chart.dispose()
    })
    this.tagPieCharts = []

    this.resizeHandlers.forEach(({ handler }) => {
      window.removeEventListener('resize', handler)
    })
    this.resizeHandlers = []
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    handleResize() {
      // 根布局变换时，通知图表重新适应
      if (this.trendChart) this.trendChart.resize()
      if (this.priorityChart) this.priorityChart.resize()
      this.tagPieCharts.forEach((chart) => chart && chart.resize())
    },

    async loadData() {
      try {
        const res = await getDashboardStats()
        if (res.data.code === 200) {
          this.stats = res.data.data
          this.$nextTick(() => {
            this.initTrendChart()
            this.initPriorityChart()
            this.prepareTagPieData()
            this.initTagPieCharts()
          })
        } else {
          this.$message.error('加载看板数据失败')
        }
      } catch (e) {
        console.error('加载看板数据失败', e)
        this.$message.error('加载看板数据失败')
      }
    },

    initTrendChart() {
      if (!this.$refs.trendChartRef) return
      if (this.trendChart) this.trendChart.dispose()

      this.trendChart = echarts.init(this.$refs.trendChartRef)
      const option = {
        title: { text: '近30天客户增长趋势', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: this.stats.dates || [],
          axisLabel: { rotate: 45, interval: 4 },
        },
        yAxis: { type: 'value', minInterval: 1 },
        series: [
          {
            data: this.stats.customerCounts || [],
            type: 'line',
            smooth: true,
            lineStyle: { color: '#667eea', width: 3 },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(102, 126, 234, 0.4)' },
                { offset: 1, color: 'rgba(102, 126, 234, 0.05)' },
              ]),
            },
            symbol: 'circle',
            symbolSize: 6,
            itemStyle: { color: '#667eea' },
          },
        ],
        grid: { left: 50, right: 20, top: 60, bottom: 60 },
      }
      this.trendChart.setOption(option)

      const handler = () => this.trendChart && this.trendChart.resize()
      window.addEventListener('resize', handler)
      this.resizeHandlers.push({ handler })
    },

    initPriorityChart() {
      if (!this.$refs.priorityChartRef) return
      if (this.priorityChart) this.priorityChart.dispose()

      this.priorityChart = echarts.init(this.$refs.priorityChartRef)
      const distribution = this.stats.priorityDistribution || {}
      const data = [
        { name: 'A级', value: distribution.A || 0, color: '#f5222d' },
        { name: 'B级', value: distribution.B || 0, color: '#fa8c16' },
        { name: 'C级', value: distribution.C || 0, color: '#1890ff' },
      ]
      const option = {
        title: { text: '客户优先级分布', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.map((d) => d.name) },
        yAxis: { type: 'value', minInterval: 1 },
        series: [
          {
            type: 'bar',
            data: data.map((d) => ({
              value: d.value,
              itemStyle: { color: d.color, borderRadius: [6, 6, 0, 0] },
            })),
            barWidth: 50,
            label: {
              show: true,
              position: 'top',
              formatter: (params) => params.value + '个',
            },
          },
        ],
        grid: { left: 50, right: 20, top: 60, bottom: 40 },
      }
      this.priorityChart.setOption(option)

      const handler = () => this.priorityChart && this.priorityChart.resize()
      window.addEventListener('resize', handler)
      this.resizeHandlers.push({ handler })
    },

    prepareTagPieData() {
      const rawData = this.stats.tagDistribution || []
      if (rawData.length === 0) {
        this.tagCategories = []
        this.tagPieDataMap = {}
        return
      }
      const map = {}
      rawData.forEach((item) => {
        const category = item.category || '未分类'
        if (!map[category]) map[category] = []
        map[category].push({ name: item.labelName, value: item.value })
      })
      this.tagCategories = Object.keys(map)
      this.tagPieDataMap = map
      this.tagPieRefs = new Array(this.tagCategories.length).fill(null)
    },

    initTagPieCharts() {
      this.tagPieCharts.forEach((chart) => chart && chart.dispose())
      this.tagPieCharts = []

      const categories = this.tagCategories
      if (categories.length === 0) return

      this.$nextTick(() => {
        const categoryColorPalette = {
          意向程度: ['#FFB3BA', '#FF8A94', '#FF6B7A'],
          客户等级: ['#B5D8EB', '#7EB8D4', '#4A90A4'],
          行业分类: ['#C7E9C0', '#9AD18C', '#6BB85A'],
          规模等级: ['#FFE4A2', '#FFD166', '#F8B84A'],
          跟进阶段: ['#D4C5F9', '#B8A3E8', '#9A7FD4'],
          未分类: ['#D3D3D3', '#BDBDBD', '#A8A8A8'],
        }
        const defaultColors = ['#FFB3BA', '#B5D8EB', '#C7E9C0', '#FFE4A2', '#D4C5F9']

        categories.forEach((category, index) => {
          const dom = this.tagPieRefs[index]
          if (!dom) return

          const chart = echarts.init(dom)
          this.tagPieCharts.push(chart)

          const data = this.tagPieDataMap[category] || []
          const total = data.reduce((sum, item) => sum + item.value, 0)

          if (data.length === 0 || total === 0) {
            chart.setOption({
              title: {
                text: '暂无数据',
                left: 'center',
                top: 'center',
                textStyle: { fontSize: 13, color: '#999' },
              },
            })
            return
          }

          const colors = categoryColorPalette[category] || defaultColors
          const coloredData = data.map((item, idx) => ({
            ...item,
            itemStyle: { color: colors[idx % colors.length] },
          }))

          const option = {
            tooltip: {
              trigger: 'item',
              formatter: (params) =>
                `<strong>${params.name}</strong><br/>客户数：${params.value}<br/>占比：${params.percent}%`,
              backgroundColor: 'rgba(255,255,255,0.9)',
              borderColor: '#ddd',
              borderWidth: 1,
            },
            legend: {
              orient: 'horizontal',
              bottom: 0,
              left: 'center',
              itemWidth: 10,
              itemHeight: 10,
              textStyle: { fontSize: 11, color: '#555' },
            },
            series: [
              {
                type: 'pie',
                radius: ['40%', '60%'],
                center: ['50%', '45%'],
                avoidLabelOverlap: true,
                itemStyle: {
                  borderRadius: 6,
                  borderColor: '#fff',
                  borderWidth: 2,
                },
                label: {
                  show: true,
                  formatter: '{b}\n{d}%',
                  fontSize: 10,
                  color: '#555',
                },
                labelLine: { length: 12, length2: 12 },
                emphasis: {
                  label: { fontSize: 12, fontWeight: 'bold' },
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0,0,0,0.2)',
                  },
                },
                data: coloredData,
              },
            ],
          }

          chart.setOption(option)

          const handler = () => chart.resize()
          window.addEventListener('resize', handler)
          this.resizeHandlers.push({ handler })
        })
      })
    },
  },
}
</script>

<style scoped>
/* 核心布局容器铺满 */
.dashboard-container {
  display: flex;
  flex-direction: column;
  flex: 1; /* 占据 #app 的全部剩余空间 */
  width: 100%; /* 占据 100% 宽度 */
  max-width: 100%; /* 强制清除 Vue 项目里常见的 max-width 限制 */
  height: 100vh; /* 保证无论内容多少，背景都铺满整个屏幕高度 */
  box-sizing: border-box;
  padding: 24px 32px;
  background: #f0f2f5;
  overflow-y: auto;
  gap: 24px;
}

.header h2 {
  margin: 0;
  font-weight: 500;
  color: #333;
}

.back-btn {
  background: none;
  border: none;
  color: #667eea;
  font-size: 14px;
  cursor: pointer;
  padding: 4px 0;
}
.back-btn:hover {
  color: #764ba2;
}

/* 统计卡片区 */
.stats-row {
  display: flex;
  gap: 20px;
  flex-wrap: wrap;
  flex-shrink: 0;
}
.stat-card {
  flex: 1;
  min-width: 140px;
  background: white;
  padding: 24px 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}
.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}
.stat-number {
  font-size: 34px;
  font-weight: bold;
  color: #333;
}
.stat-label {
  font-size: 14px;
  color: #888;
  margin-top: 8px;
}

/* 折线图 + 柱状图 */
.charts-row {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  flex: 1 1 auto;
}
.chart-box {
  flex: 1;
  min-width: 460px; /* 屏幕足够宽时必定左右并排，小于 900px 时自动换行 */
  background: white;
  padding: 16px 20px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: center;
  align-items: center;
}
.chart {
  width: 100%;
  height: 360px;
}

/* 标签分类饼图区 */
.pie-section {
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}
.pie-section-title {
  text-align: center;
  font-size: 16px;
  font-weight: normal;
  color: #333;
  margin-bottom: 16px;
}

.pie-cards-wrapper {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}
.pie-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 16px 10px 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: box-shadow 0.2s;
  display: flex;
  flex-direction: column;
}
.pie-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}
.pie-card-title {
  text-align: center;
  font-size: 14px;
  font-weight: 500;
  color: #555;
  margin-bottom: 8px;
}
.pie-chart {
  width: 100%;
  height: 280px;
}
</style>

<!-- 全局样式重置：ECharts 容器与弹窗依赖，不能删除 -->
<style>
html,
body,
#app {
  width: 100vw; /* 强制宽度 100% */
  height: 100%; /* 强制高度 100% */
  margin: 0; /* 移除浏览器默认的 8px 留白 */
  padding: 0;
  background-color: #f0f2f5;
}
#app {
  display: flex; /* 启用 Flex 布局 */
  flex-direction: column; /* 纵向排列 */
  align-items: stretch; /* 强制子元素（也就是看板）撑满横向空间 */
}
</style>
