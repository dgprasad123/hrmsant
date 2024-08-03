/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hrms.common;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyImageService {
    
    private String sourceDir;
    private String targetDir;

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public void setTargetDir(String targetDir) {
        this.targetDir = targetDir;
    }

    public void copyEmployeeImages(String employeeId) throws IOException, ImageNotFoundException {
        final Path sourcePath = Paths.get(sourceDir);
        final Path targetPath = Paths.get(targetDir);

        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        CopyFilesVisitor visitor = new CopyFilesVisitor(sourcePath, targetPath, employeeId);
        Files.walkFileTree(sourcePath, visitor);

        if (!visitor.isFileCopied()) {
            throw new ImageNotFoundException("Image not available for employee ID: " + employeeId);
        }
    }

    public void copyDefaultImage(String hrmsId) throws IOException {
        Path defaultImagePath = Paths.get("/home/cmgi/Downloads/default.jpg");
        final Path targetPath = Paths.get(targetDir);

        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        String targetFileName = hrmsId + ".jpg";
        Path targetFilePath = targetPath.resolve(targetFileName);

        Files.copy(defaultImagePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private static class CopyFilesVisitor extends SimpleFileVisitor<Path> {
        private final Path sourcePath;
        private final Path targetPath;
        private final String employeeId;
        private boolean fileCopied = false;

        public CopyFilesVisitor(Path sourcePath, Path targetPath, String employeeId) {
            this.sourcePath = sourcePath;
            this.targetPath = targetPath;
            this.employeeId = employeeId;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.getFileName().toString().contains(employeeId)) {
                System.out.println("copyfile::::::"+employeeId);
                Path targetFilePath = targetPath.resolve(sourcePath.relativize(file));
                Files.createDirectories(targetFilePath.getParent());
                Files.copy(file, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
                fileCopied = true;
            }
            return FileVisitResult.CONTINUE;
        }

        public boolean isFileCopied() {
            return fileCopied;
        }
    }

    public static class ImageNotFoundException extends Exception {
        public ImageNotFoundException(String message) {
            super(message);
        }
    }
}
