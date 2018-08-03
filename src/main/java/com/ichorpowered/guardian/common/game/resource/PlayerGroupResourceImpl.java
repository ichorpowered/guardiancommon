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
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.game.GameReference;
import com.ichorpowered.guardian.api.game.resource.PlayerGroupResource;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class PlayerGroupResourceImpl implements PlayerGroupResource {

    private final Multimap<Integer, GameReference<?>> groupContainer = HashMultimap.create();
    private final int maxContainerSize;
    private final int minGroupSize;

    @Inject
    public PlayerGroupResourceImpl(final @Assisted("maxContainerSize") int maxContainerSize,
                                   final @Assisted("minGroupSize") int minGroupSize) {
        this.maxContainerSize = maxContainerSize;
        this.minGroupSize = minGroupSize;
    }

    @Override
    public @NonNull PlayerGroupResource balance() {
        final Multimap<Integer, GameReference<?>> copyGroupContainer = HashMultimap.create(this.groupContainer);
        this.groupContainer.clear();

        copyGroupContainer.values().forEach(this::add);
        return this;
    }

    @Override
    public @NonNull PlayerGroupResource add(@NonNull GameReference<?> gameReference) {
        final int containerSize = this.groupContainer.keySet().size();
        final int totalSize = this.groupContainer.size();

        for (int i = 0; i < this.maxContainerSize; i++) {
            final double groupSize = this.groupContainer.get(i).size();

            if (groupSize < this.minGroupSize) {
                this.groupContainer.put(i, gameReference);
                break;
            }

            if (containerSize < this.maxContainerSize) {
                this.groupContainer.put(containerSize + 1, gameReference);
                break;
            } else if ((totalSize / containerSize) >= groupSize) {
                this.groupContainer.put(i, gameReference);
                break;
            }
        }

        return this;
    }

    @Override
    public @NonNull PlayerGroupResource add(@NonNull GameReference<?>... gameReferences) {
        for (GameReference<?> reference : gameReferences) {
            this.add(reference);
        }

        return this;
    }

    @Override
    public @NonNull Optional<Map.Entry<Integer, GameReference<?>>> remove(@NonNull GameReference<?> gameReference) {
        final Optional<Map.Entry<Integer, GameReference<?>>> result = this.groupContainer.entries().stream()
                .filter(entry -> entry.getValue().equals(gameReference))
                .findFirst();

        result.ifPresent(entry -> this.groupContainer.remove(entry.getKey(), entry.getValue()));
        return result;
    }

    @Override
    public @NonNull Collection<GameReference<?>> removeGroup(int group) {
        return this.groupContainer.removeAll(group);
    }

    @Override
    public @NonNull Optional<Map.Entry<Integer, GameReference<?>>> get(@NonNull GameReference<?> gameReference) {
        return this.groupContainer.entries().stream()
                .filter(entry -> entry.getValue().equals(gameReference))
                .findFirst();
    }

    @Override
    public @NonNull Collection<GameReference<?>> getGroup(int group) {
        return this.groupContainer.get(group);
    }

    @Override
    public void clear() {
        this.groupContainer.clear();
    }

    @Override
    public Iterator<Map.Entry<Integer, GameReference<?>>> iterator() {
        return this.groupContainer.entries().iterator();
    }

}
