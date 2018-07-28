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
import com.ichorpowered.guardian.api.game.GameReference;
import com.ichorpowered.guardian.api.game.model.Model;
import com.ichorpowered.guardian.api.game.model.ModelFactories;
import com.ichorpowered.guardian.api.game.model.component.Component;
import com.ichorpowered.guardian.api.game.model.value.Value;
import com.ichorpowered.guardian.api.game.model.value.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelImpl implements Model {

    private final ModelFactories modelFactories;
    private final Component.Factory componentFactory;

    private final String id;
    private final GameReference<?> gameReference;
    private final List<String> defaultComponents = Lists.newArrayList();
    private final Map<String, Component> componentContainer = Maps.newHashMap();

    @Inject
    public ModelImpl(final ModelFactories modelFactories,
                     final Component.Factory componentFactory,
                     final @Assisted String id,
                     final @Assisted GameReference<?> gameReference,
                     final @Assisted("defaultComponents") List<String> defaultComponents) {
        this.modelFactories = modelFactories;
        this.componentFactory = componentFactory;

        this.id = id;
        this.gameReference = gameReference;

        this.componentContainer.putAll(
                defaultComponents
                        .stream()
                        .map(this::createComponent)
                        .collect(Collectors.toMap(Component::getId, component -> component))
        );

        this.defaultComponents.addAll(defaultComponents);
    }

    @Override
    public @NonNull String getId() {
        return this.id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull <T> GameReference<T> getGameReference() {
        return (GameReference<T>) this.gameReference;
    }

    @Override
    public @NonNull <E> List<Value<E>> offer(@NonNull Key<E> key, E element) {
        final List<Value<E>> values = new ArrayList<>();
        this.componentContainer.values().forEach(component -> component.set(key, element).ifPresent(values::add));
        return values;
    }

    @Override
    public @NonNull <E> List<Value<E>> offer(@NonNull Set<String> components, @NonNull Key<E> key, E element) {
        final List<Value<E>> values = new ArrayList<>();
        components.forEach(componentKey -> this.componentContainer.get(componentKey).set(key, element).ifPresent(values::add));
        return values;
    }

    @Override
    public @NonNull <E> List<Value<E>> request(@NonNull Key<E> key) {
        final List<Value<E>> values = new ArrayList<>();
        this.componentContainer.values().forEach(component -> component.get(key).ifPresent(values::add));
        return values;
    }

    @Override
    public @NonNull <E> List<Value<E>> request(@NonNull Set<String> components, @NonNull Key<E> key) {
        final List<Value<E>> values = new ArrayList<>();
        components.forEach(componentKey -> this.componentContainer.get(componentKey).get(key).ifPresent(values::add));
        return values;
    }

    @Override
    public @NonNull <E> Optional<Value<E>> requestFirst(@NonNull Key<E> key) {
        return this.componentContainer.values().stream()
                .map(component -> component.get(key).orElse(null))
                .filter(Objects::nonNull)
                .findFirst();
    }

    @Override
    public @NonNull <E> Optional<Value<E>> requestFirst(@NonNull String component, @NonNull Key<E> key) {
        return this.componentContainer.get(component).get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Component createComponent(@NonNull String id) {
        return this.createComponent(id, Lists.newArrayList());
    }

    @Override
    public @NonNull Component createComponent(@NonNull String id, final List<String> defaultValues) {
        final Component component = this.componentFactory.create(id, this);
        this.componentContainer.put(id, component);

        return component;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Optional<Component> getComponent(@NonNull String id) {
        return Optional.ofNullable(this.componentContainer.get(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Optional<Component> cloneComponent(@NonNull String id, @NonNull Component component) {
        Component newComponent = this.createComponent(id);

        component.keys().stream()
                .map(component::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(value -> newComponent.set((Key<Object>) value.getKey(), value.get()));

        return Optional.ofNullable(newComponent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Optional<Component> removeComponent(@NonNull String id) {
        return Optional.ofNullable(this.componentContainer.remove(id));
    }

    @Override
    public @NonNull List<Component> getDefaultComponents() {
        return ImmutableList.copyOf(this.defaultComponents.stream().map(this.componentContainer::get).collect(Collectors.toList()));
    }

    @Override
    public @NonNull List<Component> getComponents() {
        return ImmutableList.copyOf(this.componentContainer.values());
    }

}
