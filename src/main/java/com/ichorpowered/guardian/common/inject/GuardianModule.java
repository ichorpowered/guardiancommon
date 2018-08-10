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
package com.ichorpowered.guardian.common.inject;

import com.google.inject.PrivateModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.ichorpowered.guardian.api.Guardian;
import com.ichorpowered.guardian.api.GuardianPlatform;
import com.ichorpowered.guardian.common.inject.detection.DetectionModule;
import com.ichorpowered.guardian.common.inject.game.ModelModule;
import com.ichorpowered.guardian.common.inject.game.ResourceModule;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GuardianModule extends PrivateModule {

    private final GuardianPlatform platform;

    public GuardianModule(final GuardianPlatform platform) {
        this.platform = platform;
    }

    @Override
    protected void configure() {
        this.bindAndExpose(GuardianPlatform.class).toInstance(this.platform);

        this.install(new ModelModule());
        this.install(new ResourceModule());
        this.install(new DetectionModule());

        this.requestStaticInjection(Guardian.class);
    }

    public <T> @NonNull AnnotatedBindingBuilder<T> bindAndExpose(final @NonNull Class<T> type) {
        this.expose(type);
        return this.bind(type);
    }

}
