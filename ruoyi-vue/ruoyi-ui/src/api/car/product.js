import request from '@/utils/request'

// 查詢商品列表
export function listProduct(query) {
  return request({
    url: '/car/product/list',
    method: 'get',
    params: query
  })
}

// 查詢商品詳細
export function getProduct(id) {
  return request({
    url: '/car/product/' + id,
    method: 'get'
  })
}

// 新增商品
export function addProduct(data) {
  return request({
    url: '/car/product',
    method: 'post',
    data: data
  })
}

// 修改商品
export function updateProduct(data) {
  return request({
    url: '/car/product',
    method: 'put',
    data: data
  })
}

// 刪除商品
export function delProduct(ids) {
  return request({
    url: '/car/product/' + ids,
    method: 'delete'
  })
}

// 上傳商品圖片
export function uploadProductImages(files, productId) {
  const formData = new FormData();
  for (let i = 0; i < files.length; i++) {
    formData.append('files', files[i]);
  }
  formData.append('productId', productId);
  return request({
    url: '/car/product/uploadImages',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 刪除商品圖片
export function deleteProductImage(imageId) {
  return request({
    url: '/car/product/image/' + imageId,
    method: 'delete'
  })
}

// 保存商品屬性
export function saveProductAttrs(productId, attrText) {
  return request({
    url: '/car/product/saveAttrs',
    method: 'post',
    params: {
      productId: productId,
      attrText: attrText
    }
  })
}

