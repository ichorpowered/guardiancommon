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
import com.ichorpowered.guardian.api.game.model.Component;
import com.ichorpowered.guardian.api.game.model.Model;
import com.ichorpowered.guardian.api.game.model.value.GameValue;
import com.ichorpowered.guardian.api.game.model.value.key.GameKey;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelImpl implements Model {

    private final Component.Factory componentFactory;

    private final String id;
    private final GameReference<?> gameReference;
    private final List<String> defaultComponents = Lists.newArrayList();
    private final Map<String, Component> componentContainer = Maps.newHashMap();

    @Inject
    public ModelImpl(final Component.Factory componentFactory,
                     final @Assisted String id,
                     final @Assisted GameReference<?> gameReference,
                     final @Assisted("defaultComponents") List<String> defaultComponents) {
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
    public @NonNull <E> List<GameValue<E>> offer(final @NonNull GameKey<E> gameKey, E element) {
        final List<GameValue<E>> gameValues = new ArrayList<>();
        this.componentContainer.values().forEach(component -> component.set(gameKey, element).ifPresent(gameValues::add));
        return gameValues;
    }

    @Override
    public @NonNull <E> List<GameValue<E>> offer(final @NonNull Set<String> components, final @NonNull GameKey<E> gameKey, E element) {
        final List<GameValue<E>> gameValues = new ArrayList<>();
        components.forEach(componentKey -> this.componentContainer.get(componentKey).set(gameKey, element).ifPresent(gameValues::add));
        return gameValues;
    }

    @Override
    public @NonNull <E> List<GameValue<E>> request(final @NonNull GameKey<E> gameKey) {
        final List<GameValue<E>> gameValues = new ArrayList<>();
        this.componentContainer.values().forEach(component -> component.get(gameKey).ifPresent(gameValues::add));
        return gameValues;
    }

    @Override
    public @NonNull <E> List<GameValue<E>> request(final @NonNull Set<String> components, final @NonNull GameKey<E> gameKey) {
        final List<GameValue<E>> gameValues = new ArrayList<>();
        components.forEach(componentKey -> this.componentContainer.get(componentKey).get(gameKey).ifPresent(gameValues::add));
        return gameValues;
    }

    @Override
    public @NonNull <E> Optional<GameValue<E>> requestFirst(final @NonNull GameKey<E> gameKey) {
        return this.componentContainer.values().stream()
                .map(component -> component.get(gameKey).orElse(null))
                .filter(Objects::nonNull)
                .findFirst();
    }

    @Override
    public @NonNull <E> Optional<GameValue<E>> requestFirst(final @NonNull String component, final @NonNull GameKey<E> gameKey) {
        return this.componentContainer.get(component).get(gameKey);
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Component createComponent(final @NonNull String id) {
        final Component component = this.componentFactory.create(id, this);
        this.componentContainer.put(id, component);

        return component;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Optional<Component> getComponent(final @NonNull String id) {
        return Optional.ofNullable(this.componentContainer.get(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Component cloneComponent(final @NonNull String id, final @NonNull Component component) {
        Component newComponent = this.createComponent(id);

        component.keys().stream()
                .map(component::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(value -> newComponent.set((GameKey<Object>) value.getGameKey(), value.get()));

        return newComponent;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NonNull Optional<Component> removeComponent(final @NonNull String id) {
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
