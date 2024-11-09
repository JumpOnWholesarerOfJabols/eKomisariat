package main.java.database;

import main.java.model.Policeman;
import main.java.model.User;
import main.java.utils.FileManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PolicemenDatabase implements DatabaseOperations<Policeman> {
    private final PolicemenFileManager policemenFileManager;
    private int ID = 1;

    private final Map<Integer, Policeman> data;

    public PolicemenDatabase(String folderPath) {
        this.policemenFileManager = new PolicemenFileManager(folderPath);
        data = importDataFromFile();
    }

    public PolicemenDatabase() {
        this("src/main/resources/users/");
    }

    @Override
    public Map<Integer, Policeman> importDataFromFile() {
        Map<Integer, Policeman> map = policemenFileManager.importDatabase();
        ID = map.keySet().stream().max(Integer::compareTo).orElse(0) + 1;

        return map;
    }

    @Override
    public void exportDataToFile() {
        policemenFileManager.exportDatabase(data);
    }

    @Override
    public void addItemToDatabase(Policeman item) {
        addItemToDatabase(ID++, item);
    }

    @Override
    public void addItemToDatabase(int id, Policeman item) {
        data.put(id, item);
        policemenFileManager.exportItem(id, item);
    }

    @Override
    public void removeItemFromDatabase(int id) {
        data.remove(id);
        policemenFileManager.deleteItem(id);
    }

    @Override
    public Policeman getItemFromDatabase(int id) {
        return data.get(id);
    }

    @Override
    public void updateItemInDatabase(int id, Policeman item) {
        removeItemFromDatabase(id);
        addItemToDatabase(id, item);
    }

    @Override
    public void clearDatabase() {
        data.clear();
        policemenFileManager.clearDatabase();
    }

    @Override
    public Map<Integer, Policeman> getAll() {
        return data;
    }

    @Override
    public LinkedHashMap<Integer, Policeman> getSorted(Comparator<Policeman> comparator) {
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
    public Map<Integer, Policeman> getFiltered(Predicate<Policeman> filter) {
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

    private static class PolicemenFileManager implements FileManager<Policeman> {
        private final String folderPath;
        private final Path path;
        private List<String> filesList;

        private PolicemenFileManager(String folderPath) {
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
        public Map<Integer, Policeman> importDatabase() {
            this.filesList = importFilesList(path);

            return filesList.isEmpty() ? new HashMap<>() : filesList.stream().map(this::importItem)
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        }

        @Override
        public void exportDatabase(Map<Integer, Policeman> data) {
            data.forEach(this::exportItem);
        }

        @Override
        public void clearDatabase() {
            this.filesList = new ArrayList<>(importFilesList(path));

            filesList.stream().map(PolicemenFileManager::getIdFromFilePath).forEach(this::deleteItem);
            filesList.clear();
        }

        @Override
        public void exportItem(int key, Policeman item) {
            Path userPath = Path.of(getFilePathFromId(key));

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userPath.toFile()))) {
                out.writeObject(item);

            } catch (IOException e) {

            }
        }

        @Override
        public AbstractMap.SimpleEntry<Integer, Policeman> importItem(String path) {
            Path userPath = Paths.get(path);

            if (!Files.exists(userPath)) {
                //logi - że brak pliku
                return null;
            }

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(userPath.toFile()))) {
                Policeman user = (Policeman) in.readObject();
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
