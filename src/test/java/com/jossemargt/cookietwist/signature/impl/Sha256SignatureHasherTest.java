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
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.jossemargt.cookietwist.signature.SignatureHasher;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import net.jodah.concurrentunit.Waiter;

@RunWith(JUnitParamsRunner.class)
public class Sha256SignatureHasherTest {

    private static Sha256SignatureHasher subject;
    private static String secretkey = "not-so-secret";

    @BeforeClass
    public static void setUp() throws InvalidKeyException {
        subject = new Sha256SignatureHasher(secretkey);
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
                new Object[] { "94639b39df48c578b6396f7e02cad0cb3c496a7a1440107ef0182c8fb35ecfbb",
                        new String[] { "2|1:0|10:1521518443|3:one|16:c2ltcGxlIHZhbHVl|" } },
                new Object[] { "94639b39df48c578b6396f7e02cad0cb3c496a7a1440107ef0182c8fb35ecfbb", new String[] { "2",
                        "|", "1:0", "|", "10:1521518443", "|", "3:one", "|", "16:c2ltcGxlIHZhbHVl", "|" } } };
    }

    @Test
    @Parameters
    public void testInit(String secret) {
        try {
            Sha256SignatureHasher s = new Sha256SignatureHasher(secret);
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

    @Test
    public void concurrentTest() throws TimeoutException, InvalidKeyException {
        final Waiter waiter = new Waiter();
        final String[][] valueGrid = new String[][] {
                new String[] { "09958263c46fde29c341ece86496314d7680eee3df9a04e7845b2bc8c16e3792", "1", "2", "3" },
                new String[] { "552c8e8474e35f830feff11484e78e36dee95baeed93831a54619413241874fa", "v1", "v2", "v3" },
                new String[] { "09958263c46fde29c341ece86496314d7680eee3df9a04e7845b2bc8c16e3792", "1", "2", "3" },
                new String[] { "552c8e8474e35f830feff11484e78e36dee95baeed93831a54619413241874fa", "v1", "v2", "v3" },
                new String[] { "09958263c46fde29c341ece86496314d7680eee3df9a04e7845b2bc8c16e3792", "1", "2", "3" },
                new String[] { "552c8e8474e35f830feff11484e78e36dee95baeed93831a54619413241874fa", "v1", "v2", "v3" },
                new String[] { "09958263c46fde29c341ece86496314d7680eee3df9a04e7845b2bc8c16e3792", "1", "2", "3" },
                new String[] { "552c8e8474e35f830feff11484e78e36dee95baeed93831a54619413241874fa", "v1", "v2", "v3" } };

        final SignatureHasher subject = new Sha256SignatureHasher(secretkey);
        subject.init();

        for (String[] values : valueGrid) {
            new Thread(() -> {
                String expectedSignature = values[0];
                String computedSignature = subject.computeSignature(Arrays.copyOfRange(values, 1, values.length));
                waiter.assertEquals(expectedSignature, computedSignature);
                waiter.resume();
            }).start();
        }

        waiter.await(10, TimeUnit.SECONDS, valueGrid.length);
    }

}
