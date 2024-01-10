package app.core.tests;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import app.core.entities.Coupon.Category;
import app.core.exceptions.CouponSystemException;
import app.core.services.CustomerService;
import app.core.services.login.LoginManager;
import app.core.services.login.LoginManager.ClientType;

@Component
@Order(3)
public class CustomerTest implements CommandLineRunner, ApplicationContextAware {
	private ApplicationContext ctx;
	private LoginManager lm;
	private CustomerService customerService;

	@Override
	public void run(String... args) throws Exception {
		try {
			mainCustomerTests(lm);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
		this.lm = ctx.getBean(LoginManager.class);
	}

	public void mainCustomerTests(LoginManager lm) throws CouponSystemException {
		System.out.println("==================== CUSTOMER TESTS");
		customerService = (CustomerService) lm.login("customer@mail.com", "1234", ClientType.CUSTOMER);

		customerGetDetails();

		customerAddCouponPurchase();

		customerWasCouponPurchased();

		customerGetCouponsById();

		customerGetCouponsByIdAndCategory();

		customerGetCouponsByIdAndPrice();

		customerDeleteCouponPurchase();
		System.out.println("====================");
	}

	private void customerDeleteCouponPurchase() throws CouponSystemException {
		System.out.println("--- deleteCouponPurchase for coupon with id 2");
		customerService.deleteCouponPurchase(2);
		System.out.println("--- deleted coupon purchase for coupon with id 2");
	}

	private void customerGetCouponsByIdAndPrice() throws CouponSystemException {
		System.out.println("--- getCustomerCouponsByMaxPrice for max price 20");
		System.out.println(customerService.getCustomerCouponsByMaxPrice(20));
		System.out.println();
	}

	private void customerGetCouponsByIdAndCategory() throws CouponSystemException {
		System.out.println("--- getCustomerCouponsByCategory for category clothing");
		System.out.println(customerService.getCustomerCouponsByCategory(Category.CLOTHING));
		System.out.println();
	}

	private void customerGetCouponsById() throws CouponSystemException {
		System.out.println("--- getCustomerCoupons");
		System.out.println(customerService.getCustomerCoupons());
		System.out.println();
	}

	private void customerWasCouponPurchased() throws CouponSystemException {
		System.out.println("--- wasCouponPurchased for coupon with id 2");
		System.out.println(customerService.wasCouponPurchased(2));
		System.out.println();
	}

	private void customerAddCouponPurchase() throws CouponSystemException {
		System.out.println("--- addCouponPurchase for coupon with id 2");
		customerService.addCouponPurchase(2);
		System.out.println("added coupon purchase");
		System.out.println();
	}

	private void customerGetDetails() throws CouponSystemException {
		System.out.println("--- getCustomerDetails");
		System.out.println(customerService.getCustomerDetails());
		System.out.println();
	}

}
