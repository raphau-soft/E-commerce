package com.devraf.e_commerce.service;

import com.devraf.e_commerce.db.entity.Token;
import com.devraf.e_commerce.db.entity.User;
import com.devraf.e_commerce.db.repository.TokenDAO;
import com.devraf.e_commerce.utils.Constants;
import com.devraf.e_commerce.utils.TokenEnum;
import com.devraf.e_commerce.utils.exception.TokenNotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Autowired
    private TokenDAO tokenDAO;

    @Value("${security.jwt.secret.key}")
    private String SECRET;

    public void deleteToken(String token) {
        tokenDAO.deleteByToken(token);
    }

    public void deleteExpiredTokens() {
        tokenDAO.deleteExpiredTokens();
    }

    public Token createToken(User user, TokenEnum tokenType) {
        Date now = new Date();
        Token token;
        Date expirationAt = new Date(System.currentTimeMillis() + Constants.EXPIRATION_TIMES.get(tokenType));

        if(tokenType.equals(TokenEnum.AUTH_TOKEN)) {
            token = tokenDAO.findByUserIdAndTokenType(user.getId(), tokenType.name())
                    .map(existingToken -> updateTokenIfExpired(existingToken, user, now, expirationAt))
                    .orElseGet(() -> createNewToken(user, tokenType, now, expirationAt));
        } else {
            token = createNewToken(user, tokenType, now, expirationAt);
        }

        tokenDAO.save(token);
        return token;
    }

    private Token updateTokenIfExpired(Token existingToken, User user, Date now, Date expirationAt) {
        try {
            verifyToken(existingToken.getToken());
        } catch (ExpiredJwtException e) {
            existingToken.setToken(generateToken(user.getEmail(), now, expirationAt, existingToken.getTokenType()));
            existingToken.setExpiredAt(expirationAt);
            existingToken.setUpdatedAt(now);
        }
        return existingToken;
    }

    private Token createNewToken(User user, TokenEnum tokenType, Date now, Date expirationAt) {
        return Token.builder()
                .id(0L)
                .token(generateToken(user.getEmail(), now, expirationAt, tokenType.name()))
                .tokenType(tokenType.name())
                .user(user)
                .active(true)
                .createdAt(now)
                .expiredAt(expirationAt)
                .build();
    }

    private String generateToken(String username, Date createdAt, Date expirationAt, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tpe", tokenType);

        return Jwts.builder()
                .claims().add(claims)
                .subject(username)
                .issuedAt(createdAt)
                .expiration(expirationAt)
                .and()
                .signWith(getSignKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Jws<Claims> verifyToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token);
    }

    private Claims extractAllClaims(String token) {
        return verifyToken(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, TokenEnum tokenEnum) {
        return tokenDAO.findByTokenAndTokenType(token, tokenEnum.name())
                .filter(Token::getActive)
                .filter(t -> !isTokenExpired(t.getToken()))
                .isPresent();
    }

    public Token getTokenOrThrow(String token, TokenEnum tokenEnum) {
        return tokenDAO.findByTokenAndTokenType(token, tokenEnum.name())
                .filter(Token::getActive)
                .filter(t -> !isTokenExpired(t.getToken()))
                .orElseThrow(
                        () -> new TokenNotFoundException("Token invalid: " + token)
                );
    }

    public Token getTokenByToken(String token) {
        return tokenDAO.findByToken(token)
                .orElseThrow(TokenNotFoundException::new);
    }
}
