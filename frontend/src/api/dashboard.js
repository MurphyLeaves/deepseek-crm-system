import axios from 'axios'

const BASE_URL = 'http://localhost:8080'

export const getDashboardStats = () => {
  return axios.get(BASE_URL + '/api/dashboard/stats', {
    withCredentials: true,
  })
}
