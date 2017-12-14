package com.netcracker.service.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomStringGenerator {

    private final String alphaUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String alphaLowerCase = alphaUpperCase.toLowerCase();
    private final String nums = "1234567890";
    private final String alphanum = alphaUpperCase + alphaLowerCase + nums;
    private final Random random = new Random();
    private char buf[];
    private char symbols[] = alphanum.toCharArray();

    public String nextString() {
        for (int i = 0; i < 8; i++) {
            buf[i] = symbols[random.nextInt(symbols.length)];
        }
        return new String(buf);
    }
}
