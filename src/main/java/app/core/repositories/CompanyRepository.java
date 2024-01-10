package app.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.core.entities.Company;
import app.core.exceptions.CouponSystemException;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
	/**
	 * Check if the company exists in the data storage.
	 * 
	 * @param email    The company's email.
	 * @param password The company's password.
	 * @return true if the company exists in the data storage.
	 * @throws CouponSystemException
	 */
	boolean existsByEmailAndPassword(String email, String password) throws CouponSystemException;

	/**
	 * Try to find a company with this name and password
	 * 
	 * @param email    The company email
	 * @param password The company password
	 * @return optional of company
	 * @throws CouponSystemException
	 */
	Optional<Company> findByEmailAndPassword(String email, String password) throws CouponSystemException;

	/**
	 * Try to find a company with this name and email
	 * 
	 * @param email    The company email
	 * @param password The company password
	 * @return true if company exists
	 * @throws CouponSystemException
	 */
	boolean existsByNameOrEmail(String email, String password) throws CouponSystemException;

	/**
	 * Try to find a company with this name and password
	 * 
	 * @param name     The company name
	 * @param password The company password
	 * @return optional of company
	 * @throws CouponSystemException
	 */
	Optional<Company> findByNameAndPassword(String name, String password) throws CouponSystemException;

}
