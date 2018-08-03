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
package com.ichorpowered.guardian.common.game;

import com.google.common.base.MoreObjects;
import com.ichorpowered.guardian.api.game.GameReference;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.function.Function;

public class GameReferenceImpl<T> implements GameReference<T> {

    private final String gameId;
    private final Class<T> gameClass;

    private Function<String, T> valueFunction;

    @SuppressWarnings("unchecked")
    public GameReferenceImpl(final String id,
                             final Class<T> valueClass,
                             final Function<String, T> valueFunction) {
        this.gameId = id;
        this.valueFunction = valueFunction;
        this.gameClass = valueClass;
    }

    @Override
    public @NonNull T get() {
        return this.valueFunction.apply(this.gameId);
    }

    @Override
    public @NonNull Function<String, T> getFunction() {
        return this.valueFunction;
    }

    @Override
    public @NonNull String getGameId() {
        return this.gameId;
    }

    @Override
    public @NonNull Class<T> getGameClass() {
        return this.gameClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.gameId, this.gameClass);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (!(other instanceof GameReferenceImpl<?>)) return false;
        final GameReference<?> that = (GameReference<?>) other;
        return Objects.equals(this.gameId, that.getGameId())
                && Objects.equals(this.gameClass, that.getGameClass());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("referenceId", this.gameId)
                .add("referenceClass", this.gameClass)
                .toString();
    }

}
