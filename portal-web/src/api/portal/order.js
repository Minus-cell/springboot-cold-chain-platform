import request from '@/utils/request'

export function create(data) {
  return request({
    url: '/api/portal/order/create',
    method: 'post',
    data
  })
}

export function getList(params) {
  return request({
    url: '/api/portal/order/list',
    method: 'get',
    params
  })
}

export function getByOrderNo(orderNo) {
  return request({
    url: `/api/portal/order/${orderNo}`,
    method: 'get'
  })
}

export function cancel(orderNo) {
  return request({
    url: `/api/portal/order/cancel/${orderNo}`,
    method: 'put'
  })
}
