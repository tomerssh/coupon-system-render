package app.core.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.exceptions.CouponServiceException;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

/**
 * Manages administrator interaction with the app.
 * 
 * @see ClientService
 */
@Service
@Transactional
@Scope("prototype")
public class AdminService extends ClientService {

	@Value("${admin.email:admin@admin.com}")
	private String adminEmail;
	@Value("${admin.password:admin}")
	private String adminPassword;

	@Autowired
	public AdminService(CompanyRepository companyRepo, CustomerRepository customerRepo, CouponRepository couponRepo) {
		super.companyRepo = companyRepo;
		super.customerRepo = customerRepo;
		super.couponRepo = couponRepo;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		return this.adminEmail.equals(email) && this.adminPassword.equals(password);
	}

	/**
	 * Add a company to the data storage.
	 * 
	 * @param company The company to be added.
	 * @return The company's id.
	 * @throws CouponSystemException
	 */
	public int addCompany(Company company) throws CouponSystemException {
		// check if a company with this name or email already exists
		if (!companyRepo.existsByNameOrEmail(company.getName(), company.getEmail())) {
			company = companyRepo.save(company);
			return company.getId();
		} else {
			throw new CouponServiceException("company with this name or email " + company.getName() + " : "
					+ company.getEmail() + " already exists");
		}
	}

	/**
	 * Update a given company's details.
	 * 
	 * @param company The new company.
	 * @throws CouponSystemException
	 */
	public String updateCompany(Company company) throws CouponSystemException {
		Optional<Company> opt = companyRepo.findById(company.getId());
		if (opt.isPresent()) {
			Company companyInDb = opt.get();
			companyInDb.setEmail(company.getEmail());
			companyInDb.setPassword(company.getPassword());
			companyInDb.setCoupons(company.getCoupons());
			return companyInDb.getName();
		} else {
			throw new CouponServiceException("company not found");
		}
	}

	/**
	 * Remove a company from the data storage using id.
	 * 
	 * @param companyId The company's id.
	 * @throws CouponSystemException
	 */
	public String deleteCompany(int companyId) throws CouponSystemException {
		Optional<Company> opt = companyRepo.findById(companyId);
		if (opt.isPresent()) {
			deleteCompanyCoupons(companyId);
			companyRepo.delete(opt.get());
			return opt.get().getName();
		} else {
			throw new CouponServiceException("company with id " + companyId + " not found");
		}
	}

	public void deleteCompanyCoupons(int companyId) throws CouponSystemException {
		Optional<Company> opt = companyRepo.findById(companyId);
		if (opt.isPresent()) {
			List<Coupon> companyCoupons = couponRepo.findAllByCompanyId(companyId);
			couponRepo.deleteAll(companyCoupons);
		} else {
			throw new CouponServiceException("company with id " + companyId + " not found");
		}
	}

	/**
	 * Return a list of all companies.
	 * 
	 * @return A list of all companies
	 * @throws CouponSystemException If an exception was caught while getting all
	 *                               companies
	 */
	public List<Company> getAllCompanies() throws CouponSystemException {
		return companyRepo.findAll();
	}

	/**
	 * Return a company using id
	 * 
	 * @param companyId The company's id
	 * @return A company object
	 * @throws CouponSystemException If an exception was caught while getting
	 *                               company
	 */
	public Company getCompany(int companyId) throws CouponSystemException {
		return companyRepo.findById(companyId)
				.orElseThrow(() -> new CouponServiceException("cannot find company with id: " + companyId));
	}

	/**
	 * Add a new customer to the system
	 * 
	 * @param customer The new customer to be added
	 * @throws CouponSystemException If an exception was caught while adding
	 *                               customer
	 */
	public int addCustomer(Customer customer) throws CouponSystemException {
		// check if a company with this name or email already exists
		if (!customerRepo.existsByEmailAndPassword(customer.getEmail(), customer.getPassword())) {
			customer = customerRepo.save(customer);
			return customer.getId();
		} else {
			throw new CouponServiceException("customer with this email and password " + customer.getEmail() + " : "
					+ customer.getPassword() + " already exists");
		}
	}

	/**
	 * Update an existing customer
	 * 
	 * @param customer The customer to update with new details
	 * @throws CouponSystemException If an exception was caught while updating
	 *                               customer
	 */
	public String updateCustomer(Customer customer) throws CouponSystemException {
		Optional<Customer> opt = customerRepo.findById(customer.getId());
		if (opt.isPresent()) {
			Customer customerInDb = opt.get();
			customerInDb.setEmail(customer.getEmail());
			customerInDb.setPassword(customer.getPassword());
			customerInDb.setCoupons(customer.getCoupons());
			return customerInDb.getFirstName() + " " + customerInDb.getLastName();
		} else {
			throw new CouponServiceException(
					"customer " + customer.getFirstName() + " " + customer.getLastName() + " not found");
		}
	}

	/**
	 * Removes a customer from the system
	 * 
	 * @param customerId The customer id
	 * @throws CouponSystemException If an exception was caught while removing
	 *                               customer
	 */
	public String deleteCustomer(int customerId) throws CouponSystemException {
		Optional<Customer> opt = customerRepo.findById(customerId);
		if (opt.isPresent()) {
			deleteCustomerCoupons(customerId);
			customerRepo.delete(opt.get());
			return opt.get().getFirstName() + " " + opt.get().getLastName();
		} else {
			throw new CouponServiceException("customer with id " + customerId + " not found");
		}
	}

	public void deleteCustomerCoupons(int customerId) throws CouponSystemException {
		Optional<Customer> opt = customerRepo.findById(customerId);
		if (opt.isPresent()) {
			List<Integer> customerCouponIds = couponRepo.findCouponIdsByCustomerId(customerId);
			couponRepo.deleteAllById(customerCouponIds);
		} else {
			throw new CouponServiceException("customer with id " + customerId + " not found");
		}
	}

	/**
	 * Return a list of all customers
	 * 
	 * @return A list of all customers
	 * @throws CouponSystemException If an exception was caught while trying to get
	 *                               customers
	 */
	public List<Customer> getAllCustomers() throws CouponSystemException {
		return customerRepo.findAll();
	}

	/**
	 * Return a customer using id
	 * 
	 * @param customerId The customer id
	 * @return A customer object
	 * @throws CouponSystemException If an exception was caught while getting
	 *                               customer
	 */
	public Customer getCustomer(int customerId) throws CouponSystemException {
		return customerRepo.findById(customerId)
				.orElseThrow(() -> new CouponServiceException("cannot find customer with id: " + customerId));
	}

}
