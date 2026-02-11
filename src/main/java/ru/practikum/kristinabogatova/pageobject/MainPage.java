package ru.practikum.kristinabogatova.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {

    // Кнопки и элементы
    private final By faqItems = By.xpath("//div[@class='accordion__item']");
    private final By faqQuestion = By.xpath(".//div[@class='accordion__button']");
    private final By faqAnswer = By.xpath(".//div[@class='accordion__panel']/p");

    // Открываем главную
    private final By topOrderButton = By.xpath("//div[contains(@class,'Header_Nav')]//button[text()='Заказать']");
    private final By bottomOrderButton = By.xpath("//div[contains(@class,'Home_FinishButton')]//button[text()='Заказать']");
    private final By orderHeader = By.xpath("//div[contains(@class,'Order_Header')]");
    // Кнопки и элементы
    private final By cookieButton = By.id("rcc-confirm-button");

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Конструктор
    public MainPage(WebDriver webDriver) {
        this.driver = webDriver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    // Кнопки заказа
    public OrderPage clickTopOrderButton() {
        acceptCookies();
        WebElement button = driver.findElement(topOrderButton);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderHeader));
        return new OrderPage(driver);
    }

    public OrderPage clickBottomOrderButton() {
        acceptCookies();
        WebElement button = driver.findElement(bottomOrderButton);
        scrollToElement(button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderHeader));
        return new OrderPage(driver);
    }

    // FAQ
    public void openFaqItem(int index) {
        acceptCookies();
        List<WebElement> items = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(faqItems));

        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Неверный индекс вопроса: " + index);
        }

        WebElement item = items.get(index);
        WebElement question = item.findElement(faqQuestion);

        scrollToElement(item);
        wait.until(ExpectedConditions.elementToBeClickable(question));
        question.click();

        wait.until(ExpectedConditions.visibilityOf(item.findElement(faqAnswer)));
    }

    public String getFaqAnswer(int index) {
        List<WebElement> items = driver.findElements(faqItems);
        return items.get(index).findElement(faqAnswer).getText().trim();
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}