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

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.tornado.TornadoCookieValue;
import com.jossemargt.cookietwist.tornado.TornadoCookieValue.TornadoCookieValueBuilder;
import com.jossemargt.cookietwist.tornado.transform.TornadoCookieValueDeserializer;

/**
 * The Class V2TornadoCookieValueDeserializer instantiate a {@link TornadoCookieValue} from a Tornado
 * secure cookie value string using the version 2 format.
 */
public class V2TornadoCookieValueDeserializer implements TornadoCookieValueDeserializer {

    /** The Constant COOKIE_VALUE_TOKEN_COUNT holds the expected field amount
     * for a Tornado secure cookie value string. */
    private static final int COOKIE_VALUE_TOKEN_COUNT = 6;

    /** The Constant COOKIE_FIELD_TOKEN_COUNT holds the expected token amount
     * in a field.
    */
    private static final int COOKIE_FIELD_TOKEN_COUNT = 2;

    /** The Constant TORNADO_SECURE_COOKIE_VERSION holds the Tornado secure
     * cookie value string format version. */
    private static final int TORNADO_SECURE_COOKIE_VERSION = 2;

    /** The Constant COOKIE_FIELD_VERSION_POS holds the format version field
     * position. */
    private static final int COOKIE_FIELD_VERSION_POS = 0;

    /** The Constant COOKIE_FIELD_KEYVERSION_POS holds the key version number
     * field position. */
    private static final int COOKIE_FIELD_KEYVERSION_POS = 1;

    /** The Constant COOKIE_FIELD_TIMESTAMP_POS holds the timestamp field
     * position. */
    private static final int COOKIE_FIELD_TIMESTAMP_POS = 2;

    /** The Constant COOKIE_FIELD_NAME_POS holds the name field position. */
    private static final int COOKIE_FIELD_NAME_POS = 3;

    /** The Constant COOKIE_FIELD_VALUE_POS holds the base64 value field
     * position. */
    private static final int COOKIE_FIELD_VALUE_POS = 4;

    /** The Constant COOKIE_FIELD_SIGNATURE_POS holds the signature field
     * position. */
    private static final int COOKIE_FIELD_SIGNATURE_POS = 5;

    /* (non-Javadoc)
     * @see com.jossemargt.cookietwist.tornado.transform.TornadoCookieValueDeserializer#deserialize
     */
    @Override
    public TornadoCookieValue deserialize(String value) {
        String[] tokens = value.split("\\|");

        if (tokens.length < V2TornadoCookieValueDeserializer.COOKIE_VALUE_TOKEN_COUNT) {
            throw new InvalidFormatException(
                String.format("Invalid field amount. Expected %d, got %d",
                V2TornadoCookieValueDeserializer.COOKIE_VALUE_TOKEN_COUNT, tokens.length));
        }

        TornadoCookieValueBuilder builder = TornadoCookieValue.builder();
        String field;

        for (int i = 0; i < tokens.length; i++) {
            field = tokens[i];
            switch (i) {
                case COOKIE_FIELD_VERSION_POS:
                    if (!String.valueOf(TORNADO_SECURE_COOKIE_VERSION)
                                .equals(field)) {
                        throw new InvalidFormatException(
                            String.format("Invalid version '%s'", field));
                    }
                    break;
                case COOKIE_FIELD_KEYVERSION_POS:
                    builder.withSignatureKeyVersion(extractInteger(field));
                    break;
                case COOKIE_FIELD_TIMESTAMP_POS:
                    builder.withTimestamp(extractLong(field));
                    break;
                case COOKIE_FIELD_NAME_POS:
                    builder.withName(extractString(field));
                    break;
                case COOKIE_FIELD_VALUE_POS:
                    builder.withValue(decodeValue(extractString(field)));
                    break;
                case COOKIE_FIELD_SIGNATURE_POS:
                    builder.withSignature(field);
                    break;
                default:
                    throw new InvalidFormatException("Invalid field amount");
            }
        }

        return builder.build();
    }

    /**
     * Extract long field value and validates the length with the one
     * described in the format.
     *
     * @param field the raw field string
     * @return the long enclosed in the field format
     * @throws InvalidFormatException when the value does not comply with a
     * numberic format, the field does not comply with the "length:value" format
     * or the value length does not match with the given one.
     */
    private long extractLong(String field) throws InvalidFormatException {
        long result;

        try {
            result = Long.parseLong(extractString(field), 10);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException("Invalid numeric field format", e);
        }

        return result;
    }

    /**
     * Extract integer field value and validates the length with the one
     * described in the format.
     *
     * @param field the raw field string
     * @return the integer enclosed in the field format
     * @throws InvalidFormatException when the value does not comply with a
     * numberic format, the field does not comply with the "length:value" format
     * or the value length does not match with the given one.
     */
    private int extractInteger(String field) throws InvalidFormatException {
        int result;

        try {
            result = Integer.parseInt(extractString(field), 10);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException("Invalid numeric field format", e);
        }

        return result;
    }

    /**
     * Extract field value and validates the length with the one described in
     * the format.
     *
     * @param field the raw field string
     * @return the value string enclosed in the field format
     * @throws InvalidFormatException when field does not comply with the
     * "length:value" format or the value length does not match with the given
     * one.
     */
    private String extractString(String field) throws InvalidFormatException {
        String[] tokens = field.split(":");
        int expectedLength;

        try {
            expectedLength = Integer.parseInt(tokens[0], 10);
        } catch (NumberFormatException e) {
            throw new InvalidFormatException("Invalid field length format", e);
        }


        if (expectedLength == 0 && tokens.length == 1) {
            return "";
        }

        if (tokens.length != V2TornadoCookieValueDeserializer.COOKIE_FIELD_TOKEN_COUNT) {
            throw new InvalidFormatException(
                String.format("Invalid field format '%s'", field));
        }

        String fieldValue = tokens[1];

        if (fieldValue.length() != expectedLength) {
            throw new InvalidFormatException(
                String.format("Field length missmatch. Expected %d, got %d",
                expectedLength, fieldValue.length()));
        }

        return fieldValue;
    }

    /**
     * Decodes the value string from its base64 representation.
     *
     * @param value the base64 encoded string
     * @return the decoded string
     */
    private String decodeValue(String value) {
        return new String(Base64.getDecoder().decode(value),
                            StandardCharsets.UTF_8);
    }

}
