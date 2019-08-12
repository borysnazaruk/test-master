package com.test.engine.dictionary;

public enum TField {

    STATUS_CODE("statusCode"), //
    ERROR_CODE("errorCode"), //
    ERROR_MESSAGE("errorMessage"), //
    ROLE("role"), //
    CREDENTIALS("credetials"), //
    INCORRECT_VALUE("incorrectValue"), //
    VALUE("value"), //
    CONNECTION_TYPE("connectionType"), //
    HOME_DELIVERY("Home Delivery"), //
    PRICE("price"), //
    COMMENT("Comment"), //
    AUTHOR("Author"), //
    TABLE_PATH_NAME("name"), //
    COLUMN_NAME("columnName"), //
    BUTTON_TEXT("buttonText"), //
    EXPECTED_TEXT("expectedText"), //
    FIELD_NAME("fieldName"), //
    FIELD_TYPE("fieldType"), //
    FIELD_VALUE("fieldValue"), //
    PAGE_URL("pageUrl"), //
    COMPONENT("component"), //
    PARENT("parent"), //
    NAME("name"), //
    TITLE("title"), //
    LABEL("label"), //
    PRIORITY("Priority"), //
    USERNAME("username"), //
    PASSWORD("password"), //
    EMAIL("email"), //
    CONFIRM_PASSWORD("confirm password"),
    LOGIN("login"), //
    GROUP("group"), //
    LOCALE("locale"), //
    CURBSIDE("curbside"), //
    RETURNS("Returns"), //
    PATH("Path");


    public final String text;

    /**
     * Provides a string representation of given enumeration.
     * 
     */
    private TField(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
