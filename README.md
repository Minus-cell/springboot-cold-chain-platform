# 智慧冷链物流系统 - 环境配置与启动指南

## 一、环境准备

### 1.1 必需软件清单

#### 后端开发（IDEA）

| 软件          | 版本要求  | 下载地址                                            | 用途         |
| ------------- | --------- | --------------------------------------------------- | ------------ |
| JDK           | 1.8 或 11 | https://www.oracle.com/java/technologies/downloads/ | Java运行环境 |
| Maven         | 3.8+      | https://maven.apache.org/download.cgi               | 依赖管理     |
| MySQL         | 8.0+      | https://dev.mysql.com/downloads/mysql/              | 数据库       |
| IntelliJ IDEA | 最新版    | https://www.jetbrains.com/idea/download/            | 后端IDE      |

#### 前端开发（VSCode）

| 软件    | 版本要求 | 下载地址                       | 用途             |
| ------- | -------- | ------------------------------ | ---------------- |
| Node.js | 16+ LTS  | https://nodejs.org/            | JavaScript运行时 |
| VSCode  | 最新版   | https://code.visualstudio.com/ | 前端IDE          |

### 1.2 环境验证

**验证Java是否安装成功：**

```bash
打开命令行（Windows按 Win+R，输入 cmd）

输入：
java -version

应该看到类似输出：
java version "1.8.0_xxx"
```

**验证Maven是否安装成功：**

```bash
mvn -version

应该看到类似输出：
Apache Maven 3.8.x
```

**验证Node.js是否安装成功：**

```bash
node -v
npm -v

应该看到版本号输出
```

------

## 二、数据库配置

### 2.1 MySQL安装与配置

#### 方法一：独立安装MySQL

1. 下载MySQL 8.0安装包
2. 安装时记住设置的root密码
3. 安装完成后启动MySQL服务

#### 方法二：使用集成环境（推荐新手）

下载phpStudy或WNMP：

- phpStudy：https://www.xp.cn/download.html
- 下载后启动MySQL服务即可

#### 方法二：使用Docker（如果你有Docker）

```bash
# 一行命令启动MySQL
docker run -d -p 3306:3306 --name mysql \
  -e MYSQL_ROOT_PASSWORD=root123 \
  mysql:8.0
```

### 2.2 创建数据库

**方法一：使用命令行**

```bash
mysql -u root -p
# 输入密码后执行：

CREATE DATABASE cold_chain_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
EXIT;
```

**方法二：使用Navicat（图形化工具）**

1. 下载Navicat：https://www.navicat.com/
2. 连接MySQL
3. 新建数据库 `cold_chain_db`
4. 右键选择"运行SQL文件"
5. 选择 `cold-chain-server/src/main/resources/db/sql/init.sql`

### 2.3 修改数据库连接配置

打开文件：

```
cold-chain-server/src/main/resources/application.yml
```

修改以下配置为你的实际数据库信息：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/cold_chain_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root              # ← 修改为你的用户名
    password: root123          # ← 修改为你的密码
```

------

## 三、后端项目启动（IDEA）

### 3.1 在IDEA中打开项目

1. 打开IntelliJ IDEA
2. File → Open
3. 选择 `d:\project\java\cold-chain-server` 文件夹
4. 点击 "Open as Project"
5. 等待Maven自动下载依赖（第一次需要几分钟）

### 3.2 配置Maven

如果IDEA没有自动识别Maven：

1. File → Settings → Build, Execution, Deployment → Build Tools → Maven
2. Maven home path：选择你的Maven安装目录
3. 确认后等待依赖下载完成

### 3.3 启动后端服务

**方式一：使用IDEA运行**

1. 找到入口文件：`ColdChainApplication.java`
2. 右键 → Run 'ColdChainApplication'
3. 控制台看到以下信息表示启动成功：

```
Started ColdChainApplication in x.xx seconds
```

**方式二：使用Maven命令**

```bash
cd d:\project\java\cold-chain-server
mvn spring-boot:run
```

### 3.4 验证后端启动

打开浏览器访问：

```
http://localhost:9090
```

应该能看到Spring Boot的默认页面或错误页面（正常）

------

## 四、前端项目启动（VSCode）

### 4.1 在VSCode中打开项目

1. 打开VSCode
2. File → Open Folder
3. 选择 `d:\project\java\admin-web`（先启动后台管理系统）

### 4.2 安装依赖

**在VSCode终端中执行：**

```bash
# Windows系统打开终端：Ctrl + `

cd d:\project\java\admin-web
npm install
```

等待安装完成（可能需要5-10分钟）

### 4.3 启动前端服务

```bash
npm run dev
```

应该看到类似输出：

```
DONE  Compiled successfully in xxxx ms

  Local:   http://localhost:8080/
  Network: http://192.168.x.x:8080/
```

### 4.4 打开浏览器访问

```
http://localhost:8080
```

应该能看到登录页面！

### 4.5 启动前台用户系统

1. 重新打开一个VSCode窗口
2. File → Open Folder
3. 选择 `d:\project\java\portal-web`
4. 执行 `npm install`
5. 执行 `npm run dev`

前台系统会运行在：

```
http://localhost:8081
```

------

## 五、项目运行完整流程

### 5.1 启动顺序

```
1. 启动MySQL数据库 ✅
   ↓
2. 启动后端服务（IDEA）✅
   - 地址：http://localhost:9090
   ↓
3. 启动后台管理系统（VSCode）✅
   - 地址：http://localhost:8080
   ↓
4. 启动前台用户系统（VSCode）✅
   - 地址：http://localhost:8081
```

### 5.2 测试账号

**后台管理系统：**

- 访问：http://localhost:8080
- 用户名：admin
- 密码：admin123

**前台用户系统：**

- 访问：http://localhost:8081
- 用户名：admin
- 密码：admin123

------

## 六、常见问题解决

### 6.1 Maven下载依赖失败

**问题：** 依赖下载很慢或失败

**解决：**

1. 配置Maven镜像源（推荐阿里云）
2. 找到 `cold-chain-server/pom.xml` 所在目录
3. 编辑或创建 `settings.xml` 文件：

```xml
<!-- Maven安装目录/conf/settings.xml 或 用户目录/.m2/settings.xml -->

<?xml version="1.0" encoding="UTF-8"?>
<settings>
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <name>Aliyun Maven</name>
      <url>https://maven.aliyun.com/repository/public</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

### 6.2 npm下载依赖失败

**问题：** npm install 很慢

**解决：**

```bash
# 使用淘宝镜像
npm config set registry https://registry.npmmirror.com

# 然后重新安装
npm install
```

### 6.3 端口被占用

**问题：** 启动时提示端口被占用

**解决：**

```bash
# 查找占用端口的进程
netstat -ano | findstr :9090
netstat -ano | findstr :8080
netstat -ano | findstr :8081

# 结束进程（PID替换为实际的进程ID）
taskkill /PID <PID> /F
```

或者修改端口：

- 后端：`application.yml` 中的 `server.port`
- 前端：`vue.config.js` 中的 `devServer.port`

### 6.4 数据库连接失败

**检查项：**

1. MySQL服务是否启动？
2. 用户名密码是否正确？
3. 数据库是否创建？
4. MySQL是否允许远程连接？

**修改MySQL允许远程连接：**

```sql
mysql -u root -p

USE mysql;
UPDATE user SET host='%' WHERE user='root';
FLUSH PRIVILEGES;
```

### 6.5 前端页面空白

**解决：**

1. 检查浏览器控制台（F12）是否有错误
2. 检查网络请求是否发送到后端
3. 确认后端是否启动成功
4. 检查API代理配置（`vue.config.js`）

------

## 七、项目结构说明

```
d:\project\java\
├── cold-chain-server/      # Spring Boot后端
│   ├── src/main/java/     # Java源代码
│   ├── src/main/resources/ # 配置文件
│   │   ├── application.yml       # 主配置
│   │   └── db/sql/init.sql       # 数据库脚本
│   └── pom.xml             # Maven依赖
│
├── admin-web/              # 后台管理系统（Vue）
│   ├── src/
│   │   ├── api/           # API接口
│   │   ├── views/         # 页面组件
│   │   ├── router/        # 路由配置
│   │   └── store/         # 状态管理
│   └── package.json       # npm依赖
│
└── portal-web/             # 前台用户系统（Vue）
    └── ...
```

------

## 八、快速检查清单

在启动项目前，确认以下内容：

- [ ] JDK已安装并配置环境变量
- [ ] Maven已安装并配置环境变量
- [ ] Node.js已安装并配置环境变量
- [ ] MySQL已安装并启动
- [ ] 已创建数据库 `cold_chain_db`
- [ ] 已运行 `init.sql` 初始化脚本
- [ ] 已修改 `application.yml` 中的数据库连接信息
- [ ] IDEA中已打开后端项目并下载完依赖
- [ ] VSCode中已打开前端项目并执行 `npm install`

------

## 九、技术支持

如果遇到问题：

1. 查看IDEA/VSCode的终端输出
2. 查看浏览器控制台（F12）
3. 检查数据库连接是否正常
4. 确认端口是否被占用