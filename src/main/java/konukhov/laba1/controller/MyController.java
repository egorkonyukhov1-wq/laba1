package konukhov.laba1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    // Первый эндпоинт - возвращает текст
    @GetMapping("/text")
    public String getText() {
        return "Привет! Это первый эндпоинт, который возвращает текст.";
    }

    // Второй эндпоинт - возвращает числа
    @GetMapping("/numbers")
    public int[] getNumbers() {
        return new int[]{10, 20, 30, 40, 50};
    }
}