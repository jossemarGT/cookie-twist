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
public class V2TornadoCookieCodecTest {

    private static TornadoCookieCodec subject;
    private static String secretkey = "not-so-secret";
    private static String anotherSecretkey = "like-a-ninja";

    private static long frozentimestamp = 1521518443L;

    @BeforeClass
    public static void setUp() throws InvalidKeyException {
        subject = V2TornadoCookieCodec.builder().withTimestamp(frozentimestamp).withSecretKey(secretkey)
                .withSecretKey(anotherSecretkey).build();
    }

    @Test
    @Parameters(method = "codecParameters")
    public void testEncodeCookieCookie(String name, String value, int secretKeyNumber, String expectedValue) {
        Cookie cookieFlat = new Cookie(name, value);
        Cookie cookieSigned = subject.encodeCookie(cookieFlat, secretKeyNumber);

        assertNotEquals(cookieFlat, cookieSigned);
        assertEquals(expectedValue, cookieSigned.getValue());
    }

    @Test
    @Parameters(method = "codecParameters")
    public void testDecodeCookie(String expectedName, String expectedValue, int __, String signedStringValue) {
        Cookie cookieSigned = new Cookie(expectedName, signedStringValue);
        Cookie cookieFlat = subject.decodeCookie(cookieSigned);

        assertNotEquals(cookieSigned, cookieFlat);
        assertEquals(expectedName, cookieFlat.getName());
        assertEquals(expectedValue, cookieFlat.getValue());
    }

    @SuppressWarnings("unused")
    private Object codecParameters() {
        return new Object[] { new Object[] { "one", "simple value", 0,
                "2|1:0|10:1521518443|3:one|16:c2ltcGxlIHZhbHVl|94639b39df48c578b6396f7e02cad0cb3c496a7a1440107ef0182c8fb35ecfbb" },
                new Object[] { "two", "", 1,
                        "2|1:1|10:1521518443|3:two|0:|4adc7cb305a8c03144e848df0e6fbe73736ff3d0645d7ad5f8b1c530718c50cf" },
                new Object[] { "three", "'|/unw@nt€d ch@r$?", 0,
                        "2|1:0|10:1521518443|5:three|28:J3wvdW53QG504oKsZCBjaEByJD8=|0e768d266d14412bae46aa4212809c754b9dca800e43b95dea264b406731cd6f", } };
    }

    @Test
    @Parameters
    public void testDecodeCookieThrowsInvalidFormatException(String expectedMessage, String cookieName,
            String signedStringValue) {
        Cookie cookieSigned = new Cookie(cookieName, signedStringValue);
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
        return new Object[] {
                new Object[] { "Cookie name mismatch", "three",
                        "2|1:0|10:1521518443|3:one|16:c2ltcGxlIHZhbHVl|10101100010100101010100010" },
                new Object[] { "Cookie signature mismatch", "two",
                        "2|1:0|10:1521518443|3:two|16:c2ltcGxlIHZhbHVl|10101100010100101010100010" },
                new Object[] { "Required signature key does not exist", "one",
                        "2|1:3|10:1521518443|3:one|16:c2ltcGxlIHZhbHVl|10101100010100101010100010" },
                new Object[] { "Required signature key does not exist", "one",
                        "2|2:-1|10:1521518443|3:one|16:c2ltcGxlIHZhbHVl|10101100010100101010100010" },
                new Object[] { "Invalid field quantity. Expected 6, got 5", "zero",
                        "2|1:0|10:1521518443|4:zero|8:c2hvcnQ=" },
                new Object[] { "Field length mismatch. Expected 3 characters, got 8", "two",
                        "2|1:0|10:1521518443|3:two|3:c2hvcnQ=|the_signature" },
                new Object[] { "Field length mismatch. Expected 3 characters, got 5", "three",
                        "2|1:0|10:1521518443|3:three|8:c2hvcnQ=|the_signature" },
                new Object[] { "Invalid format version '3'", "four",
                        "3|1:0|10:1521518443|4:four|8:c2hvcnQ=|the_signature" },
                new Object[] { "Invalid timestamp field format: abcdefghij", "five",
                        "2|1:0|10:abcdefghij|4:five|8:c2hvcnQ=|the_signature" },
                new Object[] { "Invalid field length format", "six",
                        "2|1:0|10:1521518443|a:six|8:c2hvcnQ=|the_signature" }, };
    }
}
