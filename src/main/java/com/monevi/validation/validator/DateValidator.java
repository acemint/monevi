package com.monevi.validation.validator;

import com.monevi.validation.annotation.ValidDate;
import com.monevi.validation.annotation.ValidEmail;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    @Override
    public boolean isValid(final String date, final ConstraintValidatorContext context) {
        return this.validateDate(date);
    }

    private boolean validateDate(final String date) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy");
            dateTimeFormatter.parseDateTime(date);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
