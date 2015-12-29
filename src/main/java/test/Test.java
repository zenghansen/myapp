package test;


import android.util.Base64;
import com.example.myapp.tools.Base64Utils;
import com.example.myapp.tools.RSAUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    private String public_key =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDO/ig5paho0yJwsR5FUZyq50Tw\n" +
                    "49fFu0im86EkLQoGaaCOEtfDotjsOX09YATSO0N5cJyM+4byU8gPMUO0DpCyxkJH\n" +
                    "L27CQ6ZyznddI809QtcwQwxPxTvVR7b/urnwwc34QMCVPQrUM/W8gLC4Vf0QbdnA\n" +
                    "gvDJmdjHLzvRwKyc3wIDAQAB";
    private String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM7+KDmlqGjTInCx\n" +
            "HkVRnKrnRPDj18W7SKbzoSQtCgZpoI4S18Oi2Ow5fT1gBNI7Q3lwnIz7hvJTyA8x\n" +
            "Q7QOkLLGQkcvbsJDpnLOd10jzT1C1zBDDE/FO9VHtv+6ufDBzfhAwJU9CtQz9byA\n" +
            "sLhV/RBt2cCC8MmZ2McvO9HArJzfAgMBAAECgYEAsk7gSeB5NWuDhH4e7GnEQric\n" +
            "e8JZ0lxl1mOSkX4235VIGHsE4OM8aCmRyDgZjxw8ILWwNgZKlh8z/FpczxOo8rrc\n" +
            "SJ8W9WpdjSrbHtGNQdH+ATvDKS2022BDVr5gD0qw3BSEX1g0ukUVETa/w6WgJSpT\n" +
            "1lbxPYEs9oFpjGVtaYkCQQDue7hXWDGy9FQ5bbCY4WbE6lUZyadtip0U0GfR50KO\n" +
            "K7dqTCyanQui9P3nT/Iwu3c+587s9eSuI6GR1oSVUIMbAkEA3jJPpyhHbzsOljvO\n" +
            "KLX6mYOxig3I2yr5BuhSL94BPXpKf8v3uH+TNEvbzFQnB5sETT7D/wMTvjNvwz25\n" +
            "dbyljQJATk8x6Px81VaFkZYDNzBMnryT1TPyVnX9vqfwb2yQ3j5cq/r3ao3HFGt+\n" +
            "NEZ8MKReM7nKqOTVgpkQpOLcBmu5oQJALSnayOZGBtThNSY41EuRAW0kRCRjJDFK\n" +
            "5t2H/xTH+cAGro097/F2cVXN+m1MdZ5LtIvCeO4eMDXyTKQaM1VB7QJAdL/nfaAT\n" +
            "IcXD+qD2kFwql9tvyyA7+HMMNOrFttV8pQoXVh+PGRYtRACRJBFLPYV2cKU6g+w8\n" +
            "ruLkGg/MRjwCyg==";

    public void testa() {
        System.out.printf("1111");
    }

    public static void main(String arg[]) {
        new Test().test();
    }

    private void test() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()).toString());
        //InputStream inPublic = getResources().getAssets().open("rsa_public_key.pem");
        InputStream inPublic = Test.class.getClassLoader().getResourceAsStream("rsa_public_key.pem");
        InputStream inPri = Test.class.getClassLoader().getResourceAsStream("private_key.pem");
        try {
            String source = "ZKQpolvihxnXFu0TCtSCiQbuKTXFghdSxhBKflbo";
            Integer seconds = (int) (System.currentTimeMillis() / 1000);
             source = source + "," + seconds.toString();
            //PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
            PublicKey publicKey = RSAUtils.loadPublicKey(public_key);
            PrivateKey privateKey = RSAUtils.loadPrivateKey(private_key);
            byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
            String afterencrypt = Base64Utils.encode(encryptByte);
            System.out.println(afterencrypt);
           // afterencrypt = "oonfk8tVuCxhnW1F4PU/7E/SuBtyYJ47945OEyTFoQjxbdjLcnuFIzzws5HmpcKEe58zN5RcyCUCa1oqxBLfvQ+AF/j6UuytzJoEIdLt8VUIkAG0ofwvlQGKS+s3eQ4VzL6m9uscplO7S8UHStISHxwzsMkuPNigkex2vt8Q/nI=";
            byte[] temp = Base64Utils.decode(afterencrypt);
            byte[] decryptByte = RSAUtils.decryptData(temp, privateKey);
            String decryptStr = new String(decryptByte);
            System.out.println(decryptStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void test1() {
        InputStream in = null;
        try {
            in = Test.class.getClassLoader().getResourceAsStream("private_key.pem");
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

}
