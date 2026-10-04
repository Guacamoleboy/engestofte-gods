package engestofte.domain.populate.dao;

import engestofte.dao.EntityManagerDAO;
import engestofte.domain.role.entity.Role;
import jakarta.persistence.EntityManager;

public class PopulateDAO extends EntityManagerDAO<Role> {

	// Attributes

	// _________________________________________________________________________________________________________________

	public PopulateDAO(EntityManager entityManager) {
		super(entityManager, Role.class);
	}

	// _________________________________________________________________________________________________________________

	public String restart() {
		return executeQuery(() -> {
			String tables = (String) em.createNativeQuery(
					"SELECT string_agg(format('%I.%I', schemaname, tablename), ', ') " +
							"FROM pg_tables WHERE schemaname = 'public'")
				.getSingleResult();

			if (tables != null && !tables.isBlank()) {
				em.createNativeQuery("TRUNCATE TABLE " + tables + " RESTART IDENTITY CASCADE").executeUpdate();
			}

			em.createNativeQuery("ALTER TABLE public.ai_flow_interactions DROP CONSTRAINT IF EXISTS ai_flow_interactions_status_check")
				.executeUpdate();
			em.createNativeQuery("ALTER TABLE public.ai_flow_interactions ADD CONSTRAINT ai_flow_interactions_status_check " +
					"CHECK (status IN ('IN_PROGRESS', 'STEP_COMPLETE', 'OUT_OF_SCOPE', 'DONE'))")
				.executeUpdate();
			return "Database restarted";
		});
	}
}
