package jupiterpi.sitesviewer;

import com.amazonaws.util.IOUtils;
import jupiterpi.sitesviewer.exceptions.PageDoesNotExistException;
import jupiterpi.sitesviewer.filetool.FileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@RestController
@RequestMapping(path = "")
public class Controller {
    @Autowired
    Loader loader;

    @GetMapping("/hello")
    public String hello () {
        return "Hello!";
    }

    @GetMapping("/pages/{pageName}")
    public String getPage(@PathVariable String pageName) throws PageDoesNotExistException {
        return loader.getPage(pageName);
    }

    @GetMapping("/pages/{pageName}/{pathVariables}")
    public String getPageWithPathVariables(@PathVariable String pageName, @PathVariable String[] pathVariables) {
        return loader.getPageWithPathVariables(pageName, pathVariables);
    }

    @GetMapping("/res/{resName}")
    public Object getRes(@PathVariable String resName) {
        return loader.getRes(resName);
    }

    @GetMapping("/pic/{resName}")
    public ResponseEntity<byte[]> getPicture(@PathVariable String resName) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        InputStream in = new FileInputStream(new File(".\\res\\" + resName));
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        byte[] res = IOUtils.toByteArray(in);
        return new ResponseEntity<>(res, headers, HttpStatus.OK);
    }

    @PostMapping("/stop")
    public void stop() {
        System.err.println("SHUTTING DOWN BY REQUEST");
        System.exit(0);
    }
}
