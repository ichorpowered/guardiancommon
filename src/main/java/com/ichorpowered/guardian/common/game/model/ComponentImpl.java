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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.game.model.Model;
import com.ichorpowered.guardian.api.game.model.ModelFactories;
import com.ichorpowered.guardian.api.game.model.component.Component;
import com.ichorpowered.guardian.api.game.model.value.Value;
import com.ichorpowered.guardian.api.game.model.value.key.ValueKey;
import com.ichorpowered.guardian.api.game.model.value.key.ValueKeyRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ComponentImpl implements Component {

    private final ModelFactories modelFactories;
    private final ValueKeyRegistry valueKeyRegistry;

    private final String key;
    private final Model model;
    private final List<String> defaultKeys = Lists.newArrayList();
    private final Map<ValueKey<?>, Value<?>> valueContainer = Maps.newHashMap();

    @Inject
    public ComponentImpl(final ModelFactories modelFactories,
                         final ValueKeyRegistry valueKeyRegistry,
                         final @Assisted String key,
                         final @Assisted Model model,
                         final @Assisted List<String> defaultValues) {
        this.modelFactories = modelFactories;
        this.valueKeyRegistry = valueKeyRegistry;
        this.key = key;
        this.model = model;

        defaultKeys.forEach(defaultKey -> this.modelFactories.createValue(this, this.valueKeyRegistry.get(defaultKey)).map(value ->
                this.valueContainer.put(this.valueKeyRegistry.get(defaultKey), value)));

        this.defaultKeys.addAll(defaultValues);
    }

    @Override
    public @NonNull String getKey() {
        return this.key;
    }

    @Override
    public @NonNull Model getModel() {
        return this.model;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<Value<E>> getValue(@NonNull ValueKey<E> valueKey) {
        return Optional.ofNullable((Value<E>) this.valueContainer.get(valueKey));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<Value<E>> setValue(@NonNull ValueKey<E> valueKey, E element) {
        final Value<E> value = (Value<E>) this.valueContainer.get(valueKey);
        if (!this.valueContainer.containsKey(valueKey)) return Optional.empty();

        return Optional.of(value.set(element));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<Value<E>> removeValue(@NonNull ValueKey<E> valueKey) {
        return Optional.ofNullable((Value<E>) this.valueContainer.remove(valueKey));
    }

    @Override
    public @NonNull ImmutableList<String> defaultKeys() {
        return ImmutableList.copyOf(this.defaultKeys);
    }

    @Override
    public @NonNull ImmutableList<ValueKey<?>> keys() {
        return ImmutableList.copyOf(this.valueContainer.keySet());
    }

}
