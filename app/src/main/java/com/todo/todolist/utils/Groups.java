package com.todo.todolist.utils;

import java.util.ArrayList;
import java.util.List;

public class Groups {

    static final String GENERAL = "General";
    static final String PERSON = "Person";
    static final String WORK = "Work";
    static final String OTHER = "Other";

    public static List<String> getGroupList() {
        List<String> groupsList = new ArrayList<>();
        groupsList.add(GENERAL);
        groupsList.add(PERSON);
        groupsList.add(WORK);
        groupsList.add(OTHER);
        return groupsList;
    }
}
