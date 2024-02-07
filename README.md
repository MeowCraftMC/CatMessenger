# CatMessenger / 小猫信使

基于 RabbitMQ 的多 MC 服务器和 Matrix 群的消息互通软件，同样支持在MC服务器之间混合传递。

## 传输协议

版本：**V2.0（2024.2.7 更新）**



### 消息（Message）

每一条被广播的内容都被称为消息。

消息由消息实体（MessageEntity）数组组成。

使用Json格式传递。



#### 消息实体类型

##### 文本

