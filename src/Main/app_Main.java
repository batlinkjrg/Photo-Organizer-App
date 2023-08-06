package Main;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import BasicWindow.BasicWindow_main;

public class app_Main {

    private static JFrame window;

    public static void main(String[] args) {
        window = new BasicWindow_main();
    }

    private static void photoSorter() {
        String outputPath = "/home/batlinkjrg/Desktop/Photo_Sorter_App_Test";
        String inputPath = "/home/batlinkjrg/Desktop/Coding Projects/Java Projects/00 - Current Projects/Test Files";

        PhotoSorter_API sorter = new PhotoSorter_API(inputPath, outputPath, "Sorted Media",true, false, false);
        sorter.initializePhotoProcessor();
    }

    public static JFrame window() {
        return window;
    }
}
