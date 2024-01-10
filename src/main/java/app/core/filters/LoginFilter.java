package app.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import app.core.utils.JwtUtil;
import app.core.utils.JwtUtil.ClientDetails;

public class LoginFilter implements Filter {
	private JwtUtil jwtUtil;

	public LoginFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//			throws IOException, ServletException {
//		System.out.println("login filter before");
//		HttpServletRequest req = (HttpServletRequest) request;
//		String token = req.getHeader("token");
//		System.out.println(token);
//		try {
//			jwtUtil.extractClient(token);
//			chain.doFilter(request, response);
//			System.out.println("login filter after");
//		} catch (Exception e) {
//			e.printStackTrace();
//			HttpServletResponse resp = (HttpServletResponse) response;
//			resp.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not logged in");
//		}
//	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String token = req.getHeader("token");
		System.out.println("===== LOGIN FILTER TOKEN: " + token);

		if (token == null && req.getMethod().equals("OPTIONS")) {
			System.out.println("this is preflight request: " + req.getMethod());
			chain.doFilter(request, response);
			return;
		}

		try {
			ClientDetails clientDetails = jwtUtil.extractClient(token);
			System.out.println("===== LOGIN FILTER: " + clientDetails);
			chain.doFilter(request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			resp.addHeader("Access-Control-Allow-Origin", "http://localhost:4200");
			resp.sendError(HttpStatus.UNAUTHORIZED.value(), "not logged in");
		}

	}

}
