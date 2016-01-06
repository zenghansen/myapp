import java.io.*;
import java.net.Socket;

public class Client {
    public static final String IP_ADDR = "192.168.1.17";//服务器地址
    public static final int PORT = 9501;//服务器端口号

    public static void main(String[] args) {
        System.out.println("客户端启动...");
        System.out.println("当接收到服务器端字符为 \"OK\" 的时候, 客户端将终止\n");
        final  String a = "";
        if(a == ""){
            System.out.printf("1");
        }
        /*Socket socket = null;
        try {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket(IP_ADDR, PORT);
            while (true) {
                //读取服务器端数据
                byte[] buf = new byte[1024];
                int len = 0;
                InputStream input = socket.getInputStream();
                while((len = input.read(buf))!=-1){
                    String tem = new String(buf, 0, len,"UTF-8");
                    String  ck = tem.substring(tem.length()-5,tem.length());
                    System.out.printf(tem);
                    if(ck.equals("//END")){
                        break;
                    }
                }
                System.out.printf("a");
                //向服务器端发送数据
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                String str = "hello";
               // out.writeUTF(str);
                Thread.sleep(2000);
                //out.close();
               // input.close();
            }
        } catch (Exception e) {
            System.out.println("客户端异常:" + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                    System.out.println("客户端 finally 异常:" + e.getMessage());
                }
            }
        }
*/
    }
}