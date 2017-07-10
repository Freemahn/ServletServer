package com.intera.server.storage;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents storage of all Records with some methods similar to simple DB
 * <p>
 * Created by pgordon on 23.06.2017.
 */
public class Storage {
    private ArrayList<Record> list;

    public Storage() {
        list = new ArrayList<>();
    }


    public void put(Record record) {
        list.add(record);
    }

    public String findAll(String query) {
        List<Record> result = list.stream()
                .filter(Record.findByQuery(query))
                .collect(Collectors.toList());
        return prettyPrint(result);
    }

    public String prettyPrint() {
        return prettyPrint(list);
    }


    /**
     * Prints one record for "list" command
     */
    public String prettyPrint(Record r) {
        String str = "";
        str += "|";
        str += padRight("" + r.getId(), 2);
        str += "|";
        str += padRight(r.getName(), 10);
        str += "|";
        str += padRight(r.getSurname(), 20);
        str += "|";
        str += padRight(r.getPatronymic(), 15);
        str += "|";
        str += padRight(r.getPosition(), 8);
        str += "|\r\n";
        return str;
    }

    /**
     * Prints collection of records for "list" command
     */
    public String prettyPrint(Collection<Record> records) {
        StringBuilder table = new StringBuilder();
        for (Record r : records) {
            table.append("|--|----------|--------------------|---------------|--------|\r\n");
            table.append(prettyPrint(r));
        }
        table.append("|--|----------|--------------------|---------------|--------|");
        return table.toString();
    }

    /**
     * This method adds some spaces to right
     *
     * @param s current string
     * @param n number of symbols in final string
     * @return current string padded with spaces up to length=n
     */
    public String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }


}
