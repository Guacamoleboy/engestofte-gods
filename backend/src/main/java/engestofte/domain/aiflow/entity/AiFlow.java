package engestofte.domain.aiflow.entity;

import engestofte.domain.aiflow.enums.AiFlowStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Table(name = "ai_flow_interactions")
public class AiFlow {

	// _________________________________________________________________________________________________________________

	// Expected Column Layout in DB
	// __________________
	//
	//		PgAdmin
	//		_______
	//		id | answer | current_question | customer_name | language | acknowledgement | next_question | step | status
	//
	// __________________
	// Tested: NO
	// Date: 02/10-2026

	// _________________________________________________________________________________________________________________

	// ______ | COLUMNS | ______________________________________________________________________________________________

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "answer", nullable = false, length = 2000)
	private String answer;

	@Column(name = "current_question", nullable = false, length = 500)
	private String currentQuestion;

	@Column(name = "customer_name", length = 160)
	private String customerName;

	@Column(name = "language", nullable = false, length = 2)
	private String language;

	@Column(name = "acknowledgement", nullable = false, length = 1000)
	private String acknowledgement;

	@Column(name = "next_question", nullable = false, length = 1000)
	private String nextQuestion;

	@Column(name = "step", nullable = false)
	private Integer step;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 32)
	private AiFlowStatus status;

	// ______ | NESTED FIELDS | ________________________________________________________________________________________

	public static class Fields {
		public static final String ID = "id";
		public static final String ANSWER = "answer";
		public static final String CURRENT_QUESTION = "currentQuestion";
		public static final String CUSTOMER_NAME = "customerName";
		public static final String LANGUAGE = "language";
		public static final String ACKNOWLEDGEMENT = "acknowledgement";
		public static final String NEXT_QUESTION = "nextQuestion";
		public static final String STEP = "step";
		public static final String STATUS = "status";
	}

}
