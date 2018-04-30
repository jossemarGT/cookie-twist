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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.InvalidKeyException;

import javax.servlet.http.Cookie;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class V1TornadoCookieCodecTest {

    private static TornadoCookieCodec subject;
    private static String secretkey = "not-so-secret";
    private static long frozentimestamp = 1521518443L;

    @BeforeClass
    public static void setUp() throws InvalidKeyException {
        subject = V1TornadoCookieCodec.builder().withTimestamp(frozentimestamp).withSecretKey(secretkey).build();
    }

    @Test
    @Parameters(method = "codecParameters")
    public void testEncodeCookieCookie(String name, String value, String expectedValue) {
        Cookie cookieFlat = new Cookie(name, value);
        Cookie cookieSigned = subject.encodeCookie(cookieFlat);

        assertNotEquals(cookieFlat, cookieSigned);
        assertEquals(expectedValue, cookieSigned.getValue());
    }

    @Test
    @Parameters(method = "codecParameters")
    public void testDecodeCookie(String name, String expectedValue, String signedStringValue) {
        Cookie cookieSigned = new Cookie(name, signedStringValue);
        Cookie cookieFlat = subject.decodeCookie(cookieSigned);

        assertNotEquals(cookieSigned, cookieFlat);
        assertEquals(expectedValue, cookieFlat.getValue());
    }

    @SuppressWarnings("unused")
    private Object codecParameters() {
        return new Object[] {
                new Object[] { "name", "value", "value|1521518443|8d2c562f3831063fbc70cd0b35da54aa4e4e730e" },
                new Object[] { "empty", "", "|1521518443|c5a458437f54c606003f809a401dd44d539eb025" },
                new Object[] { "tricky", "trick4|Str!n€",
                        "trick4|Str!n€|1521518443|4159ecdafe58baf080d68d96eb4efd6d730795d0" },
                new Object[] { "tricky2", "tricky|String|the|revenge",
                        "tricky|String|the|revenge|1521518443|013403d99c1dfc22a0d7762d924a25147624cc6c" } };
    }

    @Test
    @Parameters
    public void testDecodeCookieThrowsInvalidFormatException(String expectedMessage, String signedStringValue) {
        Cookie cookieSigned = new Cookie("name", signedStringValue);
        try {
            subject.decodeCookie(cookieSigned);
            fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertTrue("Expected InvalidFormatException", e instanceof InvalidFormatException);
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    private Object parametersForTestDecodeCookieThrowsInvalidFormatException() {
        return new Object[] { new Object[] { "Cookie signature mismatch", "value|1521518443|1001010001010010010" },
                new Object[] { "Invalid field quantity: 2", "value|1521518443" },
                new Object[] { "Invalid field quantity: 2", "value|1521518443|" },
                new Object[] { "Invalid timestamp format: ''", "value||the_signature" },
                new Object[] { "Invalid field quantity: 1", "value" } };
    }

}
