package com.netcracker.asserts;

public interface RegexTemplate {

    String PET_NAME = "[a-zA-Z]|[а-яА-Я]";
    String URL = "^(http|https):\\/\\/[-a-zA-Z0-9+&@#\\/%?=~_|,!:.;]*[-a-zA-Z0-9+@#\\/%=&_|]";
    String URL_IMAGE = "^(http|https):\\/\\/[-a-zA-Z0-9+&@#\\/%?=~_|,!:.;]*[-a-zA-Z0-9+@#\\/%=&_|].((jpg)|(png))";
}
