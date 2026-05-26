import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/api/admin/vehicle/list',
    method: 'get',
    params
  })
}

export function getById(id) {
  return request({
    url: `/api/admin/vehicle/${id}`,
    method: 'get'
  })
}

export function create(data) {
  return request({
    url: '/api/admin/vehicle',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/api/admin/vehicle',
    method: 'put',
    data
  })
}

export function remove(id) {
  return request({
    url: `/api/admin/vehicle/${id}`,
    method: 'delete'
  })
}

export function getLocations() {
  return request({
    url: '/api/admin/vehicle/location',
    method: 'get'
  })
}
