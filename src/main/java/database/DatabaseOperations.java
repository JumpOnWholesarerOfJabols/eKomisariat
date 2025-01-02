package main.java.database;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public interface DatabaseOperations<T> {
    Map<Integer, T> importDataFromFile();

    int getItemID(T item);
    void exportDataToFile();

    void addItemToDatabase(T item);

    void addItemToDatabase(int id,T item);

    void removeItemFromDatabase(int id);

    T getItemFromDatabase(int id);

    void updateItemInDatabase(int id,T item);

    void clearDatabase();

    Map<Integer, T> getAll();

    LinkedHashMap<Integer, T> getSorted(Comparator<T> comparator);

    Map<Integer, T>  getFiltered(Predicate<T> filter);

    boolean checkDuplicate(int id);
}
