import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/portal/auth/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/api/portal/auth/register',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/api/portal/auth/logout',
    method: 'post'
  })
}
