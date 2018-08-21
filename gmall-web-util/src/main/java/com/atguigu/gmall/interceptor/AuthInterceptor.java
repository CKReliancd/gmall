package com.atguigu.gmall.interceptor;


import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.util.HttpClientUtil;
import com.atguigu.gmall.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//@Component注解加入spring框架，拦截方法
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 判断当前访问的方法是否需要认证拦截
        //这个反射类专门反射方法信息，来提供方法里的注解信息
        HandlerMethod method = (HandlerMethod) handler;
        LoginRequire methodAnnotation = method.getMethodAnnotation(LoginRequire.class);

        //如果没有注解标识，则直接放行
        if (methodAnnotation == null) {
            return true;
        }

        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);

        //如果用户是新登陆的，则在访问的字符串里面有一个新token
        String newToken = request.getParameter("newToken");

        String token = "";

//        oldToken空，新token空，用户从没登陆

        //oldToken不空，新token空，用户登陆过
        if (StringUtils.isNotBlank(oldToken) && StringUtils.isBlank(newToken)) {
            //登陆过
            token = oldToken;
        }

        //oldToken空，新token不空，用户第一次登陆
        if (StringUtils.isBlank(oldToken) && StringUtils.isNotBlank(newToken)) {
            //第一次登陆
            token = newToken;
        }

        //oldToken不空，新token不空，用户登录过期
        if (StringUtils.isNotBlank(oldToken) && StringUtils.isNotBlank(newToken)) {
            //登陆过期
            token = newToken;
        }

        //如果方法需要登陆，且token是空，则重定向到登陆页面
        if (methodAnnotation.ifNeedSuccess() && StringUtils.isBlank(token)) {
            StringBuffer requestURL = request.getRequestURL();
            response.sendRedirect("http://localhost:8085/index?returnURL=" + requestURL);
            return false;
        }


        String success = "";
        if (StringUtils.isNotBlank(token)) {
            // 远程访问passport，验证token
            success = HttpClientUtil.doGet("http://localhost:8085/verify?token=" + token + "&salt=" + getMyIp(request));

        }

        //如果验证不成功，且方法需要登录，则重定向到登录页面
        if (!success.equals("success") && methodAnnotation.ifNeedSuccess()) {
            response.sendRedirect("http://localhost:8085/index");
            return false;
        }

        //只有购物车方法是token验证不成功且需要方法需要登陆时，才会通过
        if (!success.equals("success") && !methodAnnotation.ifNeedSuccess()) {

            // 购物车方法

            return true;
        }

        if (success.equals("success")) {
            // cookie验证通过，重新刷新cookie的过期时间
            CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);

            //将用户信息放入到应用请求
            Map userMap = JwtUtil.decode("atguigu", token, getMyIp(request));
            request.setAttribute("userId", userMap.get("userId"));
            request.setAttribute("nickName", userMap.get("nickName"));

        }


        return true;
    }

    private String getMyIp(HttpServletRequest request) {
        String ip = "";
        ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();//直接获取ip
        }
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";//设置一个虚拟ip
        }
        return ip;
    }

}
