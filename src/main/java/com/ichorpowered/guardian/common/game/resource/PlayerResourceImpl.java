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
package com.ichorpowered.guardian.common.game.resource;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.game.GameReference;
import com.ichorpowered.guardian.api.game.resource.PlayerResource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerResourceImpl implements PlayerResource {

    private final Integer maxContainerSize;
    private final Integer maxGroupSize;
    private final Integer minGroupSize;

    private int groupIndex = 0;
    private Multimap<Integer, GameReference<?>> resourceContainer = HashMultimap.create();
    private Map<String, GameReference<?>> referenceContainer = Maps.newHashMap();

    @Inject
    public PlayerResourceImpl(final @Assisted("maxContainerSize") Integer maxContainerSize,
                              final @Assisted("maxGroupSize") Integer maxGroupSize,
                              final @Assisted("minGroupSize") Integer minGroupSize) {
        this.maxContainerSize = maxContainerSize;
        this.maxGroupSize = maxGroupSize;
        this.minGroupSize = minGroupSize;
    }

    @Override
    public @NonNull PlayerResourceImpl add(GameReference<?> reference) {
        if (this.resourceContainer.get(this.groupIndex).size() >= this.maxGroupSize && this.groupIndex >= this.maxGroupSize * this.maxContainerSize) throw new StackOverflowError();

        if (!this.resourceContainer.get(this.groupIndex).isEmpty() && this.resourceContainer.get(this.groupIndex).size() < this.maxGroupSize) {
            this.referenceContainer.put(reference.getGameId(), reference);
            this.resourceContainer.get(this.groupIndex).add(reference);
        } else {
            this.referenceContainer.put(reference.getGameId(), reference);
            this.resourceContainer.put(this.groupIndex++, reference);
        }

        return this;
    }

    @Override
    public @NonNull PlayerResourceImpl add(GameReference<?>[] references) {
        if (this.resourceContainer.get(this.groupIndex).size() >= this.maxGroupSize && this.groupIndex >= this.maxGroupSize * this.maxContainerSize) throw new StackOverflowError();

        List<GameReference<?>> buffer = new ArrayList<>();
        for (GameReference<?> reference : references) {
            if (buffer.size() < this.maxGroupSize) {
                buffer.set(buffer.size(), reference);
            } else {
                this.resourceContainer.putAll(this.groupIndex++, buffer);
                buffer.clear();
            }

            this.referenceContainer.put(reference.getGameId(), reference);
        }

        return this;
    }

    @Override
    public @NonNull Collection<GameReference<?>> get(int group) {
        return this.resourceContainer.get(group);
    }

    @Override
    public @NonNull Optional<Integer> get(GameReference<?> reference) {
        for (Map.Entry<Integer, GameReference<?>> entry : this.resourceContainer.entries()) {
            if (!entry.getValue().equals(reference)) continue;
            return Optional.of(entry.getKey());
        }

        return Optional.empty();
    }

    @Override
    public @NonNull Optional<GameReference<?>> getReference(@NonNull String id) {
        return Optional.ofNullable(this.referenceContainer.get(id));
    }

    @Override
    public @NonNull Collection<Map.Entry<Integer, GameReference<?>>> getAll() {
        return ImmutableList.copyOf(this.resourceContainer.entries());
    }

    @Override
    public @NonNull Boolean remove(GameReference<?> reference) {
        boolean result = false;

        for (Map.Entry<Integer, GameReference<?>> entry : this.resourceContainer.entries()) {
            if (!entry.getValue().equals(reference)) continue;
            result = this.resourceContainer.remove(entry.getKey(), entry.getValue());
            break;
        }

        this.referenceContainer.remove(reference.getGameId());

        return result;
    }

    @Override
    public @NonNull Boolean remove(GameReference<?> reference, int group) {
        this.referenceContainer.remove(reference.getGameId());
        return this.resourceContainer.remove(group, reference);
    }

    @Override
    public @NonNull Collection<GameReference<?>> removeAll(int group) {
        return this.resourceContainer.removeAll(group).stream()
                .map(gameReference -> this.referenceContainer.remove(gameReference.getGameId()))
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        this.referenceContainer.clear();
        this.resourceContainer.clear();
    }

    @Override
    public @NonNull Integer getContainerSize() {
        return this.resourceContainer.size();
    }

    @Override
    public @NonNull Integer getMaxContainerSize() {
        return this.maxContainerSize;
    }

    @Override
    public @NonNull Integer getMaxGroupSize() {
        return this.maxGroupSize;
    }

    @Override
    public @NonNull Integer getMinGroupSize() {
        return this.minGroupSize;
    }

}
