package at.fhjoanneum.ippr.gui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouterController {

    @RequestMapping({
        "/",
        "/dashboard",
    })
    public String index() {
        return "forward:/index.html";
    }
}