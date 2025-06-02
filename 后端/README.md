# 南开大学食堂管理系统

## 项目简介
本系统是基于SpringBoot + MyBatis + PageHelper技术栈开发的南开大学食堂管理系统，实现了用户、食堂、档口、菜品、评论、点赞、评分等功能。

## 目录结构

```
canteen-management-system/
├── docs/                       # 文档目录
│   ├── DataBase.sql            # 数据库结构脚本
│   ├── Document.md             # API接口文档
│   └── 项目结构说明.md          # 项目结构说明文档
├── scripts/                    # 脚本文件目录
│   ├── entrypoint.sh           # 入口脚本
│   └── start.sh                # 启动脚本
├── src/                        # 源代码目录
│   └── main/
│       ├── java/com/nku/canteen/
│       │   ├── CanteenApplication.java    # 应用程序入口
│       │   ├── config/                    # 配置类目录
│       │   ├── controller/                # 控制器目录
│       │   ├── dto/                       # 数据传输对象目录
│       │   ├── entity/                    # 实体类目录
│       │   ├── exception/                 # 异常处理目录
│       │   ├── mapper/                    # MyBatis映射接口目录
│       │   ├── service/                   # 服务接口及实现目录
│       │   └── util/                      # 工具类目录
│       └── resources/
│           ├── application.yml            # 应用配置文件
│           ├── application-dev.yml        # 开发环境配置
│           ├── application-prod.yml       # 生产环境配置
│           └── mapper/                    # MyBatis XML映射文件目录
├── .gitignore                  # Git忽略文件
├── pom.xml                     # Maven项目配置文件
├── mvnw                        # Maven包装器脚本(Unix/Linux)
└── mvnw.cmd                    # Maven包装器脚本(Windows)
```

## 技术栈
- SpringBoot 3.x
- MyBatis
- PageHelper
- MySQL 8.0
- JWT (用于身份验证)

## 项目功能
1. 用户管理：注册、登录、个人信息管理
2. 食堂管理：食堂信息展示、食堂档口管理
3. 菜品管理：菜品展示、搜索、详情查看
4. 评价系统：用户评分、评论、点赞
5. 权限管理：普通用户和管理员权限分离

## 启动方式
1. 确保已安装JDK 17+，MySQL 8.0+，Maven 3.8+
2. 导入`docs/DataBase.sql`创建数据库
3. 配置`src/main/resources/application.yml`中的数据库连接信息
4. 执行以下命令启动项目：
   ```
   ./mvnw spring-boot:run
   ```
   或使用脚本：
   ```
   ./scripts/start.sh
   ```

## 接口文档
详细的API接口说明请参考`docs/Document.md`文件。
