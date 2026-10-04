package engestofte.config;

import engestofte.domain.aiflow.entity.AiFlow;
import engestofte.domain.enquiry.entity.EnquiryContact;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import engestofte.domain.event.entity.Event;
import engestofte.domain.event.entity.EventMessage;
import engestofte.domain.role.entity.Role;
import engestofte.domain.useraccount.entity.UserAccount;
import org.hibernate.cfg.Configuration;

public class HibernateAnnotation {

    // Attributes

    // _________________________________________________________________________________________________________________

	public static void registerEntities(Configuration configuration) {
		configuration.addAnnotatedClass(AiFlow.class);
		configuration.addAnnotatedClass(Role.class);
		configuration.addAnnotatedClass(UserAccount.class);
		configuration.addAnnotatedClass(WeddingEnquiry.class);
		configuration.addAnnotatedClass(EnquiryContact.class);
		configuration.addAnnotatedClass(Event.class);
		configuration.addAnnotatedClass(EventMessage.class);
    }

}
