import request from './request'

/**
 * 获取当前AI评估提示词
 */
export const getPrompt = () => {
  return request.get('/api/config/prompt')
}

/**
 * 更新AI评估提示词
 * @param {string} prompt - 新的提示词内容
 */
export const updatePrompt = (prompt) => {
  return request.put('/api/config/prompt', prompt, {
    headers: { 'Content-Type': 'application/json' },
  })
}
