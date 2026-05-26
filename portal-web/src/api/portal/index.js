import request from '@/utils/request'

export function getStats(params) {
  return request({
    url: '/api/portal/index/stats',
    method: 'get',
    params
  })
}

export function getNews() {
  return request({
    url: '/api/portal/index/news',
    method: 'get'
  })
}
