package engestofte.domain.user.entity;

import engestofte.domain.role.entity.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_accounts", uniqueConstraints = @UniqueConstraint(name = "uk_user_accounts_email", columnNames = "email"))
public class UserAccount {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		id | full_name | email | password_hash | role_id
	//
	// __________________
	// Tested: NO
	// Date: 04/10-2026

	// _________________________________________________________________________________________________________________

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "full_name", nullable = false, length = 160)
	private String fullName;

	@Column(name = "email", nullable = false, length = 254)
	private String email;

	@Column(name = "password_hash", nullable = false, length = 60)
	private String passwordHash;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String FULL_NAME = "fullName";
		public static final String EMAIL = "email";
		public static final String PASSWORD_HASH = "passwordHash";
		public static final String ROLE = "role";
	}
}
