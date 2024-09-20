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
import br.com.api.projeto.model.domain.enums.Status;
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
			
			room.setStatus(Status.EMRESERVA);
			roomRepository.save(room);
			reserveRepository.save(newReserve);		
			
			
			return "Reserva criada";
			
		}catch (RuntimeException e) {
			return e.getMessage();
		}
		
	
	}
	
	public List<Reserve> findAllReserves(){
		List<Reserve> list = reserveRepository.findAll();
		return list;	
	}
	
	public List<Reserve> findAllReservesByUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = authService.getUser(authentication);
		List<Reserve> list = reserveRepository.findAllReservesByUserId(user.getId());
		
		return list;
		
	}
	
	private void  verifyDates(LocalDate checkin,LocalDate checkout,String id) {
		
		List<Reserve> list = reserveRepository.findAllReservesByRoomId(id);
		
		list = list.stream().filter(l->l.getCheckin().isBefore(checkout)&& l.getCheckout().isAfter(checkin)).collect(Collectors.toList());
		
		if(checkin.isAfter(checkout)) {
			throw new RuntimeException("A data de entrada não pode ser após a data de saída");
		}
	
		
		if(!list.isEmpty()) {
			throw new RuntimeException("Esse intervalo de  data selecionada não está disponível para reserva - "+list.get(0).getCheckin()
			+" - "+list.get(0).getCheckout());
		}
	}
	
	public  String deleteReserveById(String id) {
		 reserveRepository.deleteById(id);
		 return "Reserva excluída";
	}
	

}
