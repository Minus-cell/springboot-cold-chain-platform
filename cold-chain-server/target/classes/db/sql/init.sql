-- 智慧冷链物流系统数据库初始化脚本
-- 数据库名称：cold_chain_db

CREATE DATABASE IF NOT EXISTS cold_chain_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE cold_chain_db;

-- ----------------------------
-- 1. 用户表
-- ----------------------------
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    status TINYINT(1) DEFAULT 1 COMMENT '状态（0禁用 1启用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- 2. 角色表
-- ----------------------------
DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(200) DEFAULT NULL COMMENT '描述',
    status TINYINT(1) DEFAULT 1 COMMENT '状态（0禁用 1启用）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- 3. 用户角色表
-- ----------------------------
DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT(20) NOT NULL COMMENT '用户ID',
    role_id BIGINT(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- ----------------------------
-- 4. 权限表
-- ----------------------------
DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    parent_id BIGINT(20) DEFAULT 0 COMMENT '父权限ID',
    name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_key VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    path VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
    component VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
    icon VARCHAR(50) DEFAULT NULL COMMENT '图标',
    sort INT(11) DEFAULT 0 COMMENT '排序',
    type TINYINT(1) DEFAULT 1 COMMENT '类型（1菜单 2按钮）',
    status TINYINT(1) DEFAULT 1 COMMENT '状态（0禁用 1启用）',
    PRIMARY KEY (id),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- ----------------------------
-- 5. 角色权限表
-- ----------------------------
DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    role_id BIGINT(20) NOT NULL COMMENT '角色ID',
    permission_id BIGINT(20) NOT NULL COMMENT '权限ID',
    PRIMARY KEY (id),
    KEY idx_role_id (role_id),
    KEY idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限表';

-- ----------------------------
-- 6. 运单表
-- ----------------------------
DROP TABLE IF EXISTS order_info;
CREATE TABLE order_info (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    sender_name VARCHAR(50) NOT NULL COMMENT '寄件人姓名',
    sender_phone VARCHAR(20) NOT NULL COMMENT '寄件人电话',
    sender_address VARCHAR(200) NOT NULL COMMENT '寄件人地址',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收件人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收件人电话',
    receiver_address VARCHAR(200) NOT NULL COMMENT '收件人地址',
    goods_name VARCHAR(100) NOT NULL COMMENT '货物名称',
    goods_weight DECIMAL(10,2) DEFAULT NULL COMMENT '货物重量(kg)',
    goods_volume DECIMAL(10,2) DEFAULT NULL COMMENT '货物体积(m³)',
    temperature_requirement VARCHAR(50) DEFAULT NULL COMMENT '温度要求',
    order_status TINYINT(1) DEFAULT 0 COMMENT '订单状态（0待取件 1运输中 2派送中 3已签收 4已取消）',
    vehicle_id BIGINT(20) DEFAULT NULL COMMENT '车辆ID',
    user_id BIGINT(20) DEFAULT NULL COMMENT '用户ID（前台用户）',
    total_price DECIMAL(10,2) DEFAULT NULL COMMENT '总价',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_order_status (order_status),
    KEY idx_order_user (user_id),
    KEY idx_order_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='运单表';

-- ----------------------------
-- 7. 车辆表
-- ----------------------------
DROP TABLE IF EXISTS vehicle_info;
CREATE TABLE vehicle_info (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    vehicle_no VARCHAR(20) NOT NULL COMMENT '车牌号',
    vehicle_type VARCHAR(50) DEFAULT NULL COMMENT '车辆类型',
    driver_name VARCHAR(50) DEFAULT NULL COMMENT '司机姓名',
    driver_phone VARCHAR(20) DEFAULT NULL COMMENT '司机电话',
    temperature_range VARCHAR(50) DEFAULT NULL COMMENT '温度范围',
    max_load DECIMAL(10,2) DEFAULT NULL COMMENT '最大载重(kg)',
    status TINYINT(1) DEFAULT 0 COMMENT '状态（0空闲 1运输中 2维护中）',
    current_lat DECIMAL(10,6) DEFAULT NULL COMMENT '当前纬度',
    current_lng DECIMAL(10,6) DEFAULT NULL COMMENT '当前经度',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_vehicle_no (vehicle_no),
    KEY idx_vehicle_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车辆表';

-- ----------------------------
-- 8. 温度记录表
-- ----------------------------
DROP TABLE IF EXISTS temperature_record;
CREATE TABLE temperature_record (
    id BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT(20) DEFAULT NULL COMMENT '运单ID',
    vehicle_id BIGINT(20) DEFAULT NULL COMMENT '车辆ID',
    temperature DECIMAL(5,2) NOT NULL COMMENT '温度值(℃)',
    humidity DECIMAL(5,2) DEFAULT NULL COMMENT '湿度值(%)',
    location VARCHAR(200) DEFAULT NULL COMMENT '位置描述',
    record_time DATETIME NOT NULL COMMENT '记录时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_temp_order (order_id),
    KEY idx_temp_vehicle (vehicle_id),
    KEY idx_temp_record_time (record_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='温度记录表';

-- ----------------------------
-- 初始化数据
-- ----------------------------

-- 插入管理员用户 (密码: admin123，使用MD5加密)
INSERT INTO sys_user (username, password, real_name, phone, email, status) VALUES
('admin', '0192023a34f8bdb1ba7ab6c7d4a4f9c5', '系统管理员', '13800138000', 'admin@coldchain.com', 1),
('operator', 'e10adc3949ba59abbe56e057f20f883e', '调度员', '13800138001', 'operator@coldchain.com', 1);

-- 插入角色
INSERT INTO sys_role (role_name, role_code, description, status) VALUES
('超级管理员', 'ROLE_ADMIN', '系统超级管理员，拥有所有权限', 1),
('运营调度员', 'ROLE_OPERATOR', '负责运单调度和车辆管理', 1),
('客服人员', 'ROLE_CUSTOMER', '负责客户服务和订单跟进', 1);

-- 插入用户角色关系
INSERT INTO sys_user_role (user_id, role_id) VALUES
(1, 1),
(2, 2);

-- 插入权限菜单
INSERT INTO sys_permission (parent_id, name, permission_key, path, component, icon, sort, type, status) VALUES
(0, '仪表盘', 'dashboard', '/dashboard', 'dashboard/index', 'el-icon-data-line', 1, 1, 1),
(0, '运单管理', 'order', '/order', 'order/index', 'el-icon-document', 2, 1, 1),
(0, '车辆管理', 'vehicle', '/vehicle', 'vehicle/index', 'el-icon-truck', 3, 1, 1),
(0, '温控管理', 'temperature', '/temperature', 'temperature/index', 'el-icon-thermometer', 4, 1, 1),
(0, '系统管理', 'system', '/system', 'system/index', 'el-icon-setting', 5, 1, 1),
(0, '用户管理', 'system:user', '/system/user', 'system/user/index', 'el-icon-user', 51, 1, 1),
(0, '角色管理', 'system:role', '/system/role', 'system/role/index', 'el-icon-postcard', 52, 1, 1),
(0, '权限管理', 'system:permission', '/system/permission', 'system/permission/index', 'el-icon-key', 53, 1, 1);

-- 插入车辆数据
INSERT INTO vehicle_info (vehicle_no, vehicle_type, driver_name, driver_phone, temperature_range, max_load, status, current_lat, current_lng) VALUES
('京A12345', '冷藏车', '张三', '13900001111', '-18~-25℃', 5000.00, 1, 39.904200', 116.407400),
('京B67890', '冷藏车', '李四', '13900002222', '-18~-25℃', 5000.00, 0, 39.914200', 116.417400),
('京C11111', '保温车', '王五', '13900003333', '2~8℃', 3000.00, 0, 39.924200', 116.427400),
('京D22222', '冷藏车', '赵六', '13900004444', '-18~-25℃', 5000.00, 1, 39.934200', 116.437400);

-- 插入运单数据
INSERT INTO order_info (order_no, sender_name, sender_phone, sender_address, receiver_name, receiver_phone, receiver_address, goods_name, goods_weight, goods_volume, temperature_requirement, order_status, vehicle_id, user_id, total_price) VALUES
('DD202401010001', '北京食品公司', '010-12345678', '北京市朝阳区某某路1号', '上海超市', '021-87654321', '上海市浦东新区某某街2号', '冷冻猪肉', 500.00, 2.50, '-18℃以下', 1, 1, 1, 1500.00),
('DD202401010002', '杭州医药公司', '0571-11111111', '杭州市西湖区某某路3号', '南京医院', '025-22222222', '南京市鼓楼区某某街4号', '冷藏药品', 100.00, 0.50, '2~8℃', 2, 2, 1, 800.00),
('DD202401010003', '广州海鲜市场', '020-33333333', '广州市天河区某某路5号', '深圳酒楼', '0755-44444444', '深圳市南山区某某街6号', '新鲜海鲜', 200.00, 1.00, '0~5℃', 1, 1, 1, 600.00);

-- 插入温度记录
INSERT INTO temperature_record (order_id, vehicle_id, temperature, humidity, location, record_time) VALUES
(1, 1, -20.5, 85.0, '北京市朝阳区', '2024-01-01 08:00:00'),
(1, 1, -21.0, 84.5, '天津市', '2024-01-01 10:00:00'),
(1, 1, -20.8, 85.2, '河北省沧州市', '2024-01-01 12:00:00'),
(2, 2, 4.5, 70.0, '杭州市西湖区', '2024-01-01 08:00:00'),
(2, 2, 5.0, 69.5, '湖州市', '2024-01-01 10:00:00'),
(3, 1, -19.0, 86.0, '广州市天河区', '2024-01-01 08:00:00'),
(3, 1, -18.5, 85.8, '深圳市宝安区', '2024-01-01 10:00:00');
