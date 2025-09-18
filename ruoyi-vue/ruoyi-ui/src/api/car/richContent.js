import request from '@/utils/request'

// 查询富文本内容列表
export function listRichContent(query) {
  return request({
    url: '/car/richContent/list',
    method: 'get',
    params: query
  })
}

// 查询富文本内容详细
export function getRichContent(id) {
  return request({
    url: '/car/richContent/' + id,
    method: 'get'
  })
}

// 新增富文本内容
export function addRichContent(data) {
  return request({
    url: '/car/richContent',
    method: 'post',
    data: data
  })
}

// 修改富文本内容
export function updateRichContent(data) {
  return request({
    url: '/car/richContent',
    method: 'put',
    data: data
  })
}

// 删除富文本内容
export function delRichContent(id) {
  return request({
    url: '/car/richContent/' + id,
    method: 'delete'
  })
}

// 批量更新富文本内容排序
export function updateRichContentOrder(data) {
  return request({
    url: '/car/richContent/order',
    method: 'put',
    data: data
  })
}