package jkml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.MXRecord;

class MXRecordFinderTests {

	private static final String MAIL_HOST = "gmail.com";

	private static final Logger logger = LoggerFactory.getLogger(MXRecordFinderTests.class);

	private void logRecords(List<MXRecord> records) {
		records.forEach(r -> logger.info("{} (priority: {})", r.getTarget().toString(true), r.getPriority()));
	}

	@BeforeEach
	void beforeEach(TestInfo testInfo) {
		logger.info("# Start of test: {}", testInfo.getDisplayName());
	}

	@Test
	void testLookUpShuffleAndSort() throws Exception {
		var records = MXRecordFinder.lookUp(MAIL_HOST);
		var beforeCount = records.size();
		logger.info("List before shuffling and sorting:");
		logRecords(records);

		MXRecordFinder.shuffleAndSort(records);
		var afterCount = records.size();
		logger.info("List after shuffling and sorting:");
		logRecords(records);

		assertEquals(beforeCount, afterCount);
	}

	@Test
	void testFindRecords() throws Exception {
		var records = MXRecordFinder.findRecords(MAIL_HOST);
		assertFalse(records.isEmpty());
		logRecords(records);
	}

	@Test
	void testFindHosts() throws Exception {
		var hosts = MXRecordFinder.findHosts(MAIL_HOST);
		assertFalse(hosts.isEmpty());
		hosts.forEach(logger::info);
	}

}
