package main.java.database;

import main.java.model.Report;
import main.java.utils.FileManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReportsDatabase implements DatabaseOperations<Report> {
    private final ReportsFileManager reportsFileManager;
    private static int ID = 1;

    private final Map<Integer, Report> data;

    public ReportsDatabase(String folderPath) {
        reportsFileManager = new ReportsFileManager(folderPath);
        data = importDataFromFile();
    }

    public ReportsDatabase() {
        this("src/main/resources/reports/");
    }

    public int getItemID(Report report) {
        return data.entrySet().stream().filter(e -> e.getValue().equals(report)).map(Map.Entry::getKey).findFirst().orElse(-1);
    }

    @Override
    public Map<Integer, Report> importDataFromFile() {
        Map<Integer, Report> map = reportsFileManager.importDatabase();
        ID = map.keySet().stream().max(Integer::compareTo).orElse(0) + 1;

        return map;
    }

    @Override
    public void exportDataToFile() {
        reportsFileManager.exportDatabase(data);
    }

    @Override
    public void addItemToDatabase(Report item) {
        addItemToDatabase(ID++, item);
    }

    @Override
    public void addItemToDatabase(int id, Report item) {
        data.put(id, item);
        reportsFileManager.exportItem(id, item);
    }

    @Override
    public void removeItemFromDatabase(int id) {
        data.remove(id);
        reportsFileManager.deleteItem(id);
    }

    @Override
    public Report getItemFromDatabase(int id) {
        return data.get(id);
    }

    @Override
    public Map<Integer, Report> getAll() {
        return data;
    }

    @Override
    public LinkedHashMap<Integer, Report> getSorted(Comparator<Report> comparator) {
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
    public Map<Integer, Report> getFiltered(Predicate<Report> filter) {
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

    @Override
    public void updateItemInDatabase(int id, Report item) {
        removeItemFromDatabase(id);
        addItemToDatabase(id, item);
    }

    @Override
    public void clearDatabase() {
        data.clear();
        reportsFileManager.clearDatabase();
    }


    private static class ReportsFileManager implements FileManager<Report> {
        private final String folderPath;
        private final Path path;
        private List<String> filesList;

        private ReportsFileManager(String folderPath) {
            this.folderPath = folderPath;
            this.path = Paths.get(folderPath);
            this.filesList = importFilesList(path);
        }

        private String getFilePathFromId(int id) {
            return folderPath + id + ".dat";
        }

        private static int getIdFromFilePath(String filePath) {
            Path path = Paths.get(filePath);
            String filename = path.getFileName().toString();
            return Integer.parseInt(filename.split("\\.")[0]);
        }

        private void refreshFilesList() {
            filesList.clear();
            filesList.addAll(importFilesList(path));
        }

        @Override
        public Map<Integer, Report> importDatabase() {
            this.filesList = new ArrayList<>(importFilesList(path));

            return filesList.isEmpty() ? new HashMap<>() : filesList.stream().map(this::importItem)
                    .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        }

        @Override
        public void exportDatabase(Map<Integer, Report> data) {
            data.forEach(this::exportItem);
        }

        @Override
        public void clearDatabase() {
            this.filesList = new ArrayList<>(importFilesList(path));

            filesList.stream().map(ReportsFileManager::getIdFromFilePath).forEach(this::deleteItem);
            filesList.clear();
        }

        @Override
        public void exportItem(int key, Report item) {
            Path itemPath = Path.of(getFilePathFromId(key));

            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(itemPath.toFile()))) {
                out.writeObject(item);
                //logi - wyeksportowano item
            } catch (IOException e) {
                //logi - blad podczas eksportu itemu
            }
        }

        @Override
        public AbstractMap.SimpleEntry<Integer, Report> importItem(String path) {
            Path reportPath = Paths.get(path);

            if (!Files.exists(reportPath)) {
                //logi - że brak pliku
                return null;
            }

            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(reportPath.toFile()))) {
                Report report = (Report) in.readObject();
                int id = getIdFromFilePath(path);
                return new AbstractMap.SimpleEntry<>(id, report);
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
