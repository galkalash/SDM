package SDM.Servlets;

import Dtos.ItemFromJsonDto;
import Dtos.MapLocationDto;
import Dtos.StoreDto;
import Dtos.UserDto;
import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;
import User.UserManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CreateNewStoreServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SDMInterface sdmInterface = ServletUtils.getSDMManager(getServletContext());
        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        Gson gson = new Gson();

        UserDto storeOwner = userManager.getUserByName(SessionUtils.getUsername(req));
        String zoneName = req.getParameter("zone");
        int storeId = Integer.parseInt(req.getParameter("storeId"));
        String storeName = req.getParameter("storeName");
        int xLocation = Integer.parseInt(req.getParameter("xLocation"));
        int yLocation = Integer.parseInt(req.getParameter("yLocation"));
        float ppk = Float.parseFloat(req.getParameter("PPK"));
        String itemsJson = req.getParameter("items");

        StoreDto newStore = new StoreDto(storeId,storeName,ppk,new MapLocationDto(xLocation,yLocation),storeOwner);
        ItemFromJsonDto[] items = gson.fromJson(itemsJson, ItemFromJsonDto[].class);
        sdmInterface.addNewStoreToZone(zoneName,newStore, Arrays.asList(items));
    }
}
