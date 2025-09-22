import request from '@/utils/request'

// 獲取關於內容
export function getAbout() {
  return request({
    url: '/car/about',
    method: 'get'
  })
}

// 修改關於內容
export function updateAbout(data) {
  return request({
    url: '/car/about',
    method: 'put',
    data: data
  })
}
