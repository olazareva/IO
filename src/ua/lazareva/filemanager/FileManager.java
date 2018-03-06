package ua.lazareva.filemanager;

import java.io.*;

public class FileManager {

    //написать класс FileManager со следующими методами:
    //принимает путь к папке, возвращает количество файлов в папке и всех подпапках по пути
    public static int calculateFiles(String path) {
        File file = validatedPath(path);
        return calculateFilesOrDirs(file, "FILE");
    }

    // принимает путь к папке, возвращает количество папок в папке и всех подпапках по пути
    public static int calculateDirs(String path) {
        File file = validatedPath(path);
        return calculateFilesOrDirs(file, "DIRECTORY");
    }

    // метод по копированию папок и файлов.
    // Параметр from - путь к файлу или папке,
    // параметр to - путь к папке куда будет производиться копирование.
    public static boolean copy(String from, String to) throws IOException {

        boolean successFlag;
        File pathFrom = new File(from);
        File pathTo = new File(to);

        validateInput(pathFrom, pathTo);
        if (!pathTo.exists()) {
            if (!pathTo.mkdirs()) {
                throw new IOException(pathTo + " - Folder could not be created");
            }
        }
        if (pathFrom.isFile()) {
            successFlag = copyFile(pathFrom, pathTo);
        } else {
            successFlag = copyAll(pathFrom, new File(pathTo.getAbsolutePath() + "/" + pathFrom.getName()));
        }
        return successFlag;
    }

    private static boolean copyFile(File pathFrom, File pathTo) throws IOException {
        String fileName;
        fileName = pathFrom.getName();
        File targetFile = buildTargetFileName(pathTo.getPath() + "/" + fileName);
        if (targetFile.createNewFile()) {
            return copyData(pathFrom, targetFile);
        } else {
            throw new IOException(targetFile + " - File could not be created");
        }
    }

    private static void validateInput(File pathFrom, File pathTo) {
        String pathSourceFrom;
        if (!pathFrom.exists()) {
            throw new IllegalArgumentException("Path \"" + pathFrom + "\" doesn't exist");
        }
        if (pathFrom.isFile()) {
            pathSourceFrom = pathFrom.getParent();
        } else {
            pathSourceFrom = pathFrom.getAbsolutePath();
        }
        if (pathTo.getAbsolutePath().contains(pathSourceFrom) && !pathFrom.isFile()) {
            throw new IllegalArgumentException("You are going to copy folder into itself. Action denied!");
        }
    }

    private static boolean copyAll(File sourceFile, File targetFile) {
        String tmpTarget;
        File tmpTargetFile;
        try {
            if (sourceFile.isDirectory()) {
                tmpTargetFile = targetFile;
                tmpTargetFile.mkdirs();

                for (File f : sourceFile.listFiles()) {
                    tmpTarget = (targetFile.getAbsolutePath());
                    if (f.isFile()) {
                        tmpTarget = (targetFile.getAbsolutePath() + "/" + f.getName());
                        tmpTargetFile = buildTargetFileName(tmpTarget);
                        if (tmpTargetFile.createNewFile()) {
                            copyFile(f, tmpTargetFile);
                        }
                    }
                    if (f.isDirectory()) {
                        tmpTarget = tmpTarget + "/" + f.getName();
                        tmpTargetFile = new File(tmpTarget);
                        copyAll(f, tmpTargetFile);
                    }
                }
                return true;
            }
            return false;
        } catch (NullPointerException npe) {
            System.out.println("Failed to get info about " + sourceFile);
            npe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean copyData(File sourceFile, File targetFile) {
        try {
            FileInputStream inputFile = new FileInputStream(sourceFile);
            FileOutputStream outputFile = new FileOutputStream(targetFile);
            byte[] bufferSize = new byte[1024];
            int buf;
            while ((buf = inputFile.read(bufferSize)) != -1) {
                outputFile.write(bufferSize, 0, buf);///??? outputFile.write(bufferSize);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static File buildTargetFileName(String s) {
        File targetFile = new File(s);
        String fileName = targetFile.getName();
        if (targetFile.exists()) {
            int delimit = fileName.lastIndexOf(".");
            String extension = fileName.substring(delimit);
            String name = fileName.substring(0, delimit);
            targetFile = new File(targetFile.getParent() + "/" + name + "_" + System.currentTimeMillis() + extension);
        }
        return targetFile;
    }

    // метод по перемещению папок и файлов. Параметр from - путь к файлу или папке, параметр to - путь к папке куда будет производиться перемещение.
    public static boolean move(String from, String to) throws IOException {
        copy(from, to);
        return delete(from);

    }

    private static boolean delete(String path) {
        File pathDelete = new File(path);
        boolean successFlag=false;
        try {
            if (pathDelete.isDirectory()) {
                for (File f : pathDelete.listFiles()) {
                    if (f.isFile()) {
                        f.delete();
                    }
                    if (f.isDirectory()) {
                        delete(f.getAbsolutePath());
                        f.delete();
                    }
                }
            }
            successFlag =pathDelete.delete();
        } catch (NullPointerException npe) {
            System.out.println("Failed to get info about " + path);
            npe.printStackTrace();
        }
        return successFlag;
    }


    public static int calculateFilesOrDirs(File path, String type) {
        int count = 0, countFile = 0, countDir = 0;
        try {
            if (path.isDirectory()) {
                for (File f : path.listFiles()) {
                    if (f.isFile()) {
                        countFile++;
                    }
                    if (f.isDirectory()) {
                        countDir++;
                        count += calculateFilesOrDirs(new File(f.getAbsolutePath()), type);
                    }
                }
            }
        } catch (NullPointerException npe) {
            System.out.println("Failed to get info about " + path);
            npe.printStackTrace();
        }

        switch (type) {
            case "FILE":
                return countFile + count;
            case "DIRECTORY":
                return countDir + count;
            case "ALL":
                return countDir + countFile + count;
            default:
                return -1;
        }
    }

    private static File validatedPath(String path) throws NullPointerException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("Path \"" + path + "\" doesn't exist");
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Path \"" + path + "\" should point to the folder, not to the file");
        }
        return file;
    }
}
