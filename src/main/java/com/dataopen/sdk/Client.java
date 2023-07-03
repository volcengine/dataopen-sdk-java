/*
 * Copyright 2023 DataOpen SDK Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dataopen.sdk;

import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Client {
    public String app_id;
    public String app_secret;
    public String url;
    public String env;
    public String expiration;
    private String _access_token;
    private int _ttl;
    private long _token_time;

    public static final String OPEN_APIS_PATH = "/open-apis";

    public Client(
            String app_id,
            String app_secret,
            String url,
            String env,
            String expiration) {
        this.app_id = app_id;
        this.app_secret = app_secret;
        this.url = (url != null && url != "") ? url : "https://analytics.volcengineapi.com";
        this.env = (env != null && env != "") ? env : "dataopen";
        this.expiration = expiration != null ? expiration : "1800";
        this._ttl = 0;
        this._access_token = "";
        this._token_time = 0;
    }

    public Client(
            String app_id,
            String app_secret) {
        this(app_id, app_secret, null, null, null);
    }

    public Map<String, Object> request(
            String service_url,
            String method) throws IOException {
        return this.request(service_url, method, null, null, null);
    }

    public Map<String, Object> request(
            String service_url,
            String method,
            Map<String, String> headers,
            Map<String, Object> params,
            Map<String, Object> body) throws IOException {
        String upper_case_method = method.toUpperCase();

        if (this._access_token == null || !this._valid_token()) {
            try {
                this.handle_token();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> new_headers = new HashMap<>();
        new_headers.put("Authorization", this._access_token);
        new_headers.put("Content-Type", "application/json");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            new_headers.put(entry.getKey(), entry.getValue());
        }

        String completed_url = this.url +
                "/" +
                this.env +
                Client.OPEN_APIS_PATH +
                service_url;
        String query_url = this._joint_query(completed_url, params);

        Map<String, Object> resp;

        switch (upper_case_method) {
            case "GET":
                resp = fetch(query_url, "GET", new_headers, null);
                break;
            case "POST":
            case "PUT":
            case "DELETE":
            case "PATCH":
                resp = fetch(query_url, upper_case_method, new_headers, body);
                break;
            default:
                throw new Error("Invalid request method");
        }

        return resp;
    }

    public void handle_token() throws IOException {
        String authorization_url = this.env + Client.OPEN_APIS_PATH + "/v1/authorization";
        String completed_url = this.url + "/" + authorization_url;

        Map<String, Object> map = new HashMap<>();
        map.put("app_id", this.app_id);
        map.put("app_secret", this.app_secret);

        Map<String, Object> resp = fetch(completed_url, "POST", new HashMap<String, String>(), map);

        long token_time = System.currentTimeMillis();

        if ((Double) resp.get("code") == 200.0 && resp.get("data") != null) {
            Map<String, Object> data = (Map<String, Object>) resp.get("data");
            this._ttl = ((Double) data.get("ttl")).intValue();
            this._token_time = token_time;
            this._access_token = (String) data.get("access_token");
        }
    }

    public boolean is_authenticated() {
        return !this._access_token.isEmpty();
    }

    public boolean _valid_token() {
        long current_time = System.currentTimeMillis();

        if (current_time - this._token_time > this._ttl * 1000) {
            return false;
        }

        return true;
    }

    private String _joint_query(String url, Map<String, Object> params) {
        StringJoiner paramStr = new StringJoiner("&");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            paramStr.add(entry.getKey() + "=" + entry.getValue());
        }
        return url + "?" + paramStr;
    }

    private Map<String, Object> fetch(String url, String method, Map<String, String> headers, Map<String, Object> body)
            throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();

        conn.setRequestMethod(method);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }

        if (body != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = new Gson().toJson(body).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return new Gson().fromJson(response.toString(), HashMap.class);
        }
    }
}