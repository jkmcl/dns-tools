package jkml;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.MXRecord;

public class MXRecordFinderTests {

	private static final String MAIL_HOST = "gmail.com";

	private final Logger log = LoggerFactory.getLogger(MXRecordFinderTests.class);

	private void logRecords(List<MXRecord> records) {
		records.forEach(r -> log.info("{} {}", r.getTarget(), r.getPriority()));
	}

	@Test
	public void testLookUpShuffleAndSort() throws Exception {
		MXRecordFinder finder = new MXRecordFinder();

		List<MXRecord> records = finder.lookUp(MAIL_HOST);
		log.info("List before shuffling and sorting:");
		logRecords(records);

		finder.shuffleAndSort(records);
		log.info("List after shuffling and sorting:");
		logRecords(records);
	}

	@Test
	public void testFindRecords() throws Exception {
		MXRecordFinder finder = new MXRecordFinder();
		logRecords(finder.findRecords(MAIL_HOST));
	}

	@Test
	public void testFindHosts() throws Exception {
		MXRecordFinder finder = new MXRecordFinder();
		finder.findHosts(MAIL_HOST).forEach(c -> log.info(c));
	}

}
