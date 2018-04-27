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
package com.jossemargt.cookietwist.signature.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.security.InvalidKeyException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class Sha1SignatureHasherTest {

    private static Sha1SignatureHasher subject;
    private static String secretkey = "not-so-secret";

    @BeforeClass
    public static void setUp() throws InvalidKeyException {
        subject = new Sha1SignatureHasher(secretkey);
        subject.init();
    }

    @Test
    @Parameters
    public void testComputeSignature(String expectedSum, String[] values) {
        String sum = subject.computeSignature(values);
        assertEquals(expectedSum, sum);
    }

    @SuppressWarnings("unused")
    private Object parametersForTestComputeSignature() {
        return new Object[] {
                new Object[] { "eaf80caf307333d1c148b9d7dd226689300389c8", new String[] { "value|1521518443" } },
                new Object[] { "eaf80caf307333d1c148b9d7dd226689300389c8",
                        new String[] { "value", "|", "1521518443" } } };
    }

    @Test
    @Parameters
    public void testInit(String secret) {
        try {
            Sha1SignatureHasher s = new Sha1SignatureHasher(secret);
            s.init();
            fail("Expected exception to be thrown");
        } catch (java.lang.IllegalArgumentException e) {
            assertTrue("Expected IllegalArgumentException due empty key",
                    e.getMessage().contains("Unallowed null or empty secret key"));
        } catch (Exception e) {
            e.printStackTrace();
            fail(String.format("Expected another exception, got: %s", e.getClass()));
        }
    }

    @SuppressWarnings("unused")
    private Object parametersForTestInit() {
        return new Object[] { new Object[] { "" }, new Object[] { null } };

    }

}
