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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exceptions.CouponServiceException;
import app.core.exceptions.CouponSystemException;
import app.core.services.CustomerService;

@RestController
@RequestMapping("rest/customer")
@CrossOrigin
public class CustomerController extends ClientController {

	private CustomerService service;

	@Autowired
	public CustomerController(CustomerService service) {
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

	private void initCustomer(String token) {
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
		this.service.setCustomerId(id);
		this.service.setCustomerById(id);
	}

	@PostMapping("purchase/coupon")
	public String purchaseCoupon(@RequestBody Integer couponId, @RequestHeader String token) {
		try {
			this.initCustomer(token);
			return this.service.addCouponPurchase(couponId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("remove/coupon/{couponId}")
	public void removeCouponPurchase(@PathVariable int couponId, @RequestHeader String token) {
		try {
			this.initCustomer(token);
			this.service.deleteCouponPurchase(couponId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/coupon/all")
	public List<Coupon> getAllCoupons(@RequestHeader String token) {
		try {
			this.initCustomer(token);
			return this.service.getAllCoupons();
		} catch (CouponServiceException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("get/coupon/amount/{couponId}")
	public int getCouponPurchaseAmount(@PathVariable int couponId, @RequestHeader String token)
			throws CouponSystemException {
		try {
			this.initCustomer(token);
			return this.service.getCouponPurchaseAmount(couponId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/coupon")
	public List<Coupon> getCustomerCoupons(@RequestHeader String token) {
		try {
			this.initCustomer(token);
			return this.service.getCustomerCoupons();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/coupon/category/{category}")
	public List<Coupon> getCustomerCouponsByCategory(@PathVariable Category category, @RequestHeader String token) {
		try {
			this.initCustomer(token);
			return this.service.getCustomerCouponsByCategory(category);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/coupon/price/{maxPrice}")
	public List<Coupon> getCustomerCouponsByMaxPrice(@PathVariable double maxPrice, @RequestHeader String token) {
		try {
			this.initCustomer(token);
			return this.service.getCustomerCouponsByMaxPrice(maxPrice);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/customer")
	public Customer getCustomerDetails(@RequestHeader String token) {
		try {
			this.initCustomer(token);
			return this.service.getCustomerDetails();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
