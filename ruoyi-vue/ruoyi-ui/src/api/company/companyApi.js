import request from '@/utils/request'

// 导出公司信息
export function getCompanySearchOptions() {
  return request({
    url: '/api/report/getCompanySearchOptions',
    method: 'get'
  })
}

// 
export function getLastestYearData(query) {
  return request({
    url: '/api/report/getLastestYearData',
    method: 'post',
    data: query
  })
}

// 
export function getCompanyReportData(query) {
  return request({
    url: '/api/report/getCompanyReportData',
    method: 'post',
    data: query
  })
}

// 
export function getValidYears(includeMiddleYear) {
  return request({
    url: '/api/report/getValidYears',
    method: 'get',
    params: {
      includeMiddleYear
    }
  })
}

// 
export function getDetailLineChart(query) {
  return request({
    url: '/api/report/getDetailLineChart',
    method: 'post',
    data: query
  })
}

// 
export function getDataLineChart(query) {
  return request({
    url: '/api/report/getDataLineChart',
    method: 'post',
    data: query
  })
}

// 
export function getSandian(query) {
  return request({
    url: '/api/report/sandian',
    method: 'post',
    data: query
  })
}


export function getCompanyReportDataWithInc(data) {
  return request({
    url: '/api/report/getCompanyReportDataWithInc',
    method: 'post',
    data
  })
}

// 
export function getCompanyDetailDataWithInc(data) {
  return request({
    url: '/api/report/getCompanyDetailDataWithInc',
    method: 'post',
    data
  })
}

// 
export function overview(query) {
  return request({
    url: '/api/report/overview',
    method: 'get',
    params: query
  })
}

// 
export function getDetailDataByCompany(companyId) {
  return request({
    url: '/api/report/getDetailDataByCompany',
    method: 'get',
    params: { companyId: companyId }
  })
}

// 
export function getReportDataByCompany(companyId) {
  return request({
    url: '/api/report/getReportDataByCompany',
    method: 'get',
    params: { companyId: companyId }
  })
}

export function colors(){
  return request({
    url: '/api/report/colors',
    method: 'get',
  })
}

export function cellStyle(r, levelColors) {
  let row = r.row;
  let col = r.column;
  if (!col || !col.property) {
    return;
  }
  let value = row[col.property];
  let value2 = row[col.property + '2'];
  let inc = row[col.property + 'Inc'];

  if (col.property.endsWith('Inc')) {
    inc = row[col.property];
  }
  let font = 'font-weight: bold;color: #ffffff';
  if (inc != null) {
    let c = null;
    // if(inc > 30 || inc < -30){
    //   debugger;
    // }
    for(let idx in levelColors){
      let lc = levelColors[idx];
      if((lc.type == 1 && inc > lc.level) || (lc.type == 2 && inc < lc.level)){
        c = lc.color;
        break;
      }
    }
    for(let idx in levelColors){
      let lc = levelColors[idx];
      if(lc.type == 0 && lc.level == inc){
        c = lc.color;
        break;
      }
    }
    if(c != null){
      return `background-color: ${c};` + font;
    }else{
      return '';
    }
  }
}