/*
 * MIT License
 *
 * Copyright (c) 2017 Connor Hartley
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.ichorpowered.guardian.common.util;

import java.util.Objects;

public class Ordered<E> {

    private final long index;

    private E element;

    public Ordered(final long index) {
        this(index, null);
    }

    public Ordered(final long index, final E element) {
        this.index = index;
        this.element = element;
    }

    public Ordered<E> next(final E element) {
        return new Ordered<>(this.index + 1, element);
    }

    public Ordered<E> nextEmpty() {
        return new Ordered<>(this.index + 1);
    }

    public long getNext() {
        return this.index + 1;
    }

    public long getIndex() {
        return this.index;
    }

    public E getElement() {
        return this.element;
    }

    public void setElement(final E element) {
        this.element = element;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.index, this.element);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ordered<?>)) return false;
        final Ordered<?> that = (Ordered<?>) obj;
        return Objects.equals(this.index, that.index)
                && Objects.equals(this.element, that.element);
    }

}
