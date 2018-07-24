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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.ichorpowered.guardian.api.Guardian;
import com.ichorpowered.guardian.api.game.GameReference;
import com.ichorpowered.guardian.api.game.model.Model;
import com.ichorpowered.guardian.api.game.model.ModelFactories;
import com.ichorpowered.guardian.api.game.model.ModelRegistry;
import com.ichorpowered.guardian.api.game.model.component.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ModelRegistryImpl implements ModelRegistry {

    private final Map<GameReference<?>, Model> modelContainer = Maps.newHashMap();

    @Inject private ModelFactories modelFactories;
    @Inject private Model.Factory modelFactory;

    @Override
    public <T extends Model> @NonNull T create(final String id, final GameReference<?> gameReference) {
        return this.create(id, gameReference, Lists.newArrayList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> @NonNull T create(final String id, final GameReference<?> gameReference, final List<String> defaultComponents) {
        final List<String> components = Guardian.getDefinitionConfiguration().getComponents(id);

        defaultComponents.addAll(components);

        final T model = (T) this.modelFactory.create(id, gameReference, components);
        this.modelContainer.put(gameReference, model);

        return model;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T extends Model> Optional<T> get(final GameReference<?> gameReference) {
        return Optional.ofNullable((T) this.modelContainer.get(gameReference));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T extends Model> Optional<T> remove(final GameReference<?> gameReference) {
        return Optional.ofNullable((T) this.modelContainer.remove(gameReference));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> @NonNull T clone(final String id, final GameReference<?> gameReference, final T otherFrame) {
        T newModel = this.create(id, gameReference,
                otherFrame.getDefaultComponents()
                        .stream()
                        .map(Component::getKey)
                        .collect(Collectors.toList())
        );

        otherFrame.getComponents().forEach(component -> newModel.cloneComponent(component.getKey(), component));

        return newModel;
    }

}
