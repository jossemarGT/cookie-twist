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

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.tornado.TornadoCookieValue;
import com.jossemargt.cookietwist.tornado.TornadoCookieValue.TornadoCookieValueBuilder;
import com.jossemargt.cookietwist.tornado.transform.TornadoCookieValueDeserializer;

/**
 * The Class V1TornadoCookieValueDeserializer instantiate a {@link TornadoCookieValue} from a Tornado
 * secure cookie value string using the version 1 format.
 */
public class V1TornadoCookieValueDeserializer implements TornadoCookieValueDeserializer {

    /** The Constant COOKIE_VALUE_TOKEN_MIN_COUNT holds the minimal field amount
     * expected in a Tornado secure cookie value string (V1). */
    private static final int COOKIE_VALUE_TOKEN_MIN_COUNT = 3;

    /** The Constant COOKIE_FIELD_SIGNATURE_REVERSE_POS holds the signature
     * field position from the end to the start. */
    private static final int COOKIE_FIELD_SIGNATURE_REVERSE_POS = 0;

    /** The Constant COOKIE_FIELD_TIMESTAMP_REVERSE_POS holds the timestamp
     * field position from the end to the start. */
    private static final int COOKIE_FIELD_TIMESTAMP_REVERSE_POS = 1;

    /* (non-Javadoc)
     * @see com.jossemargt.cookietwist.tornado.transform.TornadoCookieValueDeserializer#deserialize
     */
    @Override
    public TornadoCookieValue deserialize(String value) {
        String[] tokens = value.split("\\|");
        TornadoCookieValueBuilder modelBuilder = TornadoCookieValue.builder();
        StringBuilder valueBuilder = new StringBuilder();

        int tokenCount = tokens.length;

        if (tokenCount < V1TornadoCookieValueDeserializer.COOKIE_VALUE_TOKEN_MIN_COUNT) {
            throw new InvalidFormatException(
                    String.format("Invalid field amount, got %d", tokenCount));
        }

        int offset;
        String field;
        for (int i = tokenCount; i > 0; i--) {
            offset = tokenCount - i;
            field = tokens[i - 1];

            switch (offset) {
            case COOKIE_FIELD_SIGNATURE_REVERSE_POS:
                modelBuilder.withSignature(field);
                break;
            case COOKIE_FIELD_TIMESTAMP_REVERSE_POS:
                long timestamp;
                try {
                    timestamp = Long.parseLong(field, 10);
                } catch (NumberFormatException e) {
                    throw new InvalidFormatException("Invalid timestamp", e);
                }
                modelBuilder.withTimestamp(timestamp);
                break;
            default:
                if (i == 1) {
                    valueBuilder.insert(0, field);
                    modelBuilder.withValue(valueBuilder.toString());
                } else {
                    valueBuilder.insert(0, "|").insert(1, field);
                }
                break;
            }
        }

        return modelBuilder.build();
    }
}
