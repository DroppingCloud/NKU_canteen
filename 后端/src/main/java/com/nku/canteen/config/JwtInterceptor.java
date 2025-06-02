package com.nku.canteen.config;

import com.nku.canteen.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT认证拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 前置拦截，验证用户是否登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        
        // 如果请求头中没有Authorization信息则直接放行(后续通过权限注解校验)
        if (!StringUtils.hasText(token)) {
            return true;
        }

        // 如果token不为空，检查token格式
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 验证token是否有效
        if (jwtUtil.validateToken(token)) {
            // 将用户ID和用户类型添加到请求属性中，方便后续使用
            request.setAttribute("userId", jwtUtil.getUserIdFromToken(token));
            request.setAttribute("userType", jwtUtil.getUserTypeFromToken(token));
            return true;
        }

        // Token无效，返回401状态码
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":2,\"msg\":\"未授权或token已过期\"}");
        return false;
    }
} 