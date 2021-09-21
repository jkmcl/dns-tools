package jkml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class MXRecordFinder {

	private MXRecordFinder() {
	}

	public static List<MXRecord> lookUp(String name) throws IOException {
		List<MXRecord> mxRecords = new ArrayList<>(0);
		Record[] records = new Lookup(name, Type.MX).run();
		if (records != null) {
			for (Record r : records) {
				mxRecords.add((MXRecord) r);
			}
		}
		return mxRecords;
	}

	static void shuffleAndSort(List<MXRecord> records) {
		Collections.shuffle(records);
		records.sort(Comparator.comparingInt(MXRecord::getPriority));
	}

	public static List<MXRecord> findRecords(String name) throws IOException {
		List<MXRecord> records = lookUp(name);
		shuffleAndSort(records);
		return records;
	}

	public static List<String> findHosts(String name) throws IOException {
		List<MXRecord> records = findRecords(name);
		List<String> hosts = new ArrayList<>(records.size());
		records.forEach(r -> hosts.add(r.getTarget().toString(true)));
		return hosts;
	}

}
