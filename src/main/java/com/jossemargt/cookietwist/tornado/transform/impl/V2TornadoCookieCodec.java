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

import java.security.InvalidKeyException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.signature.SignatureHasher;
import com.jossemargt.cookietwist.signature.impl.Sha256SignatureHasher;
import com.jossemargt.cookietwist.tornado.TornadoCookieValue;
import com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec;

/**
 * The Class V2TornadoCookieCodec contains the serializer, de-serializer and
 * signature hasher(s) needed to encode or decode a Tornado secure cookie V2.
 */
public final class V2TornadoCookieCodec extends TornadoCookieCodec {

    /** The hasher list for cookie value signature. */
    private ArrayList<SignatureHasher> hasherList;

    /**
     * Instantiates a new Tornado cookie codec V2.
     *
     * @param builder
     *            the {@link Builder} instance
     */
    private V2TornadoCookieCodec(Builder builder) {
        super(builder);
        this.hasherList = builder.hasherList;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec#
     * getTornadoCookieValueFrom(javax.servlet.http.Cookie)
     */
    @Override
    public TornadoCookieValue getTornadoCookieValueFrom(Cookie source) {
        TornadoCookieValue cookieValue = deserializer.deserialize(source.getValue());

        if (!cookieValue.getName().equals(source.getName())) {
            throw new InvalidFormatException("Cookie name mismatch");
        }

        return cookieValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec#
     * computeSignature(com.jossemargt.cookietwist.tornado.TornadoCookieValue)
     */
    @Override
    protected String computeSignature(TornadoCookieValue cookieValue) {
        int signatureNumber = cookieValue.getSignatureKeyVersion();

        if (signatureNumber < 0 || signatureNumber >= hasherList.size()) {
            throw new InvalidFormatException("Required signature key does not exist");
        }

        SignatureHasher hasher = hasherList.get(signatureNumber);
        TornadoCookieValue temp = TornadoCookieValue.builderFrom(cookieValue).withSignature("").build();

        String serializedValue = serializer.serialize(temp);

        return hasher.computeSignature(serializedValue, "|");

    }

    /**
     * Factory method for {@link V2TornadoCookieCodec.Builder}.
     *
     * @return the builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * As its name suggests the Class Builder handles the {@link TornadoCookieCodec}
     * creation process.
     */
    public static final class Builder extends TornadoCookieCodec.Builder<Builder> {

        /** The hasher list to be used by the {@link V2TornadoCookieCodec} instance. */
        private ArrayList<SignatureHasher> hasherList;

        /**
         * Instantiates a new builder.
         */
        private Builder() {
            super();
            this.serializer = new V2TornadoCookieValueSerializer();
            this.deserializer = new V2TornadoCookieValueDeserializer();
            this.hasherList = new ArrayList<SignatureHasher>();
        }

        /**
         * Adds a SignatureHasher object with the given secret key in the hasherList to
         * be use by the {@link V2TornadoCookieCodec} instance.
         *
         * @param secretKey
         *            the secret key String.
         * @return the builder
         */
        public Builder withSecretKey(String secretKey) {
            hasherList.add(new Sha256SignatureHasher(secretKey));
            return self();
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec.Builder#build
         */
        @Override
        public TornadoCookieCodec build() throws InvalidKeyException {
            for (SignatureHasher hasher : hasherList) {
                hasher.init();
            }
            return new V2TornadoCookieCodec(this);
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec.Builder#self
         */
        @Override
        protected Builder self() {
            return this;
        }
    }

}
