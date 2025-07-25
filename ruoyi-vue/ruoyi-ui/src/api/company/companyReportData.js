import request from '@/utils/request'

// 查询公司月报详细列表
export function listCompanyReportData(query) {
  return request({
    url: '/company/companyReportData/list',
    method: 'get',
    params: query
  })
}

// 查询公司月报详细详细
export function getCompanyReportData(id) {
  return request({
    url: '/company/companyReportData/' + id,
    method: 'get'
  })
}

// 新增公司月报详细
export function addCompanyReportData(data) {
  return request({
    url: '/company/companyReportData',
    method: 'post',
    data: data
  })
}

// 修改公司月报详细
export function updateCompanyReportData(data) {
  return request({
    url: '/company/companyReportData',
    method: 'put',
    data: data
  })
}

// 删除公司月报详细
export function delCompanyReportData(id) {
  return request({
    url: '/company/companyReportData/' + id,
    method: 'delete'
  })
}

// 导出公司月报详细
export function exportCompanyReportData(query) {
  return request({
    url: '/company/companyReportData/export',
    method: 'get',
    params: query
  })
}

// 导出公司月报详细
export function exportCompanyReportDataModal() {
  return request({
    url: '/company/companyReportData/exportModel',
    method: 'get'
  })
}

// 导入公司月报详细
export function importCompanyReportData(query) {
  return request({
    url: '/company/companyReportData/importData',
    method: 'post',
    params: query
  })
}