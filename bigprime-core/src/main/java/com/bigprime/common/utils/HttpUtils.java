package com.bigprime.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.bigprime.common.exception.ServerException;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyw
 * @version 1.0
 */
public class HttpUtils {
    private static RestTemplate restTemplate;

    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();

        Map<String, String> params = new HashMap<>();
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            String value = request.getParameter(parameter);
            if (StrUtil.isNotBlank(value)) {
                params.put(parameter, value);
            }
        }

        return params;
    }

    public static String getDomain() {
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    public static String getOrigin() {
        HttpServletRequest request = getHttpServletRequest();
        return request.getHeader(HttpHeaders.ORIGIN);
    }

    public static <T> T sendHttpPost(String url, Object param, Class<T> clazz) {
        HttpResponse response = HttpRequest
                .post(url)
                .header(Header.CONTENT_TYPE, "application/json")
                .timeout(30000)
                .body(JSONUtil.toJsonStr(param))
                .execute();
        return getResult(clazz, response);
    }

    public static <T> T sendHttpGet(String url, HashMap<String, Object> paramMap, Class<T> clazz) {
        HttpResponse response = HttpRequest
                .get(url)
                .form(paramMap)
                .header(Header.CONTENT_TYPE, "application/json")
                .timeout(30000)
                .execute();
        return getResult(clazz, response);
    }

    private static <T> T getResult(Class<T> clazz, HttpResponse response) {
        if (response.getStatus() == HttpStatus.OK.value()) {
            String body = response.body();
            boolean isJson = JSONUtil.isTypeJSON(body);
            if (isJson) {
                return JSONUtil.toBean(body, clazz);
            }else{
                return ConvertUtils.ConvertType(body, clazz);
            }
        } else {
            throw new ServerException("Failed to call API");
        }
    }

    public static <T> T sendServerPost(String url, Object param, Class<T> clazz) {
        if (restTemplate == null) {
            restTemplate = SpringUtil.getBean(RestTemplate.class);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(param, headers);
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, entity, clazz);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            throw new ServerException("Failed to call API");
        }
    }
}
