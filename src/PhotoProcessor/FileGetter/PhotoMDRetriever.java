package PhotoProcessor.FileGetter;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.imageio.ImageIO;
import java.awt.image.*;
import org.apache.tika.parser.*;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.metadata.*;
import org.apache.tika.exception.*;
import org.apache.tika.sax.*;
import org.xml.sax.SAXException;


public final class PhotoMDRetriever implements Runnable {
    private File photoFile;
    private LocalDate photoDate;
    private MD_Retriever md_Retriever;

    public PhotoMDRetriever (File file, MD_Retriever md_Retriever) {
        this.photoFile = file;
        this.md_Retriever = md_Retriever;   

    }

    // Thread action
    @Override
    public void run() {
        try {
            photoMDReader(this.photoFile);
            md_Retriever.addComplete();
        } catch (IOException | SAXException | TikaException e) {
            System.out.println("This file was unable to be processed");
            md_Retriever.addPhotoWithoutDate(this.photoFile);
            md_Retriever.addComplete();
            e.printStackTrace();
        }
    }
    
    // Access date of photo
    private final void photoMDReader(File file) throws IOException, SAXException, TikaException {
        // Needed to disable tika ORC which really slows down metadata scan
        TesseractOCRConfig config = new TesseractOCRConfig();
        config.setSkipOcr(true);
        ParseContext context = new ParseContext();
        context.set(TesseractOCRConfig.class, config);

        // MetaData Parser creation
        BasicFileAttributes basicData = Files.readAttributes(file.toPath(), BasicFileAttributes.class); // Basic File info
        Parser fileParser = new AutoDetectParser(); 
        BodyContentHandler contentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        
        // Load image into ram for processing
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        InputStream inputstream = new ByteArrayInputStream(fileBytes);

        // FileInputStream inputstream = new FileInputStream(file);
        // Parse the File
        fileParser.parse(inputstream, contentHandler, metadata, context); // In-depth file info

        // Read out metadata info
        scanMetaData(metadata, file, inputstream, fileBytes);
        inputstream.close();
    }

    // Scan the metadata for the date
    private final void scanMetaData(Metadata metadata,File file, InputStream inputstream, byte[] fileBytes) {
        // Determine date
        if (metadata.get("dcterms:created") != null) {
            // Get date from file
            String date = metadata.get("dcterms:created").toString();
            date = date.substring(0, 10);
            
            // Print date for verification
            System.out.println(file.getName() + " ---- DateTaken -> " + date);

            // Take date from string and convert it into a date variable
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            this.photoDate = LocalDate.parse(date, formatter);

            // Add to list of photos with a date
            md_Retriever.addPhotoWithDate(file, photoDate);
        } else {
            // If the photo doesn't have a date add it to a list of photos without a date
            System.out.println(file.getName() + " ---- Date not avaliable ----");
            
            md_Retriever.addPhotoWithoutDate(file);
        }
    }

    // Add to image Ai 
    // Currently needs to be made
    private final void addToAi(byte[] fileBytes) {
        BufferedImage image = createImageFromBytes(fileBytes);

        

    }

    // Take an image and Buffer it in memory
    private BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
