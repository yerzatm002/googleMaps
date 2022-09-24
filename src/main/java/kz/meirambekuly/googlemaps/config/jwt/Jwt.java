package kz.meirambekuly.googlemaps.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class Jwt {
    private static final String key = "FZMMlPgVdl+mFWotKCDxbCj8c7u1LYVfNlgqBdnVASrVJs+vK6ddSpUOX/DnJIO+3RyCKSQN3AqMTdRxqRhGhA==";
    private static final Integer expirationDay = 7;

    public static String generateJwt(String email, Collection<? extends GrantedAuthority> authorities) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        for (RoleDto role : roleDTO) {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//        }
        return Jwts.builder().setSubject(email)
                .claim("authorities",
                        authorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .setIssuedAt(new Date()).setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(expirationDay)))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(key)))
                .compact();
    }

    public static Claims decodeJwt(String jwt) {
        return Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(key))).parseClaimsJws(jwt).getBody();
    }
}
