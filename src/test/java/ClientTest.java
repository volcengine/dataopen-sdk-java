import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.dataopen.sdk.Client;

import static org.junit.Assert.assertNotNull;

public class ClientTest {
    @Test
    public void accessTokenTest() throws IOException {
        String app_id = "";
        String app_secret = "";

        Client client = new Client(app_id, app_secret);
        client.handle_token();

        System.out.println("is_authenticated: " + client.is_authenticated());

        assertNotNull(client.is_authenticated());
    }

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

        // Output results
        System.out.println("Output requestGetTest: " + res);
    }

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

    @Test
    public void requestMaterialPostTest() throws IOException {
        String app_id = "";
        String app_secret = "";

        Client client = new Client(app_id, app_secret, "https://analytics.volcengineapi.com", null);

        Map<String, String> headers = new HashMap<>();

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> body = new HashMap<>();
        body.put("name", "ccnnodetest");
        body.put("title", "测试title");
        body.put("type", "component");
        body.put("description", "测试description2");
        body.put("frameworkType", "react");

        Map<String, Object> res = client.request(
                "/dataopen/open-apis/material/openapi/v1/material",
                "PUT",
                headers,
                params,
                body);

        // Output results
        System.out.println("Output requestMaterialPostTest: " + res);
    }
}