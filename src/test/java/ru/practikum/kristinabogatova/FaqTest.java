package ru.practikum.kristinabogatova;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practikum.kristinabogatova.utils.WebDriverUtils;
import ru.practikum.kristinabogatova.pageobject.MainPage;

import java.util.Collection;
import java.util.List;

import static ru.practikum.kristinabogatova.utils.GlobalConst.CHROME;
import static ru.practikum.kristinabogatova.utils.GlobalConst.FIREFOX;

@RunWith(Parameterized.class)
public class FaqTest {

    private MainPage mainPage;

    @Parameterized.Parameter(0)
    public String browser;

    @Parameterized.Parameter(1)
    public int questionIndex;

    private static final String[] EXPECTED_ANSWERS = {
            "Сутки — 400 рублей. Оплата курьеру — наличными или картой.",
            "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.",
            "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.",
            "Только начиная с завтрашнего дня. Но скоро станем расторопнее.",
            "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.",
            "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.",
            "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.",
            "Да, обязательно. Всем самокатов! И Москве, и Московской области."
    };

    @Parameterized.Parameters(name = "Browser: {0}, Question: {1}")
    public static Collection<Object[]> data() {
        return List.of(
                new Object[]{CHROME, 0},
                new Object[]{CHROME, 1},
                new Object[]{CHROME, 2},
                new Object[]{CHROME, 3},
                new Object[]{CHROME, 4},
                new Object[]{CHROME, 5},
                new Object[]{CHROME, 6},
                new Object[]{CHROME, 7},
                new Object[]{FIREFOX, 0},
                new Object[]{FIREFOX, 1},
                new Object[]{FIREFOX, 2},
                new Object[]{FIREFOX, 3},
                new Object[]{FIREFOX, 4},
                new Object[]{FIREFOX, 5},
                new Object[]{FIREFOX, 6},
                new Object[]{FIREFOX, 7}
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
    public void faqAnswerShouldBeCorrect() {
        mainPage.openFaqItem(questionIndex);
        String actualAnswer = mainPage.getFaqAnswer(questionIndex);
        String expectedAnswer = EXPECTED_ANSWERS[questionIndex];

        Assert.assertEquals(
                "Неверный ответ на вопрос №" + (questionIndex + 1),
                expectedAnswer,
                actualAnswer
        );
    }

    @After
    public void tearDown() {
        mainPage.close();
    }
}