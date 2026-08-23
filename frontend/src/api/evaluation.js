import request from './request'

/**
 * 对指定客户进行AI评估
 * @param {number} customerId - 客户ID
 * @param {boolean} refresh - true=重新生成，false=使用缓存
 */
export const evaluateCustomer = (customerId, refresh = false) => {
  return request.post(`/api/evaluation/${customerId}?refresh=${refresh}`, {})
}
