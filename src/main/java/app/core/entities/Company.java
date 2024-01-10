package app.core.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Generate a company object to send to the data storage.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "coupons")
@Entity
public class Company implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String email;
	private String password;
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Coupon> coupons;

	public Company(int id) {
		super();
		this.id = id;
	}

	public Company(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Company(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public Company(int id, String name, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

}
