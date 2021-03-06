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
import com.ichorpowered.guardian.api.game.model.Component;
import com.ichorpowered.guardian.api.game.model.Model;
import com.ichorpowered.guardian.api.game.model.ModelFactories;
import com.ichorpowered.guardian.api.game.model.ModelRegistry;
import com.ichorpowered.guardian.api.game.model.value.GameValue;
import com.ichorpowered.guardian.api.game.model.value.key.GameKey;
import com.ichorpowered.guardian.api.game.model.value.key.GameKeyRegistry;
import com.ichorpowered.guardian.common.game.model.ComponentImpl;
import com.ichorpowered.guardian.common.game.model.ModelFactoriesImpl;
import com.ichorpowered.guardian.common.game.model.ModelImpl;
import com.ichorpowered.guardian.common.game.model.ModelRegistryImpl;
import com.ichorpowered.guardian.common.game.model.value.GameValueImpl;
import com.ichorpowered.guardian.common.game.model.value.key.GameKeyImpl;
import com.ichorpowered.guardian.common.game.model.value.key.GameKeyRegistryImpl;
import net.kyori.violet.AbstractModule;

public final class ModelModule extends AbstractModule {

    @Override
    protected void configure() {
        this.bind(ModelFactories.class).to(ModelFactoriesImpl.class);
        this.bind(ModelRegistry.class).to(ModelRegistryImpl.class);
        this.bind(GameKeyRegistry.class).to(GameKeyRegistryImpl.class);

        this.install(new FactoryModuleBuilder().implement(Model.class, ModelImpl.class).build(Model.Factory.class));
        this.install(new FactoryModuleBuilder().implement(Component.class, ComponentImpl.class).build(Component.Factory.class));
        this.install(new FactoryModuleBuilder().implement(GameKey.class, GameKeyImpl.class).build(GameKey.Factory.class));
        this.install(new FactoryModuleBuilder().implement(GameValue.class, GameValueImpl.class).build(GameValue.Factory.class));
    }

}
