import request from '@/utils/request'

// 查詢商品目錄分類列表
export function listProductCategory(query) {
  return request({
    url: '/car/productCategory/list',
    method: 'get',
    params: query
  })
}

// 查詢商品目錄分類詳細
export function getProductCategory(id) {
  return request({
    url: '/car/productCategory/' + id,
    method: 'get'
  })
}

// 查詢商品目錄分類下拉樹結構
export function treeselect() {
  return request({
    url: '/car/productCategory/treeselect',
    method: 'get'
  })
}

// 根據角色ID查詢商品目錄分類樹結構
export function roleCategoryTreeselect(roleId) {
  return request({
    url: '/car/productCategory/roleCategoryTreeselect/' + roleId,
    method: 'get'
  })
}

// 新增商品目錄分類
export function addProductCategory(data) {
  return request({
    url: '/car/productCategory',
    method: 'post',
    data: data
  })
}

// 修改商品目錄分類
export function updateProductCategory(data) {
  return request({
    url: '/car/productCategory',
    method: 'put',
    data: data
  })
}

// 刪除商品目錄分類
export function delProductCategory(ids) {
  return request({
    url: '/car/productCategory/' + ids,
    method: 'delete'
  })
}

