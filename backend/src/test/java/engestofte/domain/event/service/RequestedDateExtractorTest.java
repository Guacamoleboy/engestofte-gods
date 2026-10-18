package engestofte.domain.event.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RequestedDateExtractorTest {

    // Attributes

    // _________________________________________________________________________________________________________________

	@Test
	void extractsDatesAcrossSupportedFormatsAndLanguages() {
		assertEquals("12.07.2028", RequestedDateExtractor.extract("We prefer 12.07.2028."));
		assertEquals("July 12th, 2028", RequestedDateExtractor.extract("July 12th, 2028 would work."));
		assertEquals("12.-14. juli 2028", RequestedDateExtractor.extract("12.-14. juli 2028"));
		assertEquals("März 2028", RequestedDateExtractor.extract("März 2028"));
	}

    // _________________________________________________________________________________________________________________

	@Test
	void extractsEveryDateAndReturnsNullWhenNoDateIsPresent() {
		assertEquals("2028-07-12 - August 2028", RequestedDateExtractor.extract("2028-07-12 or August 2028"));
		assertNull(RequestedDateExtractor.extract("We are flexible about the date."));
		assertNull(RequestedDateExtractor.extract("  "));
		assertNull(RequestedDateExtractor.extract(null));
	}

}