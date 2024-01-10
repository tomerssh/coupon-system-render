package app.core.tests;

import java.time.LocalDate;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exceptions.CouponSystemException;
import app.core.services.CompanyService;
import app.core.services.login.LoginManager;
import app.core.services.login.LoginManager.ClientType;

@Component
@Order(2)
public class CompanyTest implements CommandLineRunner, ApplicationContextAware {
	private ApplicationContext ctx;
	private LoginManager lm;
	private CompanyService companyService;

	@Override
	public void run(String... args) throws Exception {
		try {
			mainCompanyTests(lm);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
		this.lm = ctx.getBean(LoginManager.class);
	}

	public void mainCompanyTests(LoginManager lm) throws CouponSystemException {
		System.out.println("==================== COMPANY TESTS");
		companyService = (CompanyService) lm.login("company@mail.com", "1234", ClientType.COMPANY);

		companyAddCoupon();

		companyGetCoupons();

		companyGetCouponsByPrice();

		companyUpdateCoupons();

		companyGetCouponsByCategory();

		companyFindDuplicateCoupon();

		companyDeleteCoupon();

		companyGetCouponAmount();
		System.out.println("====================");
	}

	private void companyGetCouponAmount() throws CouponSystemException {
		System.out.println("--- getAmount for coupon with id 2");
		System.out.println(companyService.getAmount(2));
	}

	private void companyDeleteCoupon() throws CouponSystemException {
		System.out.println("--- deleteCoupon for coupon with id 1");
		companyService.deleteCoupon(1);
		System.out.println("--- deleted coupon with id 1");
		System.out.println();
	}

	private void companyFindDuplicateCoupon() throws CouponSystemException {
		System.out.println("--- findDuplicateCoupon for coupon with id 1");
		System.out.println(companyService.findDuplicateCoupon(1));
		System.out.println();
	}

	private void companyGetCouponsByCategory() throws CouponSystemException {
		System.out.println("--- getCompanyCoupons by camping category");
		System.out.println(companyService.getCompanyCoupons(Category.CAMPING));
		System.out.println();
	}

	private void companyUpdateCoupons() throws CouponSystemException {
		System.out.println("--- updateCoupon for coupon with id 1");
		companyService.updateCoupon(new Coupon(1, Category.CAMPING, "updated tent", "updated tent description",
				LocalDate.of(2021, 1, 2), LocalDate.of(2022, 1, 2), 5, 100, "updated tent image"));
		System.out.println("updated coupon with id 1");
		System.out.println();
	}

	private void companyGetCouponsByPrice() throws CouponSystemException {
		System.out.println("--- getCompanyCoupons by max price 50");
		System.out.println(companyService.getCompanyCoupons(50));
		System.out.println();
	}

	private void companyGetCoupons() throws CouponSystemException {
		System.out.println("--- getCompanyCoupons");
		System.out.println(companyService.getCompanyCoupons());
		System.out.println();
	}

	private void companyAddCoupon() throws CouponSystemException {
		System.out.println("--- addCoupon");
		System.out.println(companyService.addCoupon(new Coupon(Category.CAMPING, "tent", "tent description",
				LocalDate.of(2021, 1, 2), LocalDate.of(2022, 1, 2), 5, 100, "tent image")));
		System.out.println(companyService.addCoupon(new Coupon(Category.CLOTHING, "tshirt", "tshirt description",
				LocalDate.of(2021, 1, 2), LocalDate.of(2022, 1, 2), 10, 50, "tshirt image")));
		System.out.println();
	}

}
