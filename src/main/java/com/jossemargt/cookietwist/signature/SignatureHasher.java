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
package com.jossemargt.cookietwist.signature;

import java.nio.charset.StandardCharsets;
import java.util.Formatter;

import javax.crypto.Mac;

/**
 * The abstract Class SignatureHasher handles the cryptographic signature
 * calculation regardless the symmetric algorithm to be use, which has to be
 * provided by any child class in the init method.
 */
public abstract class SignatureHasher {

    /** The hmac secret key byte array. */
    protected byte[] hmacSecretKey;

    /** The hasher instance from {@link Mac}. */
    protected Mac hasher;

    /** The initialized variable flags the hasher instance state. **/
    protected boolean initialized = false;

    /**
     * Instantiates a new signature hasher with a secret key String and a
     * un-initialized {@link Mac} instance.
     *
     * @param secret
     *            the secret String UTF-8 encoded
     * @throws IllegalArgumentException
     *             when the secret key is a null or empty String value.
     */
    public SignatureHasher(String secret) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("Unallowed null or empty secret key");
        }
        this.hmacSecretKey = secret.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Compute HMAC signature for the given sequence of Strings.
     *
     * @param values
     *            the sequence of values from where the signature will be computed
     * @return the formatted hexadecimal string
     * @throws IllegalStateException
     *             when this method is called from an un-initialized signature
     *             hasher
     */
    public String computeSignature(String... values) {
        if (!initialized) {
            throw new IllegalStateException("Un-initialized signature hasher");
        }

        byte[] result;

        synchronized (hasher) {
            for (String v : values) {
                hasher.update(v.getBytes(StandardCharsets.UTF_8));
            }

            result = hasher.doFinal();
        }

        return toHexString(result);
    }

    /**
     * Initializes the {@link Mac} instance with an specific symmetric signature
     * algorithm and the secret key.
     *
     * @throws IllegalArgumentException
     *             when a the {@link Mac} is initialized with an invalid secret key.
     */
    public abstract void init();

    /**
     * To hex string, transforms a byte array to its hexadecimal String
     * representation byte per byte.
     *
     * @param bytes
     *            the byte array to be formatted
     * @return the formated string
     */
    private String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        String result = formatter.toString();
        formatter.close();

        return result;
    }
}
