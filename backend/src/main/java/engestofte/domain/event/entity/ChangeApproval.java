package engestofte.domain.event.entity;

import engestofte.domain.event.changeproposal.ChangeApprovalDecision;
import engestofte.domain.event.changeproposal.ChangeApprovalParty;
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
@Table(name = "change_approvals", uniqueConstraints = @UniqueConstraint(name = "uk_change_approval_proposal_party", columnNames = { "change_proposal_id", "party" }))
public class ChangeApproval {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		id | change_proposal_id | actor_account_id | party | decision | explanation | decided_at
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
	@JoinColumn(name = "change_proposal_id", nullable = false)
	private ChangeProposal changeProposal;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "actor_account_id", nullable = false)
	private UserAccount actorAccount;

	@Enumerated(EnumType.STRING)
	@Column(name = "party", nullable = false, length = 16)
	private ChangeApprovalParty party;

	@Enumerated(EnumType.STRING)
	@Column(name = "decision", nullable = false, length = 16)
	private ChangeApprovalDecision decision;

	@Column(name = "explanation", length = 1000)
	private String explanation;

	@Column(name = "decided_at", nullable = false)
	private Instant decidedAt;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String CHANGE_PROPOSAL = "changeProposal";
		public static final String ACTOR_ACCOUNT = "actorAccount";
		public static final String PARTY = "party";
		public static final String DECISION = "decision";
		public static final String EXPLANATION = "explanation";
		public static final String DECIDED_AT = "decidedAt";
	}
}
