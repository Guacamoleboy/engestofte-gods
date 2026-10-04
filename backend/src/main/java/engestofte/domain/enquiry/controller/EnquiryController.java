package engestofte.domain.enquiry.controller;

import engestofte.domain.enquiry.dto.request.EnquirySubmissionRequestDTO;
import engestofte.domain.enquiry.dto.request.EnquiryOwnerReviewRequestDTO;
import engestofte.domain.enquiry.service.EnquiryService;
import engestofte.exception.ApiException;
import engestofte.security.jwt.JwtService;
import engestofte.security.jwt.JwtUtil;
import engestofte.util.ContextHelper;
import engestofte.util.TryCatchHelper;
import io.javalin.http.Context;

public class EnquiryController {

	// Attributes
	private final EnquiryService enquiryService;

	// _________________________________________________________________________________________________________________

	public EnquiryController(EnquiryService enquiryService) {
		this.enquiryService = enquiryService;
	}

	// _________________________________________________________________________________________________________________

	public void submit(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			String token = ContextHelper.extractBearerToken(context);
			if (!JwtUtil.isAccessTokenValid(token)) throw new ApiException(401, "A valid access token is required");
			Integer accountId = JwtService.getClaimAccountId(token);
			return enquiryService.submit(accountId, context.bodyAsClass(EnquirySubmissionRequestDTO.class));
		}, "Enquiry submitted");
	}

	// _________________________________________________________________________________________________________________

	public void listForAccount(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			String token = ContextHelper.extractBearerToken(context);
			if (!JwtUtil.isAccessTokenValid(token)) throw new ApiException(401, "A valid access token is required");
			Integer accountId = JwtService.getClaimAccountId(token);
			return enquiryService.findForAccount(accountId);
		}, "Enquiries loaded");
	}

	// _________________________________________________________________________________________________________________

	public void listForOwnerReview(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			requireOwner(context);
			return enquiryService.findForOwnerReview();
		}, "Owner enquiries loaded");
	}

	// _________________________________________________________________________________________________________________

	public void findOwnerReview(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			requireOwner(context);
			return enquiryService.findOwnerReviewById(parseId(context.pathParam("id")));
		}, "Owner enquiry loaded");
	}

	// _________________________________________________________________________________________________________________

	public void saveOwnerReview(Context context) {
		TryCatchHelper.tryCatchHelper(context, () -> {
			requireOwner(context);
			return enquiryService.saveOwnerReview(
					parseId(context.pathParam("id")),
					context.bodyAsClass(EnquiryOwnerReviewRequestDTO.class));
		}, "Owner review saved");
	}

	// _________________________________________________________________________________________________________________

	private static Integer requireOwner(Context context) {
		String token = ContextHelper.extractBearerToken(context);
		if (!JwtUtil.isAccessTokenValid(token)) throw new ApiException(401, "A valid access token is required");
		if (!"OWNER".equals(JwtService.getClaimRole(token))) throw new ApiException(403, "Owner access is required");
		return JwtService.getClaimAccountId(token);
	}

	private static Integer parseId(String value) {
		try {
			int id = Integer.parseInt(value);
			if (id < 1) throw new NumberFormatException();
			return id;
		} catch (NumberFormatException exception) {
			throw new ApiException(400, "A valid enquiry ID is required");
		}
	}
}
