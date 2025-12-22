import request from '@/utils/request'

// 查詢菜單維護列表（樹狀結構）
export function listMenu(query) {
  return request({
    url: '/car/menu/list',
    method: 'get',
    params: query
  })
}

// 查詢父菜單列表（用於下拉選擇）
export function listParentMenu() {
  return request({
    url: '/car/menu/parentList',
    method: 'get'
  })
}

// 查詢菜單維護詳細
export function getMenu(id) {
  return request({
    url: '/car/menu/' + id,
    method: 'get'
  })
}

// 新增菜單維護
export function addMenu(data) {
  return request({
    url: '/car/menu',
    method: 'post',
    data: data
  })
}

// 修改菜單維護
export function updateMenu(data) {
  return request({
    url: '/car/menu',
    method: 'put',
    data: data
  })
}

// 刪除菜單維護
export function delMenu(ids) {
  return request({
    url: '/car/menu/' + ids,
    method: 'delete'
  })
}

// 更新菜單顯示狀態
export function updateMenuShowStatus(data) {
  return request({
    url: '/car/menu/updateShowStatus',
    method: 'put',
    data: data
  })
}

// 更新菜單排序
export function updateMenuOrder(data) {
  return request({
    url: '/car/menu/updateOrder',
    method: 'put',
    data: data
  })
}

// 批量更新菜單排序
export function batchUpdateMenuOrder(data) {
  return request({
    url: '/car/menu/batchUpdateOrder',
    method: 'put',
    data: data
  })
}
