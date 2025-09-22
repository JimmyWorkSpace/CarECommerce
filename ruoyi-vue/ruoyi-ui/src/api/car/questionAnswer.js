import request from '@/utils/request'

// 查询问答模块列表
export function listQuestionAnswer(query) {
  return request({
    url: '/car/questionAnswer/list',
    method: 'get',
    params: query
  })
}

// 查询问答模块详细
export function getQuestionAnswer(id) {
  return request({
    url: '/car/questionAnswer/' + id,
    method: 'get'
  })
}

// 新增问答模块
export function addQuestionAnswer(data) {
  return request({
    url: '/car/questionAnswer',
    method: 'post',
    data: data
  })
}

// 修改问答模块
export function updateQuestionAnswer(data) {
  return request({
    url: '/car/questionAnswer',
    method: 'put',
    data: data
  })
}

// 删除问答模块
export function delQuestionAnswer(id) {
  return request({
    url: '/car/questionAnswer/' + id,
    method: 'delete'
  })
}