package main.java.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface FileManager<T> {

    default List<String> importFilesList(Path folderPath){
        if(Files.notExists(folderPath)){
            try{
                Files.createDirectories(folderPath);
            }
            catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }

        try (Stream<Path> p = Files.list(folderPath)){
            return p.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(name -> name.endsWith(".dat"))
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    Map<Integer,T> importDatabase();

    void exportDatabase(Map<Integer,T> data);

    void clearDatabase();

    void exportItem(int key,T item);

    AbstractMap.SimpleEntry<Integer, T> importItem(String path);

    void deleteItem(int id);
}

