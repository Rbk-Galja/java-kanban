package ru.practicum.vasichkina.schedule.manager.HTTP;

import java.util.Arrays;
import java.util.regex.Pattern;

public enum Endpoint {
    GET_TASKS("^/tasks$"),
    GET_TASKS_ID("^/tasks/\\d+$"),

    GET_SUBTASKS("^/subtasks$"),
    GET_SUBTASKS_ID("^/subtasks/\\d+$"),

    GET_EPICS("^/epics$"),
    GET_EPICS_ID("^/epics/\\d+$"),
    GET_EPICS_ID_SUBTASKS("^/epics/\\d+/subtasks$"),

    POST_TASKS("^/tasks$"),
    POST_TASKS_ID("^/tasks/\\d+$"),

    POST_SUBTASKS("^/subtasks$"),
    POST_SUBTASKS_ID("^/subtasks/\\d+$"),

    UNKNOWN(
            "^(?!/(tasks|epics|subtasks|history|prioritized)(?:$|/)).*");

    private final String path;

    Endpoint(String path) {
        this.path = path;
    }

    public static Endpoint getEndpoint(String method, String path) {
        return Arrays.stream(Endpoint.values())
                .filter(e -> e.matches(method, path))
                .findFirst()
                .orElse(UNKNOWN);
    }

    private boolean matches(String method, String path) {
        return this.name().startsWith(method.toUpperCase()) &&
                Pattern.matches(this.path, path);
    }


}

