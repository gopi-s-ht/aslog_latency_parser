package latency_parser.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        return "No resource found";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
