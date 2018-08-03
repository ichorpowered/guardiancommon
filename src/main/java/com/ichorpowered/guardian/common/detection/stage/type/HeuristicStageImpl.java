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
package com.ichorpowered.guardian.common.detection.stage.type;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.ichorpowered.guardian.api.detection.stage.Stage;
import com.ichorpowered.guardian.api.detection.stage.StageProcess;
import com.ichorpowered.guardian.api.detection.stage.process.Heuristic;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class HeuristicStageImpl implements Stage<Heuristic> {

    private final Map<Class<? extends Heuristic>, Heuristic> processes = Maps.newHashMap();
    private final int maximum;

    @Inject
    public HeuristicStageImpl(final List<StageProcess> processes,
                              final int maximum) {
        processes.forEach(stageProcess -> this.processes.put((Class<Heuristic>) stageProcess.getClass(), (Heuristic) stageProcess));
        this.maximum = maximum;
    }

    @Override
    public @NonNull Optional<Heuristic> getProcess(final @NonNull Class<? extends Heuristic> stageProcessType) {
        return Optional.ofNullable(this.processes.get(stageProcessType));
    }

    @Override
    public @NonNull Stream<Heuristic> getProcesses() {
        return this.processes.values().stream();
    }

    @Override
    public int getMaximum() {
        return this.maximum;
    }

    @Override
    public int getSize() {
        return this.processes.size();
    }

    @Override
    public Iterator<Heuristic> iterator() {
        return this.processes.values().iterator();
    }

}
