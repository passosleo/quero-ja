package com.leonardo.queroja.enums;

public enum Status {
    NOT_OWNED(0, "Meus desejos"),
    OWNED(1, "Já tenho");
    private final Integer code;
    private final String description;

    Status(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Status fromCode(Integer code) {
        for (Status status : Status.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return description;
    }
}
