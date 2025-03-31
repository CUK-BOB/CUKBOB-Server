package CUK.CUKBOB.oauth.Jwt;

//Jwt 관련 에러를 나타내는 enum
public enum JwtValidationType {
    VALID_JWT,
    INVALID_JWT_SIGNATURE,
    INVALID_JWT_TOKEN,
    EXPIRED_JWT_TOKEN,
    UNSUPPORTED_JWT_TOKEN,
    EMPTY_JWT
}
