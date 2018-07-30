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
import com.ichorpowered.guardian.api.game.model.ModelFactories;
import com.ichorpowered.guardian.api.game.model.component.Component;
import com.ichorpowered.guardian.api.game.model.value.Value;
import com.ichorpowered.guardian.api.game.model.value.key.Key;
import com.ichorpowered.guardian.api.game.model.value.store.Stores;
import com.ichorpowered.guardian.common.game.model.value.ValueImpl;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

@Singleton
public final class ModelFactoriesImpl implements ModelFactories {

    @Inject private ValueImpl.Factory valueFactory;

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<Value<E>> createValue(@NonNull Component component, @NonNull Key<E> key) {
        if (key.getValueStore().equals(Stores.PHYSICAL)) {
            E value = null;

            try {
                value = Guardian.getGlobalConfiguration().getModel(component.getModel().getId()).getNode("data", component.getId(), key.getKey()).getValue(key.getElementType());
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }

            return value == null ? Optional.empty() : Optional.of((Value<E>) this.valueFactory.create(key, value));
        }

        return Optional.empty();
    }

    @Override
    public @NonNull <E> Optional<Value<E>> createValue(@NonNull Component component, @NonNull Key<E> key, @NonNull E element) {
        if (key.getValueStore().equals(Stores.PHYSICAL)) {
            return this.createValue(component, key).map(value -> value.set(element));
        }

        if (key.getValueStore().equals(Stores.VIRTUAL)) {
            return Optional.of((Value<E>) this.valueFactory.create(key, element));
        }

        return Optional.empty();
    }

}
