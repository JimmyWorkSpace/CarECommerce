import request from '@/utils/request'

// 查询推荐管理列表
export function listRecommand(query) {
  return request({
    url: '/car/recommand/list',
    method: 'get',
    params: query
  })
}

// 查询推荐管理详细
export function getRecommand(id) {
  return request({
    url: '/car/recommand/' + id,
    method: 'get'
  })
}

// 新增推荐管理
export function addRecommand(data) {
  return request({
    url: '/car/recommand',
    method: 'post',
    data: data
  })
}

// 修改推荐管理
export function updateRecommand(data) {
  return request({
    url: '/car/recommand',
    method: 'put',
    data: data
  })
}

// 删除推荐管理
export function delRecommand(id) {
  return request({
    url: '/car/recommand/' + id,
    method: 'delete'
  })
}

// 导出推荐管理
export function exportRecommand(query) {
  return request({
    url: '/car/recommand/export',
    method: 'post',
    params: query
  })
}

// 设置推荐状态
export function setRecommended(data) {
  return request({
    url: '/car/recommand/setRecommended',
    method: 'put',
    data: data
  })
}
