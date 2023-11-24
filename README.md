# openapi 使用说明

## Client参数说明

| 字段       | 类型            | 默认值                          | 含义                            |
|------------|-----------------|---------------------------------|---------------------------------|
| app_id     | String          | 无                              | 应用的唯一标识符                  |
| app_secret | String          | 无                              | 用于应用的安全认证的密钥          |
| url        | String 或 null | "https://analytics.volcengineapi.com"    | 服务器的URL地址                  |
| expiration | String 或 null | "1800"                          | 过期时间，单位是秒            |

## client.request参数说明

| 字段         | 类型                               | 默认值    | 含义                                            |
|--------------|------------------------------------|-----------|------------------------------------------------|
| service_url  | String                             | 无        | 请求的服务 URL 地址                            |
| method       | String                             | 无        | 请求的 HTTP 方法，例如 "GET", "POST" 等        |
| headers      | Map<String, String>                | {}        | 请求头，包含的信息如认证凭据，内容类型等       |
| params       | Map<String, Object>                | {}        | URL 参数，用于GET请求                          |
| body         | Map<String, Object>                | {}        | 请求体，通常在POST或PUT请求中包含发送的数据    |

## 远程maven地址

https://central.sonatype.com/artifact/io.github.volcengine/dataopen-sdk-java/overview

## 获取与安装

```xml
<dependency>
    <groupId>io.github.volcengine</groupId>
    <artifactId>dataopen-sdk-java</artifactId>
    <version>1.0.4</version>
</dependency>
```

## 举例

### 1、Get 方法

```java
import com.dataopen.sdk.Client;

public class ClientTest {
    @Test
    public void requestGetTest() throws IOException {
        String app_id = "";
        String app_secret = "";

        Client client = new Client(app_id, app_secret);

        Map<String, String> headers = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("app", 46);
        params.put("page_size", 2);
        params.put("page", 1);

        Map<String, Object> body = new HashMap<>();

        Map<String, Object> res = client.request("/dataopen/open-apis/xxx/openapi/v1/open/flight-list", "GET", headers, params, body);

        System.out.println("Output requestGetTest: " + res);
    }
}
```

### 2、Post 方法

```java
import com.dataopen.sdk.Client;

public class ClientTest {
    @Test
    public void requestPostTest() throws IOException {
        String app_id = "";
        String app_secret = "";

        Client client = new Client(app_id, app_secret);

        Map<String, String> headers = new HashMap<>();

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> body = new HashMap<>();
        body.put("uid_list", new String[] { "1111111110000" });

        Map<String, Object> res = client.request(
                "/dataopen/open-apis/xxx/openapi/v1/open/flight/version/6290880/add-test-user",
                "POST",
                headers,
                params,
                body);

        // Output results
        System.out.println("Output requestPostTest: " + res);
    }
}
```