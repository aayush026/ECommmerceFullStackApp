package com.ecomm.ayush.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTTokenProvider {
	private final SecretKey key;
	  public JWTTokenProvider() {
	        String secret = JWTConstants.SECRET_KEY;
	        if (secret.length() < 32) {
	            throw new IllegalArgumentException("Secret key must be at least 32 characters long");
	        }
	        this.key = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
	    }

	public String generateToken(Authentication auth) {
		SecretKey key=new SecretKeySpec(JWTConstants.SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
		return Jwts.builder().setSubject(auth.getName()).setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + 846000000)).claim("email", auth.getName()).signWith(key)
				.compact();
	}

	public String getEmailFromJwtToken(String jwt) {
		jwt = jwt.substring(7);
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		String email = String.valueOf(claims.get("email"));
		return email;
	}

	public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> auths = new HashSet<>();
		for (GrantedAuthority authority : collection) {
			auths.add(authority.getAuthority());
		}
		return String.join(",", auths);
	}
}
