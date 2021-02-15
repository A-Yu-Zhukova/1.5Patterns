package ru.netology.patterns.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.patterns.data.RegistrationByCardInfo;
import ru.netology.patterns.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class FormTestV1 {
    static RegistrationByCardInfo info;

    @BeforeAll
    static void setUpReplan() {
        info = DataGenerator.Registration.generateByCard("ru");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        info = DataGenerator.Registration.generateByCard("ru");
    }

    @Test
    void testCorrect() {
        String meetDate = DataGenerator.Registration.getMeetDate(3);
        String expected = "Встреча успешно запланирована на " + meetDate;
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(info.getCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue(info.getName());
        form.$("[data-test-id=phone] input").setValue(info.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();

        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText(expected));
    }

    @Test
    void testReplan() {
        String meetDate = DataGenerator.Registration.getMeetDate(3);
        String expected = "Встреча успешно запланирована на " + meetDate;
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(info.getCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue(info.getName());
        form.$("[data-test-id=phone] input").setValue(info.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();

        $("[data-test-id=replan-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=replan-notification] button").click();

        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__content").shouldHave(exactText(expected));
    }

    @Test
    void testBadCity() {
        String expected = "Доставка в выбранный город недоступна";
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(DataGenerator.Registration.getBadCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(DataGenerator.Registration.getMeetDate(3));
        form.$("[data-test-id=name] input").setValue(info.getName());
        form.$("[data-test-id=phone] input").setValue(info.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();
        $("[data-test-id=city]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=city].input_invalid .input__inner .input__sub").shouldHave(exactText(expected));
    }

    @Test
    void testBadDate() {
        String meetDate = DataGenerator.Registration.getMeetDate(1);
        String expected = "Заказ на выбранную дату невозможен";
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(info.getCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] .input__control").setValue(meetDate);
        form.$("[data-test-id=name] input").setValue(info.getName());
        form.$("[data-test-id=phone] input").setValue(info.getPhone());
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();
        $("[data-test-id=date] .input").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=date] .input_invalid .input__inner .input__sub").shouldHave(exactText(expected));
    }

    @Test
    void testBadName() {
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(info.getCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(DataGenerator.Registration.getMeetDate(3));
        form.$("[data-test-id=phone] input").setValue(info.getPhone());
        form.$("[data-test-id=name] input").setValue(DataGenerator.Registration.getBadName());
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();
        $("[data-test-id=name]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=name].input_invalid .input__inner .input__sub").shouldHave(exactText(expected));
    }

    @Test
    void testBadAgreement() {
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(info.getCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(DataGenerator.Registration.getMeetDate(3));
        form.$("[data-test-id=name] input").setValue(info.getName());
        form.$("[data-test-id=phone] input").setValue(info.getPhone());
        form.$("button.button_view_extra").click();
        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    @Test
    void testBadPhone() {
        String expected = "Поле обязательно для заполнения";
        SelenideElement form = $("form");
        form.$("[data-test-id=city] input").setValue(info.getCity());
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.CONTROL + "A");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=date] input").setValue(DataGenerator.Registration.getMeetDate(3));
        form.$("[data-test-id=name] input").setValue(info.getName());
        form.$("[data-test-id=phone] input").setValue(DataGenerator.Registration.getBadPhone());
        form.$("[data-test-id=agreement]").click();
        form.$("button.button_view_extra").click();
        $("[data-test-id=phone]").shouldHave(cssClass("input_invalid"));
        $("[data-test-id=phone].input_invalid .input__inner .input__sub").shouldHave(exactText(expected));
    }
}
