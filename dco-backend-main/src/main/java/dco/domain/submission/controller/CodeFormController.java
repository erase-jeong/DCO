package dco.domain.submission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CodeFormController {

    @GetMapping("/code-form")
    public String compileCode() {
        return "code-form";
    }
}
