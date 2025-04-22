


| 服务           | 版本     | 
|--------------|--------|
| **Redis**    | 6.0.16 | 
| **MySQL**    | 8.0.41 | 
| **RabbitMQ** | 3.9.27 | 


在logback.xml更改日志输出级别。

访问 http://localhost:8080/swagger-ui/index.html#/
或 http://localhost:8080/api #自定义，在application.yml中配置。





## 安装 MySQL
```bash
sudo apt-get update # 更新软件源
sudo apt-get install mysql-server # 安装mysql
sudo systemctl start mysql  # 启动 MySQL 服务
sudo systemctl enable mysql  # 设置 MySQL 开机自启动
sudo systemctl status mysql  # 查看 MySQL 服务状态
sudo mysql -u root -p # 输入密码登录mysql
```
### 配置 MySQL 远程访问
```bash
sudo nano /etc/mysql/mysql.conf.d/mysqld.cnf  # 打开 MySQL 配置文件
# 找到bind-address = 127.0.0.1 修改为 bind-address = 0.0.0.0 或 直接注释 或 特定 IP
sudo systemctl restart mysql  # 重启 MySQL 服务
```
```sql
CREATE USER 'username'@'%' IDENTIFIED BY 'password';  # 创建远程用户
GRANT ALL PRIVILEGES ON *.* TO 'username'@'%' WITH GRANT OPTION;  # 授权远程访问
FLUSH PRIVILEGES;  # 刷新权限
```
***云服务器需要在安全组放行3306端口***

## 安装 Redis
```bash
sudo apt-get update # 更新软件源
sudo apt install redis-server  # 安装 Redis 服务
sudo systemctl start redis-server  # 启动 Redis 服务
sudo systemctl enable redis-server  # 设置 Redis 开机自启动
sudo systemctl status redis-server  # 查看 Redis 服务状态
```
### 配置 Redis 远程访问
```bash
sudo nano /etc/redis/redis.conf  # 打开 Redis 配置文件
# 找到 bind 127.0.0.1 修改为 bind 0.0.0.0 或 改为特定 IP
# 找到protected-mode yes 修改为 protected-mode no
# 可以找到被注释掉的 requirepass foobared 取消注释并设置密码 requirepass password (可选)
sudo systemctl restart redis-server  # 重启 Redis 服务
```
***推荐下载Redis Insight 进行 Redis 监控和管理。***

***云服务器需要在安全组放行6379端口***

## 安装 RabbitMQ
```bash
sudo apt-get update # 更新软件源
sudo apt install erlang  # 安装 Erlang，RabbitMQ 依赖于 Erlang
sudo apt install rabbitmq-server  # 安装 RabbitMQ 服务
sudo systemctl start rabbitmq-server  # 启动 RabbitMQ 服务
sudo systemctl enable rabbitmq-server  # 设置 RabbitMQ 开机自启动
sudo systemctl status rabbitmq-server  # 查看 RabbitMQ 服务状态
sudo rabbitmq-plugins enable rabbitmq_management  # 启用 RabbitMQ Web 管理插件（可选，推荐）
```
### 创建 RabbitMQ 用户
```bash
sudo rabbitmqctl add_user username password  # 添加用户
sudo rabbitmqctl set_user_tags username administrator  # 设置用户为管理员
sudo rabbitmqctl set_permissions -p / user ".*" ".*" ".*"  # 设置该用户的权限
```

***通过 http://rabbitmq_host:15672/ 访问RabbitMQ管理界面***

***云服务器需要在安全组放行5672和15672(web管理界面)端口***