import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/api/admin/temperature/list',
    method: 'get',
    params
  })
}

export function getChart(orderId) {
  return request({
    url: `/api/admin/temperature/${orderId}/chart`,
    method: 'get'
  })
}

export function getAlarms() {
  return request({
    url: '/api/admin/temperature/alarm',
    method: 'get'
  })
}

export function getLatest() {
  return request({
    url: '/api/admin/temperature/latest',
    method: 'get'
  })
}
