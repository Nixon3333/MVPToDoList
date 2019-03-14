package com.todo.todolist.constants;

import java.util.ArrayList;
import java.util.List;

public class Languages {

    static final String RUSSIAN = "ru";
    static final String ENGLISH = "en";

    public static List<String> getLanguages() {
        List<String> list = new ArrayList<>();
        list.add(RUSSIAN);
        list.add(ENGLISH);
        return list;
    }
}
