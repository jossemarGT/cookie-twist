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

import javax.servlet.http.Cookie;

import com.jossemargt.cookietwist.signature.SignatureHasher;
import com.jossemargt.cookietwist.signature.impl.Sha1SignatureHasher;
import com.jossemargt.cookietwist.tornado.TornadoCookieValue;
import com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec;

/**
 * The Class V1TornadoCookieCodec contains the serializer, de-serializer and
 * signature hasher(s) needed to encode or decode a Tornado secure cookie V1.
 */
public final class V1TornadoCookieCodec extends TornadoCookieCodec {

    /** The hasher instance for cookie value signature. */
    private SignatureHasher hasher;

    /**
     * Instantiates a new Tornado cookie codec V1.
     *
     * @param builder
     *            the {@link Builder} instance
     */
    private V1TornadoCookieCodec(Builder builder) {
        super(builder);
        this.hasher = builder.hasher;
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

        return TornadoCookieValue.builderFrom(cookieValue).withName(source.getName()).build();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec#
     * computeSignature(com.jossemargt.cookietwist.tornado.TornadoCookieValue)
     */
    @Override
    protected String computeSignature(TornadoCookieValue cookieValue) {
        return hasher.computeSignature(cookieValue.getName(), cookieValue.getValue(),
                String.valueOf(cookieValue.getTimestamp()));

    }

    /**
     * Factory method for {@link V1TornadoCookieCodec.Builder}.
     *
     * @return the builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * As its name suggests the Class Builder handles the {@link TornadoCookieCodec}
     * creation process.
     */
    public static final class Builder extends TornadoCookieCodec.Builder<Builder> {

        /**
         * The signature hasher to be used by the {@link V1TornadoCookieCodec} instance.
         */
        private SignatureHasher hasher;

        /**
         * Instantiates a new builder.
         */
        private Builder() {
            super();
            this.serializer = new V1TornadoCookieValueSerializer();
            this.deserializer = new V1TornadoCookieValueDeserializer();
        }

        /*
         * (non-Javadoc)
         *
         * @see com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec.Builder#
         * withSecretKey()
         */
        @Override
        public Builder withSecretKey(String secretKey) {
            hasher = new Sha1SignatureHasher(secretKey);
            return self();
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.jossemargt.cookietwist.tornado.transform.TornadoCookieCodec.Builder#build
         */
        @Override
        public TornadoCookieCodec build() {
            hasher.init();
            return new V1TornadoCookieCodec(this);
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
