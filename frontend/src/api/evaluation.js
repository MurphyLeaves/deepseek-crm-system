import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

/**
 * 对指定客户进行AI评估
 * @param {number} customerId - 客户ID
 * @param {boolean} refresh - true=重新生成，false=使用缓存
 */
export const evaluateCustomer = (customerId, refresh = false) => {
  return axios.post(
    `${BASE_URL}/api/evaluation/${customerId}?refresh=${refresh}`,
    {},
    {
      withCredentials: true,
    },
  )
}
