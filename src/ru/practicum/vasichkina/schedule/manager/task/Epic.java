package ru.practicum.vasichkina.schedule.manager.task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> addToEpic = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(Integer id, String name, String description) {
        super(id, name, description);
    }

    public ArrayList<Integer> getAddToEpic() {
        return addToEpic;
    }

    public void setAddToEpic(ArrayList<Integer> addToEpic) {
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
