package com.example.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dictionary")
public class DictionaryController {

	@CrossOrigin()
	@RequestMapping(value = "/describe/{word}", method = RequestMethod.GET)
	public String describe(@PathVariable("word") String word) {
		return word;
	}

}
