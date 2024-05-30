package org.NadarKanloev.Команда_Корпоративной_шины_данных_и_микросервисов;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Record {
    long account;
    String name;
    double value;
    @Override
    public String toString(){
        return "Record{" +
                "account=" + account +
                ", name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
