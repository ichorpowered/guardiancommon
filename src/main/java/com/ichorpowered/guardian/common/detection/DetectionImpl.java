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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ichorpowered.guardian.api.detection.Detection;
import com.ichorpowered.guardian.api.detection.stage.StageCycle;
import org.checkerframework.checker.nullness.qual.NonNull;

public class DetectionImpl implements Detection {

    private final String id;
    private final String name;
    private final StageCycle stageCycle;
    private final Object plugin;

    @Inject
    public DetectionImpl(final @Assisted("id") String id,
                         final @Assisted("name") String name,
                         final @Assisted StageCycle stageCycle,
                         final @Assisted Object plugin) {
        this.id = id;
        this.name = name;
        this.stageCycle = stageCycle;
        this.plugin = plugin;
    }

    @Override
    public @NonNull String getId() {
        return this.id;
    }

    @Override
    public @NonNull String getName() {
        return this.name;
    }

    @Override
    public @NonNull StageCycle getStageCycle() {
        return this.stageCycle;
    }

    @Override
    public @NonNull Object getPlugin() {
        return this.plugin;
    }

}
