package engestofte.domain.enquiry.controller;

import engestofte.domain.enquiry.dto.request.EnquirySubmissionRequestDTO;
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
}
