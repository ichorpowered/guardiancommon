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
import com.ichorpowered.guardian.api.game.model.ModelFactories;
import com.ichorpowered.guardian.api.game.model.component.Component;
import com.ichorpowered.guardian.api.game.model.value.Value;
import com.ichorpowered.guardian.api.game.model.value.key.ValueKey;
import com.ichorpowered.guardian.api.game.model.value.store.ValueStores;
import com.ichorpowered.guardian.common.game.model.value.ValueImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public final class ModelFactoriesImpl implements ModelFactories {

    @Inject private ValueImpl.Factory valueFactory;

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<Value<E>> createValue(@NonNull Component component, @NonNull ValueKey<E> valueKey) {
        E element = null;
        if (valueKey.getValueStore().equals(ValueStores.PHYSICAL)) {
            element = component.getModel().getModelConfiguration().getData(component.getModel().getGameReference(), valueKey).orElse(null);
        }

        return Optional.of((Value<E>) this.valueFactory.create(valueKey, element));
    }

}
