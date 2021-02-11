package ru.netology.patterns;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class FormTestV1 {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    private String getDate(int daysForward) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysForward);
        Date dt = calendar.getTime();
        return dateFormat.format(dt).toString();
    }

    @Test
    void testCorrect() {
        String meetDate = getDate(3);
        String expected = "Встреча успешно запланирована на " + meetDate;
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Саранск");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue("Василий Петров-Водкин");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();

        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText(expected));
    }

    @Test
    void testReplan() {
        String meetDate = getDate(3);
        String expected = "Встреча успешно запланирована на " + meetDate;
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Саранск");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue("Василий Петров-Водкин");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();

        $("[data-test-id=replan-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] button").click();

        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText(expected));
    }

    @Test
    void testBadCity() {
        String meetDate = getDate(3);
        String expected = "Доставка в выбранный город недоступна";
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Малые упыри");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue("Василий Петров-Водкин");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();
        $("[data-test-id=city]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=city].input_invalid .input__inner .input__sub").shouldHave(exactText(expected));
    }

    @Test
    void testBadDate() {
        String meetDate = getDate(1);
        String expected = "Заказ на выбранную дату невозможен";
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Саранск");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] .input__control").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue("Василий Петров-Водкин");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();
        $("[data-test-id=date] .input").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=date] .input_invalid .input__inner .input__sub").shouldHave(exactText(expected));
    }

    @Test
    void testBadName() {
        String meetDate = getDate(3);
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Саранск");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue("Vasya");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();
        $("[data-test-id=name]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=name].input_invalid .input__inner .input__sub").shouldHave(exactText(expected));
    }

    @Test
    void testBadAgreement() {
        String meetDate = getDate(3);
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Саранск");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue("Василий Петров-Водкин");
        form.$("[data-test-id=phone] input").setValue("+79270000000");
        form.$("button.button_view_extra").click();
        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    @Test
    void testBadPhone() {
        String meetDate = getDate(3);
        String expected = "Поле обязательно для заполнения";
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue("Саранск");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue("Василий Петров-Водкин");
        form.$("[data-test-id=phone] input").setValue("");
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();
        $("[data-test-id=phone]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=phone].input_invalid .input__inner .input__sub").shouldHave(exactText(expected));
    }
}
