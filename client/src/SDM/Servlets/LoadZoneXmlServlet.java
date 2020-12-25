package SDM.Servlets;

import Engine.SDMInterface;
import SDM.Utils.ServletUtils;
import SDM.Utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Scanner;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadZoneXmlServlet extends HttpServlet {


    private synchronized void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        String username = SessionUtils.getUsername(req);
        SDMInterface sdmManager = ServletUtils.getSDMManager(getServletContext());
        StringBuilder xml = new StringBuilder();
        try {
            Collection<Part> parts = req.getParts();
            for(Part part : parts){
                xml.append(readFromInputStream(part.getInputStream()));
            }
            FileWriter writer = new FileWriter("xml.xml");
            writer.write(xml.toString());
            writer.close();
            File file = new File("xml.xml");
            sdmManager.addNewZone(file,username);
            resp.getOutputStream().write("XML loaded successfully.".getBytes());
        } catch (Exception exp) {
            try {
                resp.getOutputStream().write(exp.getMessage().getBytes());
            } catch (IOException e) { }
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req,resp);
    }
}
