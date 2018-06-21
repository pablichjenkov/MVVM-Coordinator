package com.interval.common.login;


public class LoginEvent<T> {

    private T payload;

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public static class InternalSignUpStart extends LoginEvent {}
    public static class InternalSignUpFail extends LoginEvent<String> {}
    public static class InternalSignUpSuccess extends LoginEvent {}
    public static class InternalLoginStart extends LoginEvent {}
    public static class InternalLoginFail extends LoginEvent<String> {}
    public static class InternalLoginSuccess extends LoginEvent {}
}
