import request from '@/utils/request'

export function getRoleList(params) {
  return request({
    url: '/api/admin/role/list',
    method: 'get',
    params
  })
}

export function getRole(id) {
  return request({
    url: `/api/admin/role/${id}`,
    method: 'get'
  })
}

export function addRole(data) {
  return request({
    url: '/api/admin/role',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/api/admin/role',
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/api/admin/role/${id}`,
    method: 'delete'
  })
}