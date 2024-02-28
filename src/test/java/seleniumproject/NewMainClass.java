import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NewMainClass {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
		Thread.sleep(5000);

		driver.findElement(By.xpath("/html/body/div/div[3]/details/summary")).click();
		Thread.sleep(2000);

		driver.findElement(By.id("jsondata")).clear();
		driver.findElement(By.id("jsondata")).sendKeys("[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, {\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, {\"name\": \"Sara\", \"age\" : 42, \"gender\": \"female\"}, {\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, {\"name\": \"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]");
		Thread.sleep(2000);

		driver.findElement(By.id("refreshtable")).click();

		List<Map<String, String>> expectedData = Arrays.asList(
				Map.of("name", "Bob", "age", "20", "gender", "male"), // Changed "Bob" to "ob"
				Map.of("name", "George", "age", "42", "gender", "male"),
				Map.of("name", "Sara", "age", "42", "gender", "female"),
				Map.of("name", "Conor", "age", "40", "gender", "male"),
				Map.of("name", "Jennifer", "age", "42", "gender", "female")
		);

		List<WebElement> rows = driver.findElements(By.xpath("//table/tbody/tr"));

		boolean testFailed = false;

		for (int i = 0; i < rows.size(); i++) {
			WebElement row = rows.get(i);
			List<WebElement> cells = row.findElements(By.tagName("td"));

			for (int j = 0; j < cells.size(); j++) {
				String actualValue = cells.get(j).getText();
				String expectedValue = expectedData.get(i).get(cells.get(j).getAttribute("data-th"));

				if (!actualValue.equals(expectedValue)) {
					System.out.println("Assertion failed for row " + (i + 1) + ", column " + (j + 1));
					testFailed = true;
				}
			}
		}

		if (!testFailed) {
			System.out.println("All assertions passed successfully!");
		} else {
			System.out.println("Some assertions failed. Please check the table data.");
		}

		Thread.sleep(2000);

		driver.quit();

	}
}
