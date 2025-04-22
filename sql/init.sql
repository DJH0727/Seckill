-- 创建 seckill 数据库（如果没有创建的话）
CREATE DATABASE IF NOT EXISTS seckill DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 使用 seckill 数据库
USE seckill;

Drop TABLE IF EXISTS `user`;
-- 创建 user 表
CREATE TABLE `user` (
                        `id` BIGINT AUTO_INCREMENT PRIMARY KEY,    -- 用户唯一标识，自动增长
                        `name` VARCHAR(50) NOT NULL UNIQUE,         -- 用户名，不能为空，并且唯一
                        `password` VARCHAR(100) NOT NULL           -- 密码，不能为空
);
-- 创建 product 表
DROP TABLE IF EXISTS `product`;
CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
                         name VARCHAR(100) NOT NULL COMMENT '商品名称',
                         stock INT NOT NULL COMMENT '库存数量',
                         price DECIMAL(10,2) NOT NULL COMMENT '商品价格'
) COMMENT='商品表';
insert product (name, stock, price) values ('iPhone X', 10, 8999);
insert product (name, stock, price) values ('华为畅享7', 10, 6999);

-- 创建 order_info 表
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE order_info (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
                            user_id BIGINT NOT NULL COMMENT '用户ID',
                            goods_id BIGINT NOT NULL COMMENT '商品ID',
                            quantity INT NOT NULL DEFAULT 1 COMMENT '购买数量',
                            create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
