package engestofte.domain.event.service;

import engestofte.ATest;
import engestofte.domain.auth.dto.request.RegisterRequestDTO;
import engestofte.domain.auth.service.AuthService;
import engestofte.domain.enquiry.entity.EnquiryContact;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import engestofte.domain.event.entity.Event;
import engestofte.domain.event.enums.EventCategory;
import engestofte.domain.event.enums.EventStatus;
import engestofte.domain.useraccount.entity.UserAccount;
import engestofte.testdata.TestJsonData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventServiceTest extends ATest {

    // Attributes
	private EventService service;
	private Integer eventId;
	private Integer accountId;

    // _________________________________________________________________________________________________________________

	@BeforeEach
	void setupService() {
		service = new EventService(em);
		new AuthService(emf).register(TestJsonData.read("testdata/data/core.json", "user1", RegisterRequestDTO.class));

		UserAccount account = em.createQuery("SELECT account FROM UserAccount account", UserAccount.class).getSingleResult();
		accountId = account.getId();

		WeddingEnquiry enquiry = new WeddingEnquiry();
		enquiry.setSubmissionId("8df40d38-cac0-41c5-8ff0-71d1063e1710");
		enquiry.setLanguage("en");
		enquiry.setRawDraft(TestJsonData.readNode("testdata/data/core.json", "eventData"));
		enquiry.setStatus(EnquiryStatus.APPROVED);
		enquiry.setCreatedAt(Instant.now());

		em.getTransaction().begin();
		em.persist(enquiry);

		EnquiryContact contact = new EnquiryContact();
		contact.setEnquiry(enquiry);
		contact.setUserAccount(account);
		contact.setPrimary(true);
		em.persist(contact);

		Event event = new Event();
		event.setWeddingEnquiry(enquiry);
		event.setCategory(EventCategory.WEDDING);
		event.setStatus(EventStatus.APPROVED);
		event.setEventData(TestJsonData.readNode("testdata/data/core.json", "eventData"));
		event.setGuestAccessTokenHash(EventAccessToken.createHash());
		event.setCreatedAt(Instant.now());
		event.setApprovedAt(Instant.now());
		em.persist(event);

		em.getTransaction().commit();

		eventId = event.getId();
	}

    // _________________________________________________________________________________________________________________

	@Test
	void ownerDepositRequestAndPrimaryContactPaymentMoveEventAndEnquiryTogether() {
		service.requestDeposit(eventId);
		assertEquals(EventStatus.AWAITING_DEPOSIT, service.getById(eventId).getStatus());
		assertEquals(EnquiryStatus.AWAITING_DEPOSIT, service.getById(eventId).getWeddingEnquiry().getStatus());

		service.payDeposit(eventId, accountId);

		assertEquals(EventStatus.BOOKED, service.getById(eventId).getStatus());
		assertEquals(EnquiryStatus.BOOKED, service.getById(eventId).getWeddingEnquiry().getStatus());
	}

}