import request from '@/utils/request'

// 查詢票券方案列表
export function listCard(query) {
  return request({
    url: '/car/card/list',
    method: 'get',
    params: query
  })
}

// 查詢票券方案詳細
export function getCard(id) {
  return request({
    url: '/car/card/' + id,
    method: 'get'
  })
}

// 新增票券方案
export function addCard(data) {
  return request({
    url: '/car/card',
    method: 'post',
    data: data
  })
}

// 修改票券方案
export function updateCard(data) {
  return request({
    url: '/car/card',
    method: 'put',
    data: data
  })
}

// 刪除票券方案
export function delCard(id) {
  return request({
    url: '/car/card/' + (Array.isArray(id) ? id.join(',') : id),
    method: 'delete'
  })
}
