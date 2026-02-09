package ru.practikum.kristinabogatova.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class FaqPage extends MainPage {

    // Кнопки и элементы
    private final By faqItems = By.xpath("//div[@class='accordion__item']");
    private final By faqQuestion = By.xpath(".//div[@class='accordion__button']");
    private final By faqAnswer = By.xpath(".//div[@class='accordion__panel']/p");

    // Конструктор
    public FaqPage(String browser) {
        super(browser, Duration.ofSeconds(10));
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
}