import request from '@/utils/request'

// 查询精选好车列表
export function listSalesRecommend(query) {
  return request({
    url: '/car/salesRecommend/list',
    method: 'get',
    params: query
  })
}

// 设置车辆推荐状态
export function setSalesRecommended(data) {
  return request({
    url: '/car/salesRecommend/setRecommended',
    method: 'put',
    data: data
  })
}

// 更新车辆销售的后台审核结果
export function updateAdminCheck(data) {
  return request({
    url: '/car/salesRecommend/updateAdminCheck',
    method: 'put',
    data: data
  })
}