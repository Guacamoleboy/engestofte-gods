package engestofte.domain.enquiry.mapper.response;

import com.fasterxml.jackson.databind.JsonNode;
import engestofte.domain.enquiry.dto.response.EnquiryOwnerReviewResponseDTO;
import engestofte.domain.enquiry.dto.response.EnquiryOwnerReviewSummaryResponseDTO;
import engestofte.domain.enquiry.entity.WeddingEnquiry;

import java.util.List;

public class EnquiryOwnerReviewResponseMapper {

	// _________________________________________________________________________________________________________________

	public static List<EnquiryOwnerReviewSummaryResponseDTO> toSummaryDTOs(List<WeddingEnquiry> enquiries) {
		return enquiries.stream().map(EnquiryOwnerReviewResponseMapper::toSummaryDTO).toList();
	}

	// _________________________________________________________________________________________________________________

	public static EnquiryOwnerReviewResponseDTO toDTO(WeddingEnquiry enquiry) {
		EnquiryOwnerReviewResponseDTO response = new EnquiryOwnerReviewResponseDTO();
		response.setId(enquiry.getId());
		response.setSubmissionId(enquiry.getSubmissionId());
		response.setLanguage(enquiry.getLanguage());
		response.setStatus(enquiry.getStatus());
		response.setEventId(enquiry.getEvent() == null ? null : enquiry.getEvent().getId());
		response.setEventApprovedAt(enquiry.getEvent() == null ? null : enquiry.getEvent().getApprovedAt());
		response.setSubmittedAt(enquiry.getCreatedAt());
		response.setDraft(enquiry.getRawDraft());
		response.setAiAssessment(enquiry.getAiAssessment());
		response.setInternalNote(enquiry.getInternalNote());
		response.setCustomerQuestion(enquiry.getCustomerQuestion());
		return response;
	}

	// _________________________________________________________________________________________________________________

	private static EnquiryOwnerReviewSummaryResponseDTO toSummaryDTO(WeddingEnquiry enquiry) {
		EnquiryOwnerReviewSummaryResponseDTO response = new EnquiryOwnerReviewSummaryResponseDTO();
		response.setId(enquiry.getId());
		response.setCustomerName(enquiry.getRawDraft().path("customerName").asText(""));
		response.setSummary(getSummary(enquiry.getAiAssessment()));
		response.setStatus(enquiry.getStatus());
		response.setSubmittedAt(enquiry.getCreatedAt());
		return response;
	}

	private static String getSummary(JsonNode assessment) {
		return assessment == null ? "AI assessment unavailable" : assessment.path("summary").asText("");
	}
}
