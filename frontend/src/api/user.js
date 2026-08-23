import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

/**
 * 获取用户列表
 */
export const getUserList = () => {
  return axios.get(BASE_URL + '/api/user/list', {
    withCredentials: true,
  })
}

/**
 * 修改密码
 * @param {string} oldPassword - 原密码
 * @param {string} newPassword - 新密码
 */
export const updatePassword = (oldPassword, newPassword) => {
  return axios.put(
    BASE_URL + '/api/user/password',
    {
      oldPassword,
      newPassword,
    },
    {
      withCredentials: true,
    },
  )
}
