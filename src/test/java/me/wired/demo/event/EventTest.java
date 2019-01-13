package me.wired.demo.event;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder().build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        Event event = Event.builder()
                .id(new Integer(1))
                .offline(false)
                .free(true)
                .eventStatus(EventStatus.STARTED)
                .name("홍길동")
                .description("설명")
                .beginEnrollmentDateTime(LocalDateTime.now())
                .closeEnrollmentDateTime(LocalDateTime.now())
                .beginEventDateTime(LocalDateTime.now())
                .endEventDateTime(LocalDateTime.now())
                .location(null)
                .basePrice(0)
                .maxPrice(0)
                .limitOfEnrollment(0).build();
        assertThat(event.getName().equals("홍길동"));
    }

    @Test
    public void testFree() {
        // Given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        // When
        event.update();
        // Then
        AssertionsForClassTypes.assertThat(event.isFree()).isTrue();

        // Given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        // When
        event.update();
        // Then
        AssertionsForClassTypes.assertThat(event.isFree()).isFalse();

        // Given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        // When
        event.update();
        // Then
        AssertionsForClassTypes.assertThat(event.isFree()).isFalse();
    }

    @Test
    public void testOffline() {
        // Given
        Event event = Event.builder()
                .location("마곡")
                .build();
        // When
        event.update();
        // Then
        AssertionsForClassTypes.assertThat(event.isOffline()).isTrue();

        // Given
        event = Event.builder()
                .build();
        // When
        event.update();
        // Then
        AssertionsForClassTypes.assertThat(event.isOffline()).isFalse();

    }
}
