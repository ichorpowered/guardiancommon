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
package com.ichorpowered.guardian.common.inject.game;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.ichorpowered.guardian.api.game.resource.PlayerGroupResource;
import com.ichorpowered.guardian.api.game.resource.PlayerResource;
import com.ichorpowered.guardian.api.game.resource.ResourceFactories;
import com.ichorpowered.guardian.common.game.resource.PlayerGroupResourceImpl;
import com.ichorpowered.guardian.common.game.resource.PlayerResourceImpl;
import com.ichorpowered.guardian.common.game.resource.ResourceFactoriesImpl;
import net.kyori.violet.AbstractModule;

public final class ResourceModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ResourceFactories.class).to(ResourceFactoriesImpl.class);

        this.install(new FactoryModuleBuilder().implement(PlayerGroupResource.class, PlayerGroupResourceImpl.class).build(PlayerGroupResource.Factory.class));
        this.install(new FactoryModuleBuilder().implement(PlayerResource.class, PlayerResourceImpl.class).build(PlayerResource.Factory.class));
    }

}
