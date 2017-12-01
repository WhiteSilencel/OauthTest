package com.example.oauth.http;


import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;


/**
 * @author LiMengXi
 */
public class OkHttpUtils {
    /**
     * 连接超时时间
     */
    private static final long DEFAULT_MILLISECONDS = 10L;

    /**
     * volatile 在主内存有，其他线程不单独缓存
     */
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;

    /**
     * 构造方法
     * 提示:设置超时时间在OKhttp3.0以后,使用build的方式进行
     * @param okHttpClient
     */
    private OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                    .build();
        } else {  //ADD
            mOkHttpClient = okHttpClient;
        }

    }

    /**
     * 双重校验单例
     * 初始化Client
     * @param okHttpClient
     * @return
     */
    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                /**
                 * 在单例模式下，创建OkHttpUtils实例，代表OkHttpUtils类只有一个OkHttpClient实例
                 */
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取实例
     *
     * @return
     */
    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    /**
     * 03.创建一个call对象，参数就是Request请求对象，发送请求
     * Get请求
     *
     * @param url    url
     * @param params HashMap参数
     * @return
     */
    public Call get(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            /*
                获取服务器返回的参数列表
             */
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            sb.append("?");
            /*
                拼接成url的格式
             */
            for (Map.Entry<String, String> entry : entrySet) {
                sb.append(entry.getKey());
                sb.append("=");
                try {
                    sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append("&");
            }
            /*
                去掉最后一个&
             */
            sb.deleteCharAt(sb.length() - 1);
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url + sb.toString()).get().build();
        return mOkHttpClient.newCall(request);
    }


    /**
     * Post 请求
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    public Call post(String url, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        //构建请求参数form
        FormBody formBody = formBuilder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        return mOkHttpClient.newCall(request);
    }

    /**
     * PUT请求
     * @param put_url
     * @param params
     * @return
     * @throws HttpException
     * @throws IOException
     */
    public static String PUT(String put_url, NameValuePair[] params) throws HttpException, IOException {
        HttpClient httpClient = new HttpClient();
        PutMethod httpPut = new PutMethod(put_url);
        httpPut.setQueryString(params);   //以键值对形式传递参数
        httpPut.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        httpClient.executeMethod(httpPut);
        String res = httpPut.getResponseBodyAsString();
        return res;
    }

    /**
     * DELETE请求
     * @param url
     * @param dataForm
     * @return
     */
    public static String DELETE(String url,Map<String,Object> dataForm){
        HttpClient httpClient = new HttpClient();
        DeleteMethod deleteMethod = new DeleteMethod(url);

        List<NameValuePair> data = new ArrayList<NameValuePair>();
        if(dataForm!=null){
            Set<String> keys = dataForm.keySet();
            for(String key:keys){
                NameValuePair nameValuePair = new NameValuePair(key, (String) dataForm.get(key));
                data.add(nameValuePair);
            }
        }
        deleteMethod.setQueryString(data.toArray(new NameValuePair[0]));
        try {
            int statusCode = httpClient.executeMethod(deleteMethod);
            if (statusCode != HttpStatus.SC_OK) {
                return "Method failed: " + deleteMethod.getStatusLine();
            }

            // Read the response body.
            byte[] responseBody = deleteMethod.getResponseBody();
            // Deal with the response.
            // Use caution: ensure correct character encoding and is not binary data
            return new String(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            deleteMethod.releaseConnection();
        }
        return null;
    }
}

