import request from '@/utils/request'

export function getStats() {
  return request({
    url: '/api/admin/dashboard/stats',
    method: 'get'
  })
}

export function getTrend() {
  return request({
    url: '/api/admin/dashboard/trend',
    method: 'get'
  })
}

export function getRealtime() {
  return request({
    url: '/api/admin/dashboard/realtime',
    method: 'get'
  })
}
