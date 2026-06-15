package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class SetupSteps {

    private final steps.StepData stepData;
    private static final Logger log = LoggerFactory.getLogger(SetupSteps.class);

    private static final String BASE_URL = "https://automationintesting.online";

    public SetupSteps(steps.StepData stepData) {
        this.stepData = stepData;
    }

    @Before
    public void setUp(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@api")) return;
        String env = System.getProperty("env", "local");
        String browser = System.getProperty("browser", "chrome");
        boolean headless = env.equalsIgnoreCase("ci");

        log.info("Starting scenario: {} | Browser: {} | Headless: {}", scenario.getName(), browser, headless);

        switch (browser.toLowerCase()) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOptions = new FirefoxOptions();
                if (headless) ffOptions.addArguments("--headless");
                stepData.webDriver = new FirefoxDriver(ffOptions);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) edgeOptions.addArguments("--headless");
                stepData.webDriver = new EdgeDriver(edgeOptions);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                }
                stepData.webDriver = new ChromeDriver(chromeOptions);
            }
        }

        stepData.webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        stepData.webDriver.manage().window().maximize();
        stepData.url = BASE_URL;
    }

    @After
    public void teardown(Scenario scenario) {
        if (scenario.getSourceTagNames().contains("@api")) return;
        if (scenario.isFailed() && stepData.webDriver != null) {
            log.warn("Scenario failed: {} — capturing screenshot", scenario.getName());
            final byte[] screenshot = ((TakesScreenshot) stepData.webDriver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }

        if (stepData.webDriver != null) {
            stepData.webDriver.quit();
            log.info("Driver quit after scenario: {}", scenario.getName());
        }
    }
}
