package engestofte.domain.aiflow.provider;

import com.sun.net.httpserver.HttpServer;
import engestofte.domain.aiflow.dto.request.AiFlowRequestDTO;
import engestofte.domain.aiflow.dto.request.AiFlowTurnDTO;
import engestofte.testdata.TestJsonData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpenAiFlowProviderTest {
	private HttpServer server;
	private final List<String> requests = new ArrayList<>();
	private OpenAiFlowProvider provider;

	@BeforeEach
	void startApiStub() throws Exception {
		server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		server.createContext("/responses", exchange -> {
			requests.add(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
			String content = requests.size() == 1
					? "{\"acknowledgement\":\"Thank you.\",\"next_question\":\"What date?\",\"customer_name\":\"User One\",\"step\":1,\"expected_guest_count\":0,\"status\":\"IN_PROGRESS\"}"
					: "{\"summary\":\"A wedding for User One.\",\"missing_information\":[],\"uncertainties\":[],\"conflicts\":[],\"upsell_suggestions\":[]}";
			String response = "{\"output\":[{\"content\":[{\"type\":\"output_text\",\"text\":"
					+ engestofte.config.PoolConfig.getMapper().writeValueAsString(content) + "}]}]}";
			byte[] body = response.getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().set("Content-Type", "application/json");
			exchange.sendResponseHeaders(200, body.length);
			exchange.getResponseBody().write(body);
			exchange.close();
		});
		server.start();
		provider = new OpenAiFlowProvider("test-key", HttpClient.newHttpClient(),
				URI.create("http://127.0.0.1:" + server.getAddress().getPort() + "/responses"));
	}

	@AfterEach
	void stopApiStub() {
		server.stop(0);
	}

	@Test
	void aiRequestsSetExplicitOutputTokenLimits() throws Exception {
		AiFlowRequestDTO request = TestJsonData.read("testdata/data/core.json", "aiFlowRequest", AiFlowRequestDTO.class);
		request.setRequestId("b4afc574-8dbb-4588-a76e-bd7f34b9f582");
		provider.respond(request);

		AiFlowTurnDTO turn = new AiFlowTurnDTO();
		turn.setQuestion("What is your name?");
		turn.setAnswer("User One");
		provider.assess(List.of(turn), "en");

		assertEquals(1200, engestofte.config.PoolConfig.getMapper().readTree(requests.get(0)).path("max_output_tokens").asInt());
		assertEquals(2400, engestofte.config.PoolConfig.getMapper().readTree(requests.get(1)).path("max_output_tokens").asInt());
	}
}
