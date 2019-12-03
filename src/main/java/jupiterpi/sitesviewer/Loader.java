package jupiterpi.sitesviewer;

import jupiterpi.sitesviewer.filetool.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class Loader {
    public String getPage(String pageName) {
        ArrayList<String> pageParts = getPageParts(pageName);
        return itemsToString(pageParts);
    }

    public String getPageWithPathVariables(String pageName, String[] pathVariables) {
        ArrayList<String> pageParts = getPageParts(pageName);

        ArrayList<String> pathVariableLines = new ArrayList<String>();
        ArrayList<String> putPathVaribaleParts = getInternalSource("putPathVariable.html");
        for (String pathVariable : pathVariables) {
            pathVariableLines.add(putPathVaribaleParts.get(0) + pathVariable + putPathVaribaleParts.get(1));
        }
        String pathVariableLinesString = listToString(pathVariableLines);

        pageParts.add(1, pathVariableLinesString);
        return itemsToString(pageParts);
    }

    private ArrayList<String> getPageParts(String pageName) {
        JSONObject pageData = getJSONFromFile(".\\pages\\" + pageName + ".json");

        String containerName = (String) pageData.get("container");
        JSONObject containerData = getJSONFromFile(".\\containers\\" + containerName);
        String containerTopName = (String) containerData.get("top");
        String containerTop = getFileAsString(".\\containers\\" + containerTopName);
        String containerBottomName = (String) containerData.get("bottom");
        String containerBottom = getFileAsString(".\\containers\\" + containerBottomName);

        String pageFileName = (String) pageData.get("content");
        String page = getFileAsString(".\\pages\\" + pageFileName);

        String metaLines = "";
        Map<String, String> meta = (Map<String, String>) pageData.get("meta");
        for (String key : meta.keySet()) {
            String value = meta.get(key);
            ArrayList<String> metaParts = getInternalSource("setMeta.html");
            String metaLine = metaParts.get(0) + key + metaParts.get(1) + value + metaParts.get(2);
            metaLines = metaLine + "\n" + metaLines;
        }

        ArrayList<String> pageParts = new ArrayList<String>();
        pageParts.add(containerTop);
        pageParts.add(metaLines);
        pageParts.add(page);
        pageParts.add(containerBottom);

        return pageParts;
    }

    public ArrayList<String> getInternalSource(String name) {
        return new FileTool(".\\internal\\" + name).getFile();
    }

    public String getRes(String resName) {
        return getFileAsString(".\\res\\" + resName);
    }

    public JSONObject getJSONFromFile(String fileName) {
        FileTool file = new FileTool(fileName);
        String fileString = itemsToString(file.getFile());
        return (JSONObject) JSONValue.parse(fileString);
    }

    public String getFileAsString(String fileName) {
        ArrayList<String> file = new FileTool(fileName).getFile();
        return listToString(file);
    }

    private String itemsToString(ArrayList<String> list) {
        String str = "";
        for (String item : list) {
            str += item;
        }
        return str;
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