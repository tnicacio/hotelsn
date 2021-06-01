package com.tnicacio.seniorhotel.services;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnicacio.seniorhotel.dto.BookingDTO;
import com.tnicacio.seniorhotel.entities.Booking;
import com.tnicacio.seniorhotel.entities.Garage;
import com.tnicacio.seniorhotel.entities.Room;
import com.tnicacio.seniorhotel.repositories.BookingRepository;
import com.tnicacio.seniorhotel.repositories.GarageRepository;
import com.tnicacio.seniorhotel.repositories.PersonRepository;
import com.tnicacio.seniorhotel.repositories.RoomRepository;
import com.tnicacio.seniorhotel.services.exceptions.DatabaseException;
import com.tnicacio.seniorhotel.services.exceptions.ResourceNotFoundException;

@Service
public class BookingService {
	
	private static final double DAILY_PRICE_ON_WEEKDAYS = 120.0;
	private static final double DAILY_PRICE_ON_WEEKENDS = 150.0;
	private static final double GARAGE_PRICE_ON_WEEKDAYS = 15.0;
	private static final double GARAGE_PRICE_ON_WEEKENDS = 20.0;
	private static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(-3);
	private static final OffsetTime TIME_LIMIT_TO_CHECKOUT = OffsetTime.of(16, 30, 0, 0, ZONE_OFFSET);
	
	@Autowired
	BookingRepository repository;
	
	@Autowired
	PersonRepository personRepository;

	@Autowired
	RoomRepository roomRepository;
	
	@Autowired
	GarageRepository garageRepository;

	@Transactional
	public BookingDTO insert(BookingDTO BookingDto) {
		try {
			Booking entity = new Booking();
			copyDtoToEntity(BookingDto, entity);
			
			Double expectedPrice = calculatePrice(
					entity.getStartDate(),
					entity.getEndDate(),
					entity.getGarage() != null);
			entity.setExpectedPrice(expectedPrice);

			entity = repository.save(entity);
			return new BookingDTO(entity);
		} catch (ConstraintViolationException e) {
			throw new DatabaseException("Constraint violation");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}	
	}

	@Transactional(readOnly = true)
	public Page<BookingDTO> findAll(Pageable pageable) {
		Page<Booking> page = repository.findAll(pageable);
		return page.map(booking -> new BookingDTO(booking));
	}

	@Transactional(readOnly = true)
	public BookingDTO findById(long id) {
		Optional<Booking> optionalBooking = repository.findById(id);
		Booking entity = optionalBooking.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new BookingDTO(entity);
	}

	@Transactional
	public BookingDTO update(Long id, BookingDTO BookingDto) {
		try {
			Booking entity = repository.getOne(id);
			copyDtoToEntity(BookingDto, entity);
			entity = repository.save(entity);
			return new BookingDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(long id) {
		try {
			repository.deleteById(id);					
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}		
	}
	
	@Transactional(readOnly = true)
	public Page<BookingDTO> checkedIn(Pageable pageable) {
		return repository.checkedIn(pageable).map(bk -> new BookingDTO(bk));
	}

	@Transactional(readOnly = true)
	public Page<BookingDTO> checkedOut(Pageable pageable) {
		return repository.checkedOut(pageable).map(bk -> new BookingDTO(bk));
	}

	@Transactional(readOnly = true)
	public Page<BookingDTO> current(Pageable pageable) {
		return repository.current(pageable).map(bk -> new BookingDTO(bk));
	}

	@Transactional(readOnly = true)
	public Page<BookingDTO> open(Pageable pageable) {
		return repository.open(pageable).map(bk -> new BookingDTO(bk));
	}
	
	@Transactional
	public BookingDTO checkIn(long id) {
		try {
			Booking entity = repository.getOne(id);
			if (entity.getDtCheckin() != null) {
				throw new DatabaseException("Booking already confirmed");
			}
			entity.setDtCheckin(Instant.now());
			
			entity.getRoom().setIsAvailable(false);
			if (entity.getGarage() != null) {
				entity.getGarage().setIsAvailable(false);
			}

			entity = repository.save(entity);
			return new BookingDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	@Transactional
	public BookingDTO checkOut(Long id) {
		try {
			Booking entity = repository.getOne(id);
			if (entity.getDtCheckout() != null) {
				throw new DatabaseException("Booking already completed");
			}
			if (entity.getDtCheckin() == null) {
				throw new DatabaseException("Missing Check-in");
			}
			
			Instant dtCheckout = Instant.now();
			
			entity.setDtCheckout(dtCheckout);
			Double realPrice = calculatePrice(entity.getDtCheckin(), 
					dtCheckout,
					entity.getGarage() != null);
			entity.setRealPrice(realPrice);
			
			entity.getRoom().setIsAvailable(true);
			if (entity.getGarage() != null) {
				entity.getGarage().setIsAvailable(true);
			}
			
			entity = repository.save(entity);
			return new BookingDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}
	
	@Transactional(readOnly = true)
	public Page<BookingDTO> findAllByPersonId(Long id, Pageable pageable){
		return repository.findAllByPersonId(id, pageable).map(repo -> new BookingDTO(repo));
	}

	private void copyDtoToEntity(BookingDTO dto, Booking entity) {
		entity.setStartDate(dto.getStartDate());
		entity.setEndDate(dto.getEndDate());
		entity.setDtCheckin(dto.getDtCheckin());
		entity.setDtCheckout(dto.getDtCheckout());
		entity.setExpectedPrice(dto.getExpectedPrice());
		entity.setRealPrice(dto.getRealPrice());
		
		entity.setPerson(personRepository.getOne(dto.getPersonId()));
		
		Room room = roomRepository.getOne(dto.getRoomId());
		entity.setRoom(validRoom(room));
		if (dto.getGarageId() != null) {
			Garage garage = garageRepository.getOne(dto.getGarageId());
			entity.setGarage(validGarage(garage));			
		}
	}
	
	private Garage validGarage(Garage entity) {
		if (entity != null && !entity.getIsAvailable()){
			throw new DatabaseException("Garage selected is Not Available!");
		}
		return entity;
	}

	private Room validRoom(Room entity) {
		if (entity != null && !entity.getIsAvailable()){
			throw new DatabaseException("Room selected is Not Available!");
		}
		return entity;
	}

	private boolean passedTimeLimitOnDay(Instant day) {
		OffsetTime timeOfTheDay = OffsetTime.ofInstant(day, ZONE_OFFSET);
		return timeOfTheDay.isAfter(TIME_LIMIT_TO_CHECKOUT);
	}
	
	private Map<String, Long> countBusinessAndWeekendDaysBetween(Instant startDate, Instant endDate){        
        long daysBetweenWithLastDayIncluded = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        
        if (passedTimeLimitOnDay(endDate)) {
        	daysBetweenWithLastDayIncluded += 1;
        }
        
        Predicate<Instant> isWeekend = date -> {
        	DayOfWeek dayOfWeek = date.atOffset(ZONE_OFFSET).getDayOfWeek();
        	return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
        };
 
        long businessDays = Stream.iterate(startDate, date -> date.plus(1, ChronoUnit.DAYS))
        		.limit(daysBetweenWithLastDayIncluded)
                .filter(isWeekend.negate()).count();
        long weekendDays = daysBetweenWithLastDayIncluded - businessDays;
        
        Map<String, Long> mapWithReturnData = new HashMap<>();
        mapWithReturnData.put("BUSINESS_DAYS", businessDays);
        mapWithReturnData.put("WEEKEND_DAYS", weekendDays);
        return mapWithReturnData;
    }
	
	
	public Double calculatePrice(Instant startDate, Instant endDate, boolean hasGarage) {
		if (startDate == null || endDate == null) {
			return null;
		}
		
		double expectedPrice = 0.0;
		
		Map<String, Long> businessAndWeekendDays = countBusinessAndWeekendDaysBetween(startDate, endDate);
		expectedPrice += businessAndWeekendDays.get("BUSINESS_DAYS") * DAILY_PRICE_ON_WEEKDAYS;
		expectedPrice += businessAndWeekendDays.get("WEEKEND_DAYS") * DAILY_PRICE_ON_WEEKENDS;
		
		if (hasGarage) {
			expectedPrice += businessAndWeekendDays.get("BUSINESS_DAYS") * GARAGE_PRICE_ON_WEEKDAYS;
			expectedPrice += businessAndWeekendDays.get("WEEKEND_DAYS") * GARAGE_PRICE_ON_WEEKENDS;
		}
		
		return expectedPrice;
	}

}
