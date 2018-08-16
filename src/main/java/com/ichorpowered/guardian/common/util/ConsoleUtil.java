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

import static org.slf4j.helpers.MessageFormatter.arrayFormat;

import org.fusesource.jansi.Ansi;

public final class ConsoleUtil {

    private final String message;

    private ConsoleUtil(final String message) {
        this.message = message;
    }

    public static String of(final String message, final String... replace) {
        return builder().add(message, replace).buildAndGet();
    }

    public static ConsoleUtil append(final String message, final String... replace) {
        return builder().add(message, replace).build();
    }

    public static String of(final Ansi.Color color, final boolean bright, final String message, final String... replace) {
        return builder(color, bright).add(message, replace).buildAndGet();
    }

    public static ConsoleUtil append(final Ansi.Color color, final boolean bright, final String message, final String... replace) {
        return builder(color, bright).add(message, replace).build();
    }

    public static Builder builder() {
        return builder(Ansi.Color.WHITE, true);
    }

    public static Builder builder(final Ansi.Color color, final boolean bright) {
        return new Builder(color, bright);
    }

    public static class Builder {

        private final Ansi.Color def;
        private final boolean bright;

        private String message = "";

        private Builder(final Ansi.Color def, boolean bright) {
            this.def = def;
            this.bright = bright;
        }

        public Builder add(final String message, final String... replace) {
            final Ansi ansi = Ansi.ansi();

            if (this.bright) ansi.reset().fg(this.def).bold();
            else ansi.fg(this.def).boldOff();

            this.message += ansi.a(arrayFormat(message, replace).getMessage()).reset().toString();
            return this;
        }

        public Builder add(final Ansi.Color color, final String message, final String... replace) {
            final Ansi ansi = Ansi.ansi();

            if (this.bright) ansi.reset().fg(color).bold();
            else ansi.fg(color).boldOff();

            this.message += ansi.a(arrayFormat(message, replace).getMessage()).reset().toString();
            return this;
        }

        public ConsoleUtil build() {
            return new ConsoleUtil(this.message);
        }

        public String buildAndGet() {
            return new ConsoleUtil(this.message).message;
        }

    }
}
