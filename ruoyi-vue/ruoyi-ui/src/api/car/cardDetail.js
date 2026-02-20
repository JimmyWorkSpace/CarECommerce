import request from '@/utils/request'

// 券碼列表（支援券碼模糊搜尋、分頁）
export function listCardDetail(query) {
  return request({
    url: '/car/cardDetail/list',
    method: 'get',
    params: query
  })
}

// 票券明細詳情（含所屬用戶用戶名、手機號）
export function getCardDetail(id) {
  return request({
    url: '/car/cardDetail/detail/' + id,
    method: 'get'
  })
}

// 核銷（傳 ticketCode）
export function redeemCardDetail(ticketCode) {
  return request({
    url: '/car/cardDetail/redeem',
    method: 'post',
    data: { ticketCode }
  })
}
