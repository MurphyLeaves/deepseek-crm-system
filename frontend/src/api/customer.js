import axios from 'axios'

// 基础路径配置（根据你的后端地址）
const BASE_URL = 'http://localhost:8080'

// 分页查询
export const getCustomerPage = (params) => {
  return axios.get(BASE_URL + '/api/customers/page', {
    params: params,
    withCredentials: true,
  })
}

// 新增客户
export const saveCustomer = (data) => {
  return axios.post(BASE_URL + '/api/customers', data, {
    withCredentials: true,
  })
}
// 根据ID查询客户详情
export const getCustomerById = (id) => {
  return axios.get(BASE_URL + '/api/customers/' + id, {
    withCredentials: true
  });
};

// 更新客户
export const updateCustomer = (id, data) => {
  return axios.put(BASE_URL + '/api/customers/' + id, data, {
    withCredentials: true
  });
};

// 删除客户
export const deleteCustomer = (id) => {
  return axios.delete(BASE_URL + '/api/customers/' + id, {
    withCredentials: true
  });
};
