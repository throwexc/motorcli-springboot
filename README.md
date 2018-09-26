# motorcli-springboot

**一个基于 Spring boot 脚手架类库，作为其它 Maven 工程的父工程，减少子工程的配置项、提供常用工具、封装常用参数和 DTO 等数据对象、集成常用框架和类库，可以减少开发难度，提高开发效率。**

> motorcli-springboot-common 公共类库模块
>> utils 常用工具<br>
>> dto DTO 数据传输对象<br>
>> exception 异常<br>

> motorcli-springboot-datasource 数据库类库模块 (druid)

> motorcli-springboot-mybatis Mybatis模块
>> config 参数集成封装<br>
>> 通过 Mapper 集成<br>
>> 分页插件集成<br>

> motorcli-springboot-web WEB 模块
>> MotorWebControllerBase.java Contrller 基类<br>
>> error 异常监听、统一异常处理<br>
>> params Request 请求参数映射<br>
>> result 返回结果<br>

> motorcli-springboot-restfull RESTFull 模块
>> MotorApiBase.java API 基类<br>
>> dto DTO 数据传输对象封装<br>
>> dto.converter 列表数据、树形数据转换器<br>
>> result 返回数据封装<br>
