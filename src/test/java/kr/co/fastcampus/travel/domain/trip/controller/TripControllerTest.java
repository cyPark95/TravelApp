//package kr.co.fastcampus.travel.domain.trip.controller;
//
//import static kr.co.fastcampus.travel.common.RestAssuredUtils.restAssuredGetWithToken;
//import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerary;
//import static kr.co.fastcampus.travel.common.TravelTestUtils.createItinerarySaveRequest;
//import static kr.co.fastcampus.travel.common.TravelTestUtils.createTrip;
//import static kr.co.fastcampus.travel.common.TravelTestUtils.requestDeleteApi;
//import static kr.co.fastcampus.travel.common.TravelTestUtils.requestFindAllTripApi;
//import static kr.co.fastcampus.travel.common.response.Status.FAIL;
//import static kr.co.fastcampus.travel.common.response.Status.SUCCESS;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.SoftAssertions.assertSoftly;
//import static org.junit.jupiter.api.Assertions.assertAll;
//
//import io.restassured.RestAssured;
//import io.restassured.path.json.JsonPath;
//import io.restassured.response.ExtractableResponse;
//import io.restassured.response.Response;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.stream.IntStream;
//import kr.co.fastcampus.travel.common.ApiTest;
//import kr.co.fastcampus.travel.common.response.Status;
//import kr.co.fastcampus.travel.domain.itinerary.controller.dto.request.save.ItinerarySaveRequest;
//import kr.co.fastcampus.travel.domain.itinerary.entity.Itinerary;
//import kr.co.fastcampus.travel.domain.itinerary.repository.ItineraryRepository;
//import kr.co.fastcampus.travel.domain.trip.controller.dto.request.TripSaveRequest;
//import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripResponse;
//import kr.co.fastcampus.travel.domain.trip.controller.dto.response.TripSummaryResponse;
//import kr.co.fastcampus.travel.domain.trip.entity.Trip;
//import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//
//public class TripControllerTest extends ApiTest {
//
//    @Autowired
//    private TripRepository tripRepository;
//
//    @Autowired
//    private ItineraryRepository itineraryRepository;
//
//    @Test
//    @DisplayName("여행 등록")
//    void addTrip() {
//        // given
//        String url = "/api/trips";
//        TripSaveRequest request = new TripSaveRequest(
//            "이름",
//            LocalDate.parse("2010-01-01"), LocalDate.parse("2010-01-02"),
//            false
//        );
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//            .given().log().all()
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .body(request)
//            .when()
//            .post(url)
//            .then().log().all()
//            .extract();
//
//        // then
//        JsonPath jsonPath = response.jsonPath();
//        String status = jsonPath.getString("status");
//        TripResponse data = jsonPath.getObject("data", TripResponse.class);
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//
//        assertSoftly((softly) -> {
//            softly.assertThat(status).isEqualTo("SUCCESS");
//            softly.assertThat(data.id()).isNotNull();
//            softly.assertThat(data.name()).isEqualTo("이름");
//            softly.assertThat(data.startDate().toString()).isEqualTo("2010-01-01");
//            softly.assertThat(data.endDate().toString()).isEqualTo("2010-01-02");
//            softly.assertThat(data.isForeign()).isEqualTo(false);
//        });
//    }
//
//    @Test
//    @DisplayName("여정 없는 여행 조회")
//    void getOnlyTrip() {
//        // given
//        String url = "/api/trips/{tripId}";
//        Trip trip = Trip.builder()
//            .name("여행")
//            .startDate(LocalDate.now())
//            .endDate(LocalDate.now().plusDays(7))
//            .build();
//
//        tripRepository.save(trip);
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//            .given().log().all()
//            .pathParams("tripId", trip.getId())
//            .when().get(url)
//            .then().log().all()
//            .extract();
//
//        // then
//        JsonPath jsonPath = response.jsonPath();
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertAll(
//            () -> assertThat(jsonPath.getString("status")).isEqualTo(Status.SUCCESS.name()),
//            () -> assertThat(jsonPath.getLong("data.id")).isEqualTo(trip.getId()),
//            () -> assertThat(jsonPath.getList("data.itineraries").size()).isEqualTo(0)
//        );
//    }
//
//    @Test
//    @DisplayName("여행 수정")
//    void editTrip() {
//        // given
//        tripRepository.save(
//            Trip.builder().name("이름").startDate(LocalDate.of(2010, 1, 1))
//                .endDate(LocalDate.of(2010, 1, 2)).isForeign(false)
//                .build()
//        );
//
//        String url = "/api/trips/1";
//        TripSaveRequest request = new TripSaveRequest(
//                "이름2",
//                LocalDate.parse("2011-01-01"),
//                LocalDate.parse("2011-01-02"),
//                true
//        );
//
//        // when
//        ExtractableResponse<Response> response = RestAssured
//            .given().log().all()
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .body(request)
//            .when()
//            .put(url)
//            .then().log().all()
//            .extract();
//
//        // then
//        JsonPath jsonPath = response.jsonPath();
//        String status = jsonPath.getString("status");
//        TripResponse data = jsonPath.getObject("data", TripResponse.class);
//
//        assertSoftly((softly) -> {
//            softly.assertThat(status).isEqualTo("SUCCESS");
//            softly.assertThat(data.id()).isNotNull();
//            softly.assertThat(data.name()).isEqualTo("이름2");
//            softly.assertThat(data.startDate()).isEqualTo("2011-01-01");
//            softly.assertThat(data.endDate()).isEqualTo("2011-01-02");
//            softly.assertThat(data.isForeign()).isEqualTo(true);
//        });
//    }
//
//    @Test
//    @DisplayName("여행 목록 조회")
//    void findAll() {
//        // given
//        List<Trip> saveTrips = IntStream.range(0, 2).mapToObj(i -> saveTrip()).toList();
//
//        // when
//        ExtractableResponse<Response> response = requestFindAllTripApi();
//
//        // then
//        JsonPath jsonPath = response.jsonPath();
//        String status = jsonPath.getString("status");
//        List<TripSummaryResponse> data = jsonPath.getList("data", TripSummaryResponse.class);
//
//        assertSoftly(softly -> {
//            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//            softly.assertThat(status).isEqualTo("SUCCESS");
//            softly.assertThat(data.size()).isEqualTo(2);
//            softly.assertThat(data).contains(createTripSummaryResponse(saveTrips.get(0)));
//            softly.assertThat(data).contains(createTripSummaryResponse(saveTrips.get(1)));
//        });
//    }
//
//    private static TripSummaryResponse createTripSummaryResponse(Trip trip) {
//        return new TripSummaryResponse(
//                trip.getId(),
//                trip.getName(),
//                trip.getStartDate(),
//                trip.getEndDate(),
//                trip.isForeign()
//        );
//    }
//
//    @Test
//    @DisplayName("여정 포함 여행 조회")
//    void getContainTrip() {
//        // given
//        Trip trip = Trip.builder()
//            .name("여행")
//            .startDate(LocalDate.now())
//            .endDate(LocalDate.now().plusDays(7))
//            .build();
//
//        IntStream.range(0, 3)
//            .forEach(i -> {
//                Itinerary itinerary = Itinerary.builder().build();
//                itinerary.registerTrip(trip);
//            });
//
//        tripRepository.save(trip);
//
//        String url = "/api/trips/" + trip.getId();
//
//        // when
//        ExtractableResponse<Response> response = restAssuredGetWithToken(url);
//
//        // then
//        JsonPath jsonPath = response.jsonPath();
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertAll(
//            () -> assertThat(jsonPath.getString("status")).isEqualTo(Status.SUCCESS.name()),
//            () -> assertThat(jsonPath.getLong("data.id")).isEqualTo(trip.getId()),
//            () -> assertThat(jsonPath.getList("data.itineraries").size()).isEqualTo(3)
//        );
//    }
//
//    @Test
//    @DisplayName("없는 여행 삭제")
//    void deleteTrip_failureException() {
//        //given
//        String url = "/api/trips/-1";
//
//        //when
//        ExtractableResponse<Response> response = requestDeleteApi(url);
//
//        //then
//        JsonPath jsonPath = response.jsonPath();
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
//        assertThat(jsonPath.getString("status")).isEqualTo(FAIL.name());
//    }
//
//    @Test
//    @DisplayName("여행 삭제")
//    void deleteTrip() {
//        //given
//        Trip trip = saveTrip();
//        String url = "/api/trips/" + trip.getId();
//
//        //when
//        ExtractableResponse<Response> response = requestDeleteApi(url);
//
//        //then
//        JsonPath jsonPath = response.jsonPath();
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//        assertThat(jsonPath.getString("status")).isEqualTo(SUCCESS.name());
//    }
//
//
//
//    @Test
//    @DisplayName("여정 복수 등록")
//    void addItineraries() {
//        // given
//        Trip trip = createTrip();
//        tripRepository.save(trip);
//        String url = "/api/itineraries?tripId=1";
//        List<ItinerarySaveRequest> itineraries = IntStream.range(0, 3)
//                .mapToObj(i -> createItinerarySaveRequest())
//                .toList();
//        ItinerariesSaveRequest request = new ItinerariesSaveRequest(1L, itineraries);
//
//        //when
//        ExtractableResponse<Response> response = RestAssured
//            .given().log().all()
//            .contentType(MediaType.APPLICATION_JSON_VALUE)
//            .body(request)
//            .when()
//            .post(url)
//            .then().log().all()
//            .extract();
//
//        // then
//        JsonPath jsonPath = response.jsonPath();
//        String status = jsonPath.getString("status");
//        TripResponse data = jsonPath.getObject("data", TripResponse.class);
//
//        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
//
//        assertSoftly((softly) -> {
//            softly.assertThat(status).isEqualTo("SUCCESS");
//            softly.assertThat(data.id()).isEqualTo(1);
//            softly.assertThat(data.name()).isEqualTo("tripName");
//            softly.assertThat(data.itineraries().size()).isEqualTo(3);
//        });
//    }
//
//    private Trip saveTrip() {
//        Trip trip = createTrip();
//        return tripRepository.save(trip);
//    }
//
//    private Itinerary saveItinerary(Trip trip) {
//        Itinerary itinerary = createItinerary(trip);
//        return itineraryRepository.save(itinerary);
//    }
//}