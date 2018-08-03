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
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ichorpowered.guardian.api.Guardian;
import com.ichorpowered.guardian.api.game.GameReference;
import com.ichorpowered.guardian.api.game.model.Component;
import com.ichorpowered.guardian.api.game.model.Model;
import com.ichorpowered.guardian.api.game.model.ModelRegistry;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public final class ModelRegistryImpl implements ModelRegistry {

    private final Map<String, Model> modelContainer = Maps.newHashMap();

    @Inject private Model.Factory modelFactory;

    @Override
    public <T extends Model> @NonNull T create(final @NonNull String id, final @NonNull GameReference<?> gameReference) {
        return this.create(id, gameReference, Lists.newArrayList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> @NonNull T create(final @NonNull String id, final @NonNull GameReference<?> gameReference,
                                               final @NonNull List<String> defaultComponents) {
        final List<String> components = Lists.newArrayList();
        try {
            components.addAll(Guardian.getGlobalConfiguration().getModel(id).getNode("components").getList(TypeToken.of(String.class)));
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }

        defaultComponents.addAll(components);

        final T model = (T) this.modelFactory.create(id, gameReference, components);
        this.modelContainer.put(gameReference.getGameId(), model);

        return model;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T extends Model> Optional<T> get(final @NonNull GameReference<?> gameReference) {
        return Optional.ofNullable((T) this.modelContainer.get(gameReference.getGameId()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T extends Model> Optional<T> remove(final @NonNull GameReference<?> gameReference) {
        return Optional.ofNullable((T) this.modelContainer.remove(gameReference.getGameId()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Model> @NonNull T clone(final @NonNull String id, final @NonNull GameReference<?> gameReference, final @NonNull T otherFrame) {
        T newModel = this.create(id, gameReference,
                otherFrame.getDefaultComponents()
                        .stream()
                        .map(Component::getId)
                        .collect(Collectors.toList())
        );

        otherFrame.getComponents().forEach(component -> newModel.cloneComponent(id, component));

        return newModel;
    }

}
