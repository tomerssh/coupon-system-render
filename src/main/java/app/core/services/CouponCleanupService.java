package app.core.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.exceptions.CouponSystemException;
import app.core.repositories.CouponRepository;

/**
 * Service for cleaning expired coupons
 */
@Service
@Transactional
public class CouponCleanupService {
	private CouponRepository couponRepo;

	@Autowired
	public CouponCleanupService(CouponRepository couponRepo) {
		this.couponRepo = couponRepo;
	}

	/**
	 * Remove all expired coupons and purchase history
	 * 
	 * @throws CouponSystemException If a database access error occurred
	 * @return The count of deleted coupons
	 */
	public int cleanExpiredCoupons() throws CouponSystemException {
		return couponRepo.deleteAllByEndDateBefore(LocalDate.now());
	}

}
