package engestofte.domain.event.entity;

import engestofte.domain.event.changeproposal.ChangeProposalStatus;
import engestofte.domain.useraccount.entity.UserAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "change_proposals")
public class ChangeProposal {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		id | event_id | field_name | old_value | new_value | proposer_account_id
	//		| proposer_party | status | rejection_explanation | created_at | resolved_at
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
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;

	@Column(name = "field_name", nullable = false, length = 64)
	private String fieldName;

	@Column(name = "old_value", nullable = false, length = 1000)
	private String oldValue;

	@Column(name = "new_value", nullable = false, length = 1000)
	private String newValue;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "proposer_account_id", nullable = false)
	private UserAccount proposerAccount;

	@Enumerated(EnumType.STRING)
	@Column(name = "proposer_party", nullable = false, length = 16)
	private engestofte.domain.event.changeproposal.ChangeApprovalParty proposerParty;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 16)
	private ChangeProposalStatus status;

	@Column(name = "rejection_explanation", length = 1000)
	private String rejectionExplanation;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "resolved_at")
	private Instant resolvedAt;

	@OneToMany(mappedBy = "changeProposal", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("decidedAt ASC")
	private List<ChangeApproval> approvals = new ArrayList<>();

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String EVENT = "event";
		public static final String FIELD_NAME = "fieldName";
		public static final String OLD_VALUE = "oldValue";
		public static final String NEW_VALUE = "newValue";
		public static final String PROPOSER_ACCOUNT = "proposerAccount";
		public static final String PROPOSER_PARTY = "proposerParty";
		public static final String STATUS = "status";
		public static final String REJECTION_EXPLANATION = "rejectionExplanation";
		public static final String CREATED_AT = "createdAt";
		public static final String RESOLVED_AT = "resolvedAt";
		public static final String APPROVALS = "approvals";
	}
}
