package Tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import PhotoProcessor.FileGetter.*;

public class SortingTests {
    
   // Record Time 24.9s - 26s, Hard Drive, 69,114 Files
    @Test
    public void fileHDDSortIteration1() {
        String filePath = ""; //"M:\\07 - Coding Projects\\Java Projects\\00 - Current Projects\\Test Files";
        FileGetter fileGetter = new FileGetter(filePath, null, false);
        assertTrue(true);
    }

    // Record 11s, Solid State Drive, 354,712 files
    @Test
    public void fileSDDSortIteration1() {
        String filePath = "/run/media/batlinkjrg/Games (2)/9 - Minecraft Server/00 - Backup/plugins/dynmap/web/tiles/";
        FileGetter fileGetter = new FileGetter(filePath, null, false);

        int files = fileGetter.getPhotoList().size() + fileGetter.getVideoList().size();
        System.out.println("Files Found: " + files);
        assertTrue(files > 1);
    } 

    
   /*  // Record 68.1s
    @Test
    public void fileSortIteration2() {
        String filePath = "J:\\Server BackUp"; //"M:\\07 - Coding Projects\\Java Projects\\00 - Current Projects\\Test Files";
        FileGetter2 fileGetter2 = new FileGetter2(filePath);
        assertTrue(true);
    } */
    
}
