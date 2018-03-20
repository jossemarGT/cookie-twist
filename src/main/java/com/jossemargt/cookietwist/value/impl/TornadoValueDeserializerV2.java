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
package com.jossemargt.cookietwist.value.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.value.CookieValueModel;
import com.jossemargt.cookietwist.value.Deserializer;

public class TornadoValueDeserializerV2 implements Deserializer {

    private static final int COOKIE_VALUE_TOKEN_COUNT = 6;
    private static final int COOKIE_FIELD_TOKEN_COUNT = 2;

    @Override
    public CookieValueModel deserialize(String value) throws InvalidFormatException {
        String []tokens = value.split("\\|");

        if (tokens.length != TornadoValueDeserializerV2.COOKIE_VALUE_TOKEN_COUNT) {
            throw new InvalidFormatException(String.format("Invalid value format. Expected %d fields, got %d",
                    TornadoValueDeserializerV2.COOKIE_VALUE_TOKEN_COUNT, tokens.length));
        }

        if (!"2".equals(tokens[0])) {
            throw new InvalidFormatException(String.format("Invalid version format '%s'", tokens[0]));
        }

        long timestamp;
        try {
            timestamp = Long.parseLong(extractFieldValue(tokens[2]), 10);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException("Invalid timestamp format, expected UNIX epoch", e);
        }

        CookieValueModel model = CookieValueModel.builder()
                .withSignatureKeyVersion(Integer.parseInt(extractFieldValue(tokens[1]), 10))
                .withTimestamp(timestamp)
                .withName(extractFieldValue(tokens[3]))
                .withValue(decodeValue(extractFieldValue(tokens[4])))
                .withSignature(tokens[5])
                .build();
        return model;
    }

    private String extractFieldValue(String field) throws InvalidFormatException {
        String []tokens = field.split(":");
        int expectedLength;

        try {
            expectedLength = Integer.parseInt(tokens[0], 10);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException("Invalid field length format", e);
        }


        if (expectedLength == 0 && tokens.length == 1) {
            return "";
        }

        if (tokens.length != TornadoValueDeserializerV2.COOKIE_FIELD_TOKEN_COUNT) {
            throw new InvalidFormatException(String.format("Invalid value field format '%s'", field));
        }

        String fieldValue = tokens[1];

        if (fieldValue.length() != expectedLength) {
            throw new InvalidFormatException(String.format("Field length missmatch. Expected %d, got %d",
                    expectedLength, fieldValue.length()));
        }

        return fieldValue;
    }

    private String decodeValue(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }

}
