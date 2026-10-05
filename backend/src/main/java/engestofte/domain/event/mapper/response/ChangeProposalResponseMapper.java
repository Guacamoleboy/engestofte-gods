package engestofte.domain.event.mapper.response;

import engestofte.domain.event.changeproposal.ChangeApprovalDecision;
import engestofte.domain.event.changeproposal.ChangeApprovalParty;
import engestofte.domain.event.dto.response.ChangeProposalResponseDTO;
import engestofte.domain.event.entity.ChangeProposal;

import java.util.List;

public class ChangeProposalResponseMapper {

	// _________________________________________________________________________________________________________________

	public static List<ChangeProposalResponseDTO> toDTOs(List<ChangeProposal> proposals) {
		return proposals.stream().map(ChangeProposalResponseMapper::toDTO).toList();
	}

	// _________________________________________________________________________________________________________________

	public static ChangeProposalResponseDTO toDTO(ChangeProposal proposal) {
		ChangeProposalResponseDTO response = new ChangeProposalResponseDTO();
		response.setId(proposal.getId());
		response.setFieldName(proposal.getFieldName());
		response.setOldValue(proposal.getOldValue());
		response.setNewValue(proposal.getNewValue());
		response.setProposerName(proposal.getProposerAccount().getFullName());
		response.setProposerParty(proposal.getProposerParty());
		response.setStatus(proposal.getStatus());
		response.setCustomerApproved(proposal.getApprovals().stream().anyMatch(approval -> approval.getParty() == ChangeApprovalParty.CUSTOMER && approval.getDecision() == ChangeApprovalDecision.APPROVED));
		response.setOwnerApproved(proposal.getApprovals().stream().anyMatch(approval -> approval.getParty() == ChangeApprovalParty.OWNER && approval.getDecision() == ChangeApprovalDecision.APPROVED));
		response.setRejectionExplanation(proposal.getRejectionExplanation());
		response.setCreatedAt(proposal.getCreatedAt());
		return response;
	}
}
