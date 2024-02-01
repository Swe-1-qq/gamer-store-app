package com.swe1qq.gamestore.persistence.entity;

public enum ErrorTemplates {
    REQUIRED("Поле %s є обов'язковим до заповнення."),
    MIN_LENGTH("Поле %s не може бути меншим за %d симв."),
    MAX_LENGTH("Поле %s не може бути більшим за %d симв."),
    ONLY_LATIN("Поле %s лише латинські символи та символ _."),
    WRONG_TYPE("Поле %s не є існуючим типом."),
    WRONG_ID_FORMAT("Поле %s не підпорядковуєтсья формату UUID."),
    PASSWORD(
            "Поле %s латинські миволи, хочаб одна буква з великої, одна з малої та хочаб одна цифра.");

    private String template;

    ErrorTemplates(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }
}
