package ru.practicum.task_trecker.task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<SubTask> addToEpic = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
    }

    public ArrayList<SubTask> getAddToEpic() {
        return addToEpic;
    }

    public void setAddToEpic(ArrayList<SubTask> addToEpic) {
        this.addToEpic = addToEpic;
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
