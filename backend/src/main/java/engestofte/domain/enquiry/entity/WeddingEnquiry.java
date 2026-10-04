package engestofte.domain.enquiry.entity;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.enquiry.enums.EnquiryStatus;
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
@Table(name = "wedding_enquiries", uniqueConstraints = @UniqueConstraint(name = "uk_wedding_enquiries_submission_id", columnNames = "submission_id"))
public class WeddingEnquiry {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		id | submission_id | language | raw_draft | status | created_at
	//		| ai_assessment | internal_note | customer_question
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

	@Column(name = "submission_id", nullable = false, length = 36)
	private String submissionId;

	@Column(name = "language", nullable = false, length = 2)
	private String language;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "raw_draft", nullable = false, columnDefinition = "jsonb")
	private JsonNode rawDraft;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "ai_assessment", columnDefinition = "jsonb")
	private JsonNode aiAssessment;

	@Column(name = "internal_note", columnDefinition = "text")
	private String internalNote;

	@Column(name = "customer_question", length = 1500)
	private String customerQuestion;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 32)
	private EnquiryStatus status;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String SUBMISSION_ID = "submissionId";
		public static final String LANGUAGE = "language";
		public static final String RAW_DRAFT = "rawDraft";
		public static final String AI_ASSESSMENT = "aiAssessment";
		public static final String INTERNAL_NOTE = "internalNote";
		public static final String CUSTOMER_QUESTION = "customerQuestion";
		public static final String STATUS = "status";
		public static final String CREATED_AT = "createdAt";
	}
}
