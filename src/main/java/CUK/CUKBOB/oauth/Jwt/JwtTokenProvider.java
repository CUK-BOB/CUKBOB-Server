package CUK.CUKBOB.oauth.Jwt;

import CUK.CUKBOB.oauth.Config.ValueConfig;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static CUK.CUKBOB.oauth.Jwt.JwtValidationType.*;
import static io.jsonwebtoken.Header.JWT_TYPE;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.util.Base64.getEncoder;
import static javax.xml.crypto.dsig.SignatureProperties.TYPE;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final ValueConfig valueConfig;

    //토큰을 생성해줌
    //Authentication에서 사용자 정보를 추출하고,
    //expiration을 통해 토큰 만료시간 설정
    //jwt 생성
    public String generateToken(Authentication authentication, long expiration) {
        return Jwts.builder()
                .setHeaderParam(TYPE, JWT_TYPE)
                .setClaims(generateClaims(authentication))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    //jwt에 포함된 Claims 생성 (?)
    private Claims generateClaims(Authentication authentication) {
        Claims claims = Jwts.claims();
        claims.put("Id", authentication.getPrincipal());
        return claims;
    }

    //jwt 서명할 때 사용되는 secretKey 생성
    private SecretKey getSigningKey() {
        String encodedKey = getEncoder().encodeToString(valueConfig.getSecretKey().getBytes());
        return hmacShaKeyFor(encodedKey.getBytes());
    }

    //토큰 검증
    public JwtValidationType validateToken(String token) {
        try {
            getBody(token);
            return VALID_JWT;
        } catch (MalformedJwtException exception) {
            log.error(exception.getMessage());
            return INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException exception) {
            log.error(exception.getMessage());
            return EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException exception) {
            log.error(exception.getMessage());
            return UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException exception) {
            log.error(exception.getMessage());
            return EMPTY_JWT;
        }
    }

    //토큰에서 Claims 추출
    private Claims getBody(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //추출한 Claims안에 Id라는 키로 저장된 값을 Long타입으로 변환
    public Long getUserFromJwt(String token) {
        Claims claims = getBody(token);
        log.info("🎯 JWT 파싱된 ID: {}", claims.get("Id")); // 이거 추가!
        return Long.parseLong(claims.get("Id").toString());
    }

}
