package me.wired.demo.event;

        import org.modelmapper.ModelMapper;
        import org.springframework.hateoas.MediaTypes;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestMapping;

        import java.net.URI;

        import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
        import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @PostMapping//("/api/events")
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) {
        /*Event event = Event.builder()
                .name(eventDto.getName())
                .description((eventDto.getDescription()))
                .build();*/
        Event event = modelMapper.map(eventDto, Event.class);

        Event newEvent = this.eventRepository.save(event);
        //URI createdUri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();
        //URI createdUri = linkTo(EventController.class).slash("{id}").toUri();
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        return ResponseEntity.created(createdUri).body(newEvent);
    }
}
