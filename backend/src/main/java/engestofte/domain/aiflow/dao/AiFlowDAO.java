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
	
}