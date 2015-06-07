package com.daugherty.e2c;

import java.math.BigDecimal;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.daugherty.e2c.business.ValidationException;

public class E2CMatchers {

    private E2CMatchers() {
    }

    @Factory
    public static <T> Matcher<Date> equalToWithinTolerance(Date date, long tolerance) {
        return new DateToleranceMatcher(date, tolerance);
    }

    public static class DateToleranceMatcher extends TypeSafeMatcher<Date> {

        private final Date date;
        private final long tolerance;

        public DateToleranceMatcher(Date date, long tolerance) {
            this.date = date;
            this.tolerance = tolerance;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is within " + tolerance + " milliseconds of " + date);
        }

        @Override
        public boolean matchesSafely(Date item) {
            return item.getTime() > (date.getTime() - tolerance) && item.getTime() < (date.getTime() + tolerance);
        }
    }

    @Factory
    public static <T> Matcher<BigDecimal> equalToWithinTolerance(BigDecimal bigDecimal, long tolerance) {
        return new BigDecimalToleranceMatcher(bigDecimal, tolerance);
    }

    public static class BigDecimalToleranceMatcher extends TypeSafeMatcher<BigDecimal> {

        private final BigDecimal bigDecimal;
        private final long tolerance;

        public BigDecimalToleranceMatcher(BigDecimal bigDecimal, long tolerance) {
            this.bigDecimal = bigDecimal;
            this.tolerance = tolerance;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("is more than " + tolerance + " away from " + bigDecimal);
        }

        @Override
        public boolean matchesSafely(BigDecimal item) {
            return item.doubleValue() > (BigDecimal.valueOf(bigDecimal.doubleValue() - tolerance).doubleValue())
                    && item.doubleValue() < (BigDecimal.valueOf(bigDecimal.doubleValue() + tolerance).doubleValue());

        }
    }

    @Factory
    public static <T> Matcher<ValidationException> fieldHasValidationError(String fieldName, String messageKey) {
        return new ValidationExceptionMessageKeyMatcher(fieldName, messageKey);
    }

    public static class ValidationExceptionMessageKeyMatcher extends TypeSafeMatcher<ValidationException> {

        private final String fieldName;
        private final String messageKey;

        public ValidationExceptionMessageKeyMatcher(String fieldName, String messageKey) {
            this.fieldName = fieldName;
            this.messageKey = messageKey;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(fieldName + " contains a message key matching '" + messageKey + "'");
        }

        @Override
        public boolean matchesSafely(ValidationException item) {
            return item.getValidationError().getErrors().containsEntry(fieldName, messageKey);
        }

    }

}
