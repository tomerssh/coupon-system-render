package app.core.controllers;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.exceptions.CouponSystemException;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import app.core.services.login.LoginManager;
import app.core.services.login.LoginManager.ClientType;
import app.core.utils.JwtUtil;
import app.core.utils.JwtUtil.ClientDetails;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {
	private LoginManager loginManager;
	private JwtUtil jwtUtil;

	@Autowired
	public LoginController(LoginManager loginManager, JwtUtil jwtUtil) {
		this.loginManager = loginManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping
	public Cookie login(@RequestParam String email, @RequestParam String password, ClientType clientType) {
		try {
			ClientService service = this.loginManager.login(email, password, clientType);
			int id = extractId(service);
			String token = jwtUtil.generateToken(new ClientDetails(id, email, clientType));
			Cookie cookie = new Cookie("token", token);
			cookie.setHttpOnly(true);
			return cookie;
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

	private int extractId(ClientService service) {
		try {
			if (service instanceof CompanyService) {
				CompanyService companyService = (CompanyService) service;
				return companyService.getCompanyDetails().getId();
			} else if (service instanceof CustomerService) {
				CustomerService customerService = (CustomerService) service;
				return customerService.getCustomerDetails().getId();
			}
			// in case service is an instance of AdminService return 0, if the login
			// credentials are wrong or the client type is null
			// loginManager.login will throw an exception
			else {
				return 0;
			}
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

}
