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

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ichorpowered.guardian.api.detection.Detection;
import com.ichorpowered.guardian.api.detection.DetectionBuilder;
import com.ichorpowered.guardian.api.detection.DetectionController;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@Singleton
public class DetectionControllerImpl implements DetectionController {

    private final DetectionBuilderImpl.Factory detectionBuilderFactory;
    private final Map<String, Detection> detectionContainer = Maps.newHashMap();

    @Inject
    public DetectionControllerImpl(final DetectionBuilderImpl.Factory detectionBuilderFactory) {
        this.detectionBuilderFactory = detectionBuilderFactory;
    }

    @Override
    public @NonNull DetectionBuilder builder() {
        return this.detectionBuilderFactory.create();
    }

    @Override
    public @NonNull Optional<Detection> getDetection(final @NonNull String detectionId) {
        return Optional.ofNullable(this.detectionContainer.get(detectionId));
    }

    @Override
    public @NonNull DetectionController removeDetection(final @NonNull String detectionId) {
        this.detectionContainer.remove(detectionId);
        return this;
    }

    void setDetection(final @NonNull Detection detection) {
        this.detectionContainer.put(detection.getId(), detection);
    }

    @Override
    public Iterator<Detection> iterator() {
        return this.detectionContainer.values().iterator();
    }

}
