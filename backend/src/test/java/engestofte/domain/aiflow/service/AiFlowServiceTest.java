package engestofte.domain.aiflow.service;

import engestofte.ATest;
import engestofte.domain.aiflow.dao.AiFlowDAO;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.response.AiFlowResponseDTO;
import engestofte.domain.aiflow.entity.AiFlow;
import engestofte.domain.aiflow.enums.AiFlowStatus;
import engestofte.domain.aiflow.provider.AiFlowProvider;
import engestofte.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class AiFlowServiceTest extends ATest {

    // Attributes
	private AiFlowDAO aiFlowDAO;

	// _________________________________________________________________________________________________________________

	@BeforeEach
	void clearInteractions() {
		aiFlowDAO = new AiFlowDAO(em);
		aiFlowDAO.deleteAll();
	}

	// _________________________________________________________________________________________________________________

	@Test
	void interactValidatesThenPersistsTheProviderResponse() {
		AtomicReference<AiFlowRequestDTO> receivedRequest = new AtomicReference<>();
		AiFlowProvider provider = request -> {
			receivedRequest.set(request);
			return response();
		};
		AiFlowRequestDTO request = validRequest();
		AiFlow result = new AiFlowService(em, provider).interact(request);
		assertSame(request, receivedRequest.get());
		assertNotNull(result.getId());
		assertEquals("user1", result.getAnswer());
		assertEquals("Thank you.", result.getAcknowledgement());
		assertEquals("What date are you considering?", result.getNextQuestion());
		assertEquals(AiFlowStatus.IN_PROGRESS, result.getStatus());
		assertEquals(result.getId(), aiFlowDAO.getById(result.getId()).getId());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void interactRejectsInvalidRequestBeforeCallingProvider() {
		AtomicReference<AiFlowRequestDTO> receivedRequest = new AtomicReference<>();
		AiFlowService service = new AiFlowService(em, request -> {
			receivedRequest.set(request);
			return response();
		});
		AiFlowRequestDTO request = validRequest();
		request.setLanguage("fr");
		ApiException exception = assertThrows(ApiException.class, () -> service.interact(request));

		assertEquals(400, exception.getCode());
		assertEquals("Language must be da, en or de", exception.getMessage());
		assertNull(receivedRequest.get());
		assertTrue(aiFlowDAO.getAll().isEmpty());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void interactReportsUnavailableProviderWithoutPersisting() {
		ApiException exception = assertThrows(ApiException.class, () ->
                new AiFlowService(em, null)
                .interact(validRequest())
        );
		assertEquals(500, exception.getCode());
		assertEquals("AI service is unavailable", exception.getMessage());
		assertTrue(aiFlowDAO.getAll().isEmpty());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void retryingTheSameRequestReturnsTheStoredResultWithoutCallingTheProviderAgain() {
		AtomicInteger providerCalls = new AtomicInteger();
		AiFlowProvider provider = request -> {
			providerCalls.incrementAndGet();
			return response();
		};
		AiFlowService service = new AiFlowService(em, provider);
		AiFlowRequestDTO request = validRequest();

		AiFlow firstResult = service.interact(request);
		em.clear();
		AiFlow retryResult = service.interact(request);

		assertEquals(firstResult.getId(), retryResult.getId());
		assertEquals(1, providerCalls.get());
		assertEquals(1, aiFlowDAO.getAll().size());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void requestIdCannotBeReusedForDifferentInput() {
		AiFlowService service = new AiFlowService(em, request -> response());
		service.interact(validRequest());
		AiFlowRequestDTO changed = validRequest();
		changed.setAnswer("a different answer");

		ApiException exception = assertThrows(ApiException.class, () -> service.interact(changed));

		assertEquals(409, exception.getCode());
		assertEquals(1, aiFlowDAO.getAll().size());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void interactRequiresAValidRequestIdBeforeCallingProvider() {
		AtomicReference<AiFlowRequestDTO> receivedRequest = new AtomicReference<>();
		AiFlowService service = new AiFlowService(em, request -> {
			receivedRequest.set(request);
			return response();
		});
		AiFlowRequestDTO request = validRequest();
		request.setRequestId("not-a-uuid");

		ApiException exception = assertThrows(ApiException.class, () -> service.interact(request));

		assertEquals(400, exception.getCode());
		assertEquals("A valid request_id is required", exception.getMessage());
		assertNull(receivedRequest.get());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void stepThreeDoesNotRepeatTheStepTwoAvailabilityNotice() {
		AiFlowRequestDTO request = validRequest();
		request.setStep(3);
		AiFlowResponseDTO providerResponse = response();
		providerResponse.setStep(3);
		providerResponse.setAcknowledgement("Tak for din bekræftelse, Lisa. 2027 er desværre fuldt booket, men I kan fortsat sende forespørgslen til vurdering.");
		providerResponse.setNextQuestion("Hvor mange gæster forventer I?");
		AiFlow result = new AiFlowService(em, ignored -> providerResponse).interact(request);

		assertEquals("Tak for din bekræftelse, Lisa.", result.getAcknowledgement());
		assertEquals("Hvor mange gæster forventer I?", result.getNextQuestion());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void stepTwoKeepsTheAvailabilityNoticeInAcknowledgementOnly() {
		AiFlowRequestDTO request = validRequest();
		request.setStep(2);
		AiFlowResponseDTO providerResponse = response();
		providerResponse.setStep(2);
		providerResponse.setAcknowledgement("Tak for datooplysningen. 2027 er fuldt booket.");
		providerResponse.setNextQuestion("2027 er desværre fuldt booket. Ønsker du at fortsætte forespørgslen, Lisa?");
		AiFlow result = new AiFlowService(em, ignored -> providerResponse).interact(request);

		assertEquals("Tak for datooplysningen. 2027 er fuldt booket.", result.getAcknowledgement());
		assertEquals("Ønsker du at fortsætte forespørgslen, Lisa?", result.getNextQuestion());
	}

	// _________________________________________________________________________________________________________________

	@Test
	void confirmingContinuationDoesNotRepeatTheStepTwoAvailabilityNoticeOnTheStepThreeTransition() {
		AiFlowRequestDTO request = validRequest();
		request.setStep(2);
		request.setAnswer("Jeps");
		request.setCurrentQuestion("Ønsker du at fortsætte forespørgslen, Lisa?");
		AiFlowResponseDTO providerResponse = response();
		providerResponse.setStep(2);
		providerResponse.setStatus(AiFlowStatus.STEP_COMPLETE);
		providerResponse.setAcknowledgement("Tak for din bekræftelse, Lisa. 2027 er desværre fuldt booket, men du kan fortsat sende forespørgslen til vurdering hos Engestofte Gods.");
		providerResponse.setNextQuestion("");
		AiFlow result = new AiFlowService(em, ignored -> providerResponse).interact(request);

		assertEquals("Tak for din bekræftelse, Lisa.", result.getAcknowledgement());
		assertEquals("", result.getNextQuestion());
	}

	// _________________________________________________________________________________________________________________

	private AiFlowRequestDTO validRequest() {
		AiFlowRequestDTO request = engestofte.testdata.TestJsonData.read("testdata/data/core.json", "aiFlowRequest", AiFlowRequestDTO.class);
		request.setRequestId("b4afc574-8dbb-4588-a76e-bd7f34b9f582");
		return request;
	}

	// _________________________________________________________________________________________________________________

	private AiFlowResponseDTO response() {
		return engestofte.testdata.TestJsonData.read("testdata/data/core.json", "aiFlowResponse", AiFlowResponseDTO.class);
	}

}
