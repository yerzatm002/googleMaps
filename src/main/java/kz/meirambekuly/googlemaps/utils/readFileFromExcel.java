import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Vector;
import kz.meirambekuly.googlemaps.models.Location;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class readFileFromExcel {
    public static void main(String[] args) {
        SpringApplication.run(GoogleMapsApplication.class, args);
        String fileName="C:\\Users\\User\\Downloads\\hacknu-dev-data.xlsx";
        //Read an Excel File and Store in a Vector
        Vector dataHolder=readExcelFile(fileName);
        //Print the data read
        printCellDataToConsole(dataHolder);
    }
    public static void readExcelFile(String fileName)
    {
        /** --Define a Vector
         --Holds Vectors Of Cells
         */

        try{
            /** Creating Input Stream**/
            //InputStream myInput= ReadExcelFile.class.getResourceAsStream( fileName );
            FileInputStream myInput = new FileInputStream(fileName);

            /** Create a POIFSFileSystem object**/

            /** Create a workbook using the File System**/
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            for (int i = 0; i < myWorkBook.getNumberOfSheets(); i++) {

                /** Get the first sheet from workbook**/
                XSSFSheet mySheet = myWorkBook.getSheetAt(i);
                /** We now need something to iterate through the cells.**/
                Iterator rowIter = mySheet.rowIterator();

                while(rowIter.hasNext()){
                    XSSFRow myRow = (XSSFRow) rowIter.next();
                    if (myRow.getCell(0) != null){
                        Iterator cellIter = myRow.cellIterator();
                        Vector cellStoreVector=new Vector();
                        while(cellIter.hasNext()){
                            XSSFCell myCell = (XSSFCell) cellIter.next();
                            cellStoreVector.addElement(myCell.toString());
                        }
                        cellStoreVector.addElement(String.valueOf(i));
                        if(!cellStoreVector.get(0).equals("Latitude") && !cellStoreVector.get(0).equals("") ){
                            double lat = Double.parseDouble(cellStoreVector.get(0));
                            double lng = Double.parseDouble(cellStoreVector.get(1));
                            double alt = Double.parseDouble(cellStoreVector.get(2));
                            String identifier = cellStoreVector.get(3);
                            long timestamp = Long.parseLong(cellStoreVector.get(4));
                            int floor = Integer.parseInt(cellStoreVector.get(5));
                            double horizontal = Double.parseDouble(cellStoreVector.get(6));
                            double vertical = Double.parseDouble(cellStoreVector.get(7));
                            double confidence = Double.parseDouble(cellStoreVector.get(8));
                            int pageNumber = Integer.parseInt(cellStoreVector.get(9));
                            Location location = new Location(lat, lng, alt, identifier, timestamp, floor, horizontal, vertical, confidence, pageNumber);
                        }

                    }

                }


            }

        }catch (Exception e){e.printStackTrace(); }
    }
}