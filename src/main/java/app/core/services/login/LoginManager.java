package app.core.services.login;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.core.exceptions.CouponServiceException;
import app.core.exceptions.CouponSystemException;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;

/**
 * Singleton - Login system
 */
@Service
@Transactional
public class LoginManager implements ApplicationContextAware {
	public enum ClientType {
		ADMINISTRATOR, COMPANY, CUSTOMER
	}

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}

	/**
	 * Attempt to login a client to the system
	 * 
	 * @param email      The client email
	 * @param password   The client password
	 * @param clientType The client type
	 * @return A client facade
	 * @throws CouponSystemException If the client does not exist
	 */
	public ClientService login(String email, String password, ClientType clientType) throws CouponSystemException {
		if (clientType == null) {
			throw new CouponServiceException("cannot login, invalid ClientType");
		}
		switch (clientType) {
		case ADMINISTRATOR:
			AdminService adminService = ctx.getBean(AdminService.class);
			if (adminService.login(email, password)) {
				return adminService;
			} else {
				adminService = null;
				throw new CouponServiceException("cannot login, invalid admin credentials");
			}
		case COMPANY:
			CompanyService companyService = ctx.getBean(CompanyService.class);
			if (companyService.login(email, password)) {
				return companyService;
			} else {
				companyService = null;
				throw new CouponServiceException("cannot login, invalid company credentials");
			}
		case CUSTOMER:
			CustomerService customerService = ctx.getBean(CustomerService.class);
			if (customerService.login(email, password)) {
				return customerService;
			} else {
				customerService = null;
				throw new CouponServiceException("cannot login, invalid customer credentials");
			}
		default:
			throw new CouponServiceException("login failed");
		}
	}

}
