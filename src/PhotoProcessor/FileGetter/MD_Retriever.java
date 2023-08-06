package PhotoProcessor.FileGetter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.tika.exception.*;
import org.xml.sax.SAXException;


public class MD_Retriever extends Thread {
    public static final String TYPE_PHOTO = "photo";
    public static final String TYPE_VIDEO = "video";

    private String fileType;
    private List<File> fileList;
    private Map<File, LocalDate> initialList;
    private List<File> noDateList;
    private int progress = 0;

    public MD_Retriever (List<File> fileList, String type) {
        this.fileList = fileList;
        this.fileType = type;
        // Create the lists 
        this.noDateList = new ArrayList<File>();
        this.initialList = new HashMap<File, LocalDate>();
    }

    @Override
    public void run() {
        try {
            if(this.fileType == "photo") { // If the type is photos use the photo metadata grabber
                createPhotoDateList(fileList);
            } else if (this.fileType == "video") { // If the type is videos use the video metadata grabber
                createVideoDateList(fileList);
            } else { // If there is no type throw an exception
                // Throw exception for invalid type
                throw new Exception("Invalid Type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create a List of photos
    private void createPhotoDateList(List<File> files) throws IOException, SAXException, TikaException, InterruptedException {
        // Create Threads
        int sorterThreads = Runtime.getRuntime().availableProcessors()/2;
        ExecutorService threadPool = Executors.newFixedThreadPool(sorterThreads);
        for(File file : files) {
            PhotoMDRetriever photoRetriever = new PhotoMDRetriever(file, this);
            threadPool.submit(photoRetriever);
        }
        
        threadPool.shutdown();

        do {
            System.out.println("Processing...");
            TimeUnit.MILLISECONDS.sleep(750);
        } while(!threadPool.isTerminated());
    }

    // Create a List of videos with dates
    private void createVideoDateList(List<File> files) throws InterruptedException {
        // create the thread pool for the videos
        int sorterThreads = Runtime.getRuntime().availableProcessors()/2;
        ExecutorService threadPool = Executors.newFixedThreadPool(sorterThreads);

        int fileCount = files.size();
        int filesProcessed = 0;
        for(File currentFile : files) {
            VideoMDRetriever videoMD = new VideoMDRetriever(currentFile, this);
            threadPool.submit(videoMD);
            filesProcessed++;
        }

        threadPool.shutdown();

        do {
            System.out.println("Processing...");
            TimeUnit.MILLISECONDS.sleep(750);
        } while(!threadPool.isTerminated());
        
    }



    // ****** Video Processing ******

    // add videos with date
    public synchronized void addVideosWithDate(File file, LocalDate date) {
        this.initialList.put(file, date);
    }

    // add videos without date
    public synchronized void addVideosWithoutDate(File file) {
        this.noDateList.add(file);
    }



    // ****** Photo Processing ******

    // Add photos to list with their date
    public synchronized void addPhotoWithDate(File file, LocalDate date) {
        this.initialList.put(file, date);
    }

    // Add photos that dont have a date to a list
    public synchronized void addPhotoWithoutDate(File file) {
        this.noDateList.add(file);
    }



    // ***** Extra *****

    // Keep track of progress from the other threads
    public synchronized void addComplete() {
        this.progress++;
    }

    // Print progress percentage
    private static void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10; // 10unit for 100%
        int remainProcent = ((100 * remain) / total) / maxBareSize;
        char defaultChar = '-';
        String icon = "*";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("[");
        for (int i = 0; i < remainProcent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainProcent, bare.length());
        System.out.print("\r" + bareDone + bareRemain + " " + remainProcent * 10 + "%" + " ");
        if (remain == total) {
            System.out.print("\n");
        }
    }

    // Print results
    public final void printResults() {
         // Print out final results
        System.out.println();
        System.out.println("****Process Results****");
        System.out.println();
        System.out.println("Results - ");

        int totalProccessed = this.progress;

        System.out.println("Total " + this.fileType + " -> " + fileList.size());
        System.out.println("Total processed -> " + totalProccessed);
        System.out.println("Amount with date -> " + initialList.size());
        System.out.println("Amount with no date - > " + noDateList.size());
    }

    // Print Lists
    public static final void printList(Map<File, LocalDate> initialList, List<File> noDateList) {
        // Print map
        for (Map.Entry<File, LocalDate> entry : initialList.entrySet()) {
            System.out.println(entry.getKey() + " Date Taken -> " + entry.getValue().toString());
        }

        for (File file : noDateList) {
            System.out.println(file + " ---- No Date ----");
        }
    }

    // Get the list of items with a date
    public final Map<File, LocalDate> getDatedList() {
        return this.initialList;
    }

    // Get the list of items without a date
    public final List<File> getNoDateList() {
        return this.noDateList;
    }

    // Get number of processed files
    public final int getProcessedCount() {
        return this.progress;
    }

    // Generic Meta Data Getter
    public static LocalDate getDate(File file) throws IOException {
        BasicFileAttributes metadata = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        
        String creation = metadata.creationTime().toString();
        String modified = metadata.lastModifiedTime().toString();

        LocalDate fileDate; // To store the date to be returned

        // Get values to compare
        LocalDate creationDate;
        int creation_year = Integer.parseInt(creation.substring(0, 4));
        int creation_month = Integer.parseInt(creation.substring(5, 7));
        int creation_day = Integer.parseInt(creation.substring(8, 10));
        creationDate = LocalDate.of(creation_year, creation_month, creation_day);

        LocalDate modifiedDate;
        int modified_year = Integer.parseInt(modified.substring(0, 4));
        int modified_month = Integer.parseInt(modified.substring(5, 7));
        int modified_day = Integer.parseInt(modified.substring(8, 10)); 
        modifiedDate = LocalDate.of(modified_year, modified_month, modified_day);

        // Decide which date is older and use that
        if (creation_year != modified_year) {
            if(creation_year > modified_year) {
                fileDate = modifiedDate;
            } else {
                fileDate = creationDate;
            }

        } else if (creation_month != modified_month) {

            if(creation_day > modified_day) {
                fileDate = modifiedDate;
            } else {
                fileDate = creationDate;
            }

        } else if (creation_day != modified_day) {

            if(creation_day > modified_day) {
                fileDate = modifiedDate;
            } else {
                fileDate = creationDate;
            }

        } else {
            fileDate = creationDate;
        }
        
        return fileDate;
    }
}

