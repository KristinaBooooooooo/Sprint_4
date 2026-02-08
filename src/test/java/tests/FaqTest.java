package tests;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.practikum.kristinabogatova.pageobject.MainPage;

import java.time.Duration;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class FaqTest {

    private static final String CHROME = "chrome";
    private static final String FIREFOX = "firefox";

    private WebDriver driver;
    private MainPage mainPage;

    @Parameterized.Parameter
    public String browser;

    @Parameterized.Parameters(name = "Browser={0}")
    public static Collection<Object[]> data() {
        return List.of(
                new Object[] { CHROME },
                new Object[] { FIREFOX }
        );
    }

    @Before
    public void setUp() {
        driver = getDriver(browser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
    }

    @Test
    public void faqAnswersShouldBeVisible() {
        for (int i = 0; i < 8; i++) {
            // Создаем final переменную для использования в лямбде
            final int questionIndex = i;

            // Открываем вопрос
            mainPage.openFaqItem(questionIndex);

            // Ждем ответ
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(driver -> {
                String answer = mainPage.getFaqAnswer(questionIndex);
                return answer != null && !answer.trim().isEmpty();
            });

            // Проверяем
            String answer = mainPage.getFaqAnswer(questionIndex);
            Assert.assertFalse("Ответ на вопрос " + (questionIndex + 1) + " не отображается", answer.isEmpty());
        }
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
