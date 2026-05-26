import request from '@/utils/request'

export function getUserList(params) {
  return request({
    url: '/api/admin/user/list',
    method: 'get',
    params
  })
}

export function getUser(id) {
  return request({
    url: `/api/admin/user/${id}`,
    method: 'get'
  })
}

export function addUser(data) {
  return request({
    url: '/api/admin/user',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/api/admin/user',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/api/admin/user/${id}`,
    method: 'delete'
  })
}