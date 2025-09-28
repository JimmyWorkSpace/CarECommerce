# 网站配置功能测试

## 功能概述
已成功实现网站配置表的内容读取和映射功能，并将配置内容应用到网站页脚。

## 实现的功能

### 1. 数据库表结构
- 创建了 `car_config` 表，包含以下字段：
  - `id`: 主键ID
  - `code`: 配置代码（唯一）
  - `value`: 配置值
  - `name`: 配置名称
  - `showOrder`: 显示顺序
  - `delFlag`: 删除标记
  - `createTime`: 创建时间

### 2. 实体类和DTO
- `CarConfigEntity`: 网站配置实体类
- `CarConfigContent`: 配置内容DTO，包含以下字段：
  - `kefu`: 客服热线
  - `youxiang`: 邮箱地址
  - `dizhi`: 公司地址
  - `fwsj1`: 服务时间1
  - `fwsj2`: 服务时间2
  - `fwsj3`: 服务时间3

### 3. 服务层
- `CarConfigService`: 配置服务类
  - `getAllConfigs()`: 获取所有配置项
  - `getConfigValue(String code)`: 根据代码获取配置值
  - `getConfigContent()`: 获取配置内容并映射到CarConfigContent对象（带缓存）

### 4. 控制器层
- `CarConfigController`: 配置API控制器
  - `GET /api/config/list`: 获取所有配置项
  - `GET /api/config/value/{code}`: 根据代码获取配置值
  - `GET /api/config/content`: 获取网站配置内容

### 5. 页面集成
- 修改了 `CarViewController`，在所有页面方法中添加了配置内容获取
- 创建了通用页脚组件 `/layout/footer.ftl`
- 修改了主布局模板，包含通用页脚
- 页脚内容现在从数据库配置表动态读取

## 测试数据
已插入以下测试配置数据：
- 客服热线: 400-123-4567
- 邮箱: service@carce.cc
- 地址: 台北市信義區信義路五段7號
- 服务时间1: 週一至週五：9:00-18:00
- 服务时间2: 週六至週日：10:00-17:00
- 服务时间3: 節假日：10:00-16:00

## API测试
可以通过以下API测试配置功能：
1. `GET /api/config/content` - 获取配置内容
2. `GET /api/config/list` - 获取所有配置项
3. `GET /api/config/value/kefu` - 获取客服热线

## 页面效果
网站页脚现在会动态显示从数据库配置表读取的联系信息和服务时间，支持实时更新配置而无需修改代码。
