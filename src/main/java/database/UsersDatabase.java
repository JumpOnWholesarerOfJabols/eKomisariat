package main.java.database;

import main.java.model.User;
import main.java.utils.FileManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UsersDatabase implements DatabaseOperations<User> {
    private final UsersFileManager usersFileManager;
    private int ID = 1;

    private final Map<Integer, User> data;

    public UsersDatabase(String folderPath) {
        this.usersFileManager = new UsersFileManager(folderPath);
        data = importDataFromFile();
    }

    @Override
    public Map<Integer, User> importDataFromFile() {
        Map<Integer, User> map = usersFileManager.importDatabase();
        ID = map.keySet().stream().max(Integer::compareTo).orElse(0) + 1;

        return map;
    }

    @Override
    public void exportDataToFile() {

    }

    @Override
    public void addItemToDatabase(User item) {
        addItemToDatabase(ID++, item);
    }

    @Override
    public void addItemToDatabase(int id, User item) {
        data.put(id, item);
        usersFileManager.exportItem(id, item);
    }

    @Override
    public void removeItemFromDatabase(int id) {

    }

    @Override
    public User getItemFromDatabase(int id) {
        return data.get(id);
    }

    @Override
    public void updateItemInDatabase(int id, User item) {

    }

    @Override
    public void clearDatabase() {

    }

    @Override
    public Map<Integer, User> getAll() {
        return data;
    }

    @Override
    public LinkedHashMap<Integer, User> getSorted(Comparator<User> comparator) {
        return null;
    }

    @Override
    public Map<Integer, User> getFiltered(Predicate<User> filter) {
        return Map.of();
    }

    @Override
    public boolean checkDuplicate(int id) {
        return false;
    }

    private static class UsersFileManager implements FileManager<User> {
        private final String folderPath;
        private final Path path;

        private UsersFileManager(String folderPath) {
            this.folderPath = folderPath;
            this.path = Paths.get(folderPath);
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
        public Map<Integer, User> importDatabase() {
            List<String> filesList = importFilesList(path);

            return filesList.isEmpty() ? new HashMap<>() : filesList.stream().map(this::importItem)
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        }

        @Override
        public void exportDatabase(Map<Integer, User> data) {

        }

        @Override
        public void clearDatabase() {

        }

        @Override
        public void exportItem(int key, User item) {
            Path userPath = Path.of(getFilePathFromId(key));

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userPath.toFile()))) {
                out.writeObject(item);

            } catch (IOException e) {

            }
        }

        @Override
        public AbstractMap.SimpleEntry<Integer, User> importItem(String path) {
            Path userPath = Paths.get(path);

            if (!Files.exists(userPath)) {
                //logi - że brak pliku
                return null;
            }

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(userPath.toFile()))) {
                User user = (User) in.readObject();
                int id = getIdFromFilePath(path);
                return new AbstractMap.SimpleEntry<>(id, user);
                //logi - wczytano item
            } catch (IOException | ClassNotFoundException e) {
                //logi - blad podczas wczytywania itemu
                return null;
            }
        }

        @Override
        public void deleteItem(int id) {

        }
    }
}
