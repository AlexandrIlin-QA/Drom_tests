package ru.alex;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class DromClass {
    public static WebDriver driver;

    /**
     * осуществление первоначальной настройки
     */
    @BeforeClass
    public static void setup() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, "eager");
        //определение пути до драйвера и его настройка
        System.setProperty("webdriver.chrome.driver", ConfProperties.getProperty("chromedriver"));
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        //создание экземпляра драйвера
        driver = new ChromeDriver(options);
        //окно разворачивается на полный экран
        driver.manage().window().maximize();
        //задержка на выполнение теста = 10 сек.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void test_search() throws InterruptedException {
        driver.get("https://auto.drom.ru/");

        // марка
        WebElement brandSelect = driver.findElement(By.cssSelector("div.css-10ib5jr > form > div > div:nth-child(1) > div:nth-child(1) input"));
        brandSelect.click();
        brandSelect.sendKeys("Toyota");
        Thread.sleep(2000);
        brandSelect.sendKeys(Keys.ENTER);

        Thread.sleep(5000);

        // модель
        WebElement modelSelect = driver.findElement(By.cssSelector("div.css-10ib5jr > form > div > div:nth-child(1) > div:nth-child(2) input"));
        modelSelect.click();
        modelSelect.sendKeys("Harrier");
        Thread.sleep(2000);
        modelSelect.sendKeys(Keys.ENTER);

        Thread.sleep(5000);

        // тип топлива
        WebElement fuelTypeSelect = driver.findElement(By.cssSelector("form > div > div:nth-child(2) > div.css-1k744as.e1lm3vns0 > div.css-12bmu6c.e1m504pf0"));
        fuelTypeSelect.click();
        List<WebElement> fuelTypes = fuelTypeSelect.findElements(By.cssSelector(".css-17vx1of"));
        for(int i = 0; i < fuelTypes.size(); i++){
            if(fuelTypes.get(i).getText().contains("Гибрид")){
                fuelTypes.get(i).click();
            }
        }

        // непроданные
        driver.findElement(By.cssSelector("body > div:nth-child(3) > div.css-1iexluz.e1m0rp603 > div.css-1f36sr9.e1m0rp604 > div.css-0.e1m0rp605 > div.css-10ib5jr > form > div > div:nth-child(3) > div.css-48ojaj.e1lm3vns0 > div.css-dixqn0.e1lp1m2z0 > div > label")).click();

        // год от
        WebElement yearSelect = driver.findElement(By.cssSelector("body > div:nth-child(3) > div.css-1iexluz.e1m0rp603 > div.css-1f36sr9.e1m0rp604 > div.css-0.e1m0rp605 > div.css-10ib5jr > form > div > div:nth-child(2) > div:nth-child(2) > div > div:nth-child(1)"));
        yearSelect.click();
        List<WebElement> years = yearSelect.findElements(By.cssSelector(".css-17vx1of"));
        for(int i = 0; i < years.size(); i++){
            if(years.get(i).getText().contains("2007")){
                years.get(i).click();
            }
        }

        // клик на расширенный поиск
        WebElement extendedButton = driver.findElement(By.cssSelector("body > div:nth-child(3) > div.css-1iexluz.e1m0rp603 > div.css-1f36sr9.e1m0rp604 > div.css-0.e1m0rp605 > div.css-10ib5jr > form > div > div.css-1xga70a.e1lm3vns0 > div:nth-child(2) > button"));
        extendedButton.click();

        // пробег
        WebElement mileageInput = driver.findElement(By.cssSelector("body > div:nth-child(3) > div.css-1iexluz.e1m0rp603 > div.css-1f36sr9.e1m0rp604 > div.css-0.e1m0rp605 > div.css-10ib5jr > form > div > div:nth-child(4) > div.css-2tak37.e1lm3vns0 > div.css-14hyps3.evnwjo70 > div.css-1r2f04i > div > div > div:nth-child(1) > div > div > div:nth-child(1) > div > div > input"));
        mileageInput.sendKeys("1");

        // отправка формы
        WebElement submitButton = driver.findElement( By.cssSelector("body > div:nth-child(3) > div.css-1iexluz.e1m0rp603 > div.css-1f36sr9.e1m0rp604 > div.css-0.e1m0rp605 > div.css-10ib5jr > form > div > div.css-1xga70a.e1lm3vns0 > div.css-14hyps3.evnwjo70 > button"));
        submitButton.click();

        Thread.sleep(5000);

        List<WebElement> paginationLinks = driver.findElements(By.cssSelector("body > div:nth-child(3) > div.css-1iexluz.e1m0rp603 > div.css-1f36sr9.e1m0rp604 > div.css-0.e1m0rp605 > div.css-1173kvb.eojktn00 > div > div:nth-child(2) > div > div > a"));
        while (paginationLinks.size() != 0){
            List<WebElement> soldCars = driver.findElements(By.cssSelector(".css-1e5mzqy"));
            Assert.assertEquals(soldCars.size(), 0);

            List<WebElement> allCars = driver.findElements(By.cssSelector(".css-l1wt7n > span"));
            for(int i = 0; i < allCars.size(); i++){
                Assert.assertTrue(Integer.parseInt(allCars.get(i).getText().split(", ")[1]) >= 2007);
            }

            List<WebElement> mileageIdenntifiers = driver.findElements(By.cssSelector(".css-1fe6w6s > span:nth-child(4)"));
            Assert.assertEquals(mileageIdenntifiers.size(), allCars.size());

            paginationLinks.get(0).click();
            paginationLinks = driver.findElements(By.cssSelector("body > div:nth-child(3) > div.css-1iexluz.e1m0rp603 > div.css-1f36sr9.e1m0rp604 > div.css-0.e1m0rp605 > div.css-1173kvb.eojktn00 > div > div:nth-child(2) > div > div > a"));
        }
    }

    @Test
    public void test_auth() throws InterruptedException{
        driver.get("https://auto.drom.ru/");

        WebElement authButton = driver.findElement(By.cssSelector("body > div:nth-child(3) > div.css-vwoqhw.el6vb8e0 > div.css-1ioe6bl.exbslet0 > div > div.css-16566ot.e173iafn0 > a"));
        authButton.click();

        WebElement login = driver.findElement(By.cssSelector("#sign"));
        login.sendKeys(ConfProperties.getProperty("login"));

        Thread.sleep(500);

        WebElement password = driver.findElement(By.cssSelector("#password"));
        password.sendKeys(ConfProperties.getProperty("password"));

        Thread.sleep(500);

        WebElement sendButton = driver.findElement(By.cssSelector("#signbutton"));
        sendButton.click();

        Thread.sleep(5000);

        driver.findElement(By.cssSelector("div.css-cvu2h3.e13r0v7w0")).click();
    }

    @Test
    public void test_calc() throws  InterruptedException {
        driver.get("https://auto.drom.ru/region25/");

        WebElement brandSelect = driver.findElement(By.cssSelector("div.css-10ib5jr > form > div > div:nth-child(1) > div:nth-child(1) input"));
        brandSelect.click();
        brandSelect.sendKeys(Keys.ARROW_DOWN);
        brandSelect.sendKeys(Keys.ARROW_DOWN);
        ArrayList<String> result = new ArrayList<>();

        List<WebElement> current = driver.findElements(By.cssSelector(".css-1r0zrug > div"));

       for(int i = 0; i < current.size(); i++){
          if(!Objects.equals(current.get(i).getText(), "") && !result.contains(current.get(i).getText())){
            result.add(current.get(i).getText());
         }
       }

        while(!current.get(16).getText().contains("УАЗ")) {
            for(int i = 0; i < 15; i++){
                brandSelect.sendKeys(Keys.ARROW_DOWN);
            }

            Thread.sleep(1000);
            current = driver.findElements(By.cssSelector(".css-1r0zrug > div"));
            for(int i = 0; i < current.size(); i++){
                if(!Objects.equals(current.get(i).getText(), "") && !result.contains(current.get(i).getText())){
                    result.add(current.get(i).getText());
                }
            }
        }

        String[] newArr = result.toArray(new String[0]);

        Arrays.sort(newArr, new Comparator<String>() {
            @Override
            public int compare(String string1, String string2) {
                String substring1 = string1.replaceAll("[\\sA-Za-zА-Яа-я&\\-\\(\\)]+", "");
                String substring2 = string2.replaceAll("[\\sA-Za-zА-Яа-я&\\-\\(\\)]+", "");

                if(substring1.equals(substring2)){
                    return 0;
                }

                if(substring1.equals("")){
                    return 1;
                }

                if(substring2.equals("")){
                    return -1;
                }

                if(Integer.parseInt(substring1) > Integer.parseInt(substring2)){
                    return -1;
                }

                return 1;
            }
        });

        System.out.format("%30s%30s\n", "Фирма", "Количество объявлений");

        for(int i = 0; i < 20; i++){
            System.out.format("%30s%30s\n", newArr[i].replaceAll("[\\d\\(\\)]+", ""), newArr[i].replaceAll("[\\sA-Za-zА-Яа-я&\\-\\(\\)]+", ""));
        }
    }
}
