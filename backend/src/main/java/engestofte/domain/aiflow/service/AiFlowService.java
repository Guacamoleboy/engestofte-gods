package engestofte.domain.aiflow.service;

import engestofte.config.DotEnv;
import engestofte.domain.aiflow.dao.AiFlowDAO;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;
import engestofte.domain.aiflow.entity.AiFlow;
import engestofte.domain.aiflow.mapper.request.AiFlowRequestMapper;
import engestofte.domain.aiflow.provider.AiFlowProvider;
import engestofte.domain.aiflow.provider.OpenAiFlowProvider;
import engestofte.exception.ApiException;
import engestofte.service.EntityManagerService;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AiFlowService extends EntityManagerService<AiFlow> {

	// Attributes
	private static final Logger LOGGER = LoggerFactory.getLogger(AiFlowService.class);
	private final AiFlowDAO aiFlowDAO;
	private final AiFlowProvider provider;

	// _________________________________________________________________________________________________________________

	public AiFlowService(EntityManager em) {
		super(new AiFlowDAO(em), AiFlow.class);
		this.aiFlowDAO = (AiFlowDAO) this.entityManagerDAO;
		String apiKey = DotEnv.getOptional("OPENAI_API_KEY");
		this.provider = apiKey == null || apiKey.isBlank() ? null : new OpenAiFlowProvider(apiKey);
		LOGGER.info("AI flow provider selected: {}", this.provider == null ? "unavailable (missing API key)" : "OpenAI");
	}

	// _________________________________________________________________________________________________________________

	public AiFlow interact(AiFlowRequestDTO request) {
		validateRequest(request);
		if (provider == null) {
			throw new ApiException(500, "AI service is unavailable");
		}
		AiFlow aiFlow = AiFlowRequestMapper.toEntity(request);
		AiFlowResponseDTO response = provider.respond(request);
		aiFlow.setAcknowledgement(response.getAcknowledgement());
		aiFlow.setNextQuestion(response.getNextQuestion());
		aiFlow.setCustomerName(response.getCustomerName());
		aiFlow.setStep(response.getStep());
		aiFlow.setStatus(response.getStatus());
		return aiFlowDAO.create(aiFlow);
	}

	// _________________________________________________________________________________________________________________

	private void validateRequest(AiFlowRequestDTO request) {
		if (request == null || request.getAnswer() == null || request.getAnswer().isBlank()
				|| request.getCurrentQuestion() == null || request.getCurrentQuestion().isBlank()) {
			throw new ApiException(400, "A question and answer are required");
		}
		if (request.getLanguage() == null || !request.getLanguage().matches("da|en|de")) {
			throw new ApiException(400, "Language must be da, en or de");
		}
		if (request.getStep() == null || request.getStep() < 1 || request.getStep() > 5) {
			throw new ApiException(400, "The flow step must be between 1 and 5");
		}
		if (request.getCustomerName() != null && request.getCustomerName().length() > 160) {
			throw new ApiException(400, "The customer name is too long");
		}
		if (request.getAnswer().length() > 2000 || request.getCurrentQuestion().length() > 500) {
			throw new ApiException(400, "The question or answer is too long");
		}
		if (request.getConversation() != null && request.getConversation().size() > 100) {
			throw new ApiException(400, "The conversation is too long");
		}
	}

}
