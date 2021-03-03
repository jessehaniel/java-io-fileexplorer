package br.com.letscode.java;

import static java.nio.file.Files.newDirectoryStream;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;
import java.util.Comparator;

public class FileSystemExplorer {

    private final Path path;

    public FileSystemExplorer(Path path) {
        this.path = path;
        this.init();
    }

    private void init() {
        System.out.println("Analisando o caminho: " + path.toAbsolutePath().toString());
        System.out.println("Contido em: " + path.getParent().toAbsolutePath().toString());
    }

    public void printDirectoryContent() {
        try (var stream = newDirectoryStream(this.path)) {
            for (Path file : stream) {
                System.out.println(file.getFileName());
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void filterFilesWithExtension(String extension) {
        this.filterFilesWithExtension(this.path, extension);
    }

    private void filterFilesWithExtension(Path path, String extension) {
        File file = path.toFile();
        File[] matchingFiles = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                File f = Paths.get(dir.getPath() + File.separator + name).toFile();
                if (f.isDirectory()) {
                    filterFilesWithExtension(Paths.get(f.getPath()), extension);
                }
                return name.endsWith(extension);
            }
        });
        if (matchingFiles == null) {
            System.out.println("Nenhum resultado encontrado");
            return;
        }
        for (File f : matchingFiles) {
            System.out.println(f.getParent()+": "+f.getName());
        }
    }

    public void listFilesSortedByModificationDate() {
        this.listFilesSortedByModificationDate(this.path.toFile());
    }

    public void listFilesSortedByModificationDate(File directory) {
        File[] directoryFileContent = getDirectoryFileContent(directory);
        visitSubdirectories(directoryFileContent);
        Arrays.sort(directoryFileContent, Comparator.comparingLong(File::lastModified));
        showDirectoryFileContent(directoryFileContent);
    }

    private File[] getDirectoryFileContent(File directory) {
        File[] directoryFileContent = directory.listFiles();
        if (directoryFileContent == null) {
            throw new NullableContentException();
        }
        return directoryFileContent;
    }

    private void visitSubdirectories(File[] directoryFileContent) {
        for (File file : directoryFileContent) {
            if (file.isDirectory()) {
                this.listFilesSortedByModificationDate(file);
            }
        }
    }

    private void showDirectoryFileContent(File[] directoryFileContent) {
        for (File file : directoryFileContent) {
            if (file.isHidden()) {
                continue;//continue X break X return
            }
            String fileName = file.getName();
            FileTime lastModified = FileTime.fromMillis(file.lastModified());
            System.out.printf("[%s] %s\n", lastModified.toString(), fileName);
        }
    }

    public void listFilesSortedBySize() throws IOException {
        File[] directoryFileContent = this.getDirectoryFileContent(this.path.toFile());

        Arrays.sort(directoryFileContent, (f1, f2) -> {
            try {
                return Long.compare(Files.size(f2.toPath()), Files.size(f1.toPath()));
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
                return 0;
            }
        });

        for (File file : directoryFileContent) {
            if (file.isHidden() || file.isDirectory()) {
                continue;
            }
            String fileName = file.getName();
            double fileSize = (double) Files.size(file.toPath()) / 1024 / 1024;
            System.out.printf("[%.2fMB] %s\n", fileSize, fileName);
        }
    }
}
