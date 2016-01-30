package com.michaelsnowden.unit_converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.*;

import static com.google.appengine.repackaged.com.google.common.collect.ImmutableMap.of;

/**
 * @author michael.snowden
 */
public abstract class AbstractSymbolLookup implements SymbolLookup {
    protected final Map<String, Term> map;

    public AbstractSymbolLookup() {
        map = new HashMap<>();
    }

    private Term recursiveLookup(String symbol, Fraction power) {
        if (map.containsKey(symbol)) {
            return map.get(symbol)
                    .getUnits()
                    .entrySet()
                    .stream()
                    .map(e -> recursiveLookup(e.getKey(), e.getValue()))
                    .reduce(new Term(map.get(symbol).getValue(), new HashMap<>()), Term::times);
        } else {
            return new Term(1.0, of(symbol, power));
        }
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Term get(Object key) {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("Cannot lookup symbols that aren't strings");
        }
        return recursiveLookup((String) key, new Fraction(1));
    }

    @Override
    public Term put(String key, Term value) {
        return map.put(key, value);
    }

    @Override
    public Term remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Term> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Term> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Term>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public Term getOrDefault(Object key, Term defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Term> action) {
        map.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Term, ? extends Term> function) {
        map.replaceAll(function);
    }

    @Override
    public Term putIfAbsent(String key, Term value) {
        return map.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    @Override
    public boolean replace(String key, Term oldValue, Term newValue) {
        return map.replace(key, oldValue, newValue);
    }

    @Override
    public Term replace(String key, Term value) {
        return map.replace(key, value);
    }

    @Override
    public Term computeIfAbsent(String key, java.util.function.Function<? super String, ? extends Term>
            mappingFunction) {
        return map.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public Term computeIfPresent(String key, BiFunction<? super String, ? super Term, ? extends Term> remappingFunction) {
        return map.computeIfPresent(key, remappingFunction);
    }

    @Override
    public Term compute(String key, BiFunction<? super String, ? super Term, ? extends Term> remappingFunction) {
        return map.compute(key, remappingFunction);
    }

    @Override
    public Term merge(String key, Term value, BiFunction<? super Term, ? super Term, ? extends Term> remappingFunction) {
        return map.merge(key, value, remappingFunction);
    }
}
