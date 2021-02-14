package ru.netology.patterns.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private DataGenerator() {
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationByCardInfo generateByCard(String locale) {
            Faker faker = new Faker(new Locale("ru"));
            return new RegistrationByCardInfo(
                    faker.name().fullName(),
                    faker.phoneNumber().phoneNumber(),
                    faker.address().cityName()
            );
        }

        public static String getMeetDate(int daysForward) {
            return LocalDate.now().plusDays(daysForward).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        }

        public static String getBadName() {
            return "Vasya";
        }

        public static String getBadCity() {
            return "Малые упыри";
        }

        public static String getBadPhone() {
            return "";
        }
    }
}