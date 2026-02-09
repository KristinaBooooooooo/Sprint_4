package ru.practikum.kristinabogatova.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class MainPage {

    private static final String CHROME = "chrome";
    private static final String FIREFOX = "firefox";

    // Кнопки и элементы
    private final By cookieButton = By.id("rcc-confirm-button");

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    // Конструктор
    public MainPage(String browser, Duration timeout) {
        this.driver = createDriver(browser);
        this.wait = new WebDriverWait(driver, timeout);
    }

    public void openHomePage() {
        driver.get("https://qa-scooter.praktikum-services.ru/");
    }

    // Куки
    public void acceptCookies() {
        List<WebElement> cookies = driver.findElements(cookieButton);
        if (!cookies.isEmpty() && cookies.get(0).isDisplayed()) {
            cookies.get(0).click();
            wait.until(ExpectedConditions.invisibilityOfElementLocated(cookieButton));
        }
    }

    // Метод для скролла
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriver createDriver(String browser) {
        if (CHROME.equals(browser)) {
            return new ChromeDriver();
        } else if (FIREFOX.equals(browser)) {
            return new FirefoxDriver();
        } else {
            throw new RuntimeException("Не найден подходящий драйвер");
        }
    }
}