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
package com.jossemargt.cookietwist.value;

import java.util.Date;

public class CookieValueModel {

    private String name, value, signature;
    private long timestamp; // UNIX epoch timestamp
    private int signatureKeyVersion;

    public CookieValueModel(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.timestamp = builder.timestamp;
        this.signatureKeyVersion = builder.signatureKeyVersion;
        this.signature = builder.signature;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name, value, signature;
        private long timestamp;
        private int signatureKeyVersion;

        private Builder() {
            this.name = "";
            this.value = "";
            this.timestamp = new Date().getTime();
            this.signatureKeyVersion = 0;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withValue(String value) {
            this.value = value;
            return this;
        }

        public Builder withSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Builder withTimestamp(Date timestamp) {
            this.timestamp = timestamp.getTime();
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withSignatureKeyVersion(int version) {
            this.signatureKeyVersion = version;
            return this;
        }

        public CookieValueModel build() {
            return new CookieValueModel(this);
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature
     *            the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the signatureKeyVersion
     */
    public int getSignatureKeyVersion() {
        return signatureKeyVersion;
    }

    /**
     * @param signatureKeyVersion
     *            the signatureKeyVersion to set
     */
    public void setSignatureKeyVersion(int signatureKeyVersion) {
        this.signatureKeyVersion = signatureKeyVersion;
    }
}
