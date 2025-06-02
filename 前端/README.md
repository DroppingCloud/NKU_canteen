# 南开大学食堂信息管理系统

本项目是基于Vue 3和Element Plus开发的南开大学食堂信息管理系统，提供食堂信息展示、档口查询、菜品浏览等功能，同时包含管理员后台系统用于管理食堂、档口和菜品信息。

## 技术栈

- 前端框架：Vue 3
- UI组件库：Element Plus
- 状态管理：Pinia
- 路由管理：Vue Router
- HTTP请求：Axios
- 构建工具：Vite

## 项目结构

```
project/
├── public/                 # 静态资源目录
├── src/                    # 源代码目录
│   ├── api/                # API接口目录
│   │   └── api.js          # API接口定义
│   ├── assets/             # 资源文件目录
│   │   ├── base.css        # 基础样式
│   │   └── main.css        # 主要样式
│   ├── components/         # 组件目录
│   │   ├── admin/          # 管理员组件
│   │   │   ├── AdminLayout.vue       # 管理页面布局
│   │   │   ├── CanteenManagement.vue # 食堂管理
│   │   │   ├── DishManagement.vue    # 菜品管理
│   │   │   └── StallManagement.vue   # 档口管理
│   │   ├── layout/         # 布局组件
│   │   │   ├── AppHeader.vue         # 应用头部
│   │   │   └── ModeSwitcher.vue      # 模式切换器
│   │   ├── DishDetail.vue  # 菜品详情组件
│   │   ├── LoginModal.vue  # 登录模态框
│   │   └── UserProfileCard.vue # 用户资料卡
│   ├── router/             # 路由目录
│   │   └── index.js        # 路由配置
│   ├── store/              # 状态管理目录
│   ├── views/              # 视图页面目录
│   │   ├── admin/          # 管理员页面
│   │   │   └── AdminHome.vue         # 管理员首页
│   │   ├── Booth.vue       # 档口详情页
│   │   ├── Canteen.vue     # 食堂详情页
│   │   └── Home.vue        # 首页
│   ├── App.vue             # 应用主组件
│   └── main.js             # 应用入口文件
├── index.html              # HTML模板
├── package.json            # 项目依赖配置
├── package-lock.json       # 依赖版本锁定
└── vite.config.js          # Vite配置文件
```

## 功能特性

1. **首页**：展示所有食堂概览信息
2. **食堂详情页**：展示特定食堂的详细信息和档口列表
3. **档口详情页**：展示特定档口的详细信息和菜品列表
4. **用户功能**：登录、查看个人资料
5. **管理员功能**：
   - 食堂管理：新增、编辑、删除食堂信息
   - 档口管理：新增、编辑、删除档口信息
   - 菜品管理：新增、编辑、删除菜品信息

## 启动项目

```bash
# 安装依赖
npm install

# 开发环境启动
npm run dev

# 构建生产版本
npm run build

# 预览生产版本
npm run preview
``` 