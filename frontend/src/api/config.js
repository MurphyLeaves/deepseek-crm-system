import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

/**
 * 获取当前AI评估提示词
 */
export const getPrompt = () => {
  return axios.get(`${BASE_URL}/api/config/prompt`, {
    withCredentials: true,
  })
}

/**
 * 更新AI评估提示词
 * @param {string} prompt - 新的提示词内容
 */
export const updatePrompt = (prompt) => {
  return axios.put(`${BASE_URL}/api/config/prompt`, prompt, {
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true,
  })
}
