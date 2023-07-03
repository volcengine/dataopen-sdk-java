# openapi 使用说明

## Client参数说明

| 字段       | 类型            | 默认值                          | 含义                            |
|------------|-----------------|---------------------------------|---------------------------------|
| app_id     | String          | 无                              | 应用的唯一标识符                  |
| app_secret | String          | 无                              | 用于应用的安全认证的密钥          |
| url        | String 或 null | "https://analytics.volcengineapi.com"    | 服务器的URL地址                  |
| env        | String 或 null | "dataopen"                      | 环境设置，可选值为 "dataopen" 或 "dataopen_staging" |
| expiration | String 或 null | "1800"                          | 过期时间，单位是秒            |

## client.request参数说明

| 字段         | 类型                               | 默认值    | 含义                                            |
|--------------|------------------------------------|-----------|------------------------------------------------|
| service_url  | String                             | 无        | 请求的服务 URL 地址                            |
| method       | String                             | 无        | 请求的 HTTP 方法，例如 "GET", "POST" 等        |
| headers      | Map<String, String>                | {}        | 请求头，包含的信息如认证凭据，内容类型等       |
| params       | Map<String, Object>                | {}        | URL 参数，用于GET请求                          |
| body         | Map<String, Object>                | {}        | 请求体，通常在POST或PUT请求中包含发送的数据    |