import request from '@/utils/request'

// 查询改制企业综合评价明细列表
export function listCompanyReportDetail(query) {
  return request({
    url: '/company/companyReportDetail/list',
    method: 'get',
    params: query
  })
}

// 查询改制企业综合评价明细详细
export function getCompanyReportDetail(id) {
  return request({
    url: '/company/companyReportDetail/' + id,
    method: 'get'
  })
}

// 新增改制企业综合评价明细
export function addCompanyReportDetail(data) {
  return request({
    url: '/company/companyReportDetail',
    method: 'post',
    data: data
  })
}

// 修改改制企业综合评价明细
export function updateCompanyReportDetail(data) {
  return request({
    url: '/company/companyReportDetail',
    method: 'put',
    data: data
  })
}

// 删除改制企业综合评价明细
export function delCompanyReportDetail(id) {
  return request({
    url: '/company/companyReportDetail/' + id,
    method: 'delete'
  })
}

// 导出改制企业综合评价明细
export function exportCompanyReportDetail(query) {
  return request({
    url: '/company/companyReportDetail/export',
    method: 'get',
    params: query
  })
}
