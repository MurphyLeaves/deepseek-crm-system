import request from './request'

/**
 * 获取用户列表
 */
export const getUserList = () => {
  return request.get('/api/user/list')
}

/**
 * 修改密码
 * @param {string} oldPassword - 原密码
 * @param {string} newPassword - 新密码
 */
export const updatePassword = (oldPassword, newPassword) => {
  return request.put('/api/user/password', {
    oldPassword,
    newPassword,
  })
}
