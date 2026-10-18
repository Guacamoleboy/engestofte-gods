package engestofte.domain.event.service;

import engestofte.domain.event.entity.Event;
import engestofte.testdata.TestJsonData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StaffEventDataRedactorTest {

    // Attributes

    // _________________________________________________________________________________________________________________

	@Test
	void redactsPrivateContactDetailsAndExtractsRequestedDates() {
		Event event = eventWithFixtureData();
		StaffEventDataRedactor.RedactedEventData redacted = StaffEventDataRedactor.redact(event, "Contact <owner@example.test> 12345678");
		assertEquals("User One <[redacted]> [redacted]", redacted.customerName());
		assertEquals("User One celebration at [redacted]", redacted.eventName());
		assertEquals("12 July 2028 - 14 July 2028", StaffEventDataRedactor.redactRequestedDate(event));
		assertEquals("Call [redacted] at [redacted]", StaffEventDataRedactor.redactContactDetails("Call user1@example.test at +45 12 34 56 78"));
	}

    // _________________________________________________________________________________________________________________

	@Test
	void fallsBackToRedactedPrimaryContactAndOmitsEmptyEventData() {
		Event event = new Event();
		event.setEventData(TestJsonData.readNode("testdata/data/core.json", "emptyEventData"));
		StaffEventDataRedactor.RedactedEventData redacted = StaffEventDataRedactor.redact(event, "User One <user1@example.test>");
		assertEquals("User One <[redacted]>", redacted.customerName());
		assertNull(redacted.eventName());
		assertNull(StaffEventDataRedactor.redactRequestedDate(event));
	}

    // _________________________________________________________________________________________________________________

	private Event eventWithFixtureData() {
		Event event = new Event();
		event.setEventData(TestJsonData.readNode("testdata/data/core.json", "eventData"));
		return event;
	}

}