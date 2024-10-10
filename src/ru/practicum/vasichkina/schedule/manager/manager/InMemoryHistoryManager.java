package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private Map<Integer, Node> taskHistory = new HashMap<>();

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        Node newNode = linkLast(task);
        remove(task.getId());
        taskHistory.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getTasksHistory() {
        List<Task> history = new ArrayList<>();
        Node node = head;
        while (node != null) {
            history.add(node.task);
            node = node.next;
        }
        return history;
    }

    @Override
    public void remove(int id) {
        Node node = taskHistory.remove(id);
        if (node == null) {
            return;
        } else {
            removeNode(node);
        }
    }

    public Node linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        return newNode;
    }

    private void removeNode(Node node) {
        if (node.prev == null && node.next == null) {
            head = null;
            tail = null;
        } else if (node.prev == null) {
            node.next.prev = null;
            head = node.next;
        } else if (node.next == null) {
            node.prev.next = null;
            tail = node.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } 
    }

    public static class Node {
        public Node prev;
        public Task task;
        public Node next;


        public Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}
