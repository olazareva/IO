package ua.lazareva.filemanager;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileManagerTest {
    private String path = "/home/olga/IdeaProjects/ThirdStep/resources/testfilemanager";

    @Test
    public void calculateFiles() {
        System.out.println(FileManager.calculateFiles(path));
    }

    @Test(expected = NullPointerException.class)
    public void calculateFilesNPException() {
        FileManager.calculateFiles(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateFilesIAException() {
        FileManager.calculateFiles(path +"/test.txt");
    }

    @Test
    public void calculateDirs() {
        System.out.println(FileManager.calculateDirs(path));
    }

    @Test(expected = NullPointerException.class)
    public void calculateDirsNPException() {
        FileManager.calculateDirs(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void calculateDirsIAException() {
        FileManager.calculateDirs(path +"/test.txt");
    }


    String from = path + "/111";
    String to = path +"/333";
    @Test
    public void copy() {
        try {
            assertTrue(FileManager.copy(from, to));
            assertEquals(FileManager.calculateFilesOrDirs(new File(from), "ALL") + 1,
                    FileManager.calculateFilesOrDirs(new File(to), "ALL"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void move() {
        try {
           // assertTrue(FileManager.copy(from, to));
            assertTrue(FileManager.move(to, from));
            assertEquals(0,
                    FileManager.calculateFilesOrDirs(new File(to), "ALL"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}