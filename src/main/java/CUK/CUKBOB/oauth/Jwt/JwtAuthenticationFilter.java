package CUK.CUKBOB.oauth.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static CUK.CUKBOB.oauth.Jwt.JwtValidationType.VALID_JWT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_HEADER = "Bearer ";
    private static final String BLANK = "";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            val token = getAccessTokenFromRequest(request);
            if (hasText(token) && jwtTokenProvider.validateToken(token) == VALID_JWT) {
                val authentication = new UserAuthentication(getId(token), null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }

        filterChain.doFilter(request, response);

    }

    //요청에서 엑세스 토큰 추출
    private String getAccessTokenFromRequest(HttpServletRequest request) {
        return isContainsAccessToken(request) ? getAuthorizationAccessToken(request) : null;
    }

    private boolean isContainsAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        return authorization != null && authorization.startsWith(BEARER_HEADER);
    }

    private String getAuthorizationAccessToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION).replaceFirst(BEARER_HEADER, BLANK);
    }

    private long getId(String token) {
        return jwtTokenProvider.getUserFromJwt(token);
    }
}