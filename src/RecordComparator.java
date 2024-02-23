import java.util.Comparator;

public class RecordComparator implements Comparator<Record> {
  @Override
  public int compare(Record record1, Record record2) {
      return record1.getUuid().compareTo(record2.getUuid());
  }
}
