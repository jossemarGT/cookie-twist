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
package com.jossemargt.cookietwist.tornado.transform.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.jossemargt.cookietwist.tornado.TornadoCookieValue;
import com.jossemargt.cookietwist.tornado.transform.TornadoCookieValueSerializer;

/**
 * The Class V2TornadoCookieValueSerializer transforms a {@link TornadoCookieValue} object into its
 * Tornado secure cookie value string representation with the format version 2.
 */
public class V2TornadoCookieValueSerializer implements TornadoCookieValueSerializer {

    /* (non-Javadoc)
     * @see com.jossemargt.cookietwist.tornado.transform.TornadoCookieValueSerializer#serialize
     */
    @Override
    public String serialize(TornadoCookieValue model) {
        StringBuilder result =  new StringBuilder("2");

        result.append("|").append(formatField(model.getSignatureKeyVersion()));
        result.append("|").append(formatField(model.getTimestamp()));
        result.append("|").append(formatField(model.getName()));
        result.append("|").append(formatField(encodeValue(model.getValue())));

        if (model.getSignature() != null && !model.getSignature().isEmpty()) {
            result.append("|").append(model.getSignature());
        }

        return result.toString();
    }

    /**
     * Encodes the given string into a base64 one.
     *
     * @param value the string to encode
     * @return the base64 encoded string
     */
    private String encodeValue(String value) {
        if (value == null) {
            value = "";
        }
        return Base64.getEncoder()
                     .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Formats the given integer number as String following the "length:field"
     * pattern.
     *
     * @param field the integer number to format
     * @return the formated string
     */
    private String formatField(int field) {
        return formatField(Integer.toString(field));
    }

    /**
     * Formats the given long number as String following the "length:field"
     * pattern.
     *
     * @param field the long number to format
     * @return the formated string
     */
    private String formatField(long field) {
        return formatField(Long.toString(field));
    }

    /**
     * Formats the given string following the "length:field" pattern.
     *
     * @param field the value string to format
     * @return the formated string
     */
    private String formatField(String field) {
        return String.format("%d:%s", field.length(), field);
    }

}
