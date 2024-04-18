package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;

public class ErrorTestData {

    public static final MatcherFactory.Matcher<ErrorInfo> ERROR_INFO_MATCHER = MatcherFactory.usingEqualsComparator(ErrorInfo.class);

    public static final ErrorInfo sameDateTimeError =
            new ErrorInfo("http://localhost/rest/profile/meals/100003", ErrorType.VALIDATION_ERROR, "You already have meal with this date/time");

    public static final ErrorInfo sameEmailErrorForAdmin =
            new ErrorInfo("http://localhost/rest/admin/users/", ErrorType.VALIDATION_ERROR, "User with this email already exists");

    public static final ErrorInfo sameEmailErrorForNewUser =
            new ErrorInfo("http://localhost/rest/profile", ErrorType.VALIDATION_ERROR, "User with this email already exists");
}
