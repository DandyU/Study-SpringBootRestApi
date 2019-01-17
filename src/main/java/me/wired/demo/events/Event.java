package me.wired.demo.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import me.wired.demo.accounts.Account;
import me.wired.demo.accounts.AccountSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(of = {"id", "name"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity()
public class Event {
    @Id @GeneratedValue
    private Integer id;
    private boolean offline;
    private boolean free;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account manager;

    public void update() {
        if (this.basePrice == 0 && this.maxPrice == 0) {
            this.free = true;
        } else {
            this.free = false;
        }

        if (this.location == null || this.location.trim().isEmpty()) {
            this.offline = false;
        } else {
            this.offline = true;
        }
    }
}
