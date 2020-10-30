package com.example.demo.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Slf4j
public class JwtUtil {
    private static final String SECRET_KEY = "secret";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String REQUEST_HEADER_KEY = "Authorization";
    private static final long EXPIRATION_TIME = 86_400_000;

    public static String generateToken(String username){
        Date expiryDate = new Date(System.currentTimeMillis()+EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String getUsernameFromToken(HttpServletRequest request){
        String token = request.getHeader(REQUEST_HEADER_KEY);
        if(token!=null){
            try{
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token.replace(TOKEN_PREFIX,"").trim())
                        .getBody();
                return claims.getSubject();
            }catch (MalformedJwtException ex) {
                log.error("Invalid JWT token");
            } catch (ExpiredJwtException ex) {
                log.error("Expired JWT token");
            } catch (UnsupportedJwtException ex) {
                log.error("Unsupported JWT token");
            } catch (IllegalArgumentException ex) {
                log.error("JWT claims string is empty.");
            }catch (SignatureException ex){
                log.error("JWT signature does not match locally computed signature.");
            }
        }
        else{
            log.warn("Token not found");
        }
        return null;
    }

}
