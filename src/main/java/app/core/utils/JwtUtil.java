package app.core.utils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import app.core.services.login.LoginManager.ClientType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
public class JwtUtil {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ClientDetails {
		public int id;
		public String email;
		public ClientType type;
	}

	private String signatureAlgorithm = SignatureAlgorithm.HS256.getJcaName();
	@Value("${jwt.util.secret.key}")
	private String encodedSecretKey;
	private Key key;
	@Value("${jwt.util.chrono.unit}")
	private String chronoUnit;
	@Value("${jwt.util.chrono.unit.number}")
	private int unitNumber;

	@PostConstruct
	public void init() {
		this.key = new SecretKeySpec(Base64.getDecoder().decode(encodedSecretKey), signatureAlgorithm);
	}

	public String generateToken(ClientDetails details) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("type", details.getType());
		claims.put("id", details.getId());
		return createToken(claims, details.getEmail());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		Instant now = Instant.now();
		Instant expiration = now.plus(this.unitNumber, ChronoUnit.valueOf(this.chronoUnit));
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date.from(now))
				.setExpiration(Date.from(expiration)).signWith(key).compact();
	}

	private Claims extractAllClaims(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
		return parser.parseClaimsJws(token).getBody();
	}

	public boolean isTokenExpired(String token) {
		try {
			extractAllClaims(token);
			return false;
		} catch (ExpiredJwtException e) {
			return true;
		}
	}

	public ClientDetails extractClient(String token) {
		Claims claims = extractAllClaims(token);
		ClientDetails details = new ClientDetails();
		details.setId(claims.get("id", Integer.class));
		details.setEmail(claims.getSubject());
		details.setType(ClientType.valueOf(claims.get("type", String.class)));
		return details;
	}

	public String extractSubject(String token) {
		return extractAllClaims(token).getSubject();
	}

	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}

	public Date extractIssuedAt(String token) {
		return extractAllClaims(token).getIssuedAt();
	}

}