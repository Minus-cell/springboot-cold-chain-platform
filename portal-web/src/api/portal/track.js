import request from '@/utils/request'

export function track(orderNo) {
  return request({
    url: `/api/portal/track/${orderNo}`,
    method: 'get'
  })
}

export function getTemperature(orderNo) {
  return request({
    url: `/api/portal/track/${orderNo}/temperature`,
    method: 'get'
  })
}

export function getLocation(orderNo) {
  return request({
    url: `/api/portal/track/${orderNo}/location`,
    method: 'get'
  })
}
