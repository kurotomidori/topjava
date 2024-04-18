package ru.javawebinar.topjava.util.exception;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.Objects;

public class ErrorInfo {
    private final String url;

    private final ErrorType type;
    private final String[] details;

    @ConstructorProperties({"url", "type", "detail"})
    public ErrorInfo(CharSequence url, ErrorType type, String... details) {
        this.url = url.toString();
        this.type = type;
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String[] getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfo errorInfo = (ErrorInfo) o;
        return Objects.equals(url, errorInfo.url) && type == errorInfo.type && Arrays.equals(details, errorInfo.details);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url, type);
        result = 31 * result + Arrays.hashCode(details);
        return result;
    }
}