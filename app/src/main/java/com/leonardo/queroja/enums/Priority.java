package com.leonardo.queroja.enums;

public enum Priority {
    LOW(0, "Baixa"),
    MEDIUM(1, "Média"),
    HIGH(2, "Alta");
    private final Integer code;
    private final String description;

    Priority(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Priority fromCode(Integer code) {
        for (Priority priority : Priority.values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return description;
    }
}
