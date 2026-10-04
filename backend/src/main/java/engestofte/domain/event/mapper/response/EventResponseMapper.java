package engestofte.domain.event.mapper.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import engestofte.config.PoolConfig;
import engestofte.domain.event.dto.response.EventApprovalResponseDTO;
import engestofte.domain.event.dto.response.EventCustomerResponseDTO;
import engestofte.domain.event.entity.Event;

public class EventResponseMapper {

	// _________________________________________________________________________________________________________________

	public static EventApprovalResponseDTO toApprovalDTO(Event event) {
		EventApprovalResponseDTO response = new EventApprovalResponseDTO();
		response.setEventId(event.getId());
		response.setStatus(event.getStatus());
		response.setCreatedAt(event.getCreatedAt());
		return response;
	}

	// _________________________________________________________________________________________________________________

	public static EventCustomerResponseDTO toCustomerDTO(Event event) {
		EventCustomerResponseDTO response = new EventCustomerResponseDTO();
		response.setEventId(event.getId());
		response.setCategory(event.getCategory());
		response.setStatus(event.getStatus());
		response.setApprovedAt(event.getApprovedAt());
		response.setEventData(toCustomerEventData(event.getEventData()));
		response.setCustomerNote(event.getCustomerNote());
		response.setCreatedAt(event.getCreatedAt());
		return response;
	}

	// _________________________________________________________________________________________________________________

	private static JsonNode toCustomerEventData(JsonNode rawDraft) {
		ObjectNode approvedData = PoolConfig.getMapper().createObjectNode();
		JsonNode customerName = rawDraft.path("customerName");
		JsonNode guestCount = rawDraft.path("expectedGuestCount");
		JsonNode conversation = rawDraft.path("conversation");
		if (customerName.isTextual()) approvedData.put("customer_name", customerName.asText());
		if (guestCount.isNumber()) approvedData.put("expected_guest_count", guestCount.asInt());
		if (conversation.isArray()) approvedData.set("conversation", conversation.deepCopy());
		return approvedData;
	}
}
