package com.leclowndu93150.baguettelib.rank;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Ranker<T> {

    private final Identifier id;
    private final Component title;
    private final Function<T, Component> valueFormatter;
    private final Comparator<T> comparator;
    private final Predicate<T> valid;

    public Ranker(Identifier id, Component title, Function<T, Component> valueFormatter,
                  Comparator<T> comparator, Predicate<T> valid) {
        this.id = id;
        this.title = title;
        this.valueFormatter = valueFormatter;
        this.comparator = comparator;
        this.valid = valid;
    }

    public Ranker(Identifier id, Component title, Function<T, Component> valueFormatter, Comparator<T> comparator) {
        this(id, title, valueFormatter, comparator, t -> true);
    }

    public Identifier id() {
        return id;
    }

    public Component title() {
        return title;
    }

    public Comparator<T> comparator() {
        return comparator;
    }

    public Component formatValue(T entry) {
        return valueFormatter.apply(entry);
    }

    public boolean isValid(T entry) {
        return valid.test(entry);
    }

    public List<T> rank(Collection<T> entries) {
        List<T> list = new ArrayList<>(entries.size());
        for (T entry : entries) {
            if (valid.test(entry)) list.add(entry);
        }
        list.sort(comparator);
        return list;
    }

    public Ranker<T> withTiebreaker(Comparator<T> tiebreaker) {
        return new Ranker<>(id, title, valueFormatter, comparator.thenComparing(tiebreaker), valid);
    }
}
