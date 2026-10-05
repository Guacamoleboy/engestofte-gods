package engestofte.domain.event.entity;

import engestofte.domain.useraccount.entity.UserAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "event_message_recipients", uniqueConstraints = @UniqueConstraint(name = "uk_event_message_recipient", columnNames = { "event_message_id", "user_account_id" }))
public class EventMessageRecipient {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		id | event_message_id | user_account_id | read_at
	//
	// __________________
	// Tested: NO
	// Date: 05/10-2026

	// _________________________________________________________________________________________________________________

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "event_message_id", nullable = false)
	private EventMessage message;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_account_id", nullable = false)
	private UserAccount recipient;

	@Column(name = "read_at")
	private Instant readAt;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String MESSAGE = "message";
		public static final String RECIPIENT = "recipient";
		public static final String READ_AT = "readAt";
	}
}
