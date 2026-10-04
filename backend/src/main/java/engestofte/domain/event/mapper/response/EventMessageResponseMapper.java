package engestofte.domain.event.mapper.response;

import engestofte.domain.event.dto.response.EventMessageResponseDTO;
import engestofte.domain.event.entity.EventMessage;

import java.util.List;

public class EventMessageResponseMapper {

	// _________________________________________________________________________________________________________________

	public static List<EventMessageResponseDTO> toDTOs(List<EventMessage> messages) {
		return messages.stream().map(EventMessageResponseMapper::toDTO).toList();
	}

	// _________________________________________________________________________________________________________________

	public static EventMessageResponseDTO toDTO(EventMessage message) {
		EventMessageResponseDTO response = new EventMessageResponseDTO();
		response.setId(message.getId());
		response.setSenderType(message.getSenderType());
		response.setSenderName(message.getSenderAccount().getFullName());
		response.setContent(message.getContent());
		response.setCreatedAt(message.getCreatedAt());
		return response;
	}
}
