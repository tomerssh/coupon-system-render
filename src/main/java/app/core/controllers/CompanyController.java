package app.core.controllers;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exceptions.CouponSystemException;
import app.core.services.CompanyService;

@RestController
@RequestMapping("rest/company")
@CrossOrigin
public class CompanyController extends ClientController {

	private CompanyService service;

	@Autowired
	public CompanyController(CompanyService service) {
		this.service = service;
	}

	@Override
	public boolean login(String email, String password) {
		try {
			return this.service.login(email, password);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	private void initCompany(String token) {
		String[] chunks = token.split("\\.");

		Base64.Decoder decoder = Base64.getUrlDecoder();

		String header = new String(decoder.decode(chunks[0]));
		String payload = new String(decoder.decode(chunks[1]));

		Map<String, Object> result = new HashMap<>();
		try {
			result = new ObjectMapper().readValue(payload, HashMap.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		int id = (int) result.get("id");
		this.service.setCompanyId(id);
		this.service.setCompanyById(id);
	}

	@PostMapping("add/coupon")
	public int addCoupon(@RequestBody Coupon coupon, @RequestHeader String token) {
		try {
			this.initCompany(token);
			return this.service.addCoupon(coupon);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/coupon/{couponId}")
	public Coupon getCouponById(@PathVariable int couponId, @RequestHeader String token) {
		try {
			this.initCompany(token);
			return this.service.getCouponById(couponId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("update/coupon")
	public void updateCoupon(@RequestBody Coupon coupon, @RequestHeader String token) {
		try {
			this.initCompany(token);
			this.service.updateCoupon(coupon);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@DeleteMapping("remove/coupon/{couponId}")
	public String deleteCoupon(@PathVariable int couponId, @RequestHeader String token) {
		try {
			this.initCompany(token);
			return this.service.deleteCoupon(couponId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("get/coupon")
	public List<Coupon> getCompanyCoupons(@RequestHeader String token) {
		try {
			this.initCompany(token);
			return this.service.getCompanyCoupons();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/coupon/category/{category}")
	public List<Coupon> getCompanyCoupons(@PathVariable Category category, @RequestHeader String token) {
		try {
			this.initCompany(token);
			return this.service.getCompanyCoupons(category);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/coupon/price/{maxPrice}")
	public List<Coupon> getCompanyCoupons(@PathVariable double maxPrice, @RequestHeader String token) {
		try {
			this.initCompany(token);
			return this.service.getCompanyCoupons(maxPrice);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/company")
	public Company getCompanyDetails(@RequestHeader String token) {
		try {
			this.initCompany(token);
			return this.service.getCompanyDetails();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
