package com.example.myapp.tools;

import android.util.Base64;
import com.example.myapp.tools.db.UserDBHelper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

/**
 * Created by hansen on 2015/11/28.
 */
public class HttpRequest {
    private String Url = null;
    private HashMap<String, String> params = null;
    private String public_key =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDO/ig5paho0yJwsR5FUZyq50Tw\n" +
                    "49fFu0im86EkLQoGaaCOEtfDotjsOX09YATSO0N5cJyM+4byU8gPMUO0DpCyxkJH\n" +
                    "L27CQ6ZyznddI809QtcwQwxPxTvVR7b/urnwwc34QMCVPQrUM/W8gLC4Vf0QbdnA\n" +
                    "gvDJmdjHLzvRwKyc3wIDAQAB";

    public HttpRequest(String Url, HashMap params) {
        this.Url = Url;
        if (params.containsKey("token")) {//token转换为签名signature
            params.put("token", signature(params.get("token").toString()));
        }
        this.params = params;
    }

    public HashMap sendHttp() {
        HashMap<String, Object> ret = send();
        HashMap re = new HashMap() {{
            put("error", true);
        }};
        if ((Boolean) ret.get("error")) {
            re.put("msg", "通讯失败");
        } else {
            JSONObject data = (JSONObject) ret.get("data");
            try {
                int err = data.getInt("error");
                if (err == 1) {
                    re.put("msg", data.getString("msg"));
                } else {
                    re.put("error", false);
                    re.put("msg", data.getString("msg"));
                    JSONObject info = (JSONObject) data.get("data");
                    re.put("data", info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return re;
    }

    public HashMap<String, Object> send() {
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

    /**
     * 签名算法
     * 现在时间戳
     */
    private String signature(String token) {
        Integer seconds = (int) (System.currentTimeMillis() / 1000);
        String source = token + "," + seconds.toString();
        String afterencrypt = null;
        try {
            PublicKey publicKey = RSAUtils.loadPublicKey(public_key);
            byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
            afterencrypt = Base64Utils.encode(encryptByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afterencrypt;
    }
}
