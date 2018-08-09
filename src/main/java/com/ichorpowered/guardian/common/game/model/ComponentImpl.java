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
import com.ichorpowered.guardian.api.game.model.Component;
import com.ichorpowered.guardian.api.game.model.Model;
import com.ichorpowered.guardian.api.game.model.ModelFactories;
import com.ichorpowered.guardian.api.game.model.value.GameValue;
import com.ichorpowered.guardian.api.game.model.value.key.GameKey;
import com.ichorpowered.guardian.api.game.model.value.key.GameKeyRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ComponentImpl implements Component {

    private final ModelFactories modelFactories;
    private final GameKeyRegistry gameKeyRegistry;

    private final String id;
    private final Model model;
    private final List<String> defaultKeys = Lists.newArrayList();
    private final Map<GameKey<?>, GameValue<?>> valueContainer = Maps.newHashMap();

    @Inject
    public ComponentImpl(final ModelFactories modelFactories,
                         final GameKeyRegistry gameKeyRegistry,
                         final @Assisted String id,
                         final @Assisted Model model) {
        this.modelFactories = modelFactories;
        this.gameKeyRegistry = gameKeyRegistry;

        this.id = id;
        this.model = model;
    }

    @Override
    public @NonNull String getId() {
        return this.id;
    }

    @Override
    public @NonNull Model getModel() {
        return this.model;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<GameValue<E>> get(final @NonNull GameKey<E> gameKey) {
        if (!this.valueContainer.containsKey(gameKey)) return this.modelFactories.createValue(this, gameKey).map(value -> (GameValue<E>) this.valueContainer.put(gameKey, value));
        return Optional.ofNullable((GameValue<E>) this.valueContainer.get(gameKey));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<GameValue<E>> set(final @NonNull GameKey<E> gameKey, final @NonNull E element) {
        if (!this.valueContainer.containsKey(gameKey)) return this.modelFactories.createValue(this, gameKey, element).map(value -> (GameValue<E>) this.valueContainer.put(gameKey, value));
        final GameValue<E> gameValue = (GameValue<E>) this.valueContainer.get(gameKey);

        return Optional.of(gameValue.set(element));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <E> Optional<GameValue<E>> remove(final @NonNull GameKey<E> gameKey) {
        return Optional.ofNullable((GameValue<E>) this.valueContainer.remove(gameKey));
    }

    @Override
    public @NonNull ImmutableList<String> defaultKeys() {
        return ImmutableList.copyOf(this.defaultKeys);
    }

    @Override
    public @NonNull ImmutableList<GameKey<?>> keys() {
        return ImmutableList.copyOf(this.valueContainer.keySet());
    }

}
