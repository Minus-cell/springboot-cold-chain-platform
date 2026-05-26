import request from '@/utils/request'

export function getInfo(params) {
  return request({
    url: '/api/portal/user/info',
    method: 'get',
    params
  })
}

export function updateInfo(data) {
  return request({
    url: '/api/portal/user/info',
    method: 'put',
    data
  })
}

export function updatePassword(params) {
  return request({
    url: '/api/portal/user/password',
    method: 'put',
    params
  })
}
