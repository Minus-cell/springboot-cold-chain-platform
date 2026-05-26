import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/admin/auth/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/api/admin/auth/logout',
    method: 'post'
  })
}

export function getUserInfo() {
  return request({
    url: '/api/admin/auth/getInfo',
    method: 'get'
  })
}
