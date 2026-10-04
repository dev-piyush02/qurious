package com.qurious.qurious.utils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class QuizRoomCodeGenerator {
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generate() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 9; i++) {
            sb.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
        }
        int hyphenPos = RANDOM.nextInt(8) + 1; // 1 to 8
        sb.insert(hyphenPos, '-');
        return sb.toString();
    }
}
