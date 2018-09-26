package com.motorcli.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope("prototype")
public class MotorWebControllerBase {

    /** 用户 Session KEY **/
    protected static final String SESSION_USER_INFO = "session_user_info";

    /** CODE KEY **/
    protected static final String CODE = "code";

    /** 单条返回结果 KEY **/
    protected static final String RECORD = "record";

    /** 多条返回结果 KEY **/
    protected static final String ITEMS = "items";

    /** 消息 KEY **/
    protected static final String MSG = "msg";

    /** 正确返回结果编码 **/
    public static int SUCCESS_CODE = 1;

    /** 正确返回结果消息 **/
    public static String SUCCESS_MSG = "操作成功";

    @Autowired
    private ObjectMapper objectMapper;

    private <T> ModelMap getModelMap(T model, String resultType) {
        ModelMap result = new ModelMap();
        result.put(CODE, SUCCESS_CODE);
        result.put(resultType, model);
        result.put(MSG, SUCCESS_MSG);
        return result;
    }

    /**
     * 创建单条记录返回结果
     * @param model 结果对象
     * @param <T> 结果类型
     * @return 返回结果
     */
    public <T> ModelMap createRecordResult(T model) {
        return getModelMap(model, RECORD);
    }

    /**
     * 创建多条记录返回结果
     * @param items 结果对象
     * @param <T> 结果类型
     * @return 返回结果
     */
    public <T> ModelMap createItemsResult(Collection<T> items) {
        return getModelMap(items, ITEMS);
    }

    /**
     * 返回上传结果
     * @param result 结果对象
     * @param response Http 返回对象
     */
    protected void responseUploadData(Object result, HttpServletResponse response) {
        response.setContentType("textml; charset=UTF-8");
        PrintWriter writer = null;
        try {
            StringBuffer strb = new StringBuffer("<html><body><textarea>");
            strb.append(this.objectMapper.writeValueAsString(result));
            strb.append("</textarea></body></html>");
            writer = response.getWriter();
            writer.write(strb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 获取绝对路径
     * @param request Http 请求对象
     * @param path 相对路径
     * @return 绝对路径
     */
    protected String getPath(HttpServletRequest request, String path) {
        String rootPath = request.getServletContext().getRealPath("/");
        return rootPath + path;
    }

    /**
     * 把请求参数转换为 Map 对象
     * @param request Http 请求对象
     * @return 参数集合
     */
    public static Map<String, Object> getParamsToMap(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            map.put(name, request.getParameter(name));
        }
        return map;
    }

    /**
     * 清除 Session
     * @param session HttpSession 对象
     */
    protected void clearSession(HttpSession session) {
        Enumeration<String> names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            session.removeAttribute(name);
        }
    }

    /**
     * 获取 IP 地址
     * @param request 请求对象
     * @return IP 地址
     */
    protected String getIp(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1")|| ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
