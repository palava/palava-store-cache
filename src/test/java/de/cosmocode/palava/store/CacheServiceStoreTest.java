/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cosmocode.palava.store;

import org.junit.After;
import org.junit.Before;

import de.cosmocode.palava.core.Framework;
import de.cosmocode.palava.core.Palava;

/**
 * Tests {@link CacheServiceStore}.
 *
 * @since 1.0
 * @author Willi Schoenborn
 */
public final class CacheServiceStoreTest extends AbstractStoreTest {

    private final Framework framework = Palava.newFramework();
    
    /**
     * Runs before each test.
     */
    @Before
    public void before() {
        framework.start();
    }
    
    @Override
    public CacheServiceStore unit() {
        return framework.getInstance(CacheServiceStore.class);
    }

    @Override
    protected Store unitWithGenerator(IdGenerator generator) {
        final CacheServiceStore unit = unit();
        unit.setGenerator(generator);
        return unit;
    }
    
    @Override
    protected boolean supportsList() {
        return false;
    }
    
    /**
     * Runs after each test.
     * 
     * @since
     */
    @After
    public void after() {
        framework.stop();
    }

}
