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

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.detection.DetectionBuilder;
import com.ichorpowered.guardian.api.detection.stage.Stage;
import com.ichorpowered.guardian.api.detection.stage.StageBuilder;
import com.ichorpowered.guardian.api.detection.stage.StageProcess;
import com.ichorpowered.guardian.common.inject.detection.StageReferenceModule;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class StageBuilderImpl implements StageBuilder {

    private final DetectionBuilder detectionBuilder;
    private final Class<? extends Stage> stageType;
    private final List<Class<? extends StageProcess>> processes = Lists.newArrayList();

    private int maximum;

    @Inject
    public StageBuilderImpl(final @Assisted DetectionBuilder detectionBuilder, final @Assisted Class<? extends Stage> stageType) {
        this.detectionBuilder = detectionBuilder;
        this.stageType = stageType;
    }

    @Override
    public @NonNull <T extends StageProcess> StageBuilder add(final @NonNull Class<T> stageClass) {
        this.processes.add(stageClass);
        return this;
    }

    @Override
    public @NonNull StageBuilder max(final int maximum) {
        this.maximum = maximum;
        return this;
    }

    @Override
    public @NonNull DetectionBuilder submit() {
        final Stage stage = Guice.createInjector(new StageReferenceModule(this.processes, this.maximum)).getInstance(this.stageType);

        return this.detectionBuilder.stage(stage);
    }

    // Implementation Factory
    public interface Factory {

        @NonNull StageBuilder create(@NonNull @Assisted DetectionBuilder detectionBuilder,
                                     @NonNull @Assisted Class<? extends Stage> stageType);

    }

}
