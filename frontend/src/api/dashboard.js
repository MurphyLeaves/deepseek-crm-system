import request from './request'

export const getDashboardStats = () => {
  return request.get('/api/dashboard/stats')
}
