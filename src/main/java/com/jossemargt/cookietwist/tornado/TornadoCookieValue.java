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
package com.jossemargt.cookietwist.tornado;

import java.util.Date;

/**
 * The Class TornadoCookieValue is plain Java representation of a Tornado cookie
 * value string.
 */
public final class TornadoCookieValue {

    /** The Cookie name. */
    private String name;

    /** The Cookie plain value string. */
    private String value;

    /** The Signed Cookie signature string. */
    private String signature;

    /** The UNIX epoch timestamp. */
    private long timestamp;

    /** The signature key version number for Tornado Signed Value V2. */
    private int signatureKeyVersion;

    /**
     * Instantiates a new cookie model from its builder.
     *
     * @param builder
     *            the TornadoCookieValueBuilder
     */
    private TornadoCookieValue(TornadoCookieValueBuilder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.timestamp = builder.timestamp;
        this.signatureKeyVersion = builder.signatureKeyVersion;
        this.signature = builder.signature;
    }

    /**
     * Instantiate a TornadoCookieValueBuilder.
     *
     * @return the TornadoCookieValueBuilder
     */
    public static TornadoCookieValueBuilder builder() {
        return new TornadoCookieValueBuilder();
    }

    /**
     * Instantiate a TornadoCookieValueBuilder with the provided
     * TornadoCookieValue's fields as initial values.
     *
     * @param cookieValue
     *            the TornadoCookieValue instance from where a new
     *            TornadoCookieValueBuilder will take its fields.
     * @return the TornadoCookieValueBuilder
     */
    public static TornadoCookieValueBuilder builderFrom(TornadoCookieValue cookieValue) {
        return new TornadoCookieValueBuilder(cookieValue);
    }

    /**
     * The CookieModel Builder class facilitates Tornado secure value model creation
     * regardless the signature and serialization version being used.
     */
    public static final class TornadoCookieValueBuilder {

        /** The Cookie name. */
        private String name;

        /** The Cookie plain value string. */
        private String value;

        /** The Signed Cookie signature string. */
        private String signature;

        /** The UNIX epoch timestamp. */
        private long timestamp;

        /** The signature key version number for Tornado Signed Value V2. */
        private int signatureKeyVersion;

        /**
         * Instantiates a new builder with the TornadoCookieValue initial field values.
         */
        private TornadoCookieValueBuilder() {
            this.name = "";
            this.value = "";
            this.timestamp = new Date().getTime();
            this.signatureKeyVersion = 0;
        }

        /**
         * Instantiates a new builder with provided TornadoCookieValue's fields as
         * initial values.
         *
         * @param cookieValue
         *            the TornadoCookieValue instance from where a new builder will take
         *            its fields.
         */
        private TornadoCookieValueBuilder(TornadoCookieValue cookieValue) {
            super();
            this.name = cookieValue.getName();
            this.value = cookieValue.getValue();
            this.timestamp = cookieValue.getTimestamp();
            this.signature = cookieValue.getSignature();
            this.signatureKeyVersion = cookieValue.getSignatureKeyVersion();
        }

        /**
         * Sets the CookieModel name.
         *
         * @param name
         *            the CookieModel name string.
         * @return the builder
         */
        public TornadoCookieValueBuilder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the CookieModel plain value string.
         *
         * @param value
         *            the CookieModel plain value string
         * @return the builder
         */
        public TornadoCookieValueBuilder withValue(String value) {
            this.value = value;
            return this;
        }

        /**
         * Sets the CookieModel signature.
         *
         * @param signature
         *            the CookieModel signature string
         * @return the builder
         */
        public TornadoCookieValueBuilder withSignature(String signature) {
            this.signature = signature;
            return this;
        }

        /**
         * Sets the Cookie UNIX epoch timestamp.
         *
         * @param timestamp
         *            a Date instance that holds the UNIX epoch timestamp
         * @return the builder
         *
         * @deprecated the TornadoCookieValue will only handle epoch UNIX timestamp as a
         *             long value since version 0.2.0
         */
        @Deprecated
        public TornadoCookieValueBuilder withTimestamp(Date timestamp) {
            this.timestamp = timestamp.getTime();
            return this;
        }

        /**
         * Sets the Cookie UNIX epoch timestamp.
         *
         * @param timestamp
         *            the UNIX epoch timestamp
         * @return the builder
         */
        public TornadoCookieValueBuilder withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Sets the signature key version number. It is only needed for Tornado signed
         * values version 2.
         *
         * @param version
         *            the signature key version number
         * @return the builder
         */
        public TornadoCookieValueBuilder withSignatureKeyVersion(int version) {
            this.signatureKeyVersion = version;
            return this;
        }

        /**
         * Instanteates a new CookieModel.
         *
         * @return the cookie model
         */
        public TornadoCookieValue build() {
            return new TornadoCookieValue(this);
        }
    }

    /**
     * Gets the name string.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the plain value string.
     *
     * @return the plain value string
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the signature string.
     *
     * @return the signature string
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Gets the UNIX epoch timestamp.
     *
     * @return the UNIX epoch timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the signature key version number.
     *
     * @return the signatureKeyVersion
     */
    public int getSignatureKeyVersion() {
        return signatureKeyVersion;
    }
}
