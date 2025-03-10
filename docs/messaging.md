# MQ Protocol



## Messaging forwarding

Exchange name (fanout): `fanout.exchange.messages` 

Queue name: `fanout.queue.<clientId>`  

Need to send ack manually.

Message body will be serialized to a json string.



### Objects

#### Message body

| Field    | Type   | Mandatory | Definition                 | Comments                    |
| -------- | ------ | --------- | -------------------------- | --------------------------- |
| platform | String | √         | Json text component.       | Displaying platform name.   |
| sender   | Object |           | See player below.          | Displaying message sender.  |
| content  | String | √         | Json text component.       | Displaying message content. |
| time     | String | √         | ISO-8601 format date time. | Send time.                  |



### Player

| Field  | Type   | Mandatory | Definition           | Comments                                                     |
| ------ | ------ | --------- | -------------------- | ------------------------------------------------------------ |
| id     | String | √         | Literal string.      | Sender id in the specific platform.<br />For Telegram: the username without the heading `@` if set, otherwise the long id. |
| uuid   | String |           | UUID without `-` .   | Player uuid.<br />Null when on tg or player is a bot.        |
| name   | String |           | Json text component. | Displaying name.                                             |
| prefix | String |           | Json text component. | Prefix.                                                      |
| suffix | String |           | Json text component. | Suffix.                                                      |

