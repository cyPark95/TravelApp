package kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save;

import java.time.LocalDateTime;
import kr.co.fastcampus.travel.domain.itinerary.entity.Transportation;

public record RouteSaveRequest(
        Transportation transportation,
        String departurePlaceName,
        String departureAddress,
        String destinationPlaceName,
        String destinationAddress,
        LocalDateTime departureAt,
        LocalDateTime arriveAt
) {

}
