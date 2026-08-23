import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

// ---------- 标签库管理 ----------

// 分页查询标签
export const getLabelPage = (params) => {
  return axios.get(BASE_URL + '/api/labels/page', {
    params: params,
    withCredentials: true,
  })
}

// 查询所有标签（下拉用）
export const getAllLabels = () => {
  return axios.get(BASE_URL + '/api/labels/all', {
    withCredentials: true,
  })
}

// 新增标签
export const saveLabel = (data) => {
  return axios.post(BASE_URL + '/api/labels', data, {
    withCredentials: true,
  })
}

// 更新标签
export const updateLabel = (id, data) => {
  return axios.put(BASE_URL + '/api/labels/' + id, data, {
    withCredentials: true,
  })
}

// 删除标签
export const deleteLabel = (id) => {
  return axios.delete(BASE_URL + '/api/labels/' + id, {
    withCredentials: true,
  })
}

// ---------- 客户打标签 ----------

// 获取客户已有的标签
export const getCustomerLabels = (customerId) => {
  return axios.get(BASE_URL + '/api/labels/customer/' + customerId, {
    withCredentials: true,
  })
}

// 给客户添加标签
export const addLabelsToCustomer = (customerId, labelIds) => {
  return axios.post(BASE_URL + '/api/customers/' + customerId + '/labels', labelIds, {
    withCredentials: true,
  })
}

// 移除客户的标签
export const removeLabelFromCustomer = (customerId, labelId) => {
  return axios.delete(BASE_URL + '/api/customers/' + customerId + '/labels/' + labelId, {
    withCredentials: true,
  })
}
