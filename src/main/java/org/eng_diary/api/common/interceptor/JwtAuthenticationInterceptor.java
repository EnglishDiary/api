package org.eng_diary.api.common.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eng_diary.api.common.context.UserContext;
import org.eng_diary.api.common.context.UserContextHolder;
import org.eng_diary.api.common.util.JwtTokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // OPTIONS 요청은 항상 허용
        }
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            if (jwtTokenUtil.validateToken(jwt)) {
                Claims claims = jwtTokenUtil.extractAllClaims(jwt);

                String loginId = claims.getSubject();
                Long memberId = Long.parseLong(claims.get("memberId").toString());

                UserContext user = UserContext.builder()
                        .loginId(loginId)
                        .memberId(memberId)
                        .build();

                UserContextHolder.setUserContext(user);
                return true;
            }
        }

        log.error("Unauthorized Request");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 요청 처리 후 ThreadLocal 정리 (메모리 누수 방지)
        UserContextHolder.clear();
    }
}
