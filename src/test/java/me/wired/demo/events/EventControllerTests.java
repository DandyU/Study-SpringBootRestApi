package me.wired.demo.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.wired.demo.common.RestDocsConfiguration;
import me.wired.demo.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest // Web용 Bean만 등록을 해줌
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ModelMapper modelMapper;
    //@MockBean
    //EventRepository eventRepository;
    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("테스트 설명")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                //.id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .beginEventDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .endEventDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                //.free(false)
                //.offline(false)
                //.eventStatus(EventStatus.PUBLISHED)
                .build();
        //Mockito.when(eventRepository.save(events)).thenReturn(events);

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                //.andExpect(jsonPath("id").exists());
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                /*.andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.query-events.href").exists())
                .andExpect(jsonPath("_links.update-events.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())*/
                .andDo(document("create-event",
                        // add link snippet
                        links(linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query event"),
                                linkWithRel("update-events").description("link to update event"),
                                linkWithRel("profile").description("link to profile")),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        //relaxedResponseFields(
                        responseFields(
                                fieldWithPath("id").description("ID of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("free of new event"),
                                fieldWithPath("offline").description("offline of new event"),
                                fieldWithPath("eventStatus").description("eventStatus of new event"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-events.href").description("linke to update-events"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("테스트 설명")
    public void createUnexpectedPropertiesErrorEvent() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .beginEventDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .endEventDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강서구 마곡동")
                .free(false)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();
        //Mockito.when(eventRepository.save(events)).thenReturn(events);


        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                //.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                //.andExpect(header().exists(HttpHeaders.LOCATION))
                //.andExpect(jsonPath("id").exists());
                //.andExpect(jsonPath("id").value(Matchers.not(100)))
                //.andExpect(jsonPath("free").value(Matchers.not(true)))
                //.andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

   @Test
   @TestDescription("프로퍼티 구성이 이상한 경우")
    public void createRequiredPropertiesErrorEvent() throws Exception {
       EventDto eventDto = EventDto.builder().build();


       this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
               .andDo(print())
               .andExpect(status().isBadRequest());
   }

    @Test
    @TestDescription("프로퍼티 값이 이상한 경우")
    public void createRequiredPropertiesErrorEvent2() throws Exception {
        EventDto eventDto = EventDto.builder()
                //.id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .beginEventDateTime(LocalDateTime.of(2019, 1, 31, 1, 2)) // error
                .endEventDateTime(LocalDateTime.of(2019, 1, 8, 1, 2)) // error
                .basePrice(10000) // error
                .maxPrice(200) // error
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                //.andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                //.andExpect(jsonPath("$[0].rejectedValue").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두 번째 페이지 조회하기")
    public void getEvents() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When
        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-events",
                                // add link snippet
                                links(linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile"),
                                linkWithRel("next").description("Move next page"),
                                linkWithRel("last").description("Move last page"),
                                linkWithRel("prev").description("Move previous page"),
                                linkWithRel("first").description("Move first page")),
                                requestParameters(
                                        parameterWithName("page").description("list page"),
                                        parameterWithName("size").description("list length"),
                                        parameterWithName("sort").description("Sort type(e.g. [key],[ASC|DESC])")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                                ),
                                responseFields(
                                        fieldWithPath("_embedded.eventList[0].id").description("ID of new event"),
                                        fieldWithPath("_embedded.eventList[0].name").description("Name of new event"),
                                        fieldWithPath("_embedded.eventList[0].description").description("description of new event"),
                                        fieldWithPath("_embedded.eventList[0].beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                        fieldWithPath("_embedded.eventList[0].closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                        fieldWithPath("_embedded.eventList[0].beginEventDateTime").description("beginEventDateTime of new event"),
                                        fieldWithPath("_embedded.eventList[0].endEventDateTime").description("endEventDateTime of new event"),
                                        fieldWithPath("_embedded.eventList[0].location").description("location of new event"),
                                        fieldWithPath("_embedded.eventList[0].basePrice").description("basePrice of new event"),
                                        fieldWithPath("_embedded.eventList[0].maxPrice").description("maxPrice of new event"),
                                        fieldWithPath("_embedded.eventList[0].limitOfEnrollment").description("limitOfEnrollment of new event"),
                                        fieldWithPath("_embedded.eventList[0].free").description("free of new event"),
                                        fieldWithPath("_embedded.eventList[0].offline").description("offline of new event"),
                                        fieldWithPath("_embedded.eventList[0].eventStatus").description("eventStatus of new event"),
                                        fieldWithPath("_embedded.eventList[0]._links.self.href").description("link to self"),
                                        fieldWithPath("_links.self.href").description("link to self"),
                                        fieldWithPath("_links.profile.href").description("link to profile"),
                                        fieldWithPath("_links.next.href").description("link to next page"),
                                        fieldWithPath("_links.last.href").description("link to last page"),
                                        fieldWithPath("_links.prev.href").description("link to prev page"),
                                        fieldWithPath("_links.first.href").description("link to first page"),
                                        fieldWithPath("page.size").description("page size"),
                                        fieldWithPath("page.totalElements").description("page total elements"),
                                        fieldWithPath("page.totalPages").description("total pages"),
                                        fieldWithPath("page.number").description("number")

                                )
                        )
                )
        ;
    }

    @Test
    @TestDescription("없는 이벤트 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        mockMvc.perform(get("/api/events/{id}", 12345))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("이벤트 한 개 조회하기")
    public void getEvent() throws Exception {
        // Given;
        Event event = this.generateEvent(100);

        // When & Then
        mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-event"))
                ;
    }

    @Test
    @TestDescription("없는 이벤트 조회했을 때 404 응답받기")
    public void updateEvent404() throws Exception {
        // Given
        Integer id = 99999;
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .beginEventDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .endEventDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                //.free(false)
                //.offline(false)
                //.eventStatus(EventStatus.PUBLISHED)
                .build();

        // When & Then
        mockMvc.perform(
                put("/api/events/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("이벤트 수정하기")
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generateEvent(1004);
        EventDto eventDto = modelMapper.map(event, EventDto.class);
        eventDto.setName("New Name");
        eventDto.setDescription("New Description");

        mockMvc.perform(
                put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                //.andExpect(jsonPath("id").exists());
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                /*.andExpect(jsonPath("_links.self.href").exists())
                .andExpect(jsonPath("_links.query-events.href").exists())
                .andExpect(jsonPath("_links.update-events.href").exists())
                .andExpect(jsonPath("_links.profile.href").exists())*/
                .andDo(document("update-event",
                        // add link snippet
                        links(linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query event"),
                                linkWithRel("update-events").description("link to update event"),
                                linkWithRel("profile").description("link to profile")),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        //relaxedResponseFields(
                        responseFields(
                                fieldWithPath("id").description("ID of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("free of new event"),
                                fieldWithPath("offline").description("offline of new event"),
                                fieldWithPath("eventStatus").description("eventStatus of new event"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-events.href").description("linke to update-events"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("Spring-" + i)
                .description("REST API Development with Spring-" + i)
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .beginEventDateTime(LocalDateTime.of(2019, 1, 8, 1, 2))
                .endEventDateTime(LocalDateTime.of(2019, 1, 31, 1, 2))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강서구 마곡동 " + i)
                .free(false)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();
        return this.eventRepository.save(event);
    }

}
