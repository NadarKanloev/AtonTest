import org.NadarKanloev.Команда_Корпоративной_шины_данных_и_микросервисов.InMemoryDB;
import org.NadarKanloev.Команда_Корпоративной_шины_данных_и_микросервисов.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryDatabaseTest {
    private InMemoryDB db;

    @BeforeEach
    public void setUp(){
        db = new InMemoryDB();
    }

    @Test
    public void TestAddRecord(){
        Record record = new Record(234678L, "Иванов Иван Иванович", 2035.34);
        db.addRecord(record);

        assertEquals(record, db.getRecordByAccount(234678L));
        assertEquals(record, db.getRecordByName("Иванов Иван Иванович"));
        assertTrue(db.getRecordByValue(2035.34).contains(record));
    }

    @Test
    public void testDeleteRecord(){
        Record record = new Record(234678L, "Иванов Иван Иванович", 2035.34);
        db.addRecord(record);
        db.deleteRecord(record);

        assertNull(db.getRecordByAccount(234678L));
        assertNull(db.getRecordByName("Иванов Иван Иванович"));
        assertNull(db.getRecordByValue(2035.34));
    }
    @Test
    public void testUpdateRecord() {
        Record record = new Record(234678L, "Иванов Иван Иванович", 2035.34);
        db.addRecord(record);

        db.updateRecord(234678L, "Петров Петр Петрович", 3000.00);

        assertNull(db.getRecordByName("Иванов Иван Иванович"));
        assertNull(db.getRecordByValue(2035.34));

        Record updatedRecord = db.getRecordByAccount(234678L);
        assertNotNull(updatedRecord);
        assertEquals("Петров Петр Петрович", updatedRecord.getName());
        assertEquals(3000.00, updatedRecord.getValue());
        assertEquals(1, db.getAccountIndex().size());
    }
    @Test
    public void testGetRecordByAccount() {
        Record record = new Record(234678L, "Иванов Иван Иванович", 2035.34);
        db.addRecord(record);

        assertEquals(record, db.getRecordByAccount(234678L));
    }

    @Test
    public void testGetRecordByName() {
        Record record = new Record(234678L, "Иванов Иван Иванович", 2035.34);
        db.addRecord(record);

        assertEquals(record, db.getRecordByName("Иванов Иван Иванович"));
    }

    @Test
    public void testGetRecordsByValue() {
        Record record1 = new Record(234678L, "Иванов Иван Иванович", 2035.34);
        Record record2 = new Record(123456L, "Петров Петр Петрович", 2035.34);
        db.addRecord(record1);
        db.addRecord(record2);

        Set<Record> records = db.getRecordByValue(2035.34);
        assertTrue(records.contains(record1));
        assertTrue(records.contains(record2));
    }

}
