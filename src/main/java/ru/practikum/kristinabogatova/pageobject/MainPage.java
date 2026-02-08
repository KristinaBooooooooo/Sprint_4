package ru.practikum.kristinabogatova.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Кнопки заказа
    private By topOrderButton = By.xpath("//div[contains(@class,'Header_Nav')]//button[text()='Заказать']");
    private By bottomOrderButton = By.xpath("//div[contains(@class,'Home_FinishButton')]//button[text()='Заказать']");
    private By cookieButton = By.id("rcc-confirm-button");

    // FAQ элементы
    private By faqQuestions = By.cssSelector("div.accordion__button");
    private By faqAnswers = By.cssSelector("div.accordion__panel p");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickTopOrderButton() {
        acceptCookies();
        driver.findElement(topOrderButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'Order_Header')]")
        ));
    }

    public void clickBottomOrderButton() {
        acceptCookies();
        WebElement button = driver.findElement(bottomOrderButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        wait.until(ExpectedConditions.elementToBeClickable(button));
        button.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'Order_Header')]")
        ));
    }

    private void acceptCookies() {

        List<WebElement> cookieButtons = driver.findElements(cookieButton);
        if (!cookieButtons.isEmpty() && cookieButtons.get(0).isDisplayed()) {
            cookieButtons.get(0).click();
        }
    }

    // Методы для FAQ теста
    public void openFaqItem(int index) {
        List<WebElement> questions = driver.findElements(faqQuestions);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", questions.get(index));
        questions.get(index).click();
    }

    public String getFaqAnswer(int index) {
        List<WebElement> answers = driver.findElements(faqAnswers);
        return answers.get(index).getText();
    }
}
