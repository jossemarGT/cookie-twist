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
package com.jossemargt.cookietwist.tornado.value.impl;

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.tornado.CookieModel;
import com.jossemargt.cookietwist.tornado.value.Deserializer;

public class TornadoValueDeserializerV1 implements Deserializer {

    private static final int COOKIE_VALUE_TOKEN_COUNT = 3;

    @Override
    public CookieModel deserialize(String value) throws InvalidFormatException {
        String []tokens = value.split("\\|");
        int tokenCount = tokens.length;

        if (tokenCount < TornadoValueDeserializerV1.COOKIE_VALUE_TOKEN_COUNT) {
            throw new InvalidFormatException(String.format("Invalid amount of fields, got %d", tokenCount));
        }

        String valueString = buildValueString(tokens, tokenCount - 2);
        long timestamp;
        try {
            timestamp = Long.parseLong(tokens[tokenCount - 2], 10);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException("Invalid timestamp format, expected UNIX epoch", e);
        }

        String signature = tokens[tokenCount - 1];

        CookieModel model = CookieModel.builder()
                                    .withValue(valueString)
                                    .withTimestamp(timestamp)
                                    .withSignature(signature)
                                    .build();
        return model;
    }

    private String buildValueString(String []tokens, int limit) {
        if (limit <= 1) {
            return tokens[0];
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < limit - 1; i++) {
            result.append(tokens[i]).append("|");
        }
        result.append(tokens[limit - 1]);

        return result.toString();
    }
}
