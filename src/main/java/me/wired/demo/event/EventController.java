package me.wired.demo.event;

        import org.modelmapper.ModelMapper;
        import org.springframework.hateoas.MediaTypes;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Controller;
        import org.springframework.validation.Errors;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestMapping;

        import javax.validation.Valid;
        import java.net.URI;

        import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
        import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }


    // @Valid Annotation을 사용하면 오브젝트에 바인딩할 때 검증을 구행할 수 있음
    @PostMapping//("/api/events")
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

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
