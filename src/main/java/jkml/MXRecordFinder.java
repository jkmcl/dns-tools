package jkml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class MXRecordFinder {

	private final Logger log = LoggerFactory.getLogger(MXRecordFinder.class);

	List<MXRecord> lookUp(String name) throws IOException {
		log.debug("Looking up MX records of {}", name);
		Record[] records = new Lookup(name, Type.MX).run();
		List<MXRecord> mxRecords = new ArrayList<>((records == null) ? 0 : records.length);
		for (Record r : records) {
			mxRecords.add((MXRecord) r);
		}
		return mxRecords;
	}

	void shuffleAndSort(List<MXRecord> records) {
		log.debug("Shuffling MX records...");
		Collections.shuffle(records);

		log.debug("Sorting shuffled MX records by priority...");
		SortedMap<Integer, List<MXRecord>> sortedMap = new TreeMap<>();
		for (MXRecord r : records) {
			int priority = r.getPriority();
			List<MXRecord> list = sortedMap.compute(priority, (k, v) -> {
				if (v == null) {
					v = new ArrayList<>();
					sortedMap.put(priority, v);
				}
				return v;
			});
			list.add(r);
		}
		records.clear();
		sortedMap.forEach((k, v) -> records.addAll(v));
	}

	public List<MXRecord> findRecords(String name) throws IOException {
		List<MXRecord> records = lookUp(name);
		shuffleAndSort(records);
		return records;
	}

	public List<String> findHosts(String name) throws IOException {
		List<MXRecord> records = findRecords(name);

		log.debug("Extracting hosts from MX records...");
		List<String> hosts = new ArrayList<>(records.size());
		records.forEach(r -> hosts.add(r.getTarget().toString(true)));
		return hosts;
	}

}
