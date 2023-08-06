package Main;

import FilePlacer.Directory;
import PhotoProcessor.PhotoSorter_Main;

public class PhotoSorter_API  extends Thread {
        public String inputDir, outputDir, outputName;
        private boolean sortNoDate, moveFile, screenshotFilter;
        private PhotoSorter_Main photoProcessor;
        
        public PhotoSorter_API(String inputDir, String outputDir, String outputName,boolean sortNoDate, boolean moveFile, boolean screenshotFilter) {
                this.inputDir = inputDir;
                this.outputDir = outputDir;
                this.outputName = outputName;
                this.sortNoDate = sortNoDate;
                this.moveFile = moveFile;
                this.screenshotFilter = screenshotFilter;
        }

        public void initializePhotoProcessor() {
                Directory.setOutDir(this.outputDir, this.outputName);
                this.photoProcessor = new PhotoSorter_Main(inputDir, this);
                photoProcessor.startSorter();
                photoProcessor.printResults();
        }

        public synchronized boolean sortNodate() {
                return this.sortNoDate;
        }

        public synchronized boolean moveFile() {
                return this.moveFile;
        }

        public synchronized boolean screenshotFilter() {
                return this.screenshotFilter;
        }

        public synchronized int getFoundFiles() {
                return this.photoProcessor.getFileCount();
        }

        public synchronized int getFileProcessedCount() {
                return this.photoProcessor.getFileProcessedCount();
        }

        public synchronized int getPlacedFiles() {
                return 0;
        }

        @Override
        public void run() {
                initializePhotoProcessor();
        }
}
