package main.java.view;

import java.util.function.Predicate;

public abstract class AbstractTablePage<T> extends AbstractPage{
    protected final boolean editEnabled;
    protected final Predicate<T> defaultFilter;
    protected Predicate<T> currentFilter;

    protected AbstractTablePage(Predicate<T> defaultFilter) {
        super();
        if (defaultFilter == null) {
            this.defaultFilter = r -> true;
            this.editEnabled = true;
        } else {
            this.defaultFilter = defaultFilter;
            this.editEnabled = false;
        }
    }
}
