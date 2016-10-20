package org.maj.ash.cmp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author shamik.majumdar
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {
    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("/app/index.html");
    }
}
