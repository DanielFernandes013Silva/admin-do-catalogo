package com.admin.catologo.application;

public abstract class UseCase <IN, OUT> {

    public abstract OUT execute(IN anIn);
}
