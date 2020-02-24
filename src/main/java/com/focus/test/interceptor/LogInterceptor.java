package com.focus.test.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*@Component
@Order(Ordered.LOWEST_PRECEDENCE)*/
public class LogInterceptor implements HandlerInterceptor {
    protected final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("receiveTimestamp", System.currentTimeMillis());
        if (request.getQueryString() != null) {
            logger.info("Receive a request from {} : {} {} {}", request.getRemoteAddr(),
                    request.getMethod(), request.getRequestURI(), request.getQueryString());
        } else {
            logger.info("Receive a request from {}: {} {}", request.getRemoteAddr(),
                    request.getMethod(), request.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("Request completed in {} ms, status {}",
                System.currentTimeMillis() - (Long) request.getAttribute("receiveTimestamp"),
                response.getStatus());
    }
}
