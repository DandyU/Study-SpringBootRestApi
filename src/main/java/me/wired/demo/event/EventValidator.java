package me.wired.demo.event;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice()) {
            errors.rejectValue("basePrice", "wrongValue", "defaultMessage");
            errors.rejectValue("maxPrice", "wrongValue", "defaultMessage");
            errors.reject("wrongPrices", "Value of prices wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore((eventDto.getBeginEventDateTime()))) {
            errors.rejectValue("endEventDateTime", "wrongValue", "defaultMessage");
            errors.reject("wrongendEventDateTime", "Value of prices wrong");
        }

        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        if (beginEventDateTime.isAfter(eventDto.getEndEventDateTime()) ||
                beginEventDateTime.isAfter(eventDto.getCloseEnrollmentDateTime()) ||
                beginEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())) {
            errors.rejectValue("beginEventDateTime", "wrongValue", "defaultMessage");
            errors.reject("wrongbeginEventDateTime", "Value of prices wrong");
        }

        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();
        if (closeEnrollmentDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
                closeEnrollmentDateTime.isBefore(eventDto.getEndEventDateTime()) ||
                closeEnrollmentDateTime.isBefore(eventDto.getBeginEventDateTime())) {
            errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "defaultMessage");
            errors.reject("wrongcloseEnrollmentDateTime", "Value of prices wrong");
        }


    }

}
