package engestofte.domain.aiflow.service;

import engestofte.domain.aiflow.dao.AiFlowDAO;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;
import engestofte.domain.aiflow.entity.AiFlow;
import engestofte.domain.aiflow.enums.AiFlowStatus;
import engestofte.domain.aiflow.mapper.request.AiFlowRequestMapper;
import engestofte.domain.aiflow.provider.AiFlowProvider;
import engestofte.domain.aiflow.provider.AiFlowProviderFactory;
import engestofte.exception.ApiException;
import engestofte.service.EntityManagerService;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class AiFlowService extends EntityManagerService<AiFlow> {

	// Attributes
	private static final Logger LOGGER = LoggerFactory.getLogger(AiFlowService.class);
	private static final Pattern YEAR_2027 = Pattern.compile("\\b2027\\b", Pattern.CASE_INSENSITIVE);
	private static final Pattern BOOKED_STATUS = Pattern.compile("\\b(?:fully\\s+booked|booked|fuldt\\s+booket|booket|ausgebucht)\\b", Pattern.CASE_INSENSITIVE);
	private static final Pattern CONTINUATION_QUESTION = Pattern.compile("\\b(?:continue|continuing|fortsæt|fortsætte|fortsætter|fortfahren)\\b", Pattern.CASE_INSENSITIVE);
	private final AiFlowDAO aiFlowDAO;
	private final AiFlowProvider provider;

	// _________________________________________________________________________________________________________________

	public AiFlowService(EntityManager em) {
		this(em, AiFlowProviderFactory.create());
	}

	// _________________________________________________________________________________________________________________

	public AiFlowService(EntityManager em, AiFlowProvider provider) {
		super(new AiFlowDAO(em), AiFlow.class);
		this.aiFlowDAO = (AiFlowDAO) this.entityManagerDAO;
		this.provider = provider;
		LOGGER.info("AI flow provider selected: {}", this.provider == null ? "unavailable (missing API key)" : this.provider.getClass().getSimpleName());
	}

	// _________________________________________________________________________________________________________________

	public AiFlow interact(AiFlowRequestDTO request) {
		validateRequest(request);
		String requestId = UUID.fromString(request.getRequestId()).toString();
		AiFlow existing = aiFlowDAO.findByRequestId(requestId);
		if (existing != null) {
			if (!sameRequest(existing, request)) throw new ApiException(409, "The request_id is already used for a different interaction");
			return existing;
		}
		if (provider == null) {
			throw new ApiException(500, "AI service is unavailable");
		}
		AiFlow aiFlow = AiFlowRequestMapper.toEntity(request);
		aiFlow.setRequestId(requestId);
		AiFlowResponseDTO response = provider.respond(request);
		sanitizeAvailabilityByStep(response, request);
		aiFlow.setAcknowledgement(response.getAcknowledgement());
		aiFlow.setNextQuestion(response.getNextQuestion());
		aiFlow.setCustomerName(response.getCustomerName());
		aiFlow.setStep(response.getStep());
		aiFlow.setStatus(response.getStatus());
		return aiFlowDAO.create(aiFlow);
	}

	private static boolean sameRequest(AiFlow existing, AiFlowRequestDTO request) {
		return Objects.equals(existing.getAnswer(), request.getAnswer())
				&& Objects.equals(existing.getCurrentQuestion(), request.getCurrentQuestion())
				&& Objects.equals(existing.getCustomerName(), request.getCustomerName())
				&& Objects.equals(existing.getStep(), request.getStep())
				&& Objects.equals(existing.getLanguage(), request.getLanguage());
	}

	private static void sanitizeAvailabilityByStep(AiFlowResponseDTO response, AiFlowRequestDTO request) {
		if (request.getStep() == 2) {
			boolean confirmsContinuation = response.getStatus() == AiFlowStatus.STEP_COMPLETE
					&& contains2027BookingStatus(response.getAcknowledgement())
					&& request.getCurrentQuestion() != null
					&& CONTINUATION_QUESTION.matcher(request.getCurrentQuestion()).find();
			if (confirmsContinuation) {
				response.setAcknowledgement(remove2027BookingSentences(response.getAcknowledgement()));
				response.setNextQuestion(remove2027BookingSentences(response.getNextQuestion()));
				return;
			}
			if (contains2027BookingStatus(response.getAcknowledgement())) {
				response.setNextQuestion(remove2027BookingSentences(response.getNextQuestion()));
			}
			return;
		}

		response.setAcknowledgement(remove2027BookingSentences(response.getAcknowledgement()));
		response.setNextQuestion(remove2027BookingSentences(response.getNextQuestion()));
	}

	private static String remove2027BookingSentences(String text) {
		if (text == null || text.isBlank()) return text;
		return Arrays.stream(text.split("(?<=[.!?])\\s+"))
				.filter(sentence -> !contains2027BookingStatus(sentence))
				.reduce((first, second) -> first + " " + second)
				.orElse("")
				.trim();
	}

	private static boolean contains2027BookingStatus(String text) {
		return text != null && YEAR_2027.matcher(text).find() && BOOKED_STATUS.matcher(text).find();
	}

	// _________________________________________________________________________________________________________________

	private void validateRequest(AiFlowRequestDTO request) {
		if (request == null || request.getAnswer() == null || request.getAnswer().isBlank()
				|| request.getCurrentQuestion() == null || request.getCurrentQuestion().isBlank()) {
			throw new ApiException(400, "A question and answer are required");
		}
		try {
			if (request.getRequestId() == null) throw new IllegalArgumentException();
			UUID.fromString(request.getRequestId());
		} catch (IllegalArgumentException exception) {
			throw new ApiException(400, "A valid request_id is required");
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
