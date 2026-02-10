package ru.practikum.kristinabogatova.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {

    private static final String BLACK = "black";

    // Первый шаг
    private final By nameField = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroField = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");
    private final By selectSearchDiv = By.xpath("//div[@class='select-search__select']");
    private final By selectSearchButton = By.xpath("//button[contains(@class,'select-search__option')]");

    // Второй шаг
    private final By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentPeriod = By.className("Dropdown-control");
    private final By blackCheckbox = By.id(BLACK);
    private final By greyCheckbox = By.id("grey");
    private final By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//div[contains(@class,'Order_Buttons')]//button[text()='Заказать']");

    // Модальные окна
    private final By orderModal = By.className("Order_Modal__YZ-d3");
    private final By confirmButton = By.xpath("//div[@class='Order_Modal__YZ-d3']//button[text()='Да']");
    private final By successModal = By.xpath("//div[contains(@class,'Order_ModalHeader')]");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public OrderPage(WebDriver webDriver) {
        this.driver = webDriver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Первый шаг
    public void fillFirstStep(String name, String surname, String address, String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField));

        driver.findElement(nameField).sendKeys(name);
        driver.findElement(surnameField).sendKeys(surname);
        driver.findElement(addressField).sendKeys(address);

        // Выбор станции метро
        driver.findElement(metroField).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(selectSearchDiv));
        driver.findElement(selectSearchButton).click();

        driver.findElement(phoneField).sendKeys(phone);

        WebElement nextBtn = driver.findElement(nextButton);
        scrollToElement(nextBtn);
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        nextBtn.click();
    }

    // Метод для скролла
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    public boolean isSecondStepOpened() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement element = shortWait.until(ExpectedConditions.visibilityOfElementLocated(dateField));
        return element.isDisplayed();
    }

    // Второй шаг
    public void fillSecondStep(String date, String rentPeriodText, String color, String comment) {
        WebElement dateInput = driver.findElement(dateField);
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.ENTER);

        driver.findElement(rentPeriod).click();
        WebElement rentOption = driver.findElement(
                By.xpath("//div[contains(@class,'Dropdown-option') and text()='" + rentPeriodText + "']"));
        rentOption.click();

        if (BLACK.equals(color)) {
            driver.findElement(blackCheckbox).click();
        } else {
            driver.findElement(greyCheckbox).click();
        }

        if (comment != null && !comment.isEmpty()) {
            driver.findElement(commentField).sendKeys(comment);
        }

        WebElement orderBtn = driver.findElement(orderButton);
        scrollToElement(orderBtn);
        wait.until(ExpectedConditions.elementToBeClickable(orderBtn));
        orderBtn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(orderModal));
        WebElement yesButton = driver.findElement(confirmButton);
        wait.until(ExpectedConditions.elementToBeClickable(yesButton));
        yesButton.click();
    }

    public boolean isOrderSuccessVisible() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement successElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(successModal));
        String modalText = successElement.getText();
        return modalText.contains("Заказ оформлен") || modalText.contains("Номер заказа");
    }

    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }
}