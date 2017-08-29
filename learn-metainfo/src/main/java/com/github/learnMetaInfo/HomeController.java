package com.github.learnMetaInfo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/29 17:40
 * note:
 */
@Controller
public class HomeController {
    @RequestMapping("index")
    public String index() {
        return "session";
    }
}
