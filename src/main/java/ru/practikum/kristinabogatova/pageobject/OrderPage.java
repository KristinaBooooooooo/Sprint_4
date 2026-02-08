package ru.practikum.kristinabogatova.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {

    private static final String BLACK = "black";
    private WebDriver driver;
    private WebDriverWait wait;

    // Первый шаг
    private By nameField = By.xpath("//input[@placeholder='* Имя']");
    private By surnameField = By.xpath("//input[@placeholder='* Фамилия']");
    private By addressField = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private By metroField = By.xpath("//input[@placeholder='* Станция метро']");
    private By phoneField = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private By nextButton = By.xpath("//button[text()='Далее']");

    // Второй шаг
    private By dateField = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private By rentPeriod = By.className("Dropdown-control");
    private By blackCheckbox = By.id(BLACK);
    private By greyCheckbox = By.id("grey");
    private By commentField = By.xpath("//input[@placeholder='Комментарий для курьера']");

    // Кнопка Заказать на втором шаге
    private By orderButton = By.xpath("//div[contains(@class,'Order_Buttons')]//button[text()='Заказать']");

    // Модальные окна
    private By orderModal = By.className("Order_Modal__YZ-d3");
    private By confirmButton = By.xpath("//div[@class='Order_Modal__YZ-d3']//button[text()='Да']");
    private By successModal = By.xpath("//div[contains(@class,'Order_ModalHeader')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
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
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='select-search__select']")
        ));

        // Выбираем первую станцию
        driver.findElement(By.xpath("//button[contains(@class,'select-search__option')]")).click();

        driver.findElement(phoneField).sendKeys(phone);

        // Клик на Далее
        WebElement nextBtn = driver.findElement(nextButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextBtn);
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        nextBtn.click();
    }

    public boolean isSecondStepOpened() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Проверяем, что элемент появился за 5 секунд
        WebElement element = shortWait.until(ExpectedConditions.visibilityOfElementLocated(dateField));
        return element.isDisplayed();
    }

    // Второй шаг
    public void fillSecondStep(String date, String rentPeriodText, String color, String comment) {
        // Заполняем дату
        WebElement dateInput = driver.findElement(dateField);
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.ENTER);

        // Выбираем срок аренды
        driver.findElement(rentPeriod).click();
        WebElement rentOption = driver.findElement(
                By.xpath("//div[contains(@class,'Dropdown-option') and text()='" + rentPeriodText + "']")
        );
        rentOption.click();

        // Выбираем цвет
        if (BLACK.equals(color)) {
            driver.findElement(blackCheckbox).click();
        } else {
            driver.findElement(greyCheckbox).click();
        }

        // Комментарий
        if (comment != null && !comment.isEmpty()) {
            driver.findElement(commentField).sendKeys(comment);
        }

        // Клик на кнопку "Заказать"
        WebElement orderBtn = driver.findElement(orderButton);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", orderBtn);
        wait.until(ExpectedConditions.elementToBeClickable(orderBtn));
        orderBtn.click();

        // Ждем модальное окно подтверждения
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderModal));

        // Кликаем "Да" в модальном окне
        WebElement yesButton = driver.findElement(confirmButton);
        wait.until(ExpectedConditions.elementToBeClickable(yesButton));
        yesButton.click();
    }

    public boolean isOrderSuccessVisible() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        // Ждем появления модального окна 5 секунд
        WebElement successElement = shortWait.until(ExpectedConditions.visibilityOfElementLocated(successModal));

        String modalText = successElement.getText();
        return modalText.contains("Заказ оформлен") || modalText.contains("Номер заказа");
    }
}
