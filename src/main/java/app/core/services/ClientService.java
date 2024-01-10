package app.core.services;

import app.core.exceptions.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

/**
 * Abstract - Manages client interaction with the app.
 */
public abstract class ClientService {
	protected CompanyRepository companyRepo;
	protected CustomerRepository customerRepo;
	protected CouponRepository couponRepo;

	/**
	 * Login the client to the system.
	 * 
	 * @param email    The client's email
	 * @param password The client's password
	 * @return true if the client entered correct credentials
	 * @throws CouponSystemException If the client does not exist
	 */
	public abstract boolean login(String email, String password) throws CouponSystemException;
}
