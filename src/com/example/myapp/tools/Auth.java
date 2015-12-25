package com.example.myapp.tools;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.example.myapp.MainActivity;
import com.example.myapp.tools.db.UserDBHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by hansen on 2015/12/16.
 */
public class Auth {
    public static String serverHost = "http://192.168.1.15/test/api/";
    public static JSONObject userInfo = null;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            if (setUserInfo(uid, token)) {
                UserDBHelper user = new UserDBHelper(at);
                user.setToken(uid,token);
                return true;
            }
        }
        return false;
    }

    public boolean setUserInfo(final String uid, final String token) {
        String url = serverHost + "Auth.php";
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
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
