package com.hoaxify.hoaxcontroller;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.CurrentUser;
import com.hoaxify.hoax.Hoax;
import com.hoaxify.hoax.vm.HoaxVM;
import com.hoaxify.hoaxservice.HoaxService;
import com.hoaxify.user.User;

@RestController
@RequestMapping("/api/1.0")
public class HoaxController {

	@Autowired
	private HoaxService hoaxService;

	@PostMapping("/hoaxes")
	public HoaxVM createHoax(@Valid @RequestBody Hoax hoax, @CurrentUser User user) {
		return new HoaxVM(hoaxService.saveHoax(user, hoax));
	}

	@GetMapping("/hoaxes")
	public Page<HoaxVM> getHoaxes(@CurrentUser User user, Pageable pageable) {
		/*
		 * return hoaxService.getAllHoaxes(pageable).map(hoax -> { return new
		 * HoaxVM(hoax); });
		 */
		return hoaxService.getAllHoaxes(pageable).map(HoaxVM::new);
	}

	@GetMapping("/users/{username}/hoaxes")
	public Page<HoaxVM> getHoaxesOfUSer(@PathVariable String username, Pageable pageable) {
		return hoaxService.getHoaxesOfUser(username, pageable).map(HoaxVM::new);
	}

	@GetMapping({"/hoaxes/{id}", "/users/{username}/hoaxes/{id}"})
	public ResponseEntity getHoaxiesRelative(@PathVariable long id,
			@PathVariable(required = false) String username,
			@RequestParam(name = "direction", defaultValue = "after") String direction, @RequestParam(name = "count", required = false, defaultValue = "false") boolean count ,Pageable pageable) {
		if (!"after".equalsIgnoreCase(direction)) {
			return new ResponseEntity(hoaxService.getOldHoaxes(id, username, pageable).map(HoaxVM::new), HttpStatus.OK);
		}
		
		if(count) {
			long newHoaxCount = hoaxService.getNewHoaxesCount(id, username);
			return ResponseEntity.ok(Collections.singletonMap("count", newHoaxCount));
		}
		
		List<HoaxVM> newHoaxes = hoaxService.getNewHoaxes(id, username, pageable).stream().map(HoaxVM::new)
				.collect(Collectors.toList());
		return new ResponseEntity(newHoaxes, HttpStatus.OK);
	}

}
