# CatMessenger / 小猫信使

**这是V3版本！**

基于 RabbitMQ 的多 MC 服务器和 Telegram 群的消息传递软件。

## 许可证

Server Side Public License (SSPL) Version 1

## 安装和使用

**请不要照抄本文档，你需要对其做一些基本的配置。**

### 前置条件

1. RabbitMQ
2. Java Runtime

#### RabbitMQ

这是一个开源的消息队列工具，你可以使用docker来创建一个服务器。为了配合本项目你需要为它设置username和password。

```shell
docker run --rm --hostname my-rabbit --name some-rabbit -p 5672:5672 -e RABBITMQ_DEFAULT_USER=minecraft -e RABBITMQ_DEFAULT_PASS=password -e RABBITMQ_DEFAULT_VHOST=/minecraft rabbitmq
```

```yml
version: '3.8'
services:
  rabbitmq:
    image: rabbitmq
    hostname: my-rabbit
    container_name: minecraft-rabbit
    ports:
      - "5672:5672"
    environment:
      RABBITMQ_DEFAULT_USER: minecraft
      RABBITMQ_DEFAULT_PASS: password
      RABBITMQ_DEFAULT_VHOST: /minecraft
```

#### java

本工具编译版本要求 >= jdk17，建议runtime也不要低于这个版本喵。

### 使用

1. 从release界面下载最新版本的jar包。
2. 上传到服务器`plugins`文件夹。
3. 启动服务器，会自动创建一个默认的配置文件。
   针对配置文件的配置，必须和上面rabbitMQ docker配置一一对应。
4. 跳转到 [Adapter项目](https://github.com/MeowCraftMC/CatMessenger-Adapter) 以继续教程。
