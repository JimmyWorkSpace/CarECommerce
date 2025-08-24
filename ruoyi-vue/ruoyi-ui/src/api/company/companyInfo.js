import request from '@/utils/request'

// 查询公司信息列表
export function listCompanyInfo(query) {
  return request({
    url: '/company/companyInfo/list',
    method: 'get',
    params: query
  })
}

// 查询公司信息详细
export function getCompanyInfo(id) {
  return request({
    url: '/company/companyInfo/' + id,
    method: 'get'
  })
}

// 新增公司信息
export function addCompanyInfo(data) {
  return request({
    url: '/company/companyInfo',
    method: 'post',
    data: data
  })
}

// 修改公司信息
export function updateCompanyInfo(data) {
  return request({
    url: '/company/companyInfo',
    method: 'put',
    data: data
  })
}

// 删除公司信息
export function delCompanyInfo(id) {
  return request({
    url: '/company/companyInfo/' + id,
    method: 'delete'
  })
}

// 导出公司信息
export function exportCompanyInfo(query) {
  return request({
    url: '/company/companyInfo/export',
    method: 'get',
    params: query
  })
}

// 导出公司信息
export function getCompanySearchOptions() {
  return request({
    url: '/company/companyInfo/getCompanySearchOptions',
    method: 'get'
  })
}