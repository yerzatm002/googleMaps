package kz.meirambekuly.googlemaps.services.impl;

import com.google.api.client.util.Maps;
import com.google.gson.stream.JsonReader;
import com.google.firebase.messaging.*;
import kz.meirambekuly.googlemaps.models.Location;
import kz.meirambekuly.googlemaps.models.User;
import kz.meirambekuly.googlemaps.repositories.LocationRepository;
import kz.meirambekuly.googlemaps.repositories.UserRepository;
import kz.meirambekuly.googlemaps.services.LocationService;
import kz.meirambekuly.googlemaps.utils.SecurityUtils;
import kz.meirambekuly.googlemaps.web.dto.LocationsDto;
import kz.meirambekuly.googlemaps.web.dto.ResponseDto;
import kz.meirambekuly.googlemaps.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final SimpMessagingTemplate messagingTemplate;

    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Override
    public ResponseDto<?> saveLocationInDanger(Location location) {
        Optional<User> user = userRepository.findUsersByEmail(SecurityUtils.getCurrentUserLogin());
        if(user.isPresent()){
            Location newLocation = locationRepository.save(location);
            ResponseMessage response = new ResponseMessage(location);
            messagingTemplate.convertAndSend("/topic/messages", response);
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data(newLocation)
                    .build();
        }
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.UNAUTHORIZED.value())
                .errorMessage("UNAUTHORIZED!")
                .build();
    }

    @Override
    public ResponseDto<?> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(locations)
                .build();
    }


    @Override
    public ResponseDto<?> getLocationsByPageNumber(Integer pageNumber) {
        if(pageNumber<=0){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .errorMessage("Invalid page number")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        List<Location> locations = locationRepository.findLocationByPageNumber(pageNumber);
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(locations)
                .build();
    }

    @Override
    public ResponseDto<?> getLocationsByIdentifier(String identifier) {
        if(identifier.isEmpty() || identifier.isBlank()){
            return ResponseDto.builder()
                    .isSuccess(false)
                    .errorMessage("Invalid identifier")
                    .httpStatus(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
        List<Location> locations = locationRepository.findLocationByIdentifier(identifier);
        return ResponseDto.builder()
                .isSuccess(true)
                .httpStatus(HttpStatus.OK.value())
                .data(locations)
                .build();
    }

    @Override
    public ResponseDto<?> saveLocation() {
        if(readExcelFile("C:\\Users\\user\\Desktop\\googleMaps\\src\\main\\resources\\data.xlsx")){
            return ResponseDto.builder()
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK.value())
                    .data("")
                    .build();
        }
        return ResponseDto.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.NO_CONTENT.value())
                .errorMessage("Error importing location!")
                .build();
    }

    public boolean readExcelFile(String fileName)
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
                            double lat = Double.parseDouble((String) cellStoreVector.get(0));
                            double lng = Double.parseDouble((String)cellStoreVector.get(1));
                            double alt = Double.parseDouble((String)cellStoreVector.get(2));
                            String identifier = (String)cellStoreVector.get(3);
                            int timestamp = (int)Double.parseDouble((String)cellStoreVector.get(4));
                            int floor = (int)Double.parseDouble(((String)cellStoreVector.get(5)).equals("null") ? "0" : (String)cellStoreVector.get(5));
                            double horizontal = Double.parseDouble((String)cellStoreVector.get(6));
                            double vertical = Double.parseDouble((String)cellStoreVector.get(7));
                            double confidence = Double.parseDouble((String)cellStoreVector.get(8));
                            String activity = (String)cellStoreVector.get(9);
                            int pageNumber = (int)Double.parseDouble((String)cellStoreVector.get(10));
                            Location location = new Location(lat, lng, alt, identifier, timestamp, floor, horizontal, vertical, confidence,activity, pageNumber);
                            locationRepository.save(location);
                        }
                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
