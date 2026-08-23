import request from './request'

/**
 * 下载导入模板
 */
export const downloadTemplate = () => {
  return request.get('/api/import-export/template', {
    responseType: 'blob',
  })
}

/**
 * 导入客户数据
 */
export const importCustomers = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/api/import-export/import', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

/**
 * 导出客户数据（支持当前筛选条件）
 */
export const exportCustomers = (params) => {
  return request.get('/api/import-export/export', {
    params: params,
    responseType: 'blob',
  })
}
