package tests;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.practikum.kristinabogatova.pageobject.MainPage;
import ru.practikum.kristinabogatova.pageobject.OrderPage;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderTest {

    private static final String CHROME = "chrome";
    private static final String FIREFOX = "firefox";
    private static final String TOP = "top";
    private static final String BOTTOM = "bottom";

    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;

    @Parameterized.Parameter(0) public String browser;
    @Parameterized.Parameter(1) public String name;
    @Parameterized.Parameter(2) public String surname;
    @Parameterized.Parameter(3) public String address;
    @Parameterized.Parameter(4) public String phone;
    @Parameterized.Parameter(5) public String date;
    @Parameterized.Parameter(6) public String rent;
    @Parameterized.Parameter(7) public String buttonType;
    @Parameterized.Parameter(8) public String color;
    @Parameterized.Parameter(9) public String comment;

    @Parameterized.Parameters(name = "{0} | {7} | {8}")
    public static Collection<Object[]> data() {
        return List.<Object[]>of(
                new Object[] {
                        CHROME,
                        "Анна",
                        "Иванова",
                        "Москва, ул. Тверская, д. 10",
                        "+79990001122", "10.02.2026",
                        "сутки",
                        TOP,
                        "black",
                        "Позвоните за 5 минут"
                },
                new Object[] {
                        FIREFOX,
                        "Иван",
                        "Петров",
                        "Санкт-Петербург, Невский проспект, д. 20", "+79990003344",
                        "11.02.2026",
                        "двое суток",
                        BOTTOM,
                        "grey",
                        "Оставьте у двери"
                }
        );
    }

    @Before
    public void setUp() {
        driver = getDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://qa-scooter.praktikum-services.ru/");

        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
    }

    @Test
    public void orderShouldBeCreatedSuccessfully() {
        // Кликаем кнопку заказа
        if (TOP.equals(buttonType)) {
            mainPage.clickTopOrderButton();
        } else {
            mainPage.clickBottomOrderButton();
        }

        // Первый шаг
        orderPage.fillFirstStep(name, surname, address, phone);

        // Проверяем второй шаг
        boolean secondStepOpened = orderPage.isSecondStepOpened();
        Assert.assertTrue("Второй шаг заказа не открылся", secondStepOpened);

        // Второй шаг
        orderPage.fillSecondStep(date, rent, color, comment);

        // Проверяем успешное оформление
        boolean successVisible = orderPage.isOrderSuccessVisible();
        if (CHROME.equals(browser) && !successVisible) {
            System.out.println("ВНИМАНИЕ: БАГ В CHROME! Окно успеха не появилось");
        }
        Assert.assertTrue("Окно успешного создания заказа не появилось", successVisible);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private WebDriver getDriver(String browser) {
        if (CHROME.equals(browser)) {
            return new ChromeDriver();
        } else if (FIREFOX.equals(browser)) {
            return new FirefoxDriver();
        } else {
            throw new RuntimeException("Не найден подходящий драйвер");
        }
    }
}
