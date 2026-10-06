package engestofte.domain.event.mapper.response;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.event.dto.response.GuestInvitationResponseDTO;
import engestofte.domain.event.entity.Event;

public class GuestInvitationResponseMapper {
	private static final int REQUESTED_DATE_TURN_INDEX = 1;

	// _________________________________________________________________________________________________________________

	public static GuestInvitationResponseDTO toDTO(Event event) {
		GuestInvitationResponseDTO response = new GuestInvitationResponseDTO();
		response.setEventId(event.getId());
		response.setCategory(event.getCategory());
		String eventName = event.getEventData().path("eventName").asText();
		response.setEventName(eventName.isBlank() ? null : eventName);
		response.setRequestedDate(getRequestedDate(event.getEventData()));
		return response;
	}

	// _________________________________________________________________________________________________________________

	private static String getRequestedDate(JsonNode eventData) {
		JsonNode conversation = eventData.path("conversation");
		if (!conversation.isArray() || conversation.size() <= REQUESTED_DATE_TURN_INDEX) return null;
		String requestedDate = conversation.get(REQUESTED_DATE_TURN_INDEX).path("answer").asText();
		return requestedDate.isBlank() ? null : requestedDate;
	}
}
