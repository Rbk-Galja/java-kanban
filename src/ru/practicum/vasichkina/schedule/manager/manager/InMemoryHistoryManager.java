package ru.practicum.vasichkina.schedule.manager.manager;

import ru.practicum.vasichkina.schedule.manager.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private Map<Integer, Node> taskHistory = new HashMap<>();
    private List<Node> nodeList = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public List<Task> getTasksHistory() {
        List<Task> history = new ArrayList<>();
        for (Node task : nodeList) {
            history.add(task.getTask());
        }
        return history;
    }


    @Override
    public void remove(int id) {
        Node node = taskHistory.remove(id);
        nodeList.remove(node);
        if (node == null) {
            return;
        } else {
            removeNode(node);
        }
    }

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, null, task);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.prev = newNode;
        }
        taskHistory.put(task.getId(), newNode);
        nodeList.add(newNode);
    }

    private void removeNode(Node node) {
        if (node.prev == null && node.next == null) {
            head = null;
            tail = null;
        } else if (node.prev == null) {
            node.next.prev = null;
        } else if (node.next == null) {
            node.prev.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

    }

    public static class Node {
        private Node prev;
        private Node next;
        private Task task;

        public Node(Node prev, Node next, Task task) {
            this.prev = prev;
            this.next = next;
            this.task = task;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }
    }
}
