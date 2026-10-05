package engestofte.domain.event.mapper.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import engestofte.config.PoolConfig;
import engestofte.domain.event.dto.response.EventApprovalResponseDTO;
import engestofte.domain.event.dto.response.EventCustomerResponseDTO;
import engestofte.domain.event.dto.response.EventOperationalResponseDTO;
import engestofte.domain.event.dto.response.EventOwnerResponseDTO;
import engestofte.domain.event.entity.Event;
import engestofte.domain.event.enums.EventStatus;
import engestofte.domain.useraccount.entity.UserAccount;
import engestofte.util.EmailRedactor;

public class EventResponseMapper {
	private static final int REQUESTED_DATE_TURN_INDEX = 1;

	// _________________________________________________________________________________________________________________

	public static EventApprovalResponseDTO toApprovalDTO(Event event) {
		EventApprovalResponseDTO response = new EventApprovalResponseDTO();
		response.setEventId(event.getId());
		response.setStatus(customerFacingStatus(event));
		response.setCreatedAt(event.getCreatedAt());
		return response;
	}

	// _________________________________________________________________________________________________________________

	public static EventCustomerResponseDTO toCustomerDTO(Event event) {
		EventCustomerResponseDTO response = new EventCustomerResponseDTO();
		response.setEventId(event.getId());
		response.setCategory(event.getCategory());
		response.setStatus(customerFacingStatus(event));
		response.setApprovedAt(event.getApprovedAt());
		response.setEventData(event.getApprovedAt() == null
				? PoolConfig.getMapper().createObjectNode()
				: toCustomerEventData(event.getEventData()));
		response.setCustomerNote(event.getApprovedAt() == null ? null : event.getCustomerNote());
		response.setCreatedAt(event.getCreatedAt());
		return response;
	}

	// _________________________________________________________________________________________________________________

	public static EventCustomerResponseDTO toCustomerDTO(Event event, boolean primaryContact) {
		EventCustomerResponseDTO response = toCustomerDTO(event);
		response.setPrimaryContact(primaryContact);
		return response;
	}

	// _________________________________________________________________________________________________________________

	public static EventCustomerResponseDTO toCustomerDTO(Event event, UserAccount primaryContact, boolean isPrimaryContact) {
		EventCustomerResponseDTO response = toCustomerDTO(event, isPrimaryContact);
		response.setPrimaryContactName(primaryContact == null ? null : primaryContact.getFullName());
		response.setCustomerEmailRedacted(primaryContact == null ? null : EmailRedactor.redact(primaryContact.getEmail()));
		return response;
	}

	// _________________________________________________________________________________________________________________

	public static EventOwnerResponseDTO toOwnerDTO(Event event, UserAccount primaryContact) {
		EventOwnerResponseDTO response = new EventOwnerResponseDTO();
		response.setEventId(event.getId());
		response.setStatus(customerFacingStatus(event));
		response.setApprovedAt(event.getApprovedAt());
		response.setExpectedGuestCount(getExpectedGuestCount(event.getEventData()));
		response.setRequestedDate(getRequestedDate(event.getEventData()));
		response.setEventName(getEventName(event.getEventData()));
		response.setPrimaryContactName(primaryContact.getFullName());
		String customerName = event.getEventData().path("customerName").asText();
		response.setCustomerName(customerName.isBlank() ? primaryContact.getFullName() : customerName);
		response.setCustomerEmailRedacted(EmailRedactor.redact(primaryContact.getEmail()));
		response.setCreatedAt(event.getCreatedAt());
		return response;
	}

	// _________________________________________________________________________________________________________________

	public static EventOperationalResponseDTO toOperationalDTO(Event event) {
		EventOperationalResponseDTO response = new EventOperationalResponseDTO();
		response.setEventId(event.getId());
		response.setCategory(event.getCategory());
		response.setStatus(customerFacingStatus(event));
		response.setApprovedAt(event.getApprovedAt());
		response.setExpectedGuestCount(getExpectedGuestCount(event.getEventData()));
		response.setRequestedDate(getRequestedDate(event.getEventData()));
		response.setCreatedAt(event.getCreatedAt());
		return response;
	}

	// _________________________________________________________________________________________________________________

	private static JsonNode toCustomerEventData(JsonNode rawDraft) {
		ObjectNode approvedData = PoolConfig.getMapper().createObjectNode();
		JsonNode customerName = rawDraft.path("customerName");
		String eventName = getEventName(rawDraft);
		if (eventName != null) approvedData.put("event_name", eventName);
		JsonNode guestCount = rawDraft.path("expectedGuestCount");
		String requestedDate = getRequestedDate(rawDraft);
		if (customerName.isTextual()) approvedData.put("customer_name", customerName.asText());
		if (guestCount.isNumber()) approvedData.put("expected_guest_count", guestCount.asInt());
		if (requestedDate != null) approvedData.put("requested_date", requestedDate);
		return approvedData;
	}

	// _________________________________________________________________________________________________________________

	private static Integer getExpectedGuestCount(JsonNode rawDraft) {
		JsonNode guestCount = rawDraft.path("expectedGuestCount");
		return guestCount.isNumber() ? Integer.valueOf(guestCount.asInt()) : null;
	}

	// _________________________________________________________________________________________________________________

	private static String getRequestedDate(JsonNode rawDraft) {
		JsonNode conversation = rawDraft.path("conversation");
		if (!conversation.isArray() || conversation.size() <= REQUESTED_DATE_TURN_INDEX) return null;
		String requestedDate = conversation.get(REQUESTED_DATE_TURN_INDEX).path("answer").asText();
		return requestedDate.isBlank() ? null : requestedDate;
	}

	// _________________________________________________________________________________________________________________

	private static String getEventName(JsonNode rawDraft) {
		String eventName = rawDraft.path("eventName").asText();
		return eventName.isBlank() ? null : eventName;
	}

	// _________________________________________________________________________________________________________________

	private static EventStatus customerFacingStatus(Event event) {
		if (event.getStatus() == EventStatus.CLOSED_BY_CUSTOMER || event.getStatus() == EventStatus.CLOSED_BY_OWNER || event.getStatus() == EventStatus.CANCELLED_BY_CUSTOMER) return event.getStatus();
		if (event.getStatus() == EventStatus.AWAITING_APPROVAL) return EventStatus.AWAITING_APPROVAL;
		return event.getApprovedAt() == null ? event.getStatus() : EventStatus.APPROVED;
	}
}
