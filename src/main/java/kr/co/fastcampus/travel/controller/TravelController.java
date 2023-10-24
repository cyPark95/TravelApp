package kr.co.fastcampus.travel.controller;

import static kr.co.fastcampus.travel.controller.util.TravelDtoConverter.toTripResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kr.co.fastcampus.travel.common.response.ResponseBody;
import kr.co.fastcampus.travel.controller.request.TripRequest;
import kr.co.fastcampus.travel.controller.response.TripResponse;
import kr.co.fastcampus.travel.entity.Trip;
import kr.co.fastcampus.travel.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TravelController {

    private final TripService tripService;

    @PostMapping("/trips")
    @Operation(summary = "여행 등록")
    public ResponseBody<TripResponse> addTrip(
        @Valid @RequestBody TripRequest request
    ) {
        Trip trip = tripService.addTrip(request);
        TripResponse response = toTripResponse(trip);
        return ResponseBody.ok(response);
    }
}
