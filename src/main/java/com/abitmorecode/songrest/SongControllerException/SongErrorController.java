package com.abitmorecode.songrest.SongControllerException;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SongErrorController implements ErrorController {
	@RequestMapping("/error")
	@ResponseBody
	public ResponseEntity<Object> handleError(HttpServletRequest request) {
		Integer statusCode = HttpStatus.METHOD_NOT_ALLOWED.value();
		Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
		return new ResponseEntity<>(String.format(
				"<html><body>" +
						"<h2>Error Page</h2>" +
						"<div>Status code: <b>%s</b></div>" +
						"<div>Exception Message: <b>%s</b></div>" +
						"<body></html>", statusCode, exception),
				HttpStatus.METHOD_NOT_ALLOWED);
	}
}