import request from './request'

// ---------- 标签库管理 ----------

// 分页查询标签
export const getLabelPage = (params) => {
  return request.get('/api/labels/page', {
    params: params,
  })
}

// 查询所有标签（下拉用）
export const getAllLabels = () => {
  return request.get('/api/labels/all')
}

// 新增标签
export const saveLabel = (data) => {
  return request.post('/api/labels', data)
}

// 更新标签
export const updateLabel = (id, data) => {
  return request.put('/api/labels/' + id, data)
}

// 删除标签
export const deleteLabel = (id) => {
  return request.delete('/api/labels/' + id)
}

// ---------- 客户打标签 ----------

// 获取客户已有的标签
export const getCustomerLabels = (customerId) => {
  return request.get('/api/labels/customer/' + customerId)
}

// 给客户添加标签
export const addLabelsToCustomer = (customerId, labelIds) => {
  return request.post('/api/customers/' + customerId + '/labels', labelIds)
}

// 移除客户的标签
export const removeLabelFromCustomer = (customerId, labelId) => {
  return request.delete('/api/customers/' + customerId + '/labels/' + labelId)
}
