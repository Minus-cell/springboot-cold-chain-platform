import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/api/admin/order/list',
    method: 'get',
    params
  })
}

export function getById(id) {
  return request({
    url: `/api/admin/order/${id}`,
    method: 'get'
  })
}

export function create(data) {
  return request({
    url: '/api/admin/order',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/api/admin/order',
    method: 'put',
    data
  })
}

export function remove(id) {
  return request({
    url: `/api/admin/order/${id}`,
    method: 'delete'
  })
}

export function updateStatus(id, status) {
  return request({
    url: `/api/admin/order/${id}/status`,
    method: 'put',
    data: { status }
  })
}
