package engestofte.domain.event.controller;

import engestofte.domain.event.service.EventService;
import engestofte.domain.event.dto.request.EventMessageRequestDTO;
import engestofte.exception.ApiException;
import engestofte.security.jwt.JwtService;
import engestofte.security.jwt.JwtUtil;
import engestofte.util.ContextHelper;
import engestofte.util.TryCatchHelper;
import io.javalin.http.Context;

public class EventController {

	// Attributes
	private final EventService eventService;

	// _________________________________________________________________________________________________________________

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	// _________________________________________________________________________________________________________________

	public void findForAccount(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			return eventService.findForAccount(parseId(context.pathParam("id")), requireCustomer(context));
		}, "Event loaded");
	}

	// _________________________________________________________________________________________________________________

	public void findMessagesForAccount(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> eventService.findMessagesForAccount(parseId(context.pathParam("id")), requireCustomer(context)), "Event messages loaded");
	}

	// _________________________________________________________________________________________________________________

	public void sendCustomerMessage(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			EventMessageRequestDTO request = context.bodyAsClass(EventMessageRequestDTO.class);
			return eventService.sendCustomerMessage(parseId(context.pathParam("id")), requireCustomer(context), request.getContent());
		}, "Message sent");
	}

	// _________________________________________________________________________________________________________________

	public void closeByCustomer(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> eventService.closeByCustomer(parseId(context.pathParam("id")), requireCustomer(context)), "Request closed");
	}

	// _________________________________________________________________________________________________________________

	private static Integer requireCustomer(Context context) {
		String token = ContextHelper.extractBearerToken(context);
		if (!JwtUtil.isAccessTokenValid(token)) throw new ApiException(401, "A valid access token is required");
		if (!"CUSTOMER".equals(JwtService.getClaimRole(token))) throw new ApiException(403, "Customer access is required");
		return JwtService.getClaimAccountId(token);
	}

	// _________________________________________________________________________________________________________________

	private static Integer parseId(String value) {
		try {
			int id = Integer.parseInt(value);
			if (id < 1) throw new NumberFormatException();
			return id;
		} catch (NumberFormatException exception) {
			throw new ApiException(400, "A valid event ID is required");
		}
	}
}
