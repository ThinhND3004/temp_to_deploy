package personal.kayden.temp_to_deploy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
    @GetMapping
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/cc")
    public String cc() {
        return "Hello World Con Cac REDEPLOY!";
    }

}
