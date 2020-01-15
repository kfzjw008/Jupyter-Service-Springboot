# Api_Helper(接口使用文档)

## 简介

记录开发接口及其使用文档,其中Main代表主域名。
## 更新记录

20200109：更新登录API。

## 登录API

### http://Main/api/login/wxlogin
基于微信小程序实现用户登录。本接口只可用作微信小程序登录。

#### 输入参数

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| code | String |微信随机提供的状态码，本码由微信直接生成/

#### 输出参数

OpenId——

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| id | int |微信随机提供的状态码，本码由微信直接生成 | 
|2| peoplenumber | String |目前暂无特殊用途|
|3| updateTime | Date |最后登录时间|
|4| insertTime | Date |初次注册时间|
|5| authority | int |用户权限码，详情参照用户权限文档/
|6| session | String |微信随机提供的Session，用于判断是否登录，本码由微信直接生成/
|7| openId | String |微信提供的用户唯一识别码，本码由微信直接生成/


## 功能API

### http://Main/api/function/SearchSAO
基于经纬度查询所在地72小时观测适宜度和相关天文指标变化。

#### 输入参数

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| LON | Double |所在地的经度，提供-180到180之间的数字，小数支持到小数点后一位/
|2| LAT | Double |所在地的纬度，提供-90到90之间的数字，小数支持到小数点后一位/

#### 输出参数

SAO——

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| city | String |所在的城市 | 
|2| Province | String |所在的省份|
|3|SAO_Data | Object |相关观测适宜度数据（提供24组，每三小时提供一组）|

SAO_Result——

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| AQI | int |空气质量指数 | 
|2| City | String |地级市名称|
|3| 1-21 | Object |时间序列，共24组数据，每组数据相隔3小时|
|4|SAO |int |观测适宜度|
|5| Temperature | int |温度（摄氏度）| 
|6| TimePoint | int | 时间点，指本组数据距离当前时刻时长，单位为小时。| 
|8| Prec_Type | String |是否降雨，无降雨则为none|
|9|Humidity | int |湿度|
|10|Wind | Object |风力相关数据|
|11|Wind_Direction | String |风向|
|12| Wind_Speed | int |风速|
|13| Transparency|  Object |透明度相关数据|
|14| Transparency_Value | int |透明度值|
|15|Lifted_Index|  Object |抬升指数相关数据|
|16|Lifted_Index_Valued | String |抬升指数|
|17| CloudCover|  Object |云层厚度相关数据|
|18| CloudCover_Value | String |云层厚度值|
|19|Seeing | Object |视宁度相关数据|
|20| Seeing_Value | int |视宁度值|
|21| Explain| String |对每一种数据的解释|


## 用户试题API

### http://Main/api/question/ModuleExercises
登录用户查询试题模块。

#### 输入参数

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| token | String |用户token值，用于验证用户登录身份/

#### 输出参数

Module——

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| id | int |试题分类编号 | 
|2| Title | String |分类名称|
|3|Description |  String  |分类描述|


### http://Main/api/question/ModuleExercisesDetails
查询分类下所有试题

#### 输入参数

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| token | String |用户token值，用于验证用户登录身份/
|1| module | int |试题分类编号/
|1| page | int |页码，每页显示10题/

#### 输出参数

ModuleExercisesDetails——

依次返回试题Object类（包括所有试题信息）和当前的页码相关信息，（页数，是否为第一页或者最后一页等）。


### http://Main/api/question/GetQuestion
根据试题ID值查询试题。

#### 输入参数

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| token | String |用户token值，用于验证用户登录身份/
|2| id | int |试题编号/

#### 输出参数

Question_Result——

返回试题Object类（包括所有试题信息）。


### http://Main/api/question/RandomGetQuestion
随机获得试题。

#### 输入参数

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| token | String |用户token值，用于验证用户登录身份/

#### 输出参数

Question_Result——

返回随机试题的Object类（包括所有试题信息）。


### http://Main/api/question/Search
试题模糊搜索

#### 输入参数

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| token | String |用户token值，用于验证用户登录身份/
|1| word | String |搜索的内容 /
|1| page | int |页码，每页显示10题/


#### 输出参数

SearchResult——

依次返回试题Object类（包括所有试题信息）和当前的页码相关信息，（页数，是否为第一页或者最后一页等）。


### http://Main/api/question/PutRecords
试题模糊搜索

#### 输入参数

| 序号 | 参数名 | 参数类型 |解释 |
| ------ | ------ | ------ | ------ |
|1| token | String |用户token值，用于验证用户登录身份/
|1| id | int |试题编号/
|1| source | String |练习试题来源/
|1| answer | String |试题答案 /
|1| openid | int |用户的openid，用于确定用户身份信息/


#### 输出参数

PutResult——

1或者0。  
1为推送成功，0为推送失败。













