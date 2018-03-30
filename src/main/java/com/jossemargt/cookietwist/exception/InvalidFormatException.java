/*
 * The MIT License
 *
 * Copyright (C) 2018 Jonnatan Jossemar Cordero
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.jossemargt.cookietwist.exception;

/**
 * The Class InvalidFormatException is meant to describe any CookieValue Model
 * de-serialization fault, mostly related to String formatting, numeric parsing
 * or un-expected amount of String tokens.
 */
@SuppressWarnings("serial")
public class InvalidFormatException extends Exception {

    /**
     * Instantiates a new invalid format exception without message string or
     * cause.
     */
    public InvalidFormatException() {
        super();
    }

    /**
     * Instantiates a new invalid format exception with a string message
     * describing the occurrence.
     *
     * @param message the description of the occurrence.
     */
    public InvalidFormatException(String message) {
        super(message);
    }

    /**
     * Instantiates a new invalid format exception with a string message
     * describing the occurrence and the Exception that triggered it.
     *
     * @param message the description of the occurrence.
     * @param cause the Exception which triggerd this one.
     */
    public InvalidFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new invalid format exception whith the one that triggered
     * it.
     *
     * @param cause the Exception which triggerd this one.
     */
    public InvalidFormatException(Throwable cause) {
        super(cause);
    }

}
