package me.wired.demo.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(JUnitParamsRunner.class)
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
    // # 1
    /*@Parameters({
            "0, 0, true",
            "100, 0, false",
            "0, 100, false",
    })*/
    // # 2
    //@Parameters(method = "paramsForTestFree")
    // # 3
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // When
        event.update();
        // Then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    // convention prefix -> "parametersFor"
    private Object[] parametersForTestFree() {
        return new Object[] {
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 200, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();
        // When
        event.update();
        // Then
        AssertionsForClassTypes.assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline() {
        return new Object[] {
                new Object[] {"마곡", true},
                new Object[] {null, false},
                new Object[] {"              ", false}
        };
    }
}
