package ru.netology.patterns;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class RegistrationByCardInfo {
    private final String name;
    private final String phone;
    private final String city;
    private final String meetDate;
}
