package ru.javawebinar.topjava.util.exception;

import java.beans.ConstructorProperties;
import java.util.Objects;

public class ErrorInfo {
    private final String url;

    private final ErrorType type;
    private final String detail;

    @ConstructorProperties({"url", "type", "detail"})
    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorInfo errorInfo = (ErrorInfo) o;
        return Objects.equals(url, errorInfo.url) && type == errorInfo.type && Objects.equals(detail, errorInfo.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, type, detail);
    }
}