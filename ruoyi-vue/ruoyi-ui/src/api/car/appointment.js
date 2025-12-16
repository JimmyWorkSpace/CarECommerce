import request from '@/utils/request'

// 查询预约看车列表
export function listAppointment(query) {
  return request({
    url: '/car/appointment/list',
    method: 'get',
    params: query
  })
}

// 查询预约看车详细
export function getAppointment(id) {
  return request({
    url: '/car/appointment/' + id,
    method: 'get'
  })
}

// 新增预约看车
export function addAppointment(data) {
  return request({
    url: '/car/appointment',
    method: 'post',
    data: data
  })
}

// 修改预约看车
export function updateAppointment(data) {
  return request({
    url: '/car/appointment',
    method: 'put',
    data: data
  })
}

// 删除预约看车
export function delAppointment(id) {
  return request({
    url: '/car/appointment/' + id,
    method: 'delete'
  })
}

// 获取车辆网址前缀配置
export function getCarbuyPrefix() {
  return request({
    url: '/car/appointment/carbuy-prefix',
    method: 'get'
  })
}