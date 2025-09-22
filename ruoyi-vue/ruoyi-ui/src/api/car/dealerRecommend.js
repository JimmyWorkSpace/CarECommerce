import request from '@/utils/request'

// 查询精选卖家列表
export function listDealerRecommend(query) {
  return request({
    url: '/car/dealerRecommend/list',
    method: 'get',
    params: query
  })
}

// 设置经销商推荐状态
export function setDealerRecommended(data) {
  return request({
    url: '/car/dealerRecommend/setRecommended',
    method: 'put',
    data: data
  })
}
