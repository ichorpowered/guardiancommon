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
package com.ichorpowered.guardian.common.game.model;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ichorpowered.guardian.api.Guardian;
import com.ichorpowered.guardian.api.game.model.Component;
import com.ichorpowered.guardian.api.game.model.ModelFactories;
import com.ichorpowered.guardian.api.game.model.value.GameValue;
import com.ichorpowered.guardian.api.game.model.value.key.GameKey;
import com.ichorpowered.guardian.api.game.model.value.store.GameStores;
import com.ichorpowered.guardian.common.game.model.value.GameValueImpl;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

@Singleton
public final class ModelFactoriesImpl implements ModelFactories {

    @Inject private GameValueImpl.Factory valueFactory;

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<GameValue<E>> createValue(final @NonNull Component component, final @NonNull GameKey<E> gameKey) {
        if (gameKey.getValueStore().equals(GameStores.PHYSICAL)) {
            E value = null;

            try {
                value = Guardian.getGlobalConfiguration().getModel(component.getModel().getId()).getNode("data", component.getId(), gameKey.getKey()).getValue(gameKey.getElementType());
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }

            return value == null ? Optional.empty() : Optional.of((GameValue<E>) this.valueFactory.create(gameKey, value));
        }

        return Optional.empty();
    }

    @Override
    public @NonNull <E> Optional<GameValue<E>> createValue(final @NonNull Component component, final @NonNull GameKey<E> gameKey, final @NonNull E element) {
        if (gameKey.getValueStore().equals(GameStores.PHYSICAL)) {
            return this.createValue(component, gameKey).map(value -> value.set(element));
        }

        if (gameKey.getValueStore().equals(GameStores.VIRTUAL)) {
            return Optional.of((GameValue<E>) this.valueFactory.create(gameKey, element));
        }

        return Optional.empty();
    }

}
