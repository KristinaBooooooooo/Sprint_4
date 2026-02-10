package ru.practikum.kristinabogatova.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static ru.practikum.kristinabogatova.utils.GlobalConst.CHROME;
import static ru.practikum.kristinabogatova.utils.GlobalConst.FIREFOX;

public class WebDriverUtils {

    public static WebDriver create(String browser) {
        if (CHROME.equals(browser)) {
            return new ChromeDriver();
        } else if (FIREFOX.equals(browser)) {
            return new FirefoxDriver();
        } else {
            throw new RuntimeException("Не найден подходящий драйвер");
        }
    }
}
