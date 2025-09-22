import request from '@/utils/request'

// 查询車輛檢舉列表
export function listReport(query) {
  return request({
    url: '/car/report/list',
    method: 'get',
    params: query
  })
}

// 查询車輛檢舉详细
export function getReport(id) {
  return request({
    url: '/car/report/' + id,
    method: 'get'
  })
}

// 新增車輛檢舉
export function addReport(data) {
  return request({
    url: '/car/report',
    method: 'post',
    data: data
  })
}

// 修改車輛檢舉
export function updateReport(data) {
  return request({
    url: '/car/report',
    method: 'put',
    data: data
  })
}

// 删除車輛檢舉
export function delReport(id) {
  return request({
    url: '/car/report/' + id,
    method: 'delete'
  })
}
