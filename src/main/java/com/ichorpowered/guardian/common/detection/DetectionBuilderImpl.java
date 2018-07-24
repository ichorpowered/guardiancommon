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
package com.ichorpowered.guardian.common.detection;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.detection.Detection;
import com.ichorpowered.guardian.api.detection.DetectionBuilder;
import com.ichorpowered.guardian.api.detection.DetectionController;
import com.ichorpowered.guardian.api.detection.stage.Stage;
import com.ichorpowered.guardian.api.detection.stage.StageBuilder;
import com.ichorpowered.guardian.api.detection.stage.StageCycle;
import com.ichorpowered.guardian.common.detection.stage.StageBuilderImpl;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class DetectionBuilderImpl implements DetectionBuilder {

    private final StageCycle.Factory stageCycleFactory;
    private final Detection.Factory detectionFactory;
    private final StageBuilderImpl.Factory stageBuilderFactory;
    private final DetectionControllerImpl detectionController;
    private final String detectionId;

    private final List<Stage<?>> stages = Lists.newArrayList();

    @Inject
    public DetectionBuilderImpl(final StageCycle.Factory stageCycleFactory,
                                final Detection.Factory detectionFactory,
                                final StageBuilderImpl.Factory stageBuilderFactory,
                                final @Assisted DetectionController detectionController,
                                final @Assisted String detectionId) {
        this.stageCycleFactory = stageCycleFactory;
        this.detectionFactory = detectionFactory;
        this.stageBuilderFactory = stageBuilderFactory;
        this.detectionController = (DetectionControllerImpl) detectionController;
        this.detectionId = detectionId;
    }


    @Override
    public @NonNull StageBuilder stage(@NonNull Class<? extends Stage<?>> stageType) {
        return this.stageBuilderFactory.create(this, stageType);
    }

    @Override
    public @NonNull DetectionBuilder stage(@NonNull Stage<?> stage) {
        this.stages.add(stage);
        return this;
    }

    @Override
    public @NonNull DetectionController register(@NonNull String name, @NonNull Object plugin) {
        final StageCycle stageCycle = this.stageCycleFactory.create(this.stages);

        this.detectionController.setDetection(this.detectionFactory.create(this.detectionId, name, stageCycle, plugin));
        return this.detectionController;
    }

    // Implementation Factory
    public interface Factory {

        @NonNull DetectionBuilder create(@NonNull @Assisted DetectionController detectionController,
                                         @NonNull @Assisted String detectionId);

    }

}
