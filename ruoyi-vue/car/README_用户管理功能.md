# 用户管理功能

## 功能概述
基于CarUserEntity实体类生成的完整用户管理维护功能，包括后端API和前端管理界面。

## 生成的文件

### 后端文件
1. **Controller层**
   - `car/src/main/java/com/ruoyi/car/controller/CarUserController.java`
   - 提供RESTful API接口，包括增删改查和导出功能

2. **Service层**
   - `car/src/main/java/com/ruoyi/car/service/ICarUserService.java` - 服务接口
   - `car/src/main/java/com/ruoyi/car/service/impl/CarUserServiceImpl.java` - 服务实现

3. **Mapper层**
   - 使用现有的 `CarUserMapper`，继承tk.mybatis的Mapper接口
   - 采用Example查询方式，避免编写复杂的XML SQL

### 前端文件
1. **Vue页面**
   - `ruoyi-ui/src/views/car/user/index.vue` - 用户管理主页面

2. **API接口**
   - `ruoyi-ui/src/api/car/user.js` - 前端API调用接口

### 数据库文件
1. **菜单SQL**
   - `car/car_user_menu.sql` - 菜单权限配置SQL

## 功能特性

### 数据字段
- **id**: 主键ID (自增)
- **phoneNumber**: 手机号 (必填，唯一)
- **nickName**: 昵称
- **delFlag**: 删除标记 (默认false)
- **createTime**: 创建时间 (自动设置)
- **lastLoginTime**: 上次登录时间

### 主要功能
1. **查询功能**
   - 支持按手机号模糊查询
   - 支持按昵称模糊查询
   - 支持按删除标记筛选
   - 分页显示

2. **增删改功能**
   - 新增用户 (自动设置创建时间和删除标记)
   - 修改用户信息
   - 逻辑删除 (设置delFlag为true)
   - 批量删除

3. **导出功能**
   - 支持Excel导出用户数据

4. **权限控制**
   - 基于若依框架的权限控制
   - 菜单权限：car:user:list
   - 操作权限：query, add, edit, remove, export

## 技术特点

### 后端技术
- 使用tk.mybatis的Example查询，避免编写XML SQL
- 逻辑删除实现，数据安全
- 自动时间戳设置
- 完整的异常处理和日志记录

### 前端技术
- 基于Element UI的现代化界面
- 响应式设计，支持移动端
- 表单验证和用户友好提示
- 权限按钮控制

## 部署说明

1. **执行菜单SQL**
   ```sql
   -- 执行 car/car_user_menu.sql 文件
   source car/car_user_menu.sql;
   ```

2. **重启应用**
   - 重启后端服务
   - 刷新前端页面

3. **权限配置**
   - 为相应角色分配用户管理权限
   - 菜单路径：系统管理 -> 汽车管理 -> 用户管理

## 使用说明

1. **访问路径**
   - 前端：`/car/user`
   - 后端API：`/car/user/*`

2. **操作流程**
   - 查询：在搜索框输入条件，点击搜索
   - 新增：点击新增按钮，填写表单提交
   - 修改：点击表格中的修改按钮，编辑后提交
   - 删除：选择记录后点击删除按钮
   - 导出：点击导出按钮下载Excel文件

## 注意事项

1. **数据安全**
   - 删除操作为逻辑删除，不会物理删除数据
   - 手机号具有唯一性约束

2. **性能优化**
   - 使用分页查询，避免大数据量问题
   - 索引优化：建议为phoneNumber字段创建索引

3. **扩展性**
   - 可根据业务需要添加更多查询条件
   - 支持自定义字段验证规则
