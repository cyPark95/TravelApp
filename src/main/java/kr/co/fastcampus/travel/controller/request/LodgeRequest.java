package kr.co.fastcampus.travel.controller.request;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record LodgeRequest(
    String placeName,
    String address,
    LocalDateTime checkInAt,
    LocalDateTime checkOutAt
) {

}