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
import com.ichorpowered.guardian.api.game.model.value.key.GameKey;
import com.ichorpowered.guardian.api.game.model.value.key.GameKeyRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.Optional;

@Singleton
public class GameKeyRegistryImpl implements GameKeyRegistry {

    private final Map<String, GameKey<?>> valueKeys = Maps.newHashMap();

    @Override
    public @NonNull Optional<GameKey<?>> get(final @NonNull String key) {
        return Optional.ofNullable(this.valueKeys.get(key));
    }

    @Override
    public @NonNull <E> Optional<GameKey<E>> get(final @NonNull String id, @NonNull TypeToken<E> typeToken) {
        final GameKey<?> gameKey = this.valueKeys.get(id);

        if (!gameKey.getElementType().equals(typeToken)) return Optional.empty();
        return Optional.ofNullable((GameKey<E>) gameKey);
    }

    @Override
    public @NonNull GameKeyRegistry register(final @NonNull GameKey<?> gameKey) {
        this.valueKeys.put(gameKey.getKey(), gameKey);
        return this;
    }

}
