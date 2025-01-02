package main.java.database;

import main.java.logger.LogEventType;
import main.java.logger.Logger;
import main.java.utils.FileManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileDatabase<T> implements DatabaseOperations<T>{
    private final FileManager<T> fileManager;
    private int lastItemID = 1;

    private final Map<Integer, T> data;

    public FileDatabase(String folderPath) {
        this.fileManager = new DatabaseFileManager(folderPath);
        data = importDataFromFile();
    }

    public int getItemID(T item){
        return data.entrySet().stream().filter(e -> e.getValue().equals(item)).map(Map.Entry::getKey).findFirst().orElse(-1);
    }

    @Override
    public Map<Integer, T> importDataFromFile() {
        Map<Integer, T> map = fileManager.importDatabase();
        lastItemID = map.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
        return map;
    }

    @Override
    public void exportDataToFile() {
        fileManager.exportDatabase(data);
    }

    @Override
    public void addItemToDatabase(T item) {
        addItemToDatabase(lastItemID++, item);
    }

    @Override
    public void addItemToDatabase(int id, T item) {
        data.put(id, item);
        fileManager.exportItem(id, item);
    }

    @Override
    public void removeItemFromDatabase(int id) {
        data.remove(id);
        fileManager.deleteItem(id);
    }

    @Override
    public T getItemFromDatabase(int id) {
        return data.get(id);
    }

    @Override
    public void updateItemInDatabase(int id, T item) {
        removeItemFromDatabase(id);
        addItemToDatabase(id, item);
    }

    @Override
    public void clearDatabase() {
        data.clear();
        fileManager.clearDatabase();
    }

    @Override
    public Map<Integer, T> getAll() {
        return data;
    }

    @Override
    public LinkedHashMap<Integer, T> getSorted(Comparator<T> comparator) {
        return data.entrySet().stream()
                .sorted(((o1, o2) -> comparator.compare(o1.getValue(), o2.getValue())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o1, o2) -> o1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<Integer, T> getFiltered(Predicate<T> filter) {
        return data.entrySet().stream().filter(o -> filter.test(o.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (o1, o2) -> o1,
                        HashMap::new
                ));
    }

    @Override
    public boolean checkDuplicate(int id) {
        return false;
    }

    private class DatabaseFileManager implements FileManager<T> {
        private final String folderPath;
        private final Path path;
        private List<String> filesList;

        private DatabaseFileManager(String folderPath) {
            this.folderPath = folderPath;
            this.path = Paths.get(folderPath);
            this.filesList = importFilesList(path);
        }

        private static int getIdFromFilePath(String filePath) {
            Path path = Paths.get(filePath);
            String filename = path.getFileName().toString();
            return Integer.parseInt(filename.split("\\.")[0]);
        }

        private String getFilePathFromId(int id) {
            return folderPath + id + ".dat";
        }

        @Override
        public Map<Integer, T> importDatabase() {
            Logger.getInstance().log(LogEventType.DATABASE_LOADED, path);
            this.filesList = importFilesList(path);

            return filesList.isEmpty() ? new HashMap<>() : filesList.stream().map(this::importItem)
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        }

        @Override
        public void exportDatabase(Map<Integer, T> data) {
            data.forEach(this::exportItem);
            Logger.getInstance().log(LogEventType.DATABASE_EXPORTED, path);
        }

        @Override
        public void clearDatabase() {
            this.filesList = new ArrayList<>(importFilesList(path));

            filesList.stream().map(DatabaseFileManager::getIdFromFilePath).forEach(this::deleteItem);
            filesList.clear();
        }

        @Override
        public void exportItem(int key, T item) {
            Path userPath = Path.of(getFilePathFromId(key));

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userPath.toFile()))) {
                out.writeObject(item);
                Logger.getInstance().log(LogEventType.ITEM_EXPORTED, userPath.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public AbstractMap.SimpleEntry<Integer, T> importItem(String path) {
            Path userPath = Paths.get(path);

            if (!Files.exists(userPath)) {
                Logger.getInstance().log(LogEventType.FILE_NOT_FOUND, userPath.toString());
                return null;
            }

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(userPath.toFile()))) {
                T user = (T) in.readObject();
                int id = getIdFromFilePath(path);

                return new AbstractMap.SimpleEntry<>(id, user);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void deleteItem(int id) {
            Path reportPath = Paths.get(getFilePathFromId(id));

            if (!Files.exists(reportPath)) {
                Logger.getInstance().log(LogEventType.FILE_NOT_FOUND, reportPath.toString());
                return;
            }

            try {
                Files.delete(reportPath);
                filesList.remove(reportPath.toString());
                Logger.getInstance().log(LogEventType.FILE_DELETED, reportPath.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
