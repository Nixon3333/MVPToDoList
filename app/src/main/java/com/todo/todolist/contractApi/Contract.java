package com.todo.todolist.contractApi;

import android.content.Context;

import com.todo.todolist.model.Task;

import java.util.Calendar;
import java.util.List;

public interface Contract {

    interface View {
        default void showTasks(List<Task> list) {
        }
    }

    interface Presenter {
        void getTasks();

        void saveTask(Task task);

        void editTask(Task task, int position, List<Task> list);

        void deleteTask(int position, List<Task> list);

        String[] getEditTask(int position, List<Task> list);

        void switchDone(int position, List<Task> list);

        default String getCurrentDate() {
            final Calendar cal = Calendar.getInstance();
            int mYear = cal.get(Calendar.YEAR);
            int mMonth = cal.get(Calendar.MONTH) + 1;
            int mDay = cal.get(Calendar.DAY_OF_MONTH);

            return mDay + "." + mMonth + "." + mYear;
        }

        void switchSelectItem(int position, List<Task> list);
    }

    interface Model {

        List<Task> loadTasks(Context context);

        void save(Task task, Context context);

        void edit(Task task, Context context, int position, List<Task> list);

        void delete(Context context, int position, List<Task> list);

        void switchDone(Context context, int position, List<Task> list);

        String[] getEdit(Context context, int position, List<Task> list);

        void switchSelect(Context context, int position, List<Task> list);

        //List<String> getDates(Context context);
    }
}
