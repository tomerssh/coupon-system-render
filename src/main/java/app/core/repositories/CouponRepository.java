package app.core.repositories;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exceptions.CouponSystemException;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
	/**
	 * Get a list of all coupons by company id
	 * 
	 * @param companyId
	 * @return A list of all coupons by company id
	 */
	@Query(value = "from Coupon where company_id=:companyId")
	List<Coupon> findAllByCompanyId(int companyId);

	/**
	 * Get a list of all coupons by company id and category
	 * 
	 * @param companyId
	 * @param category
	 * @return A list of all coupons by company id and category
	 */
	@Query(value = "from Coupon where company_id=:companyId AND category=:category")
	List<Coupon> findAllByCompanyIdAndCategory(int companyId, Category category);

	/**
	 * Get a list of all coupons by company id and max price
	 * 
	 * @param companyId
	 * @param maxPrice
	 * @return A list of all coupons by company id and max price
	 */
	@Query(value = "from Coupon where company_id=:companyId AND price<=:maxPrice")
	List<Coupon> findAllByCompanyIdAndPrice(int companyId, double maxPrice);

	/**
	 * Get a list of all coupon id's this customer owns
	 * 
	 * @param customerId The customer id
	 * @return A list of coupon id's
	 */
//	@Query(nativeQuery = true, value = "select coupon_id from customer_coupon where customer_id=:customerId")
	@Query(value = "select couplist.id from Customer c join c.coupons couplist where c.id=:customerId")
	List<Integer> findCouponIdsByCustomerId(int customerId);

//	/**
//	 * Get a list of all coupons this customer owns with this category
//	 * 
//	 * @param customerId The customer id
//	 * @param category   The coupon category
//	 * @return A list of coupons
//	 */
//	@Query(value = "from Coupon where id=:couponId AND category=:category")
//	List<Coupon> findAllByIdAndCategory(int couponId, Category category);

//	/**
//	 * Get a list of all coupons this customer owns with this max price
//	 * 
//	 * @param customerId The customer id
//	 * @param maxPrice   The maximum price
//	 * @return A list of coupons
//	 */
//	@Query(value = "from Coupon where id=:couponId AND price=:maxPrice")
//	List<Coupon> findAllByIdAndPrice(int couponId, double maxPrice);

	/**
	 * Removes all expired coupons
	 * 
	 * @param date To check for expired coupons
	 * @return The count of deleted coupons
	 */
	@Transactional
	@Modifying
	@OnDelete(action = OnDeleteAction.CASCADE)
	@Query(value = "delete from Coupon where end_date<=:date")
	int deleteAllByEndDateBefore(LocalDate date) throws CouponSystemException;

//	/**
//	 * Add a coupon to customer.
//	 * 
//	 * @param customerId The id of the customer to add the coupon to.
//	 * @param couponId   The id of the coupon that was purchased.
//	 * @throws CouponSystemException
//	 */
//	@Transactional
//	@Modifying
//	@Query(nativeQuery = true, value = "insert into customer_coupon values(:customerId, :couponId)")
//	void addCouponPurchase(int customerId, int couponId) throws CouponSystemException;

	/**
	 * Remove a coupon from customer.
	 * 
	 * @param customerId The id of the customer to remove the coupon from.
	 * @param couponId   The id of the coupon to removed.
	 * @throws CouponSystemException
	 */
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "delete from customer_coupon where customer_id=:customerId and coupon_id=:couponId limit 1")
	void deleteCouponPurchase(int customerId, int couponId) throws CouponSystemException;

	/**
	 * Check if a coupon was purchased
	 * 
	 * @param couponId   The coupon id
	 * @param customerId The customer id
	 * @return A list of coupon id's
	 * @throws CouponSystemException
	 */
//	@Query(nativeQuery = true, value = "select coupon_id from customer_coupon where customer_id=:customerId and coupon_id=:couponId")
	@Query(value = "select couplist.id from Customer c join c.coupons couplist where c.id=:customerId and couplist.id=:couponId")
	List<Integer> wasPurchased(int customerId, int couponId) throws CouponSystemException;

	/**
	 * Takes a coupon id and returns the amount value
	 * 
	 * @param couponId The coupon id
	 * @return The amount value
	 * @throws CouponSystemException
	 */
	@Query(value = "select amount from Coupon where id=:couponId")
	int getAmount(int couponId) throws CouponSystemException;

//	/**
//	 * Get a coupon id and delete the coupon purchase history
//	 * 
//	 * @param con      A connection to the database
//	 * @param couponId The coupon id
//	 * @throws CouponSystemException If a database access error occurred
//	 */
//	@Transactional
//	@Modifying
//	@Query(nativeQuery = true, value = "delete from customer_coupon where coupon_id=:couponId")
//	void deleteCouponHistory(int couponId) throws CouponSystemException;

}
