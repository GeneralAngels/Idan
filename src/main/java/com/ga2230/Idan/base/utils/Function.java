/*
 * Copyright (c) 2020 Nadav Tasher of General Angels
 * https://github.com/GeneralAngels/Shleam
 */

package com.ga2230.Idan.base.utils;

/**
 * This interface is used to define a registrable function.
 */
public interface Function {
    void execute(Object parameter) throws Exception;
}