package app.core.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exceptions.CouponServiceException;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

/**
 * Manages customer interaction with the app
 * 
 * @see ClientService
 */
@Service
@Transactional
@Scope("prototype")
public class CustomerService extends ClientService {
	// the id the the logged in customer
	private int customerId;
	// reference to the current customer
	private Customer customer;

	@Autowired
	public CustomerService(CustomerRepository customerRepo, CouponRepository couponRepo) {
		super.customerRepo = customerRepo;
		super.couponRepo = couponRepo;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setCustomerById(int id) {
		this.setCustomer(id, this.customerRepo);
	}

	private void setCustomer(int id, CustomerRepository customerRepo) {
		this.customer = this.customerRepo.findById(id).get();
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		Optional<Customer> opt = customerRepo.findByEmailAndPassword(email, password);
		if (opt.isPresent()) {
			this.customerId = opt.get().getId();
			this.customer = opt.get();
			return true;
		} else {
			return false;
		}
	}

//	public void addCouponPurchase(Coupon coupon) throws CouponSystemException {
//		coupon = couponRepo.getById(coupon.getId());
//		if (coupon.getAmount() > 0) {
//			coupon.setAmount(coupon.getAmount() - 1);
//			this.customer.getCoupons().add(coupon);
//			customerRepo.save(customer);
//		} else {
//			throw new CouponServiceException("no coupons left");
//		}
//	}
	public String addCouponPurchase(int couponId) throws CouponSystemException {
		try {
			Coupon coupon = couponRepo.getById(couponId);
			if (coupon.getAmount() > 0) {
				coupon.setAmount(coupon.getAmount() - 1);
				this.customer.getCoupons().add(coupon);
				couponRepo.saveAndFlush(coupon);
				customerRepo.saveAndFlush(this.customer);
				return coupon.getTitle();
			} else {
				throw new CouponServiceException("no coupons left");
			}
		} catch (EntityNotFoundException e) {
			throw new CouponServiceException("coupon with id " + couponId + " not found");
		}
	}

	// doesn't work without native query in couponRepo
	public void deleteCouponPurchase(int couponId) throws CouponSystemException {
		Coupon coupon = couponRepo.getById(couponId);
		int purchasedAmount = this.getCouponPurchaseAmount(couponId);
		coupon.setAmount(coupon.getAmount() + 1);
		couponRepo.deleteCouponPurchase(this.customerId, couponId);
		if (purchasedAmount == 1) {
			this.customer.getCoupons().remove(coupon);
		}
		couponRepo.saveAndFlush(coupon);
		customerRepo.saveAndFlush(this.customer);
	}

	public List<Coupon> getAllCoupons() throws CouponServiceException {
		List<Coupon> coupons = couponRepo.findAll();
		if (!coupons.isEmpty()) {
			return coupons;
		} else {
			throw new CouponServiceException("no coupons");
		}
	}

	public boolean wasCouponPurchased(int couponId) throws CouponSystemException {
		List<Integer> couponIdList = couponRepo.wasPurchased(this.customerId, couponId);
		return !couponIdList.isEmpty();
	}

	public int getCouponPurchaseAmount(int couponId) throws CouponSystemException {
		List<Integer> couponIdList = couponRepo.wasPurchased(this.customerId, couponId);
		if (!couponIdList.isEmpty()) {
			return couponIdList.size();
		} else {
			throw new CouponServiceException("coupon was not purchased");
		}
	}

	/**
	 * Return a list of all coupons of the company with given id
	 * 
	 * @param companyId The company id
	 * @return A list of all coupons
	 * @throws CouponSystemException If a database access error occurred
	 */
	private List<Coupon> getCustomerCouponsById(int customerId) throws CouponSystemException {
		List<Integer> ids = couponRepo.findCouponIdsByCustomerId(this.customerId);
		return couponRepo.findAllById(ids);
	}

	public List<Coupon> getCustomerCoupons() throws CouponSystemException {
		return getCustomerCouponsById(this.customerId);
	}

	/**
	 * Return a list of all coupons of the customer with given category
	 * 
	 * @param companyId The customer id
	 * @param category  The coupon category
	 * @return A list of all coupons of given category
	 * @throws CouponSystemException If a database access error occurred
	 */
	private List<Coupon> getCustomerCouponsByIdAndCategory(int customerId, Category category)
			throws CouponSystemException {
		return couponRepo.findAllByCompanyIdAndCategory(this.customerId, category);
	}

	public List<Coupon> getCustomerCouponsByCategory(Category category) throws CouponSystemException {
		return getCustomerCouponsByIdAndCategory(this.customerId, category);
	}

	/**
	 * Return a list of all coupons of the customer with given max price
	 * 
	 * @param companyId The customer id
	 * @param maxPrice  The coupons max price
	 * @return A list of all coupons with given max price
	 * @throws CouponSystemException If a database access error occurred
	 */
	private List<Coupon> getCustomerCouponsByIdAndMaxPrice(int customerId, double maxPrice)
			throws CouponSystemException {
		return couponRepo.findAllByCompanyIdAndPrice(this.customerId, maxPrice);
	}

	public List<Coupon> getCustomerCouponsByMaxPrice(double maxPrice) throws CouponSystemException {
		return getCustomerCouponsByIdAndMaxPrice(this.customerId, maxPrice);
	}

	/**
	 * Get the logged in customer's details
	 * 
	 * @return The logged in customer
	 * @throws CouponSystemException If a database access error occurred
	 */
	public Customer getCustomerDetails() throws CouponSystemException {
		return this.customer;
	}

}
