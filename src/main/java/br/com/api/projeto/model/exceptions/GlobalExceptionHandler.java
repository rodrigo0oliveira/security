package br.com.api.projeto.model.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> argumentNotValidException(MethodArgumentNotValidException exception){
		Map<String, Object> errors = new HashMap<String, Object>();
		errors.put("status", HttpStatus.BAD_REQUEST);
		
		Map<String,Object> detailErrors = new HashMap<String, Object>();
		
		exception.getBindingResult().getFieldErrors().stream().forEach(error->{
			detailErrors.put(error.getField(), error.getDefaultMessage());
		});
		
		errors.put("Error Details",detailErrors);
		
		return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);

	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> dataIntegrityException(DataIntegrityViolationException exception){
		Map<String, Object> error = new HashMap<String, Object>();
		error.put("status", HttpStatus.CONFLICT.value());
		error.put("message: ", exception.getCause().getMessage());
		
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
	}
}
