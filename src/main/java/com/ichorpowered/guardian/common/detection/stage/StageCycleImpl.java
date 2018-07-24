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
package com.ichorpowered.guardian.common.detection.stage;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.detection.stage.Stage;
import com.ichorpowered.guardian.api.detection.stage.StageCycle;
import com.ichorpowered.guardian.api.detection.stage.StageProcess;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class StageCycleImpl implements StageCycle {

    private final List<Stage<?>> stages;

    private Iterator<Stage<?>> stageIterator;
    private Iterator<? extends StageProcess> stageProcessIterator;

    private Stage<?> currentStage;
    private StageProcess currentStageProcess;

    @Inject
    public StageCycleImpl(final @Assisted List<Stage<?>> stages) {
        this.stages = stages;
    }

    @Override
    public @NonNull <T extends StageProcess> Optional<Stage<T>> getStage() {
        return Optional.ofNullable((Stage<T>) this.currentStage);
    }

    @Override
    public @NonNull <T extends StageProcess> Optional<T> getStageProcess() {
        return Optional.ofNullable((T) this.currentStageProcess);
    }

    @Override
    public @NonNull int size() {
        if (this.currentStage == null) return 0;
        return this.stages.get(this.stages.indexOf(this.currentStage)).getSize();
    }

    @Override
    public @NonNull int sizeFor(@NonNull Stage<?> stage) {
        return this.stages.get(this.stages.indexOf(stage)).getSize();
    }

    @Override
    public @NonNull int totalSize() {
        return this.stages.size();
    }

    @Override
    public @NonNull boolean hasNext() {
        if (this.stageIterator == null || this.stageProcessIterator == null) return true;
        return this.stageIterator.hasNext() || this.stageProcessIterator.hasNext();
    }

    @Override
    public @NonNull boolean nextStage() {
        if (this.stageIterator == null) this.stageIterator = this.stages.iterator();

        if (this.stageIterator.hasNext()) {
            // Set next stage.
            this.currentStage = this.stageIterator.next();

            // Set stage process iterator.
            this.stageProcessIterator = this.currentStage.iterator();

            if (this.stageProcessIterator.hasNext()) {
                // Set next stage process.
                this.currentStageProcess = this.stageProcessIterator.next();

                return true;
            } else {
                return this.nextStage();
            }
        }

        return false;
    }

    @Override
    public @NonNull boolean next() {
        if (this.stageIterator == null) this.stageIterator = this.stages.iterator();

        if (this.stageProcessIterator != null && this.stageProcessIterator.hasNext()) {
            // Set next stage process.
            this.currentStageProcess = this.stageProcessIterator.next();

            return true;
        } else if (this.stageIterator.hasNext()) {
            // Set next stage.
            this.currentStage = this.stageIterator.next();

            // Set stage process iterator.
            this.stageProcessIterator = this.currentStage.iterator();

            // Can proceed to running the process iterator.
            return this.next();
        }

        return false;
    }

}
