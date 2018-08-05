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
package com.ichorpowered.guardian.common;

import com.ichorpowered.guardian.api.GuardianPlatform;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class CommonGuardianPlatform implements GuardianPlatform {

    private final String platformName;
    private final String platformVersion;
    private final String gameVersion;
    private final String version;
    private final String releaseCandidate;

    public CommonGuardianPlatform(final String platformName,
                                  final String platformVersion,
                                  final String gameVersion,
                                  final String version,
                                  final String releaseCandidate) {
        this.platformName = platformName;
        this.platformVersion = platformVersion;
        this.gameVersion = gameVersion;
        this.version = version;
        this.releaseCandidate = releaseCandidate;
    }


    @Override
    public @NonNull String getPlatformName() {
        return this.platformName;
    }

    @Override
    public @NonNull String getPlatformVersion() {
        return this.platformVersion;
    }

    @Override
    public @NonNull String getGameVersion() {
        return this.gameVersion;
    }

    @Override
    public @NonNull String getVersion() {
        return this.version;
    }

    @Override
    public @NonNull String getReleaseCandidate() {
        return this.releaseCandidate;
    }

}
