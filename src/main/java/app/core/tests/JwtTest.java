package app.core.tests;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import app.core.services.login.LoginManager.ClientType;
import app.core.utils.JwtUtil;
import app.core.utils.JwtUtil.ClientDetails;

@Component
@Order(4)
public class JwtTest implements CommandLineRunner {

	@Autowired
	private JwtUtil jwtUtil;
	private static int index = 101;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("=== JWT TEST ========================================");
		ClientDetails client = new ClientDetails(index++, "company@mail", ClientType.COMPANY);
		System.out.println("ClientDetails: " + client);
		String token = jwtUtil.generateToken(client);
		System.out.println("token: " + token);
		System.out.println("extractSubject: " + jwtUtil.extractSubject(token));
		System.out.println("extractClient: " + jwtUtil.extractClient(token));
		System.out.println("extractIssuedAt: " + jwtUtil.extractIssuedAt(token));
		System.out.println("extractExpiration: " + jwtUtil.extractExpiration(token));
		System.out.println("isTokenExpired: " + jwtUtil.isTokenExpired(token));
		System.out.println("=====================================================");
		System.out.println("=== COOKIE TEST ========================================");
		Cookie cookie = new Cookie("token", token);
		cookie.setHttpOnly(true);
		System.out.println("name: " + cookie.getName());
		System.out.println("value: " + cookie.getValue());
		System.out.println("=====================================================");
	}

}
