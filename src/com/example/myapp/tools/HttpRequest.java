package com.example.myapp.tools;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by hansen on 2015/11/28.
 */
public class HttpRequest {
    private String Url = null;
    private HashMap<String, String> params = null;

    public HttpRequest(String Url, HashMap params) {
        this.Url = Url;
        this.params = params;
    }

    public HashMap<String, Object> sendHttp() {
        HashMap result = new HashMap() {{
            put("error", true);
        }};

        org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();

        //这里是你与服务器交互的地址
        String validateUrl = this.Url;//"http://192.168.191.1/YCM/testjson.php";

        //设置链接超时
        httpClient.getParams().setParameter(CoreConnectionPNames.
                CONNECTION_TIMEOUT, 5000);

        //设置读取超时
        httpClient.getParams().setParameter(
                CoreConnectionPNames.SO_TIMEOUT, 5000);

        HttpPost httpRequst = new HttpPost(validateUrl);

        //准备传输的数据
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();

        Set<String> set = this.params.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            params.add(new BasicNameValuePair(key, this.params.get(key)));

        }

        try {
            //发送请求
            httpRequst.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            //得到响应
            HttpResponse response = httpClient.execute(httpRequst);


            //返回值如果为200的话则证明成功的得到了数据
            if (response.getStatusLine().getStatusCode() == 200) {
                StringBuilder builder = new StringBuilder();

                //将得到的数据进行解析
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                for (String s = buffer.readLine(); s != null; s = buffer.readLine()) {
                    builder.append(s);

                }

                System.out.println(builder.toString());
                //得到Json对象
                JSONObject jsonObject = new JSONObject(builder.toString());

                //通过得到键值对的方式得到值
               // Integer id = jsonObject.getInt("user_id");
                //在线程中判断是否得到成功从服务器得到数据
                result.put("error", false);
                result.put("data", jsonObject);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
