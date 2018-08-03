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
package com.ichorpowered.guardian.common.inject.detection;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.ichorpowered.guardian.api.detection.stage.StageProcess;
import net.kyori.violet.AbstractModule;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class StageReferenceModule extends AbstractModule {

    private final List<Class<? extends StageProcess>> processes;
    private final int maximum;

    public StageReferenceModule(final List<Class<? extends StageProcess>> processes, final int maximum) {
        this.processes = processes;
        this.maximum = maximum;
    }

    @Override
    public void configure() {
        this.bind(Integer.class).toInstance(this.maximum);
    }

    @Provides
    @Inject
    public List<StageProcess> getStageProcesses(final @NonNull Injector injector) {
        return this.processes.stream().map(injector::getInstance).collect(Collectors.toList());
    }

}
