package sy.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;

public class Ad {
    private static MultiThreadedHttpConnectionManager cm = new MultiThreadedHttpConnectionManager();

    static {
        cm.getParams().setMaxTotalConnections(100);
        cm.getParams().setTcpNoDelay(true);

        cm.setMaxConnectionsPerHost(50);
        cm.getParams().setDefaultMaxConnectionsPerHost(50);
    }

    public static void mas() {

        String posturl = "http://127.0.0.1:8088/natplatform-promote/api/user/IMUser/setUser";
        HttpClient client = new HttpClient(cm);
        PostMethod post = null;
        try {
            post = new PostMethod(posturl);

            /** 增加调用接口权限验证 update by zhangqingmu **/
            String timestamp = new Date().getTime() + "";
            String token = UUID.randomUUID().toString();

            String hashdata = token + timestamp;
            // md5加密
            hashdata = Md5Aes.calMd5(hashdata);

            // 设置head
            post.setRequestHeader("HASHDATA", hashdata);
            post.setRequestHeader("TIMESTAMP", timestamp);
            post.setRequestHeader("TOKEN", token);

            JSONObject json = new JSONObject();
            json.put("a", "邵年文   杨成");
            json.put("b", "3434");
            System.out.println(json.toString());
            post.setRequestEntity(new ByteArrayRequestEntity(json.toString()
                    .getBytes("utf8")));

            client.executeMethod(post);

            String re = post.getResponseBodyAsString();

            System.out.println(re);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            post.releaseConnection();
        }
    }

    public static void main(String[] args) {
        new Ad().mas();
    }
}
