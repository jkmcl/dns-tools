package jkml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Type;

public class MXRecordFinder {

	private MXRecordFinder() {
	}

	public static List<MXRecord> lookUp(String name) throws IOException {
		var answers = new Lookup(name, Type.MX).run();
		if (answers == null) {
			return new ArrayList<>();
		}

		var records = new ArrayList<MXRecord>(answers.length);
		for (var ans : answers) {
			records.add((MXRecord) ans);
		}
		return records;
	}

	static List<MXRecord> shuffleAndSort(List<MXRecord> records) {
		Collections.shuffle(records);
		records.sort(Comparator.comparingInt(MXRecord::getPriority));
		return records;
	}

	public static List<MXRecord> findRecords(String name) throws IOException {
		return shuffleAndSort(lookUp(name));
	}

	public static List<String> findHosts(String name) throws IOException {
		var records = findRecords(name);
		var hosts = new ArrayList<String>(records.size());
		records.forEach(r -> hosts.add(r.getTarget().toString(true)));
		return hosts;
	}

}
