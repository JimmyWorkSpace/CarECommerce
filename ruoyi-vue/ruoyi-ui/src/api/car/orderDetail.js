import request from '@/utils/request'

// 查询订单详情列表
export function listOrderDetail(query) {
  return request({
    url: '/car/orderDetail/list',
    method: 'get',
    params: query
  })
}
