/*
 * Directory.java
 * Contains static methods to be able to create a sorted file path
 * for a given file, as well as check if that path exists already or not
 * 
 */

package FilePlacer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import com.google.common.io.Files;

import Main.app_Main;

public class Directory {
        private static String OUTPUT_DIRECTORY;
        private static String OUTPUTDIR_NAME;
        private static final int TYPE_DAY = 1;
        private static final int TYPE_MONTH = 2;
        private static final int TYPE_YEAR = 3;

        private static int dateToInt(LocalDate date, int type) {
                switch (type) {
                        case 1:
                                return date.getDayOfMonth();

                        case 2:
                                return date.getMonthValue();

                        case 3: 
                                return date.getYear();

                        default:
                                System.err.println("No Date Found");
                                return (Integer) null;
                }
        }

        public static void placeSortedFile(File file, LocalDate date) {

                if(date == null) {
                        try {
                                throw new NullPointerException("No Avaliable Date to work with");
                        } catch (Exception e) {
                                System.err.println("No date for file");
                                e.printStackTrace();
                        }
                }

                // fileDirPath is used to create the path for the file to be placed
                File fileDirPath = new File(getSortedDirPath(file, date));
                File newSortedFile = new File(getSortedDirPath(file, date) + file.separator + file.getName());

                try {
                        if(!fileDirPath.exists()) {
                                fileDirPath.mkdirs();
                        }

                        if(!newSortedFile.exists()) {
                                Files.copy(file, newSortedFile);
                        } else {
                                System.out.println("File: " + file.getName() + " -> Already Exists");
                        }
                } catch (Exception e) {
                        System.err.println("Failed to Handle File");
                }
        }

        public static void moveSortedFile(File file, LocalDate date) {
                
                if(date == null) {
                        try {
                                throw new NullPointerException("No Avaliable Date to work with");
                        } catch (Exception e) {
                                System.err.println("No date for file");
                                e.printStackTrace();
                        }
                }

                // fileDirPath is used to create the path for the file to be placed
                File fileDirPath = new File(getSortedDirPath(file, date));
                File newSortedFile = new File(getSortedDirPath(file, date) + file.separator + file.getName());

                try {
                        if(!fileDirPath.exists()) {
                                fileDirPath.mkdirs();
                        }

                        if(!newSortedFile.exists()) {
                                Files.move(file, newSortedFile);
                        } else {
                                System.out.println("File: " + file.getName() + " -> Already Exists");
                        }
                } catch (Exception e) {
                        System.err.println("Failed to Handle File");
                } 
        }

        private static String getSortedDirPath(File file, LocalDate date) {
                String newDirPath;
                String currentDirectory = Directory.OUTPUT_DIRECTORY;
                String outputName = Directory.OUTPUTDIR_NAME;
                
                // Get ints
                int dayInt = dateToInt(date, TYPE_DAY);
                int monthInt = dateToInt(date, TYPE_MONTH);
                int yearInt = dateToInt(date, TYPE_YEAR);

                // Create local path
                String monthStr = date.getMonth().name().toLowerCase();
                monthStr = Character.toUpperCase(monthStr.charAt(0)) + monthStr.substring(1);

                
                String sortedPath = outputName + file.separator + yearInt + file.separator + "(" + monthInt + ") " + monthStr + file.separator + dayInt;
                
                newDirPath = currentDirectory + file.separator + sortedPath;
                return newDirPath;
        }

        public static void setOutDir(String outputPath, String outputName) {
                Directory.OUTPUT_DIRECTORY = outputPath;
                Directory.OUTPUTDIR_NAME = outputName;
        }
}
