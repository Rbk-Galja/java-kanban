package ru.practicum.vasichkina.schedule.manager.HTTP.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.practicum.vasichkina.schedule.manager.task.TasksStatus;

import java.io.IOException;

public class TaskStatusAdapter extends TypeAdapter<TasksStatus> {

    @Override
    public void write(JsonWriter jsonWriter, TasksStatus status) throws IOException {
        if (status == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(status.toString());
        }
    }

    @Override
    public TasksStatus read(JsonReader jsonReader) throws IOException {
        String str = jsonReader.nextString();
        if (str.equals("null")) {
            return null;
        }
        return TasksStatus.valueOf(str);
    }
}
