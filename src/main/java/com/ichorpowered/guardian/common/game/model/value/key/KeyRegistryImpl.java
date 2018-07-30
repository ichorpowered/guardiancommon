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

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.inject.Singleton;
import com.ichorpowered.guardian.api.game.model.value.key.Key;
import com.ichorpowered.guardian.api.game.model.value.key.KeyRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Optional;

@Singleton
public class KeyRegistryImpl implements KeyRegistry {

    private final Map<String, Key<?>> valueKeys = Maps.newHashMap();

    @Override
    public @NonNull Optional<Key<?>> get(@NonNull String key) {
        return Optional.ofNullable(this.valueKeys.get(key));
    }

    @Override
    public @NonNull <E> Optional<Key<E>> get(@NonNull String id, @NonNull TypeToken<E> typeToken) {
        final Key<?> key = this.valueKeys.get(id);

        if (!key.getElementType().equals(typeToken)) return Optional.empty();
        return Optional.ofNullable((Key<E>) key);
    }

    @Override
    public @NonNull KeyRegistry register(@NonNull Key<?> key) {
        this.valueKeys.put(key.getKey(), key);
        return this;
    }

}
