import request from '@/utils/request'

// 查询广告列表
export function listAdvertisement(query) {
  return request({
    url: '/car/advertisement/list',
    method: 'get',
    params: query
  })
}

// 查询广告详细
export function getAdvertisement(id) {
  return request({
    url: '/car/advertisement/' + id,
    method: 'get'
  })
}

// 新增广告
export function addAdvertisement(data) {
  return request({
    url: '/car/advertisement',
    method: 'post',
    data: data
  })
}

// 修改广告
export function updateAdvertisement(data) {
  return request({
    url: '/car/advertisement',
    method: 'put',
    data: data
  })
}

// 删除广告
export function delAdvertisement(id) {
  return request({
    url: '/car/advertisement/' + id,
    method: 'delete'
  })
}

// 导出广告
export function exportAdvertisement(query) {
  return request({
    url: '/car/advertisement/export',
    method: 'post',
    params: query
  })
}

// 上传图片
export function uploadImage(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/car/advertisement/upload',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 更新广告排序
export function updateAdvertisementOrder(data) {
  return request({
    url: '/car/advertisement/order',
    method: 'put',
    data: data
  })
} 