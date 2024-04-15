package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.MatcherFactory;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;

import static ru.javawebinar.topjava.util.ValidationUtil.EXCEPTION_DUPLICATE_DATETIME;
import static ru.javawebinar.topjava.util.ValidationUtil.EXCEPTION_DUPLICATE_EMAIL;

public class ErrorTestData {

    public static final MatcherFactory.Matcher<ErrorInfo> ERROR_INFO_MATCHER = MatcherFactory.usingEqualsComparator(ErrorInfo.class);

    public static final ErrorInfo sameDateTimeError =
            new ErrorInfo("http://localhost/rest/profile/meals/100003", ErrorType.VALIDATION_ERROR, EXCEPTION_DUPLICATE_DATETIME);

    public static final ErrorInfo sameEmailErrorForAdmin =
            new ErrorInfo("http://localhost/rest/admin/users/", ErrorType.VALIDATION_ERROR, EXCEPTION_DUPLICATE_EMAIL);

    public static final ErrorInfo sameEmailErrorForNewUser =
            new ErrorInfo("http://localhost/rest/profile", ErrorType.VALIDATION_ERROR, EXCEPTION_DUPLICATE_EMAIL);
}
