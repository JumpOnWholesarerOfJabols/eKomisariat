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

    public UsersDatabase() {
        this("src/main/resources/users/");
    }

    @Override
    public Map<Integer, User> importDataFromFile() {
        Map<Integer, User> map = usersFileManager.importDatabase();
        ID = map.keySet().stream().max(Integer::compareTo).orElse(0) + 1;

        return map;
    }

    @Override
    public void exportDataToFile() {
        usersFileManager.exportDatabase(data);
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
        data.remove(id);
        usersFileManager.deleteItem(id);
    }

    @Override
    public User getItemFromDatabase(int id) {
        return data.get(id);
    }

    @Override
    public void updateItemInDatabase(int id, User item) {
        removeItemFromDatabase(id);
        addItemToDatabase(id, item);
    }

    @Override
    public void clearDatabase() {
        data.clear();
        usersFileManager.clearDatabase();
    }

    @Override
    public Map<Integer, User> getAll() {
        return data;
    }

    @Override
    public LinkedHashMap<Integer, User> getSorted(Comparator<User> comparator) {
        return data.entrySet().stream()
                .sorted(((o1, o2) -> comparator.compare(o1.getValue(), o2.getValue())))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, //object -> object.getKey()
                        Map.Entry::getValue, //object -> object.getValue()
                        (o1, o2) -> o1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<Integer, User> getFiltered(Predicate<User> filter) {
        return data.entrySet().stream().filter(o -> filter.test(o.getValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, //object -> object.getKey()
                        Map.Entry::getValue, //object -> object.getValue()
                        (o1, o2) -> o1,
                        HashMap::new
                ));
    }

    @Override
    public boolean checkDuplicate(int id) {
        return false;
    }

    public int getUserId(User user) {
        return data.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), user))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(-1);
    }

    private static class UsersFileManager implements FileManager<User> {
        private final String folderPath;
        private final Path path;
        private List<String> filesList;

        private UsersFileManager(String folderPath) {
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
        public Map<Integer, User> importDatabase() {
            this.filesList = importFilesList(path);

            return filesList.isEmpty() ? new HashMap<>() : filesList.stream().map(this::importItem)
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        }

        @Override
        public void exportDatabase(Map<Integer, User> data) {
            data.forEach(this::exportItem);
        }

        @Override
        public void clearDatabase() {
            this.filesList = new ArrayList<>(importFilesList(path));

            filesList.stream().map(UsersDatabase.UsersFileManager::getIdFromFilePath).forEach(this::deleteItem);
            filesList.clear();
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
            Path reportPath = Paths.get(getFilePathFromId(id));

            if (!Files.exists(reportPath)) {
                //logi - że brak pliku
                return;
            }

            try {
                Files.delete(reportPath);
                filesList.remove(path.toString());
                //logi ze usuwamy plik
            } catch (IOException e) {
                // logi ze jakiś blad
            }
        }
    }
}