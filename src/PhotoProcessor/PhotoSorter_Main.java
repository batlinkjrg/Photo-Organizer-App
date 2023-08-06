package PhotoProcessor;

import PhotoProcessor.FileGetter.*;
import java.util.*;

import FilePlacer.Directory;
import Main.PhotoSorter_API;
import java.io.*;
import java.time.LocalDate;

public class PhotoSorter_Main {
    String targetPath;
    private List<File> videoFiles;
    private List<File> photoFiles;

    private Map<File, LocalDate> photoDates, videoDates;
    private List<File> photoNoDates, videoNoDates;

    private MD_Retriever photoMD, videoMD;

    private int totalFiles = 0;
    private int totalProcessedFiles = 0;

    private PhotoSorter_API api;

    public PhotoSorter_Main(String targetPath, PhotoSorter_API api) {
        this.targetPath = targetPath;
        this.api = api;
    }

    public void startSorter() {
        getDates();
    }


    // Scan the given directory for the supported files
    private void searchFiles(String filePathString) {
        FileGetter fileGetter = new FileGetter(filePathString, this, this.api.screenshotFilter());
        videoFiles = fileGetter.getVideoList();
        photoFiles = fileGetter.getPhotoList();
        System.out.println("Total Files -> " + this.totalFiles);
        System.out.println();
    }

    public List getFileList() {
        List<File> fileList = new ArrayList<>();
        fileList.addAll(photoFiles);
        fileList.addAll(videoFiles);
        return fileList;
    }


    // Get all dates from files
    private void getDates() {
        searchFiles(targetPath);
        
        MD_Retriever photo_metadata = prepPhotoMD();
        photo_metadata.start();

        MD_Retriever video_metadata = prepVideoMD();
        video_metadata.start();

        try {
                photo_metadata.join();
                video_metadata.join();
        } catch (Exception e) {
                System.err.println("Fatal Error - Threads failed to merge");
                e.printStackTrace();
        }
        
        getPhotoMD(photo_metadata);
        getVideoMD(video_metadata);
        checkFiles(photo_metadata, video_metadata);
    }


    // If not all files get processed this will be called to reprocess them
    private void reGetDates(MD_Retriever photoMD, MD_Retriever videoMD) {
        photoMD.start();
        videoMD.start();

        try {
                photoMD.join();
                videoMD.join();
        } catch (Exception e) {
                System.err.println("Fatal Error - Threads failed to merge");
                e.printStackTrace();
        }

        getPhotoMD(photoMD);
        getVideoMD(videoMD);
        checkFiles(photoMD, videoMD);
    }


    // Get MetaData from the photos
    private MD_Retriever prepPhotoMD() {
        if (this.photoFiles != null) {
                this.photoMD = new MD_Retriever(this.photoFiles, MD_Retriever.TYPE_PHOTO);
                return this.photoMD;
        } else {
                System.err.println("No Photo Files Found");
                return null;
        }
    }

    private void getPhotoMD(MD_Retriever photo_metadata) {
        photoDates = photo_metadata.getDatedList();
        photoNoDates = photo_metadata.getNoDateList();
        totalProcessedFiles = totalProcessedFiles + photo_metadata.getProcessedCount();
    }


    // Get Meta Data from the videos
    private MD_Retriever prepVideoMD() {
        if (this.videoFiles != null) {
                this.videoMD = new MD_Retriever(this.videoFiles, MD_Retriever.TYPE_VIDEO); 
                return this.videoMD;
        } else {
                System.err.println("No Video Files Found");
                return null;
        }
    }

    private void getVideoMD(MD_Retriever video_metadata) {
        videoDates = video_metadata.getDatedList();
        videoNoDates = video_metadata.getNoDateList(); 
        totalProcessedFiles = totalProcessedFiles + video_metadata.getProcessedCount();
    }
    

    // Add to the file counter
    public synchronized void addFileCount() {
        this.totalFiles = this.totalFiles+1;
    }

    public int getFileCount() {
        return this.totalFiles;
    }

    public int getFileProcessedCount() {
        return this.photoMD.getProcessedCount() + this.videoMD.getProcessedCount();
    }


    // Verify all files got processed
    private void checkFiles(MD_Retriever photoMD, MD_Retriever videoMD) {
        if (this.totalProcessedFiles != this.totalFiles) {
                try {
                        throw new Exception("Not all files processed");
                } catch (Exception e) {
                       System.err.println("Restarting Process");
                } finally {
                        reGetDates(photoMD, videoMD);
                }
                
        } else {
                photoMD.printResults();
                videoMD.printResults();
                System.out.println();
                System.out.println("Process Successful, total files processed -> " + this.totalProcessedFiles);
                System.out.println();
                placeFiles(photoDates, photoNoDates);
                placeFiles(videoDates, videoNoDates);
        }
    }


    // File Placement
    private void placeFiles(Map<File, LocalDate> datedList, List<File> nondatedList) {

        for (Map.Entry<File, LocalDate> entry : datedList.entrySet()) {
            placeDateFiles(entry.getKey(), entry.getValue());
        }

        if(this.api.sortNodate()) {

            for (File file : nondatedList) {
                placeNoDateFiles(file);
            }

        }

    }

    private void placeDateFiles(File file, LocalDate date) {
        System.out.println("Placing File: " + file.getName());

        if(this.api.moveFile()) {
            Directory.moveSortedFile(file, date);
        } else {
            Directory.placeSortedFile(file, date);
        }

    }

    private void placeNoDateFiles(File file) {
        System.out.println("Placing NonDated File: " + file.getName());
        LocalDate date = null;

        try {
            date = MD_Retriever.getDate(file);
        } catch (IOException e) {
            System.err.println("Unable to Process Undated File");
            e.printStackTrace();
        }

        if(this.api.moveFile()) {
            Directory.moveSortedFile(file, date);
        } else {
            Directory.placeSortedFile(file, date);
        }

    }


    // Results
    public void printResults() {
        int noDateAmount = videoNoDates.size() + photoNoDates.size();
        int withDateAmount = videoDates.size() + photoDates.size();

        System.out.println();
        System.out.println("***** Final Results *****");
        System.out.println("Total Files Found: " + this.totalFiles);
        System.out.println("Total Files Processed: " + this.totalProcessedFiles);
        System.out.println("Total With Date: " + withDateAmount);
        System.out.println("Total Without Date: " + noDateAmount);
        System.out.println("Output Directory is: " + this.api.outputDir + File.separator + this.api.outputName);
        System.out.println();
    }
}