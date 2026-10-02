package engestofte.config;

import engestofte.domain.aiflow.entity.AiFlow;
import org.hibernate.cfg.Configuration;

public class HibernateAnnotation {

    // Attributes

    // _________________________________________________________________________________________________________________

	public static void registerEntities(Configuration configuration) {
		configuration.addAnnotatedClass(AiFlow.class);
    }

}