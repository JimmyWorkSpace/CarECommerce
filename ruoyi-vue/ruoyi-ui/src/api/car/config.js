import request from '@/utils/request'

// 查询网站配置列表
export function listConfig(query) {
  return request({
    url: '/car/config/list',
    method: 'get',
    params: query
  })
}

// 查询网站配置详细
export function getConfig(id) {
  return request({
    url: '/car/config/' + id,
    method: 'get'
  })
}

// 修改网站配置
export function updateConfig(data) {
  return request({
    url: '/car/config',
    method: 'put',
    data: data
  })
}
