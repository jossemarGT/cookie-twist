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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.jossemargt.cookietwist.signature.SignatureHasher;

/**
 * The Class Sha256SignatureHasherinitializes a {@link Mac} with the SHA1 HMAC
 * algorithm.
 */
public class Sha256SignatureHasher extends SignatureHasher {

    /**
     * The Constant HMAC_SHA256_ALGORITHM with the targeted symmetric algorithm to
     * use.
     */
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    /**
     * Instantiates a new SHA256 signature hasher with a secret key String.
     *
     * @param secret
     *            the secret String UTF-8 encoded
     */
    public Sha256SignatureHasher(String secret) {
        super(secret);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jossemargt.cookietwist.signature.SignatureHasher#init()
     */
    @Override
    public void init() {
        SecretKeySpec signingKey = new SecretKeySpec(this.hmacSecretKey, HMAC_SHA256_ALGORITHM);
        try {
            hasher = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            hasher.init(signingKey);
            this.initialized = true;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
