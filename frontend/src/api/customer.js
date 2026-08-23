import request from './request'

// 分页查询
export const getCustomerPage = (params) => {
  return request.get('/api/customers/page', {
    params: params,
  })
}

// 新增客户
export const saveCustomer = (data) => {
  return request.post('/api/customers', data)
}

// 根据ID查询客户详情
export const getCustomerById = (id) => {
  return request.get('/api/customers/' + id)
}

// 更新客户
export const updateCustomer = (id, data) => {
  return request.put('/api/customers/' + id, data)
}

// 删除客户
export const deleteCustomer = (id) => {
  return request.delete('/api/customers/' + id)
}
