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
package com.jossemargt.cookietwist.tornado.transform;

import java.security.InvalidKeyException;
import java.util.Date;

import javax.servlet.http.Cookie;

import com.jossemargt.cookietwist.exception.InvalidFormatException;
import com.jossemargt.cookietwist.tornado.TornadoCookieValue;

/**
 * The Class TornadoCookieCodec contains the serializer, de-serializer and
 * signature hasher(s) needed to encode or decode a Tornado secure cookie.
 */
public abstract class TornadoCookieCodec {

    /**
     * The Constant DEFAULT_SECRET_KEY_NUMBER represents the default key number to
     * use to sign a cookie value.
     */
    protected static final int DEFAULT_SECRET_KEY_NUMBER = 0;

    /**
     * The Constant TIMESTAMP_NOW is used to infere if a TornadoCookieValue should
     * use the default (now) or a fixed one.
     */
    private static final long TIMESTAMP_NOW = 0;

    /**
     * The serializer is a {@link TornadoCookieValueSerializer} instance that will
     * transform a cookie plain text into a Tornado secure cookie string
     * representation.
     */
    protected TornadoCookieValueSerializer serializer;

    /**
     * The deserializer is a {@link TornadoCookieValueDeserializer} instance that
     * will transform Tornado secure cookie string into its plain text
     * representation.
     */
    protected TornadoCookieValueDeserializer deserializer;

    /**
     * The timestamp value to be used in the Cookie value string generated by this
     * TornadoCookieCodec.
     */
    private long timestamp;

    /**
     * Instantiates a new TornadoCookieCodec from its builder.
     *
     * @param builder
     *            the TornadoCookieCodec abstract {@link Builder}.
     */
    protected TornadoCookieCodec(Builder<?> builder) {
        this.serializer = builder.serializer;
        this.deserializer = builder.deserializer;
        this.timestamp = builder.timestamp;
    }

    /**
     * Encode a cookie value string into its Tornado secure cookie representation
     * using the default secret key number.
     *
     * @param source
     *            the {@link Cookie} to be signed
     * @return the {@link Cookie} with the value signed
     */
    public Cookie encodeCookie(Cookie source) {
        return encodeCookie(source, DEFAULT_SECRET_KEY_NUMBER);
    }

    /**
     * Encode a {@link Cookie} value string into its Tornado secure cookie
     * representation using the given secret key number.
     *
     * @param source
     *            the {@link Cookie} to be signed
     * @param secretKeyNumber
     *            the number of the secret key used to sign the given {@link Cookie}
     * @return the {@link Cookie} with the value signed
     */
    public Cookie encodeCookie(Cookie source, int secretKeyNumber) {
        TornadoCookieValue toEncode = TornadoCookieValue.builder().withName(source.getName())
                .withValue(source.getValue()).withTimestamp(getTimestamp()).withSignatureKeyVersion(secretKeyNumber)
                .build();

        String signature = computeSignature(toEncode);

        TornadoCookieValue toSerialize = TornadoCookieValue.builderFrom(toEncode).withSignature(signature).build();

        String serializedValue = serializer.serialize(toSerialize);

        Cookie encodedCookie = (Cookie) source.clone();
        encodedCookie.setValue(serializedValue);

        return encodedCookie;
    }

    /**
     * Decode a {@link Cookie} from its Tornado secure cookie representation into a
     * plain text one.
     *
     * @param source
     *            the signed {@link Cookie} to be transformed
     * @return the {@link Cookie} with the plain text value
     */
    public Cookie decodeCookie(Cookie source) {
        TornadoCookieValue toDecode = getTornadoCookieValueFrom(source);

        String computedSignature = computeSignature(toDecode);

        if (!computedSignature.equals(toDecode.getSignature())) {
            throw new InvalidFormatException("Cookie signature mismatch");
        }

        Cookie decodedCookie = (Cookie) source.clone();
        decodedCookie.setValue(toDecode.getValue());

        return decodedCookie;
    }

    /**
     * Compute the cookie value string signature.
     *
     * @param cookieValue
     *            the cookie value to be signed
     * @return the signature string of the given {@link TornadoCookieValue}
     */
    protected abstract String computeSignature(TornadoCookieValue cookieValue);

    /**
     * Gets the tornado cookie value from a {@link Cookie} instance.
     *
     * @param source
     *            the {@link Cookie} with a Tornado signed value
     * @return the {@link TornadoCookieValue} from the {@link Cookie}'s Tornado
     *         signed value
     */
    protected abstract TornadoCookieValue getTornadoCookieValueFrom(Cookie source);

    /**
     * Gets the timestamp to be used by a {@link TornadoCookieValue}, it could be
     * the epoch representation of the current time or a fixed value based on the
     * timestamp field.
     *
     * @return the epoch timestamp
     */
    private long getTimestamp() {
        if (timestamp == TIMESTAMP_NOW) {
            return new Date().getTime();
        }

        return timestamp;
    }

    /**
     * As its name suggests the Class Builder handles the {@link TornadoCookieCodec}
     * creation process.
     *
     * @param <T>
     *            the generic type that extends from this class
     */
    public abstract static class Builder<T extends Builder<T>> {

        /** The serializer to be used by the {@link TornadoCookieCodec} instance. */
        protected TornadoCookieValueSerializer serializer;

        /** The deserializer to be used by the {@link TornadoCookieCodec} instance. */
        protected TornadoCookieValueDeserializer deserializer;

        /** The timestamp to be used by the {@link TornadoCookieCodec} instance. */
        protected long timestamp;

        /**
         * Instantiates a new {@link TornadoCookieCodec} builder.
         */
        public Builder() {
            this.timestamp = 0;
        }

        /**
         * Set the timestamp to by used by the {@link TornadoCookieCodec} instance.
         *
         * @param timestamp
         *            the epoch timestamp
         * @return the generic type that extends from this class
         */
        public T withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return self();
        }

        /**
         * Builds the {@link TornadoCookieCodec} instance.
         *
         * @return the {@link TornadoCookieCodec} instance
         * @throws InvalidKeyException
         *             when a Invalid secret key was provided to the SignatureHasher in
         *             the {@link TornadoCookieCodec} instance.
         */
        public abstract TornadoCookieCodec build() throws InvalidKeyException;

        /**
         * Replacement method for the <code>this</code> reference.
         *
         * @return the generic type that extends from this class
         */
        protected abstract T self();
    }
}
