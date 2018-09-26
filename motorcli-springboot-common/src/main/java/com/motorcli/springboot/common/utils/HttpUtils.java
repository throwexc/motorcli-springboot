package com.motorcli.springboot.common.utils;

import com.motorcli.springboot.common.utils.http.TokenInfo;
import com.motorcli.springboot.common.utils.http.FileParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP 请求 工具类
 */
@Slf4j
public class HttpUtils {

    /**
     * 构建一个 GET 请求的地址
     * @param baseUrl 请求地址
     * @param params 参数
     * @return GET 请求地址加参数
     */
    public static String buildGetUrl(String baseUrl, Map<String, String> params) {
        if(params != null && params.size() > 0) {
            int index = 0;
            for(String key : params.keySet()) {
                String andFlag = "&";
                if(index == 0) {
                    andFlag = "?";
                }
                index++;
                baseUrl += andFlag + key + "=" + params.get(key);
            }
        }
        return baseUrl;
    }

    /**
     * 创建一个 GET 请求
     * @param url 请求地址
     * @return Http GET 请求对象
     */
    public static HttpGet createGet(String url) {
        HttpGet httpget = new HttpGet(url);
        return httpget;
    }

    /**
     * 发送 GET 请求
     * @param url 请求地址
     * @return GET 请求的返回结果
     */
    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * 发送 GET 请求
     * @param url 请求地址
     * @param params 请求参数
     * @return GET 请求的返回结果
     */
    public static String get(String url, Map<String, String> params) {
        return get(url, params, null);
    }

    /**
     * 发送 GET 请求
     * @param url 请求地址
     * @param token Auth 验证 Token 信息
     * @return GET 请求的返回结果
     */
    public static String get(String url, TokenInfo token) {
        return get(url, null, token);
    }

    /**
     * 发送 GET  请求
     * @param url 请求地址
     * @param params 请求参数
     * @param tokenInfo Auth 验证 Token 信息
     * @return GET 请求的返回结果
     */
    public static String get(String url, Map<String, String> params, TokenInfo tokenInfo) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String sendUrl = buildGetUrl(url, params);
        HttpGet get = createGet(sendUrl);

        if(tokenInfo != null && !StringUtils.isEmpty(tokenInfo.getToken())) {
            get.addHeader("Authorization", tokenInfo.getTokenPrefix() + tokenInfo.getToken());
        }

        String resultString = null;
        CloseableHttpResponse response = null;
        try {
            log.info("HTTP Client GET Method Send URL : " + sendUrl);
            // 执行get请求.
            response = httpClient.execute(get);

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 打印响应状态
            if (entity != null) {
                resultString = EntityUtils.toString(entity, "UTF-8");
                log.info("HTTP Client GET Method Result : " + resultString + "");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeHttp(response, httpClient);
        }
        return resultString;
    }

    /**
     * 发送 POST 请求
     * @param url 请求地址
     * @return POST 请求返回结果
     */
    public static String post(String url) {
        return post(url, null);
    }

    /**
     * 发送 POST 请求
     * @param url 请求地址
     * @param params 请求参数
     * @return POST 请求返回结果
     */
    public static String post(String url, Map<String, String> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        setupPostParams(post, params);

        return sendPost(httpClient, post, url, params);
    }

    /**
     * 发送 POST 请求
     * @param url 请求地址
     * @param params 请求参数
     * @param tokenInfo Auth 验证 Token 信息
     * @return POST 请求返回结果
     */
    public static String post(String url, Map<String, String> params, TokenInfo tokenInfo) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        if(tokenInfo != null && !StringUtils.isEmpty(tokenInfo.getToken())) {
            post.addHeader("Authorization", tokenInfo.getTokenPrefix() + tokenInfo.getToken());
        }

        setupPostParams(post, params);

        return sendPost(httpClient, post, url, params);
    }

    /**
     * 发送带有附件的 POST 请求
     * @param url 请求地址
     * @param fileParams 文件参数
     * @return POST 请求返回结果
     */
    public static String postFile(String url, FileParams fileParams) {
        return postFile(url, null, fileParams.getFileParams(), null);
    }

    /**
     * 发送带有附件的 POST 请求
     * @param url 请求地址
     * @param fileParams 文件参数
     * @param tokenInfo Auth 验证 Token 信息
     * @return POST 请求返回结果
     */
    public static String postFile(String url, FileParams fileParams, TokenInfo tokenInfo) {
        return postFile(url, null, fileParams.getFileParams(), tokenInfo);
    }

    /**
     * 发送带有附件的 POST 请求
     * @param url 请求地址
     * @param params 请求参数
     * @param fileParams 文件参数
     * @param tokenInfo Auth 验证 Token 信息
     * @return POST 请求返回结果
     */
    public static String postFile(String url, Map<String, String> params, Map<String, File> fileParams, TokenInfo tokenInfo) {
        if(CollectionUtils.isEmpty(fileParams)) {
           return post(url, params, tokenInfo);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        if(tokenInfo != null && !StringUtils.isEmpty(tokenInfo.getToken())) {
            post.addHeader("Authorization", tokenInfo.getTokenPrefix() + tokenInfo.getToken());
        }

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        for(String fileKey : fileParams.keySet()) {
            FileBody bin = new FileBody(fileParams.get(fileKey));

            builder.addPart(fileKey, bin);

            log.info("HTTP Client POST Add Multipart Entity, File Param Name Is [" + fileKey + "]");
        }

        if(!CollectionUtils.isEmpty(params)) {
            for(String key : params.keySet()) {
                builder.addTextBody(key, params.get(key));
            }
        }

        post.setEntity(builder.build());

        return sendPost(httpClient, post, url, params);
    }

    /**
     * 发送 POST 请求
     * 传参方式以 application/json 进行传输
     * @param url 请求地址
     * @param strBody json 字符串
     * @return POST 请求返回结果
     */
    public static String postJson(String url, String strBody) {
        if(StringUtils.isEmpty(strBody)) {
           return post(url, null);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        EntityBuilder builder = EntityBuilder.create();
        builder.setContentEncoding("UTF-8");
        builder.setContentType(ContentType.APPLICATION_JSON);
        builder.setBinary(strBody.getBytes());

        post.setEntity(builder.build());

        return sendPost(httpClient, post, url, null);
    }

    /**
     * 发送 POST 请求
     * 传参方式以 application/json 进行传输
     * @param url 请求地址
     * @param strBody json 字符串
     * @param tokenInfo Auth 验证 Token 信息
     * @return POST 请求返回结果
     */
    public static String postJson(String url, String strBody, TokenInfo tokenInfo) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        if(tokenInfo != null && !StringUtils.isEmpty(tokenInfo.getToken())) {
            post.addHeader("Authorization", tokenInfo.getTokenPrefix() + tokenInfo.getToken());
        }

        if(!StringUtils.isEmpty(strBody)) {
            EntityBuilder builder = EntityBuilder.create();
            builder.setContentEncoding("UTF-8");
            builder.setContentType(ContentType.APPLICATION_JSON);
            try {
                builder.setBinary(strBody.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                builder.setBinary(strBody.getBytes());
            }

            post.setEntity(builder.build());
        }

        return sendPost(httpClient, post, url, null);
    }

    /**
     * 设置 POST 请求参数
     * @param post POST 请求对象
     * @param params 参数
     */
    public static void setupPostParams(HttpPost post, Map<String, String> params) {
        if(params !=null && params.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<>();
            for(String key : params.keySet()) {
                nvps.add(new BasicNameValuePair(key, params.get(key)));
            }

            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭 HTTP 请求
     * @param response 请求的返回结果
     * @param httpClient 请求的客户端对象
     */
    public static void closeHttp(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        if(response != null) {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 POST 请求
     * @param httpClient HTTP 客户端
     * @param post 请求对象
     * @param url 请求地址
     * @param params 请求参数
     * @return POST 请求返回结果
     */
    private static String sendPost(CloseableHttpClient httpClient, HttpPost post, String url, Map<String, String> params) {
        String resultString = null;
        CloseableHttpResponse response = null;
        try {
            log.info("HTTP Client POST Method Send URL : " + url);
            if(!CollectionUtils.isEmpty(params)) {
                log.info("HTTP Client POST Method Params : " + params);
            }
            // 执行get请求.
            response = httpClient.execute(post);

            // 获取响应实体
            HttpEntity entity = response.getEntity();
            // 打印响应状态
            if (entity != null) {
                resultString = EntityUtils.toString(entity, "UTF-8");
                log.info("HTTP Client POST Method Result : " + resultString + "");
            }
        } catch (IOException e) {
            log.error("Http Post Error ", e);
        } finally {
            closeHttp(response, httpClient);
        }
        return resultString;
    }

    /**
     * 构建 Auth 验证 Token 对象
     * HTTP 头信息中 Authorization 值的前缀默认为 Bearer
     * @param token 口令字符串
     * @return 口令信息对象
     */
    public static TokenInfo buildTokenInfo(String token) {
        return new TokenInfo(token);
    }

    /**
     * 构建 Auth 验证 Token 对象
     * @param token 口令字符串
     * @param tokenPrefix HTTP 头信息中 Authorization 值的前缀
     * @return 口令信息对象
     */
    public static TokenInfo buildTokenInfo(String token, String tokenPrefix) {
        return new TokenInfo(token, tokenPrefix);
    }

    /**
     * 构建文件上传参数
     * @param file 文件对象
     * @return 文件上传参数
     */
    public static FileParams buildFileParams(File file) {
        return new FileParams(file);
    }

    /**
     * 构建文件上传参数
     * @param files 文件对象集合
     * @return 文件上传参数
     */
    public static FileParams buildFileParams(List<File> files) {
        return new FileParams(files);
    }
}
