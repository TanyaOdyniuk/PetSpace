package com.netcracker.ui.validation;

public interface UiValidationConstants {
    String CHECK_LENGTH = "Field must contain at least three characters";
    String CHECK_FULLNESS = "This field must be filled";
    int MIN_CHAR_COUNT = 3;
    int MIN_PASS_CHAR_COUNT = 6;
    int MAX_PASS_CHAR_COUNT = 50;
    String CHECK_PASS_LEN =  "Your password length must be from "
            + MIN_PASS_CHAR_COUNT + " to " + MAX_PASS_CHAR_COUNT
            + " characters";
    String CHECK_EMAIL = "This doesn't look like a valid email address";
    String CHECK_EQ_PASS = "Field must be equals password";
}
