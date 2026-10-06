package engestofte.domain.enquiry.mapper.response;

import engestofte.domain.enquiry.dto.response.EnquirySummaryResponseDTO;
import engestofte.domain.enquiry.entity.WeddingEnquiry;
import engestofte.domain.enquiry.enums.EnquiryStatus;
import engestofte.domain.event.enums.EventStatus;

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
		response.setEventId(enquiry.getEvent() == null ? null : enquiry.getEvent().getId());
		response.setLanguage(enquiry.getLanguage());
		response.setStatus(enquiry.getEvent() != null && enquiry.getEvent().getApprovedAt() != null
				? customerStatusFor(enquiry.getEvent().getStatus())
				: enquiry.getStatus());
		response.setSubmittedAt(enquiry.getCreatedAt());
		response.setCustomerQuestion(enquiry.getCustomerQuestion());
		return response;
	}

	// _________________________________________________________________________________________________________________

	private static EnquiryStatus customerStatusFor(EventStatus status) {
		return switch (status) {
			case AWAITING_DEPOSIT -> EnquiryStatus.AWAITING_DEPOSIT;
			case BOOKED -> EnquiryStatus.BOOKED;
			case CLOSED_BY_OWNER -> EnquiryStatus.CLOSED_BY_OWNER;
			case CLOSED_BY_CUSTOMER -> EnquiryStatus.CLOSED_BY_CUSTOMER;
			case CANCELLED_BY_CUSTOMER -> EnquiryStatus.CANCELLED_BY_CUSTOMER;
			default -> EnquiryStatus.APPROVED;
		};
	}
}
