package kr.co.fastcampus.travel.domain.trip.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.co.fastcampus.travel.common.exception.EntityNotFoundException;
import kr.co.fastcampus.travel.domain.itinerary.service.dto.request.save.ItinerarySaveDto;
import kr.co.fastcampus.travel.domain.trip.entity.Trip;
import kr.co.fastcampus.travel.domain.trip.repository.TripRepository;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripSaveDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.request.TripUpdateDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripInfoDto;
import kr.co.fastcampus.travel.domain.trip.service.dto.response.TripItineraryInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TripService {

    private final TripRepository tripRepository;

    @Transactional
    public TripInfoDto addTrip(TripSaveDto dto) {
        Trip trip = tripRepository.save(dto.toEntity());
        return TripInfoDto.from(trip);
    }

    public TripItineraryInfoDto findTripItineraryById(Long id) {
        Trip trip = tripRepository.findFetchItineraryById(id)
                .orElseThrow(EntityNotFoundException::new);
        return TripItineraryInfoDto.from(trip);
    }

    public TripInfoDto findTripById(Long id) {
        Trip trip = findById(id);
        return TripInfoDto.from(trip);
    }

    private Trip findById(Long id) {
        return tripRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<TripInfoDto> findAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        return trips.stream()
                .map(TripInfoDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripInfoDto editTrip(Long tripId, TripUpdateDto dto) {
        Trip trip = findById(tripId);
        Trip updateTrip = dto.toEntity();
        trip.update(updateTrip);
        return TripInfoDto.from(trip);
    }

    @Transactional
    public void deleteTrip(Long id) {
        Trip trip = findById(id);
        tripRepository.delete(trip);
    }

    @Transactional
    public TripItineraryInfoDto addItineraries(Long id, List<ItinerarySaveDto> dto) {
        Trip trip = findById(id);
        dto.stream()
                .map(ItinerarySaveDto::toEntity)
                .forEach(trip::addItinerary);
        return TripItineraryInfoDto.from(trip);
    }
}
