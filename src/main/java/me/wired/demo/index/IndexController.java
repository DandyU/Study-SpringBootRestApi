package me.wired.demo.index;

import me.wired.demo.events.EventController;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public ResourceSupport index() {
        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(EventController.class).withRel("events"));
        return resourceSupport;
    }

}
