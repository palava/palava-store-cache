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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import de.cosmocode.palava.cache.CacheService;

/**
 * {@link CacheService} based {@link ByteStore} implementation.
 *
 * @since 1.0
 * @author Willi Schoenborn
 */
final class CacheServiceStore extends AbstractByteStore {
    
    private static final Logger LOG = LoggerFactory.getLogger(CacheServiceStore.class);

    private final CacheService cache;

    private IdGenerator generator = new UUIDGenerator();
    
    @Inject
    public CacheServiceStore(CacheService cache) {
        this.cache = Preconditions.checkNotNull(cache, "Cache");
    }
    
    @Inject(optional = true)
    void setGenerator(IdGenerator generator) {
        this.generator = Preconditions.checkNotNull(generator, "Generator");
    }
    
    @Override
    public String create(InputStream stream) throws IOException {
        Preconditions.checkNotNull(stream, "Stream");
        final String identifier = generator.generate();
        create(stream, identifier);
        return identifier;
    }

    @Override
    public void create(InputStream stream, String identifier) throws IOException {
        Preconditions.checkNotNull(stream, "Stream");
        Preconditions.checkState(cache.read(identifier) == null, "Byte array for %s already present", identifier);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(stream, output);
        final byte[] data = output.toByteArray();
        LOG.trace("Storing {} to {}", stream, identifier);
        cache.store(identifier, data);
    }
    
    @Override
    public ByteBuffer view(String identifier) throws IOException {
        Preconditions.checkNotNull(identifier, "Identifier");
        LOG.trace("Reading data from {}", identifier);
        final byte[] data = cache.read(identifier);
        Preconditions.checkState(data != null, "Unknown identifier %s", identifier);
        return ByteBuffer.wrap(data);
    }

    @Override
    public Set<String> list() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(String identifier) throws IOException {
        Preconditions.checkNotNull(identifier, "Identifier");
        LOG.trace("Removing {} from store", identifier);
        Preconditions.checkState(cache.remove(identifier) != null, "Unknown identifier %s", identifier);
    }

}
