package br.com.api.projeto.model.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.api.projeto.model.domain.Reserve;
import br.com.api.projeto.model.domain.Room;
import br.com.api.projeto.model.domain.User;
import br.com.api.projeto.model.domain.dto.NewReserveDto;
import br.com.api.projeto.model.repository.ReserveRepository;
import br.com.api.projeto.model.repository.RoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReserveService {
	
	private final ReserveRepository reserveRepository;
	private final RoomRepository roomRepository;
	private final AuthService authService;
	
	public static DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public String createReserve(NewReserveDto reserve){
		
		try {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User user = authService.getUser(authentication);
			
			Room room =  roomRepository.findByroomnumber(reserve.getRoomnumber());
			
			LocalDate checkinDate = LocalDate.parse(reserve.getCheckin(), df);
			LocalDate checkoutDate = LocalDate.parse(reserve.getCheckout(),df);
			
			verifyDates(checkinDate, checkoutDate, room.getId());
			
			Reserve newReserve = Reserve.builder()
					.id(UUID.randomUUID().toString())
					.checkin(checkinDate)
					.checkout(checkoutDate)
					.room(room)
					.user(user)
					.build();
			
			reserveRepository.save(newReserve);				
			
			return "Reserva criada";
			
		}catch (RuntimeException e) {
			return e.getMessage();
		}
		
	
	}
	
	private void  verifyDates(LocalDate checkin,LocalDate checkout,String id) {
		
		List<Reserve> list = reserveRepository.verifyDates(id);
		
		list = list.stream().filter(l->l.getCheckin().isBefore(checkout)&& l.getCheckout().isAfter(checkin)).collect(Collectors.toList());
		
		if(checkin.isAfter(checkout)) {
			throw new RuntimeException("A data de entrada não pode ser após a data de saída");
		}
	
		
		if(!list.isEmpty()) {
			throw new RuntimeException("A data selecionada não está disponível para reserva");
		}
	}
	

}
