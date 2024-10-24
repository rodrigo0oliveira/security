package br.com.api.projeto.model.exceptions;

import java.time.DateTimeException;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Null;
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
		error.put("message: ", exception.getCause().getMessage().describeConstable());
		
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<String> constraintViolationException(ConstraintViolationException exception){
		String message = "Error: Todos os valores precisam ser preenchidos,algum valor informado e nulo";

		return new ResponseEntity<>(message,(HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<String> nullPointerException(NullPointerException exception){
		String message = "Error : Algum valor informado e nulo!";

		return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DateTimeException.class)
	public ResponseEntity<String> DataTimeExcetion(DateTimeException dateTimeException){
		return new ResponseEntity<>(dateTimeException.getMessage(),HttpStatus.CONFLICT);
	}

}
