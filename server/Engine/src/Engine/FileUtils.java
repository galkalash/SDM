package Engine;

import Engine.generated.*;
import User.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {


    public static SuperDuperMarketDescriptor validateXmlInput(File xmlFile) throws JAXBException {
        if (!xmlFile.exists()) {
            throw new IllegalArgumentException("ERROR. File not exist.");
        }
        if (!xmlFile.getName().endsWith(".xml")) {
            throw new IllegalArgumentException("ERROR. File is not xml file");
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SuperDuperMarketDescriptor SDM = (SuperDuperMarketDescriptor) jaxbUnmarshaller.unmarshal(xmlFile);

        CheckDistinctItemsIDs(SDM);
        CheckDistinctStoresIDs(SDM);
        CheckIfAllStoreItemsSupported(SDM);
        CheckIfEachItemSold(SDM);
        CheckDuplicatesItemsInStores(SDM);
        CheckDistinctLocations(SDM);
        CheckValidDiscounts(SDM);

        return SDM;
    }

    private static void CheckValidDiscounts(SuperDuperMarketDescriptor SDM) {
        HashMap<Integer, Integer> storeItems;
        for (SDMStore store : SDM.getSDMStores().getSDMStore()) {
            if (store.getSDMDiscounts() != null) {
                storeItems = new HashMap<>();
                for (SDMSell item : store.getSDMPrices().getSDMSell()) {
                    storeItems.put(item.getItemId(), item.getItemId());
                }
                for (SDMDiscount discount : store.getSDMDiscounts().getSDMDiscount()) {
                    if (!storeItems.containsKey(discount.getIfYouBuy().getItemId())) {
                        throw new IllegalArgumentException("ERROR. Store with id " + store.getId() + " is not selling the item with id "
                                + discount.getIfYouBuy().getItemId() + " but has a discount on this item.");
                    }
                    for (SDMOffer offer : discount.getThenYouGet().getSDMOffer()) {
                        if (!storeItems.containsKey(offer.getItemId())) {
                            throw new IllegalArgumentException("ERROR. Store with id " + store.getId() + " is not selling the item with id "
                                    + offer.getItemId() + " but has a discount on this item.");
                        }
                    }
                }
            }
        }
    }

    private static void CheckDistinctItemsIDs(SuperDuperMarketDescriptor SDM){
        Map<Integer, SDMItem> TempMap = new HashMap<>();
        for(SDMItem curItem : SDM.getSDMItems().getSDMItem()){
            if(TempMap.containsKey(curItem.getId())){
                throw new IllegalArgumentException("ERROR. There are different products with ID - "+curItem.getId());
            }
            else {
                TempMap.put(curItem.getId(), curItem);
            }
        }
    }

    private static void CheckDistinctStoresIDs(SuperDuperMarketDescriptor SDM){
        Map<Integer, SDMStore> TempMap = new HashMap<>();
        for(SDMStore curStore : SDM.getSDMStores().getSDMStore()){
            if(TempMap.containsKey(curStore.getId())){
                throw new IllegalArgumentException("ERROR. There are different stores with ID - "+curStore.getId());
            }
            else {
                TempMap.put(curStore.getId(), curStore);
            }
        }
    }

    private static void CheckIfAllStoreItemsSupported(SuperDuperMarketDescriptor SDM){
        Map<Integer, SDMItem> TempMap = new HashMap<>();
        for(SDMItem curItem : SDM.getSDMItems().getSDMItem()){
            TempMap.put(curItem.getId(), curItem);
        }

        for(SDMStore curStore : SDM.getSDMStores().getSDMStore()){
            for(SDMSell curSell : curStore.getSDMPrices().getSDMSell()){
                if(!TempMap.containsKey(curSell.getItemId())){
                    throw new IllegalArgumentException("ERROR. The item with ID - "+curSell.getItemId() + " is not supported");
                }
            }
        }
    }

    private static void CheckIfEachItemSold(SuperDuperMarketDescriptor SDM){
        boolean[] itemsExist = new boolean[SDM.getSDMItems().getSDMItem().size()];
        int itemIndex = 0;

        for(SDMItem item : SDM.getSDMItems().getSDMItem()){
            for(SDMStore store : SDM.getSDMStores().getSDMStore()){
                for(SDMSell storeItem : store.getSDMPrices().getSDMSell()){
                    if(item.getId() == storeItem.getItemId()){
                        itemsExist[itemIndex] = true;
                        break;
                    }
                }

                if(itemsExist[itemIndex]){
                    break;
                }
            }
            if(!itemsExist[itemIndex]){
                throw new IllegalArgumentException("ERROR. the item with id: " + item.getId() +" is not sold by any store.");
            }

            itemIndex++;
        }
    }

    private static void CheckDuplicatesItemsInStores(SuperDuperMarketDescriptor SDM){
        int timesExist = 0;
        SDMSell currentSoldItem;
        for(SDMStore store : SDM.getSDMStores().getSDMStore()){
            for(SDMSell soldItem : store.getSDMPrices().getSDMSell()){
                for(SDMSell itemToCompare : store.getSDMPrices().getSDMSell()){
                    if(soldItem.getItemId() == itemToCompare.getItemId()){
                        timesExist++;
                    }
                }
                if(timesExist > 1){
                    throw new IllegalArgumentException("ERROR. item with id: " + soldItem.getItemId() + " exist " + timesExist +" times, in store with id: " + store.getId());
                }
                timesExist = 0;
            }
        }
    }

    private static void CheckDistinctLocations(SuperDuperMarketDescriptor SDM) {
        boolean[][] tempLocations = new boolean[50][50];
        for (SDMStore curStore : SDM.getSDMStores().getSDMStore()) {
            if (curStore.getLocation().getX() > 50 || curStore.getLocation().getX() < 1 ||
                    curStore.getLocation().getY() > 50 || curStore.getLocation().getY() < 1) {
                throw new IllegalArgumentException("ERROR. The store location with ID - " + curStore.getId() + " is invalid");
            } else {
                if (tempLocations[curStore.getLocation().getX() - 1][curStore.getLocation().getY() - 1] == false) {
                    tempLocations[curStore.getLocation().getX() - 1][curStore.getLocation().getY() - 1] = true;
                } else {
                    throw new IllegalArgumentException("ERROR. There are two objects in location (" +
                            curStore.getLocation().getX() + "," + curStore.getLocation().getY() + ").");
                }
            }
        }
    }

    public static Zone buildSDMFromFile(File xml, User zoneOwner) throws JAXBException {
        SuperDuperMarketDescriptor xmlSuper;
        xmlSuper = validateXmlInput(xml);
        Zone newZone = new Zone(xmlSuper.getSDMStores().getSDMStore(), xmlSuper.getSDMItems().getSDMItem(), xmlSuper.getSDMZone().getName() , zoneOwner);
        return newZone;
    }
}
