package engestofte.domain.enquiry.mapper.response;

import engestofte.domain.enquiry.dto.response.EnquirySummaryResponseDTO;
import engestofte.domain.enquiry.entity.WeddingEnquiry;

import java.util.List;

public class EnquirySummaryResponseMapper {

	// _________________________________________________________________________________________________________________

	public static List<EnquirySummaryResponseDTO> toDTOs(List<WeddingEnquiry> enquiries) {
		return enquiries.stream().map(EnquirySummaryResponseMapper::toDTO).toList();
	}

	// _________________________________________________________________________________________________________________

	private static EnquirySummaryResponseDTO toDTO(WeddingEnquiry enquiry) {
		EnquirySummaryResponseDTO response = new EnquirySummaryResponseDTO();
		response.setSubmissionId(enquiry.getSubmissionId());
		response.setLanguage(enquiry.getLanguage());
		response.setStatus(enquiry.getStatus());
		response.setSubmittedAt(enquiry.getCreatedAt());
		response.setCustomerQuestion(enquiry.getCustomerQuestion());
		return response;
	}
}
