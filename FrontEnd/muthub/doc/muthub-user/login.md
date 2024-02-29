登录接口

接口api：`/login`

请求方法：POST

请求体参数：
```json
{
    username (string): 用户名
    password (string): 密码
}
```
返回结果：
```json
{
    success (boolean): 登录是否成功
    token (string): 登录成功后的令牌
}
```

请求示例
```http
POST ${process.env.VUE_APP_API_BASE_URL}/login
Content-Type: application/json

{
  "username": "exampleuser",
  "password": "examplepassword"
}
```
成功响应示例

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "success": true,
  "token": "exampletoken"
}
```

失败响应示例
```http
HTTP/1.1 200 OK
Content-Type: application/json

{
  "success": false
}
```
```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "message": "Invalid request"
}
```