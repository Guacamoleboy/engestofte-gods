package engestofte.domain.event.controller;

import engestofte.domain.event.service.EventService;
import engestofte.domain.event.dto.request.EventMessageRequestDTO;
import engestofte.domain.event.dto.request.EventContactRequestDTO;
import engestofte.domain.event.dto.request.ChangeProposalRequestDTO;
import engestofte.domain.event.dto.request.ChangeProposalDecisionRequestDTO;
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

	public void findForOwner(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer ownerAccountId = requireOwner(context);
			return eventService.findForOwner(parseId(context.pathParam("id")), ownerAccountId);
		}, "Owner event loaded");
	}

	// _________________________________________________________________________________________________________________

	public void findImportantMessagesForOwner(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> eventService.findImportantMessages(requireOwner(context)), "Important messages loaded");
	}

	// _________________________________________________________________________________________________________________

	public void findImportantMessagesForStaff(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> eventService.findStaffImportantMessages(requireStaffAccount(context)), "Important messages loaded");
	}

	// _________________________________________________________________________________________________________________

	public void findImportantMessagesForAccount(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> eventService.findImportantMessages(requireCustomer(context)), "Important messages loaded");
	}

	// _________________________________________________________________________________________________________________

	public void addContactForOwner(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer accountId = requireOwner(context);
			EventContactRequestDTO request = context.bodyAsClass(EventContactRequestDTO.class);
			eventService.addContactPerson(parseId(context.pathParam("id")), accountId, request.getEmail(), true);
			return null;
		}, "Contact person added");
	}

	// _________________________________________________________________________________________________________________

	public void addContactForCustomer(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer accountId = requireCustomer(context);
			EventContactRequestDTO request = context.bodyAsClass(EventContactRequestDTO.class);
			eventService.addContactPerson(parseId(context.pathParam("id")), accountId, request.getEmail(), false);
			return null;
		}, "Contact person added");
	}

	// _________________________________________________________________________________________________________________

	public void findChangeProposalsForOwner(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			requireOwner(context);
			return eventService.findChangeProposalsForOwner(parseId(context.pathParam("id")));
		}, "Change proposals loaded");
	}

	// _________________________________________________________________________________________________________________

	public void findChangeProposalsForAccount(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> eventService.findChangeProposalsForAccount(parseId(context.pathParam("id")), requireCustomer(context)), "Change proposals loaded");
	}

	// _________________________________________________________________________________________________________________

	public void proposeChangeForOwner(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer accountId = requireOwner(context);
			ChangeProposalRequestDTO request = context.bodyAsClass(ChangeProposalRequestDTO.class);
			return eventService.proposeEventChange(parseId(context.pathParam("id")), accountId, request.getFieldName(), request.getNewValue(), true);
		}, "Change proposed");
	}

	// _________________________________________________________________________________________________________________

	public void proposeChangeForAccount(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer accountId = requireCustomer(context);
			ChangeProposalRequestDTO request = context.bodyAsClass(ChangeProposalRequestDTO.class);
			return eventService.proposeEventChange(parseId(context.pathParam("id")), accountId, request.getFieldName(), request.getNewValue(), false);
		}, "Change proposed");
	}

	// _________________________________________________________________________________________________________________

	public void decideChangeForOwner(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer accountId = requireOwner(context);
			ChangeProposalDecisionRequestDTO request = context.bodyAsClass(ChangeProposalDecisionRequestDTO.class);
			return eventService.decideChangeProposal(parseId(context.pathParam("id")), parseId(context.pathParam("proposalId")), accountId, request.getDecision(), request.getExplanation(), true);
		}, "Change proposal decided");
	}

	// _________________________________________________________________________________________________________________

	public void decideChangeForAccount(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer accountId = requireCustomer(context);
			ChangeProposalDecisionRequestDTO request = context.bodyAsClass(ChangeProposalDecisionRequestDTO.class);
			return eventService.decideChangeProposal(parseId(context.pathParam("id")), parseId(context.pathParam("proposalId")), accountId, request.getDecision(), request.getExplanation(), false);
		}, "Change proposal decided");
	}

	// _________________________________________________________________________________________________________________

	public void findForStaff(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer staffAccountId = requireStaffAccount(context);
			return eventService.findForStaff(parseId(context.pathParam("id")), staffAccountId);
		}, "Operational event loaded");
	}

	// _________________________________________________________________________________________________________________

	public void findAllForStaff(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			requireStaff(context);
			return eventService.findAllForStaff();
		}, "Operational events loaded");
	}

	// _________________________________________________________________________________________________________________

	public void sendStaffMessage(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer staffAccountId = requireStaffAccount(context);
			EventMessageRequestDTO request = context.bodyAsClass(EventMessageRequestDTO.class);
			return eventService.sendStaffMessage(parseId(context.pathParam("id")), staffAccountId, request.getContent());
		}, "Staff event message sent");
	}

	// _________________________________________________________________________________________________________________

	public void findMessagesForOwner(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer ownerAccountId = requireOwner(context);
			return eventService.findMessagesForOwner(parseId(context.pathParam("id")), ownerAccountId);
		}, "Owner event messages loaded");
	}

	// _________________________________________________________________________________________________________________

	public void sendOwnerMessage(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			Integer ownerAccountId = requireOwner(context);
			EventMessageRequestDTO request = context.bodyAsClass(EventMessageRequestDTO.class);
			return eventService.sendOwnerMessage(parseId(context.pathParam("id")), ownerAccountId, request.getContent());
		}, "Owner event message sent");
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

	public void closeByOwner(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			requireOwner(context);
			eventService.closeByOwner(parseId(context.pathParam("id")));
			return null;
		}, "Event closed");
	}

	// _________________________________________________________________________________________________________________

	public void requestDeposit(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			requireOwner(context);
			eventService.requestDeposit(parseId(context.pathParam("id")));
			return null;
		}, "Deposit request sent");
	}

	// _________________________________________________________________________________________________________________

	public void payDeposit(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			eventService.payDeposit(parseId(context.pathParam("id")), requireCustomer(context));
			return null;
		}, "Deposit recorded");
	}

	// _________________________________________________________________________________________________________________

	private static Integer requireCustomer(Context context) {
		String token = ContextHelper.extractBearerToken(context);
		if (!JwtUtil.isAccessTokenValid(token)) throw new ApiException(401, "A valid access token is required");
		if (!"CUSTOMER".equals(JwtService.getClaimRole(token))) throw new ApiException(403, "Customer access is required");
		return JwtService.getClaimAccountId(token);
	}

	// _________________________________________________________________________________________________________________

	private static Integer requireOwner(Context context) {
		String token = ContextHelper.extractBearerToken(context);
		if (!JwtUtil.isAccessTokenValid(token)) throw new ApiException(401, "A valid access token is required");
		if (!"OWNER".equals(JwtService.getClaimRole(token))) throw new ApiException(403, "Owner access is required");
		return JwtService.getClaimAccountId(token);
	}

	// _________________________________________________________________________________________________________________

	private static void requireStaff(Context context) {
		String token = ContextHelper.extractBearerToken(context);
		if (!JwtUtil.isAccessTokenValid(token)) throw new ApiException(401, "A valid access token is required");
		if (!"STAFF".equals(JwtService.getClaimRole(token))) throw new ApiException(403, "Staff access is required");
	}

	private static Integer requireStaffAccount(Context context) {
		String token = ContextHelper.extractBearerToken(context);
		if (!JwtUtil.isAccessTokenValid(token)) throw new ApiException(401, "A valid access token is required");
		if (!"STAFF".equals(JwtService.getClaimRole(token))) throw new ApiException(403, "Staff access is required");
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
