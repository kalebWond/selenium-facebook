import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class App {
    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.chrome.driver", "/home/kaleb/Documents/java-projects/chromedriver");
        Map<String, Object> prefs = new HashMap<String, Object>();
        // response to notification popup        
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        WebDriver driver = new ChromeDriver(options);

        // make brower window maximized
        driver.manage().window().maximize();
        driver.navigate().to("https://facebook.com");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }

        // login to facebook by filling the form
        WebElement emailBox = driver.findElement(By.id("email"));
        WebElement passwordBox = driver.findElement(By.id("pass"));
        if(!emailBox.isDisplayed() || !passwordBox.isDisplayed()) {
           driver.quit();
           return;
        }
        emailBox.sendKeys("kalebw89@gmail.com");
        passwordBox.sendKeys("Pa$$w0rd!");
        WebElement loginForm = driver.findElement(By.tagName("form"));
        loginForm.submit();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        
        // use facebook's link to navigate to friends list
        driver.navigate().to("https://facebook.com/me/friends");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }

        /*
            step 1: get a list of active friends and store them in a list
        */
        List<WebElement> frineds = driver.findElements(By.xpath("//*[@class='oajrlxb2 g5ia77u1 qu0x051f esr5mh6w e9989ue4 r7d6kgcz rq0escxv nhd2j8a9 nc684nl6 p7hjln8o kvgmc6g5 cxmmr5t8 oygrvhab hcukyx3x jb3vyjys rz4wbd8a qt6c0cv9 a8nywdso i1ao9s8h esuyzwwr f1sip0of lzcic4wl gmql0nx0 gpro0wi8']"));
        if(frineds.size() == 0) {
            System.out.println("No friends ");
            driver.quit();
            return;
        }

        /* 
            step 2: store the links to the friends profiles'
        */
        List<String> links = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            String link = frineds.get(i).getAttribute("href");
            links.add(link);
        }

        /*
            step 3: iterate through the links list
            step 4: append "/photos_all" and navigate to photos page
        */
        for (int i = 0; i < links.size(); i++) {
            driver.navigate().to(links.get(i)+"/photos_all");
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }
            List<WebElement> photoLinks = driver.findElements(By.xpath("//*[@class='oajrlxb2 g5ia77u1 qu0x051f esr5mh6w e9989ue4 r7d6kgcz rq0escxv nhd2j8a9 a8c37x1j p7hjln8o kvgmc6g5 cxmmr5t8 oygrvhab hcukyx3x jb3vyjys rz4wbd8a qt6c0cv9 a8nywdso i1ao9s8h esuyzwwr f1sip0of lzcic4wl gmql0nx0 gpro0wi8 datstx6m l9j0dhe7 k4urcfbm']"));
            photoLinks.get(0).click();
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
            }

            /**
             * step 5: get image url and stream it through JAVA ImageIo
             * stop 6: save to images folder
             */
            WebElement element = driver.findElement(By.xpath("//*[@class='ji94ytn4 r9f5tntg d2edcug0 r0294ipz']"));
            String url = element.getAttribute("src");
            URL imageUrl = new URL(url);
            BufferedImage saveImage = ImageIO.read(imageUrl);
            ImageIO.write(saveImage, "jpg", new File("/home/kaleb/Pictures/"+LocalDateTime.now() + ".jpg"));

        }
        driver.quit();
    }
}
