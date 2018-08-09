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
package com.ichorpowered.guardian.common.game.model.value;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.game.model.value.GameValue;
import com.ichorpowered.guardian.api.game.model.value.key.GameKey;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.function.Function;

public class GameValueImpl<E> implements GameValue<E> {

    private final GameKey<E> gameKey;
    private final E defaultElement;

    private E element;

    @Inject
    private GameValueImpl(final @Assisted("gameKey") GameKey gameKey,
                          final @Assisted("defaultElement") Object defaultElement) {
        this.gameKey = (GameKey<E>) gameKey;
        this.defaultElement = (E) defaultElement;
    }

    @Override
    public @NonNull GameKey<E> getGameKey() {
        return this.gameKey;
    }

    @Override
    public @NonNull E get() {
        return this.element == null ? this.defaultElement : this.element;
    }

    @Override
    public @NonNull E getDefault() {
        return this.defaultElement;
    }

    @Override
    public @NonNull Optional<E> getDirect() {
        return Optional.ofNullable(this.element);
    }

    @Override
    public @NonNull GameValue<E> set(final @NonNull E value) {
        this.element = value;
        return this;
    }

    @Override
    public @NonNull GameValue<E> transform(final @NonNull Function<E, E> function) {
        this.element = function.apply(this.get());
        return this;
    }

    @Override
    public boolean isEmpty() {
        return this.element == null;
    }

}
