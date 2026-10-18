package engestofte.domain.aiflow.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.aiflow.entity.AiFlow;
import jakarta.persistence.EntityManager;

public class AiFlowDAO extends EntityManagerDAO<AiFlow> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public AiFlowDAO(EntityManager em) {
		super(em, AiFlow.class);
	}

	public AiFlow findByRequestId(String requestId) {
		return findEntityByColumn(requestId, AiFlow.Fields.REQUEST_ID);
	}
	
}
