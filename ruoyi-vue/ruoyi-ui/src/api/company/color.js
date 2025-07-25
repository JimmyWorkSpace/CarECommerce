import request from '@/utils/request'

// 查询等级颜色定义列表
export function listColor(query) {
  return request({
    url: '/company/color/list',
    method: 'get',
    params: query
  })
}

// 查询等级颜色定义详细
export function getColor(id) {
  return request({
    url: '/company/color/' + id,
    method: 'get'
  })
}

// 新增等级颜色定义
export function addColor(data) {
  return request({
    url: '/company/color',
    method: 'post',
    data: data
  })
}

// 修改等级颜色定义
export function updateColor(data) {
  return request({
    url: '/company/color',
    method: 'put',
    data: data
  })
}

// 删除等级颜色定义
export function delColor(id) {
  return request({
    url: '/company/color/' + id,
    method: 'delete'
  })
}
