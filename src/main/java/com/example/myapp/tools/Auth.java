package com.example.myapp.tools;

import android.app.Activity;
import com.example.myapp.tools.db.UserDBHelper;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hansen on 2015/12/16.
 */
public class Auth {
    public static String serverHost = "http://122.114.62.8/swoole/";
    public static JSONObject userInfo = null;
    public static String uid = null;
    public static String token = null;

    public HashMap<String, Object> Login(HashMap params, Activity at) {
        String url = serverHost + "Auth.php";
        params.put("act", "doLogin");
        HttpRequest foo = new HttpRequest(url, params);
        HashMap<String, Object> ret = foo.sendHttp();

        if (!(Boolean) ret.get("error")) {

            UserDBHelper user = new UserDBHelper(at);
            JSONObject info = (JSONObject) ret.get("data");
            try {
                user.setToken(info.getString("id"), info.getString("api_token"));//保存token
                userInfo = info;
                uid = info.getString("id");
                token = info.getString("api_token");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public HashMap<String, Object> Reg(HashMap params, Activity at) {
        String url = serverHost + "Auth.php";
        params.put("act", "doReg");
        HttpRequest foo = new HttpRequest(url, params);
        HashMap<String, Object> ret = foo.sendHttp();
        return ret;
    }

    public Boolean verify(Activity at) {
        if (userInfo != null) {
            return true;
        }
        UserDBHelper db = new UserDBHelper(at);
        String uid = db.getUid();
        if (uid != null) {
            String token = db.getToken(uid);
            if (getUserInfo(uid, token)) {
                UserDBHelper user = new UserDBHelper(at);
                user.setToken(uid, token);
                return true;
            }
        }
        return false;
    }

    public boolean getUserInfo(final String uid, final String token) {
        String url = serverHost + "Auth.php";
        if (token == null || token.equals("")) {
            return false;
        } else {
            HashMap params = new HashMap() {{
                put("uid", uid);
                put("token", token);
                put("act", "getUserInfo");
            }};
            HttpRequest foo = new HttpRequest(url, params);
            HashMap<String, Object> ret = foo.sendHttp();

            if (!(Boolean) ret.get("error")) {
                JSONObject info = (JSONObject) ret.get("data");
                try {
                    userInfo = info;
                    this.uid = info.getString("id");
                    this.token = info.getString("api_token");
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public HashMap getFrields() {
        String url = serverHost + "Auth.php";
        HashMap params = new HashMap() {{
            put("uid", uid);
            put("token", token);
            put("act", "getFrields");
        }};
        HttpRequest foo = new HttpRequest(url, params);
        HashMap<String, Object> ret = foo.sendHttp();
        return ret;
    }

    public HashMap editInfo(final String nnStr) {
        String url = serverHost + "Auth.php";
        HashMap params = new HashMap() {{
            put("uid", Auth.uid);
            put("token", Auth.token);
            put("act", "doEditInfo");
            put("nickname", nnStr);
        }};
        HttpRequest foo = new HttpRequest(url, params);
        HashMap<String, Object> ret = foo.sendHttp();
        if (!(Boolean) ret.get("error")) {
            JSONObject info = (JSONObject) ret.get("data");
            userInfo = info;
        }
        return ret;
    }

    public void exit(Activity at) {
        UserDBHelper db = new UserDBHelper(at);
        userInfo = null;
        token = null;
        if (uid != null) {
            db.setToken(uid, "");
        }
        uid = null;
    }
}
