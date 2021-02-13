package com.inlife.webhook;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class CorrelationInterceptor extends HandlerInterceptorAdapter {

    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    private static final String CORRELATION_ID_LOG_VAR_NAME = "correlationId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String correlationId = Optional.ofNullable(request.getHeader(CORRELATION_ID_HEADER_NAME))
                .orElse("in_app_" + UUID.randomUUID().toString());
        MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
        log.info("CorrelationId: " + correlationId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("Inbound HTTP request ended ");
        response.setHeader(CORRELATION_ID_HEADER_NAME, MDC.get(CORRELATION_ID_LOG_VAR_NAME));
        MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
    }
}
