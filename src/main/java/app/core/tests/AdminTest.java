package app.core.tests;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.services.AdminService;
import app.core.services.login.LoginManager;
import app.core.services.login.LoginManager.ClientType;

@Component
@Order(1)
public class AdminTest implements CommandLineRunner, ApplicationContextAware {
	private ApplicationContext ctx;
	private LoginManager lm;
	private AdminService adminService;

	@Override
	public void run(String... args) throws Exception {
		try {
			mainAdminTests(lm);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
		this.lm = ctx.getBean(LoginManager.class);
	}

	public void mainAdminTests(LoginManager lm) throws CouponSystemException {
		System.out.println("==================== ADMIN TESTS");
		adminService = (AdminService) lm.login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);
		mainAdminCompanyTests();
		mainAdminCustomerTests();
		System.out.println("====================");
	}

	private void mainAdminCompanyTests() throws CouponSystemException {
		adminAddCompany();

		adminGetAllCompanies();

		adminUpdateCompany();

		adminGetCompany();

		adminDeleteCompanyCoupons();

		adminDeleteCompany();
	}

	private void adminDeleteCompany() throws CouponSystemException {
		System.out.println("--- deleteCompany with id 1");
		adminService.deleteCompany(1);
		System.out.println("--- deleted company with id 1");
		System.out.println();
	}

	private void adminDeleteCompanyCoupons() throws CouponSystemException {
		System.out.println("--- deleteCompanyCoupons for company with id 1");
		adminService.deleteCompanyCoupons(1);
		System.out.println("--- deleted company coupons for company with id 1");
		System.out.println();
	}

	private void adminGetCompany() throws CouponSystemException {
		System.out.println("--- getCompany with id 1");
		System.out.println(adminService.getCompany(1));
		System.out.println();
	}

	private void adminUpdateCompany() throws CouponSystemException {
		System.out.println("--- updateCompany with id 1");
		adminService.updateCompany(new Company(1, "bbb", "bbb@mail", "5678"));
		System.out.println("--- updated company with id 1");
		System.out.println();
	}

	private void adminGetAllCompanies() throws CouponSystemException {
		System.out.println("--- getAllCompanies");
		System.out.println(adminService.getAllCompanies());
		System.out.println();
	}

	private void adminAddCompany() throws CouponSystemException {
		System.out.println("--- addCompany");
		System.out.println(adminService.addCompany(new Company("aaa", "aaa@mail", "1234")));
		System.out.println(adminService.addCompany(new Company("company", "company@mail.com", "1234")));
		System.out.println();
	}

	private void mainAdminCustomerTests() throws CouponSystemException {
		adminAddCustomer();

		adminGetAllCustomers();

		adminUpdateCustomer();

		adminGetCustomer();

		adminDeleteCustomerCoupons();

		adminDeleteCustomer();
	}

	private void adminDeleteCustomer() throws CouponSystemException {
		System.out.println("--- deleteCustomer for customer with id 1");
		adminService.deleteCustomer(1);
		System.out.println("--- deleted customer with id 1");
	}

	private void adminDeleteCustomerCoupons() throws CouponSystemException {
		System.out.println("--- deleteCustomerCoupons for customer with id 1");
		adminService.deleteCustomerCoupons(1);
		System.out.println("--- deleted customer coupons for customer with id 1");
		System.out.println();
	}

	private void adminGetCustomer() throws CouponSystemException {
		System.out.println("--- getCustomer with id 1");
		System.out.println(adminService.getCustomer(1));
		System.out.println();
	}

	private void adminUpdateCustomer() throws CouponSystemException {
		System.out.println("--- updateCustomer with id 1");
		adminService.updateCustomer(new Customer(1, "ccc", "ddd", "newcustomer@mail", "5678"));
		System.out.println("updated customer with id 1");
		System.out.println();
	}

	private void adminGetAllCustomers() throws CouponSystemException {
		System.out.println("--- getAllCustomers");
		System.out.println(adminService.getAllCustomers());
		System.out.println();
	}

	private void adminAddCustomer() throws CouponSystemException {
		System.out.println("--- addCustomer");
		System.out.println(adminService.addCustomer(new Customer("aaa", "bbb", "aaa@mail", "1234")));
		System.out.println(adminService.addCustomer(new Customer("customer", "customer", "customer@mail.com", "1234")));
		System.out.println();
	}

}
