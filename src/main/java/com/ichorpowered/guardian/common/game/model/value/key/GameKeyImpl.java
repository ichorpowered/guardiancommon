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
package com.ichorpowered.guardian.common.game.model.value.key;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.game.model.value.key.GameKey;
import com.ichorpowered.guardian.api.game.model.value.store.GameStores;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GameKeyImpl<E> implements GameKey<E> {

    private final String key;
    private final TypeToken<E> elementType;
    private final TypeToken<?> attributeType;
    private final GameStores storeType;

    @Inject
    public GameKeyImpl(final @Assisted String key,
                       final @Assisted("elementType") TypeToken<?> elementType,
                       final @Assisted("attributeType") TypeToken<?> attributeType,
                       final @Assisted GameStores storeType) {
        this.key = key;
        this.elementType = (TypeToken<E>) elementType;
        this.attributeType = attributeType;
        this.storeType = storeType;
    }

    @Override
    public @NonNull String getKey() {
        return this.key;
    }

    @Override
    public @NonNull TypeToken<E> getElementType() {
        return this.elementType;
    }

    @Override
    public @NonNull TypeToken<?> getAttributeType() {
        return this.attributeType;
    }

    @Override
    public @NonNull GameStores getValueStore() {
        return this.storeType;
    }

}
