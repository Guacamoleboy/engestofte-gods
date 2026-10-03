package engestofte.domain.enquiry.entity;

import engestofte.domain.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "enquiry_contacts", uniqueConstraints = @UniqueConstraint(name = "uk_enquiry_contact_account", columnNames = { "enquiry_id", "user_account_id" }))
public class EnquiryContact {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		id | enquiry_id | user_account_id | is_primary
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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "enquiry_id", nullable = false)
	private WeddingEnquiry enquiry;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_account_id", nullable = false)
	private UserAccount userAccount;

	@Column(name = "is_primary", nullable = false)
	private boolean primary;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String ENQUIRY = "enquiry";
		public static final String USER_ACCOUNT = "userAccount";
		public static final String PRIMARY = "primary";
	}
}
