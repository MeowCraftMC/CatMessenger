# CatMessenger / 小猫信使

基于 [水晶互联 CrystalConnector](https://github.com/MeowCraftMC/CrystalConnector) 的多 MC 服务器和 Telegram 群的消息互通软件，同样支持在MC服务器之间混合传递。

## 传输协议

版本：**V1.0（2023.9.18 更新）**

小猫信使的传输协议构建在水晶互联的传输协议之上，把数据通过 CBOR 封包之后写入水晶互联发布消息的 `Payload` 字段。

小猫信使在每一端的实现都需要在认证后向水晶互联注册 `catmessenger:message` 频道的双向通信（参考 [水晶互联 - 消息方向](https://github.com/MeowCraftMC/CrystalConnector#%E6%B6%88%E6%81%AF%E6%96%B9%E5%90%91messagedirection) ）。

### 文档约定

文档中使用尖括号对 `<type:Name>` 代表必填参数，方括号对 `[type:Name]` 代表可选参数。 `type` 是参数类型，如 `string` 、 `int` 等。 `Name` 是参数名，不在具体传输过程中体现，是方便开发人员记忆的名称。

**文档中会使用表达式块来简写需要重复的内容。**

例如：

```
repeat (RepeatCount) {
	<string:Name>
}
```

用于标识此处应读取 `RepeatCount` （大于 0）次数据 `Data` 。

```
if (ShouldRead) {
	<string:Data>
}
```

用于标识此处应根据 `ShouldRead` 的值是否为 `true` 确定是否读取 `Data` 。

### 数据结构

#### 消息类型（PayloadType）

| 类型名称          | Id   | 说明                                             |
| ----------------- | ---- | ------------------------------------------------ |
| Raw               | 0    | 原始消息，各端在对应平台发送时应当**保持原样**。 |
| ChatComponent     | 1    | 聊天组件消息，方便 MC 端进行发送。               |
| ChatText          | 2    | 聊天文字消息。                                   |
| System            | 3    | 系统消息，各端在对应平台发送时应当**明确标识**。 |
| PlayerOnline      | 4    | 玩家上线或离线的消息。                           |
| ServerLifecycle   | 5    | MC 服务端启动或关闭的消息。                      |
| PlayerDeath       | 6    | 玩家在游戏中死亡的消息。                         |
| PlayerAdvancement | 7    | 玩家在游戏中达成成就的消息。                     |
| QueryOnline       | 8    | 查询在线玩家数量的消息。                         |
| QueryTime         | 9    | 查询世界时间的消息。                             |
| RunCommand        | 10   | 运行游戏内命令的消息。                           |
| QueryResultOnline | 11   | 查询在线玩家数量的结果。                         |
| QueryResultTime   | 12   | 查询世界时间的结果。                             |
| CommandResult     | 13   | 执行命令的结果。                                 |

### 消息格式

#### 原始消息（Raw）

```
0
<string:Text>
```

Text：消息内容。

#### 聊天组件消息（ChatComponent）

```
1
<string:ChatComponent>
```

ChatComponent：符合 MC [原始 JSON 文本格式（Minecraft 中文 WIKI）](https://minecraft.fandom.com/zh/wiki/%E5%8E%9F%E5%A7%8BJSON%E6%96%87%E6%9C%AC%E6%A0%BC%E5%BC%8F) 规范的聊天组件字符串。

#### 聊天文字消息（ChatText）

```
2
<string:Sender>
<string:Content>
```

Sender：文字消息发送者；

Content：文字消息内容。

#### 系统消息（System）

```
3
<string:Message>
```

Message：系统消息内容；

#### 玩家在线消息（PlayerOnline）

```
4
<bool:IsOnline>
<string:PlayerName>
```

IsOnline：true为玩家上线，false为玩家下线；

PlayerName：玩家名称。

#### 服务器运行消息（ServerLifecycle）

```
5
<bool:IsStarted>
```

IsStarted：true为服务器启动，false为服务器关闭。

#### 玩家死亡消息（PlayerDeath）

```
6
<string:DeathMessage>
<string:PlayerName>
<string:KillerName>
<string:ItemName>
```

DeathMessage：死亡提示消息模板（模板插入参数的方式应参考Java，使用 `%n%s` ，其中 `n` 是从 1 开始的第 n 个参数，`s` 代表字符串）；

PlayerName：玩家名称，模板的第一个参数；

KillerName：击杀者名称，模板的第二个参数；

ItemName：击杀者所使用的物品，模板的第三个参数。

#### 玩家进度消息（PlayerAdvancement）

```
7
<string:PlayerName>
<int32:AdvancementType>
<string:AdvancementName>
<string:AdvancementDescription>
```

PlayerName：玩家名称；

AdvancementType：0（进度）、1（目标）、2（挑战）；

AdvancementName：进度名；

AdvancementDescription：进度介绍。

#### 查询玩家数量消息（QueryOnline）

```
8
<string:ServerName>
```

ServerName：服务器名称（和水晶互联中认证的客户端名称相同）。

**服务器会用 QueryResultOnline 返回查询结果。**

#### 查询游戏内世界时间消息（QueryTime）

```
9
<string:ServerName>
<string:WorldName>
```

ServerName：服务器名称（和水晶互联中认证的客户端名称相同）；

WorldName：世界名称，用于服务器查找世界。

#### 运行服务器命令消息（RunCommand）

```
10
<string:ServerName>
<string:Command>
```

ServerName：服务器名称（和水晶互联中认证的客户端名称相同）；

Command：命令内容。

**出于安全原因请勿实现，等待协议更新。**

#### 查询玩家数量结果（QueryResultOnline）

```
11
<string:ServerName>
<int32:PlayerCount>
repeat (PlayerCount) {
	<string:PlayerName>
}
```

ServerName：服务器名称（和水晶互联中认证的客户端名称相同）；

PlayerCount：玩家数量；

PlayerName[]：每一个玩家的名称。

**聊天平台的机器人不必完全显示这些内容。**

#### 查询游戏内世界时间结果（QueryTime）

```
12
<string:ServerName>
<string:WorldName>
<int32:DayTime>
<int32:GameTime>
<int32:Day>
```

ServerName：服务器名称（和水晶互联中认证的客户端名称相同）；

WorldName：世界名称，用于服务器查找世界；

DayTime：当天日出后流逝的游戏刻数，与 MC 所使用的定义相同；

GameTime：世界总共流逝的游戏刻数，与 MC 所使用的定义相同；

Day：已流逝的游戏天数，与 MC 所使用的定义相同。

#### 运行服务器命令结果（CommandResult）

```
13
<string:ServerName>
<bool:Successful>
if (!Successful) {
	<string:Reason>
}
```

ServerName：服务器名称（和水晶互联中认证的客户端名称相同）；

Successful：是否执行成功；

Reason：执行失败时返回的原因。

**出于安全原因请勿实现，等待协议更新。**