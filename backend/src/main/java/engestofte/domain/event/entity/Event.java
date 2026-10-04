package engestofte.domain.event.entity;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import engestofte.domain.event.enums.EventCategory;
import engestofte.domain.event.enums.EventStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "events", uniqueConstraints = {
		@UniqueConstraint(name = "uk_events_wedding_enquiry_id", columnNames = "wedding_enquiry_id"),
		@UniqueConstraint(name = "uk_events_guest_access_token_hash", columnNames = "guest_access_token_hash")
})
public class Event {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		id | wedding_enquiry_id | category | status | event_data | customer_note
	//		| guest_access_token_hash | created_at | approved_at
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

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "wedding_enquiry_id", nullable = false, unique = true)
	private WeddingEnquiry weddingEnquiry;

	@Enumerated(EnumType.STRING)
	@Column(name = "category", nullable = false, length = 24)
	private EventCategory category;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 32)
	private EventStatus status;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "event_data", nullable = false, columnDefinition = "jsonb")
	private JsonNode eventData;

	@Column(name = "customer_note", columnDefinition = "text")
	private String customerNote;

	@Column(name = "guest_access_token_hash", length = 64)
	private String guestAccessTokenHash;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "approved_at")
	private Instant approvedAt;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String WEDDING_ENQUIRY = "weddingEnquiry";
		public static final String CATEGORY = "category";
		public static final String STATUS = "status";
		public static final String EVENT_DATA = "eventData";
		public static final String CUSTOMER_NOTE = "customerNote";
		public static final String GUEST_ACCESS_TOKEN_HASH = "guestAccessTokenHash";
		public static final String CREATED_AT = "createdAt";
		public static final String APPROVED_AT = "approvedAt";
	}
}
