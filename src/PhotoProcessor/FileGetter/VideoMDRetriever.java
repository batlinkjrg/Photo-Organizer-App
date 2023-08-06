/* VideoMDRetriever.java
 * 
 * Video Meta Data Retriever Class
 * Determines file type 
 * Uses InputStreams to read a file
 * Determines metadata type
 * Extracts MetaData
 * If Metadata doesn't exist or is invalid, file is considered dateless
 * 
 * Version 1
 * by Jonathan Gilliland
 */

package PhotoProcessor.FileGetter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;

import org.apache.commons.io.FilenameUtils;
import com.drew.imaging.mp4.Mp4Reader;
import com.drew.imaging.quicktime.QuickTimeReader;
import com.drew.imaging.riff.RiffProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.mov.QuickTimeAtomHandler;
import com.drew.metadata.mp4.Mp4BoxHandler;

public class VideoMDRetriever implements Runnable {

    private File videoFile;
    private LocalDate videoDate;
    private MD_Retriever md_Retriever;

    public VideoMDRetriever (File file, MD_Retriever md_Retriever) {
        this.videoFile = file;
        this.md_Retriever = md_Retriever;   
    }

    // Thread action
    @Override
    public void run() {
        try {
            videoMDReader(this.videoFile);
        } catch (Exception e) {
            md_Retriever.addVideosWithoutDate(videoFile);
            md_Retriever.addComplete();
            System.err.println("This file was unable to be processed");
            System.err.println("File added to no date list");
            System.err.println("Error... ->" + e.getCause());
        }
    }

    // Determine File Type
    private void videoMDReader(File file) throws IOException, RiffProcessingException {
        InputStream videoStream = new FileInputStream(file);
        String fileType = getFileType(file.toString());
        Metadata metadata = new Metadata();
        switch (fileType) {
            case "mp4":
                Mp4Reader.extract(videoStream, new Mp4BoxHandler(metadata));
                getMP4_MD(metadata);
                md_Retriever.addComplete();
                break;
            
            case "avi":
                BasicFileAttributes aviMD = Files.readAttributes(this.videoFile.toPath(), BasicFileAttributes.class);
                getFile_Date(aviMD);
                md_Retriever.addComplete();
                break;

            case "mov":
                QuickTimeReader.extract(videoStream, new QuickTimeAtomHandler(metadata));
                getMOV_MD(metadata);
                md_Retriever.addComplete();
                break;

            default:
                System.out.println("Not a supported video");
                md_Retriever.addVideosWithoutDate(this.videoFile);
                md_Retriever.addComplete();
                break;
        }
    }

    // Use this to check file type
    private String getFileType(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    // Get mov file metadata
    private void getMOV_MD(Metadata metadata) {
        // Parse metadata to get date
        String dateTag = "Not Avaliable";
        for (Directory directory : metadata.getDirectories()) { // searches list of directories 
            if (directory.getName() == "QuickTime") {
                for (Tag tag : directory.getTags()) { // searches directory if it is found
                    if(tag.toString().contains("Creation Time")) {
                       dateTag = tag.toString(); 
                       break;
                    } 
                }
            }
        }       

        // Get date
        // Get day and year and month from string

        int day = getDay(dateTag.substring(28, 31));
        int year = Integer.parseInt(dateTag.substring(55, 59));
        int month = getMonth(dateTag.substring(32, 35));

        // Check the year to make sure its not so old its unreasonable
        if(year <= 1990) {
            md_Retriever.addVideosWithoutDate(videoFile); 
            System.out.println("Date of Video -> Not Avaliable");
        } else {
            // create date from ints and save it
            this.videoDate = LocalDate.of(year, month, day);
            md_Retriever.addVideosWithDate(videoFile, videoDate);
            System.out.println("Date of " + videoFile.getName() + " -> " + this.videoDate);
        }
    }

    // Get avi file metadata and generic dates
    private void getFile_Date(BasicFileAttributes metadata) {
        String creation = metadata.creationTime().toString();
        String modified = metadata.lastModifiedTime().toString();

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
                this.videoDate = modifiedDate;
            } else {
                this.videoDate = creationDate;
            }
        } else if (creation_month != modified_month) {
            if(creation_day > modified_day) {
                this.videoDate = modifiedDate;
            } else {
                this.videoDate = creationDate;
            }
        } else if (creation_day != modified_day) {
            if(creation_day > modified_day) {
                this.videoDate = modifiedDate;
            } else {
                this.videoDate = creationDate;
            }
        } else {
            this.videoDate = creationDate;
        }

        md_Retriever.addVideosWithDate(videoFile, this.videoDate);
        System.out.println("(AVI or Generic) Date of " + videoFile.getName() + " -> " + this.videoDate);
    }

    // Get mp4 file metadata
    private void getMP4_MD(Metadata metadata) {
        // Parse metadata to get date
        String dateTag = "Not Avaliable";
        for (Directory directory : metadata.getDirectories()) {
            if (directory.getName() == "MP4") {
                for (Tag tag : directory.getTags()) {
                    if(tag.toString().contains("Creation Time")) {
                       dateTag = tag.toString(); 
                       break;
                    }  
                }
            }
        }

        // Get date
        if (dateTag != "Not Avaliable") {
            // Get day and year and month from string
            int day = Integer.parseInt(dateTag.substring(30, 32));
            int year = Integer.parseInt(dateTag.substring(46, 50));
            int month = getMonth(dateTag.substring(26, 29));

            // Check year and make sure its not unreasonable
            if(year <= 1990) {
                md_Retriever.addVideosWithoutDate(videoFile);
                System.out.println("Date of Video -> Not Avaliable");
            } else {
                // create date from ints and save it
                this.videoDate = LocalDate.of(year, month, day);
                md_Retriever.addVideosWithDate(videoFile, videoDate);
                System.out.println("Date of Video " + videoFile.getName() + " -> " + this.videoDate);
            }
        } else {
            md_Retriever.addVideosWithoutDate(this.videoFile);
            System.out.println("(MP4) Date of Video " + videoFile.getName() + " -> " + dateTag);
        }
    }
    
    // Print metadata
    private void printMD(Metadata metadata) {
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag);
            }
        }
    }

    // Convert month string to int
    private final int getMonth(String month) {
        int monthInt;
        switch (month) {
            case "Jan":
                return monthInt = 1;
        
            case "Feb":
                return monthInt = 2;

            case "Mar":
                return monthInt = 3;

            case "Apr":
                return monthInt = 4;

            case "May":
                return monthInt = 5;
            
            case "Jun":
                return monthInt = 6;
            
            case "Jul":
                return monthInt = 7;

            case "Aug":
                return monthInt = 8;

            case "Sep":
                return monthInt = 9;

            case "Oct":
                return monthInt = 10;

            case "Nov":
                return monthInt = 11;

            case "Dec":
                return monthInt = 12;

            default:
                return monthInt = 0;
        }
    }
    
    // Convert day string to int
    private final int getDay(String day) {
        int dayInt;
        switch (day) {
            case "Sun":
                return dayInt = 1;
            case "Mon":
                return dayInt = 2;

            case "Tue":
                return dayInt = 3;

            case "Wed":
                return dayInt = 4;

            case "Thu":
                return dayInt = 5;
            
            case "Fri":
                return dayInt = 6;
            
            case "Sat":
                return dayInt = 7;

            default:
                return dayInt = 0;
        }
    }
}
