package PhotoProcessor.FileGetter;

import java.util.*;

import org.apache.commons.io.FilenameUtils;

import PhotoProcessor.PhotoSorter_Main;

import java.io.*;

public class FileGetter {
    String folderPath;
    java.io.FileFilter photoFilter, videoFilter;
    List<File> folderPhotoFiles;
    List<File> folderVideoFiles;
    private PhotoSorter_Main sorter_Main;

    private boolean isDirectoryPath = false;
    private boolean screenshotFilter = false;

    public FileGetter (String fileSystemPath, PhotoSorter_Main sorter_Main, Boolean screenshotFilter) {
        this.sorter_Main = sorter_Main;
        this.screenshotFilter = screenshotFilter;
        this.folderPhotoFiles = new ArrayList<File>();
        this.folderVideoFiles = new ArrayList<File>();
        createFileFilters();
        
        // Set file path
        this.folderPath = fileSystemPath;
        isDirectoryPath = filepathIsDirectory(fileSystemPath);
        getFileList();

    }

    // Determines how to treat the string, by wether or not it is a directory or single file
    private void getFileList() {
        if(isDirectoryPath) {
            System.out.println("Directory Processing...");
            File fileSystem = new File(this.folderPath);

            this.folderPhotoFiles = new ArrayList<File>();
            this.folderVideoFiles = new ArrayList<File>();

            scanFolder(fileSystem);
        } else {
            System.out.println("Single File Processing...");
            placeFile(this.folderPath);
        }
    }

    // Determine if file is a directory or not
    private boolean filepathIsDirectory (String path) {
        return(new File(path).isDirectory());
    }

    // Place a single file in the proper list
    private void placeFile(String filePath) {
        String filetype = FilenameUtils.getExtension(filePath);
        File file = new File(folderPath);
        switch (filetype) {
            case "png":
                addPhotoFiles(file);
                break;
            case "jpg":
                addPhotoFiles(file);
                break;
            case "gif":
                addPhotoFiles(file);
                break;
            case "heic":
                addPhotoFiles(file);
                break;
            case "mp4":
                addVideoFiles(file);
                break;
            case "mov":
                addVideoFiles(file);
                break;
            case "avi":
                addVideoFiles(file);
                break;

            default:
                System.err.println("FileType Not Supported");
                addPhotoFiles(file); // Just as a failsafe
                break;
        }
    }

    // Scan for photo and video files
    private void addPhotoFiles(File file) {
        String filesPath = file.getPath();
        try {
            // This will scan for all photo files
            this.folderPhotoFiles.add(file);

            //Check to see if it can log file number
            if (sorter_Main != null) {this.sorter_Main.addFileCount();}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create video filters
    private void addVideoFiles(File file) {
        String filesPath = file.getPath();
        try {          
            // This will scan for all video files  
            this.folderVideoFiles.add(file);

            //Check to see if it can log file number
            if (sorter_Main != null) {this.sorter_Main.addFileCount();}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create media filters
    private void createFileFilters() {
        // Filter out photo formats
        this.photoFilter = new java.io.FileFilter() {
            public boolean accept(File directory) {
                // Only allow files of these formats
                if(directory.getName().toLowerCase().contains("screenshot") && !screenshotFilter) {
                    return false;
                } else if (directory.getName().endsWith(".jpg")) {
                    return true;
                } else if (directory.getName().endsWith(".gif")) {
                    return true;
                } else if (directory.getName().endsWith(".heic")) {
                    return true;
                } else if (directory.getName().endsWith(".png")) {
                    return true;
                } else {
                    // Make sure other files aren't included
                    return false;
                }
            }
        };

        // Filter out video formats
        this.videoFilter = new java.io.FileFilter() {
            public boolean accept(File directory) {
                // Only allow files of these formats
                if(directory.getName().endsWith(".mp4")) {
                    return true;
                } else if (directory.getName().endsWith(".mov")) {
                    return true;
                } else if (directory.getName().endsWith(".avi")) {
                    return true;
                } else {
                    // Make sure other files aren't included
                    return false;
                }
            }
        };
    }

    // Scan folders
    private void scanFolder(File folder) {
        scanForPhotoFiles(folder);
        scanForVideoFiles(folder);
    }

    // Scan directory for photos
    private void scanForPhotoFiles(File files) {
        File[] fileDirectory = files.listFiles(this.photoFilter);
        try {
            for (File file: fileDirectory) {
                if (file.isDirectory()) {
                    System.out.println("Directory - " + file.getCanonicalPath());
                    scanForPhotoFiles(file);
                } else {
                    addPhotoFiles(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Scan directory for videos
    private void scanForVideoFiles(File files) {
        File[] fileDirectory = files.listFiles(this.videoFilter);
        try {
            for (File file: fileDirectory) {
                if (file.isDirectory()) {
                    System.out.println("Directory - " + file.getCanonicalPath());
                    scanForVideoFiles(file);
                } else {
                    addVideoFiles(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // This is to print the list of files
    public final void printFileList(List<File> list, String type) {
        // This will print a files list
        System.out.println();
        System.out.println(type + " found -");
        for(File filePath : list) {
            System.out.println(filePath);
        }
        System.out.println();
    }

    // Get this photo list
    public final List<File> getPhotoList() {
        return folderPhotoFiles;
    }

    // Get the video list
    public final List<File> getVideoList() {
        return folderVideoFiles;
    }
}
