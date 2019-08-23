package com.mcsunnyside.BBSToper;

import com.sun.javafx.geometry.BoundsUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sun.awt.image.BufferedImageDevice;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

class HttpUtil {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @return URL 所代表远程资源的响应结果
     */
    static BufferedInputStream sendGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpget);
            InputStream is = response.getEntity().getContent();
            return new BufferedInputStream(is);

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
