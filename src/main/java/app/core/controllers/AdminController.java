package app.core.controllers;

import java.util.List;

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

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.services.AdminService;

@RestController
@RequestMapping("rest/admin")
@CrossOrigin
public class AdminController extends ClientController {

	private AdminService service;

	@Autowired
	public AdminController(AdminService service) {
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

	@PostMapping("add/company")
	public int addCompany(@RequestBody Company company, @RequestHeader String token) {
		try {
			return this.service.addCompany(company);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("update/company")
	public String updateCompany(@RequestBody Company company, @RequestHeader String token) {
		try {
			return this.service.updateCompany(company);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@DeleteMapping("remove/company/{companyId}")
	public String deleteCompany(@PathVariable int companyId, @RequestHeader String token) {
		try {
			return this.service.deleteCompany(companyId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("get/company")
	public List<Company> getAllCompanies(@RequestHeader String token) {
		try {
			return this.service.getAllCompanies();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/company/{companyId}")
	public Company getOneCompany(@PathVariable int companyId, @RequestHeader String token) {
		try {
			return this.service.getCompany(companyId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PostMapping("add/customer")
	public void addCustomer(@RequestBody Customer customer, @RequestHeader String token) {
		try {
			this.service.addCustomer(customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PutMapping("update/customer")
	public String updateCustomer(@RequestBody Customer customer, @RequestHeader String token) {
		try {
			return this.service.updateCustomer(customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@DeleteMapping("remove/customer/{customerId}")
	public String deleteCustomer(@PathVariable int customerId, @RequestHeader String token) {
		try {
			return this.service.deleteCustomer(customerId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("get/customer")
	public List<Customer> getAllCustomers(@RequestHeader String token) {
		try {
			return this.service.getAllCustomers();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("get/customer/{customerId}")
	public Customer getOneCustomer(@PathVariable int customerId, @RequestHeader String token) {
		try {
			return this.service.getCustomer(customerId);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
