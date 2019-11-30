package jupiterpi.sitesviewer;

import jupiterpi.sitesviewer.exceptions.PageDoesNotExistException;
import jupiterpi.sitesviewer.filetool.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class Loader {
    private String containerTopFileName = ".\\main\\container_top.html";
    private String containerBottomFileName = ".\\main\\container_bottom.html";

    public String getPage(String pageName) throws PageDoesNotExistException {
        String containerTopString = getFileAsString(containerTopFileName);
        String containerBottomString = getFileAsString(containerBottomFileName);
        ArrayList<String> pageFile = new FileTool(".\\pages\\" + pageName + ".html").getFile();
        String idLine = pageFile.get(0);
        ArrayList<String> callMeta = new FileTool(".\\main\\callMeta.html").getFile();
        pageFile.set(0, callMeta.get(0) + pageName + callMeta.get(1));
        String pageString = listToString(pageFile);
        return containerTopString + pageString + containerBottomString;
    }

    public String getMeta(String pageName) throws PageDoesNotExistException {
        return getFileAsString(".\\meta\\" + pageName + ".json");
    }

    public String getScript(String scriptName) {
        return getFileAsString(".\\scripts\\" + scriptName);
    }

    public String getFileAsString(String fileName) {
        ArrayList<String> file = new FileTool(fileName).getFile();
        return listToString(file);
    }

    private String listToString(ArrayList<String> list) {
        String str = "";
        if (list.size() == 0) return "";
        str = list.get(0);
        if (list.size() == 1) return str;
        for (int i = 1; i < list.size(); i++) {
            str += "\n" + list.get(i);
        }
        return str;
    }
}