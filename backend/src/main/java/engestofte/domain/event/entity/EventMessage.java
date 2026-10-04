package engestofte.domain.event.entity;

import engestofte.domain.event.enums.EventMessageSender;
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
@Table(name = "event_messages")
public class EventMessage {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		id | event_id | sender_account_id | sender_type | content | created_at
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
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sender_account_id", nullable = false)
	private UserAccount senderAccount;

	@Enumerated(EnumType.STRING)
	@Column(name = "sender_type", nullable = false, length = 16)
	private EventMessageSender senderType;

	@Column(name = "content", nullable = false, length = 5000)
	private String content;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String EVENT = "event";
		public static final String SENDER_ACCOUNT = "senderAccount";
		public static final String SENDER_TYPE = "senderType";
		public static final String CONTENT = "content";
		public static final String CREATED_AT = "createdAt";
	}
}
