package engestofte.domain.aiflow.provider;

import engestofte.config.DotEnv;

public final class AiFlowProviderFactory {
	private AiFlowProviderFactory() {
	}

	public static OpenAiFlowProvider create() {
		String apiKey = DotEnv.getOptional("OPENAI_API_KEY");
		return apiKey == null || apiKey.isBlank() ? null : new OpenAiFlowProvider(apiKey);
	}
}
