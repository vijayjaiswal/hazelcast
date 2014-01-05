/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.replicatedmap;

import com.hazelcast.core.EntryListener;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.monitor.LocalReplicatedMapStats;
import com.hazelcast.query.Predicate;
import com.hazelcast.replicatedmap.record.AbstractReplicatedRecordStore;
import com.hazelcast.spi.AbstractDistributedObject;
import com.hazelcast.spi.InitializingObject;
import com.hazelcast.spi.NodeEngine;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ReplicatedMapProxy<K, V>
        extends AbstractDistributedObject
        implements ReplicatedMap<K, V>, InitializingObject {

    private final AbstractReplicatedRecordStore replicatedRecordStore;

    ReplicatedMapProxy(NodeEngine nodeEngine, AbstractReplicatedRecordStore replicatedRecordStore) {
        super(nodeEngine, replicatedRecordStore.getReplicatedMapService());
        this.replicatedRecordStore = replicatedRecordStore;
    }

    @Override
    public String getName() {
        return replicatedRecordStore.getName();
    }

    @Override
    public String getPartitionKey() {
        return getName();
    }

    @Override
    public String getServiceName() {
        return ReplicatedMapService.SERVICE_NAME;
    }

    @Override
    public int size() {
        return replicatedRecordStore.size();
    }

    @Override
    public boolean isEmpty() {
        return replicatedRecordStore.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return replicatedRecordStore.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return replicatedRecordStore.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return (V) replicatedRecordStore.get(key);
    }

    @Override
    public V put(K key, V value) {
        return (V) replicatedRecordStore.put(key, value);
    }

    @Override
    public V put(K key, V value, long ttl, TimeUnit timeUnit) {
        return (V) replicatedRecordStore.put(key, value, ttl, timeUnit);
    }

    @Override
    public V remove(Object key) {
        return (V) replicatedRecordStore.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == null) {
            throw new NullPointerException("m cannot be null");
        }
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        replicatedRecordStore.clear();
    }

    @Override
    public boolean removeEntryListener(String id) {
        return replicatedRecordStore.removeEntryListenerInternal(id);
    }

    @Override
    public String addEntryListener(EntryListener<K, V> listener) {
        return replicatedRecordStore.addEntryListener(listener, null);
    }

    @Override
    public String addEntryListener(EntryListener<K, V> listener, K key) {
        return replicatedRecordStore.addEntryListener(listener, key);
    }

    @Override
    public String addEntryListener(EntryListener<K, V> listener, Predicate<K, V> predicate) {
        return replicatedRecordStore.addEntryListener(listener, predicate, null);
    }

    @Override
    public String addEntryListener(EntryListener<K, V> listener, Predicate<K, V> predicate, K key) {
        return replicatedRecordStore.addEntryListener(listener, predicate, key);
    }

    @Override
    public Set<K> keySet() {
        return replicatedRecordStore.keySet();
    }

    @Override
    public Collection<V> values() {
        return replicatedRecordStore.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return replicatedRecordStore.entrySet();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " -> " + replicatedRecordStore.getName();
    }

    @Override
    public void initialize() {
        replicatedRecordStore.initialize();
    }

    public LocalReplicatedMapStats getReplicatedMapStats() {
        return replicatedRecordStore.createReplicatedMapStats();
    }

    public void setPreReplicationHook(PreReplicationHook preReplicationHook) {
        replicatedRecordStore.setPreReplicationHook(preReplicationHook);
    }

}