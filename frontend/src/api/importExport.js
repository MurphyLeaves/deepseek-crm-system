import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

/**
 * 下载导入模板
 */
export const downloadTemplate = () => {
  return axios.get(BASE_URL + '/api/import-export/template', {
    withCredentials: true,
    responseType: 'blob',
  })
}

/**
 * 导入客户数据
 */
export const importCustomers = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return axios.post(BASE_URL + '/api/import-export/import', formData, {
    withCredentials: true,
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}

/**
 * 导出客户数据（支持当前筛选条件）
 */
export const exportCustomers = (params) => {
  return axios.get(BASE_URL + '/api/import-export/export', {
    params: params,
    withCredentials: true,
    responseType: 'blob',
  })
}
