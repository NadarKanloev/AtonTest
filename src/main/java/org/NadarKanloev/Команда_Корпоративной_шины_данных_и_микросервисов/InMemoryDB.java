package org.NadarKanloev.Команда_Корпоративной_шины_данных_и_микросервисов;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

@Setter
@Getter
@AllArgsConstructor
public class InMemoryDB {

     //Выбор структуры данных для индексации был сделан в пользу TreeMap, у которой "под капотом" находится сбалансированное красно-черное дерево, т.к худшее время основных операций - O(log(n)), в то время как HashMap - O(n)

    /**
     * Индекс записей по полю "account".
     * Ключ: account (long)
     * Значение: Record
     */
    TreeMap<Long, Record> accountIndex;
    /**
     * Индекс записей по полю "name".
     * Ключ: name (String)
     * Значение: Record
     */
    TreeMap<String, Record> nameIndex;
    /**
     * Индекс записей по полю "value".
     * Ключ: value (double)
     * Значение: HashSet<Record> - набор записей с одинаковым значением "value"
     * Использование HashSet обосновано тем, что одинаковых значений в теории может быть несколько, в то время как имя и id аккаунта скорее всего будут уникальными
     */
    TreeMap<Double, HashSet<Record>> valueIndex;


    public InMemoryDB(){
        accountIndex = new TreeMap<>();
        nameIndex = new TreeMap<>();
        valueIndex = new TreeMap<>();
    }

    /**
     * Добавление записи
     * @param record
     */
    public void addRecord(Record record){
        accountIndex.put(record.getAccount(), record);
        nameIndex.put(record.getName(), record);
        valueIndex.computeIfAbsent(record.getValue(), k -> new HashSet<>()).add(record);
    }

    /**
     * Удаление записи
     * @param record
     */
    public void deleteRecord(Record record){
        accountIndex.remove(record.getAccount());
        nameIndex.remove(record.getName());

        Set<Record> records = valueIndex.get(record.getValue());
        if(records != null){
            records.remove(record);
            if(records.isEmpty()){
                valueIndex.remove(record.getValue());
            }
        }
    }

    /**
     * Обновление записи
     * @param account
     * @param newName
     * @param newValue
     */
    public void updateRecord(long account, String newName, double newValue){
        Record record = accountIndex.get(account);
        if(record != null){
            deleteRecord(record);

            record.setName(newName);
            record.setValue(newValue);
            addRecord(record);
        }
    }

    /**
     * Получение записи по id аккаунта
     * @param account
     * @return
     */
    public Record getRecordByAccount(long account){
        return accountIndex.get(account);
    }

    /**
     * Получение записи по имени
     * @param name
     * @return
     */
    public Record getRecordByName(String name){
        return nameIndex.get(name);
    }

    /**
     * Получение записей по величине
     * @param value
     * @return
     */
    public Set<Record> getRecordByValue(double value){
        return valueIndex.get(value);
    }
}
