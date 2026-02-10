package ru.practikum.kristinabogatova;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practikum.kristinabogatova.pageobject.MainPage;
import ru.practikum.kristinabogatova.pageobject.OrderPage;
import ru.practikum.kristinabogatova.utils.WebDriverUtils;

import java.util.Collection;
import java.util.List;

import static ru.practikum.kristinabogatova.utils.GlobalConst.CHROME;
import static ru.practikum.kristinabogatova.utils.GlobalConst.FIREFOX;

@RunWith(Parameterized.class)
public class OrderTest {

    private static final String TOP = "top";
    private static final String BOTTOM = "bottom";

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
                        "+79990001122",
                        "10.02.2026",
                        "сутки",
                        TOP,
                        "black",
                        "Позвоните за 5 минут"
                },
                new Object[] {
                        FIREFOX,
                        "Иван",
                        "Петров",
                        "Санкт-Петербург, Невский проспект, д. 20",
                        "+79990003344",
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
        // передаём браузер строкой
        mainPage = new MainPage(WebDriverUtils.create(browser));
        mainPage.openHomePage();
        mainPage.acceptCookies();
    }

    @Test
    public void orderShouldBeCreatedSuccessfully() {
        //  Шаг 1: выбираем кнопку заказа
        if (TOP.equals(buttonType)) {
            orderPage = mainPage.clickTopOrderButton();
        } else {
            orderPage = mainPage.clickBottomOrderButton();
        }

        // Шаг 2: Заполняем первый шаг заказа
        orderPage.fillFirstStep(name, surname, address, phone);
        Assert.assertTrue("Второй шаг заказа не открылся", orderPage.isSecondStepOpened());

        // Шаг 3: Заполняем второй шаг
        orderPage.fillSecondStep(date, rent, color, comment);

        // Проверка успешного оформления
        Assert.assertTrue("Окно успешного создания заказа не появилось", orderPage.isOrderSuccessVisible());
    }

    @After
    public void tearDown() {
        orderPage.close();
        mainPage.close();
    }
}