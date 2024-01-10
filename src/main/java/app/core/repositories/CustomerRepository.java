package app.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	/**
	 * Check if the customer exists in the data storage.
	 * 
	 * @param email    The customer's email.
	 * @param password The customer's password.
	 * @return true if the customer exists in the data storage.
	 * @throws CouponSystemException
	 */
	boolean existsByEmailAndPassword(String email, String password) throws CouponSystemException;

	/**
	 * Try to find a customer with this name and password
	 * 
	 * @param email    The customer email
	 * @param password The customer password
	 * @return optional of customer
	 * @throws CouponSystemException
	 */
	Optional<Customer> findByEmailAndPassword(String email, String password) throws CouponSystemException;

}
