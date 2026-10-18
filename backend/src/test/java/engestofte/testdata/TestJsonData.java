package engestofte.testdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;

public final class TestJsonData {

    // Attributes
	private static final ObjectMapper MAPPER = new ObjectMapper();

    // _________________________________________________________________________________________________________________

	private TestJsonData() {
	    // singleton
    }

    // _________________________________________________________________________________________________________________

	public static <T> T read(String resourceName, String fixtureName, Class<T> type) {
		try (InputStream resource = TestJsonData.class.getClassLoader().getResourceAsStream(resourceName)) {
			if (resource == null) throw new IllegalArgumentException("Test data resource not found: " + resourceName);
			JsonNode fixture = MAPPER.readTree(resource).get(fixtureName);
			if (fixture == null || fixture.isNull()) throw new IllegalArgumentException("Test fixture not found: " + fixtureName);
			return MAPPER.treeToValue(fixture, type);
		} catch (IOException exception) {
			throw new IllegalStateException("Test data could not be read: " + resourceName, exception);
		}
	}

    // _________________________________________________________________________________________________________________

	public static JsonNode readNode(String resourceName, String fixtureName) {
		try (InputStream resource = TestJsonData.class.getClassLoader().getResourceAsStream(resourceName)) {
			if (resource == null) throw new IllegalArgumentException("Test data resource not found: " + resourceName);
			JsonNode fixture = MAPPER.readTree(resource).get(fixtureName);
			if (fixture == null || fixture.isNull()) throw new IllegalArgumentException("Test fixture not found: " + fixtureName);
			return fixture;
		} catch (IOException exception) {
			throw new IllegalStateException("Test data could not be read: " + resourceName, exception);
		}
	}

}