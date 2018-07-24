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

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.ichorpowered.guardian.api.detection.Detection;
import com.ichorpowered.guardian.api.detection.DetectionBuilder;
import com.ichorpowered.guardian.api.detection.DetectionController;
import com.ichorpowered.guardian.api.detection.stage.StageBuilder;
import com.ichorpowered.guardian.api.detection.stage.StageCycle;
import com.ichorpowered.guardian.common.detection.DetectionBuilderImpl;
import com.ichorpowered.guardian.common.detection.DetectionControllerImpl;
import com.ichorpowered.guardian.common.detection.DetectionImpl;
import com.ichorpowered.guardian.common.detection.stage.StageBuilderImpl;
import com.ichorpowered.guardian.common.detection.stage.StageCycleImpl;
import net.kyori.violet.AbstractModule;

public class DetectionModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(DetectionController.class).to(DetectionControllerImpl.class);

        this.install(new FactoryModuleBuilder().implement(Detection.class, DetectionImpl.class).build(Detection.Factory.class));
        this.install(new FactoryModuleBuilder().implement(DetectionBuilder.class, DetectionBuilderImpl.class).build(DetectionBuilderImpl.Factory.class));
        this.install(new FactoryModuleBuilder().implement(StageBuilder.class, StageBuilderImpl.class).build(StageBuilderImpl.Factory.class));

        this.install(new FactoryModuleBuilder().implement(StageCycle.class, StageCycleImpl.class).build(StageCycle.Factory.class));
    }

}
