package jupiterpi.sitesviewer;

import jupiterpi.sitesviewer.exceptions.PageDoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/scripts/{scriptName}")
    public String getScript(@PathVariable String scriptName) {
        return loader.getScript(scriptName);
    }

    @PostMapping("/stop")
    public void stop() {
        System.exit(0);
    }
}
