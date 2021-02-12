package com.hoaxify.hoaxservice;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hoaxify.filecontroller.FileAttachment;
import com.hoaxify.filecontroller.FileAttachmentRepository;
import com.hoaxify.hoax.Hoax;
import com.hoaxify.hoax.vm.HoaxVM;
import com.hoaxify.hoaxrepository.HoaxRepository;
import com.hoaxify.user.User;
import com.hoaxify.userservice.UserService;

@Service
public class HoaxService {

	@Autowired
	private HoaxRepository hoaxRepository;

	@Autowired
	private UserService userService;
	
	@Autowired
	private FileAttachmentRepository fileAttachmentRepository;

	public Hoax saveHoax(User user, Hoax hoax) {
		hoax.setUser(user);
		hoax.setTimestamp(new Date());
		
		if(hoax.getAttachment() != null) {
			FileAttachment inDB = fileAttachmentRepository.findById(hoax.getAttachment().getId()).get();
			inDB.setHoax(hoax);
			hoax.setAttachment(inDB);
		}
		
		return hoaxRepository.save(hoax);
	}

	public Page<Hoax> getAllHoaxes(Pageable pageable) {
		return hoaxRepository.findAll(pageable);
	}

	public Page<Hoax> getHoaxesOfUser(String username, Pageable pageable) {
		User userByUSername = userService.getUserByUSername(username);
		return hoaxRepository.findByUser(userByUSername, pageable);
	}

	public Page<Hoax> getOldHoaxes(long id, String username, Pageable pageable) {
		Specification<Hoax> spec = Specification.where(idLessThan(id));
		if (username != null) {
			User user = userService.getUserByUSername(username);
			// return hoaxRepository.findByIdLessThanAndUser(id, user, pageable);
			return hoaxRepository.findAll(spec.and(userIs(user)), pageable);
		}

		// return hoaxRepository.findByIdLessThan(id, pageable);
		return hoaxRepository.findAll(spec, pageable);
	}

	/*
	 * public Page<Hoax> getOldHoaxesOfUser(long id, String username, Pageable
	 * pageable) {
	 * 
	 * }
	 */

	public List<Hoax> getNewHoaxes(long id, String username, Pageable pageable) {
		Specification<Hoax> spec = Specification.where(idGreaterThan(id));
		
		if(username != null) {
			User user = userService.getUserByUSername(username);
			//return hoaxRepository.findByIdGreaterThanAndUser(id, user, pageable.getSort());
			return hoaxRepository.findAll(spec.and(userIs(user)), pageable.getSort());
		}
		//return hoaxRepository.findByIdGreaterThan(id, pageable.getSort());
		return hoaxRepository.findAll(spec, pageable.getSort());
	}

	/*
	 * public List<Hoax> getNewHoaxesOfUser(long id, String username, Pageable
	 * pageable) {
	 * 
	 * }
	 */

	public long getNewHoaxesCount(long id, String username) {
		Specification<Hoax> spec = Specification.where(idGreaterThan(id));
		
		if(username != null) {
			User user = userService.getUserByUSername(username);
			//return hoaxRepository.countByIdGreaterThanAndUser(id, user);
			return hoaxRepository.count(spec.and(userIs(user)));
		}
		//return hoaxRepository.countByIdGreaterThan(id);
		return hoaxRepository.count(spec);
	}

	/*
	 * public long getNewHoaxesCountOfUser(long id, String username) {
	 * 
	 * }
	 */
	
	private Specification<Hoax> userIs(User user) {
		return (Root<Hoax> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
				return criteriaBuilder.equal(root.get("user"), user);
		};
	}
	
	private Specification<Hoax> idLessThan(long id) {
		return (Root<Hoax> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
				return criteriaBuilder.equal(root.get("id"), id);
		};
	}
	
	private Specification<Hoax> idGreaterThan(long id) {
		return (Root<Hoax> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
				return criteriaBuilder.equal(root.get("id"), id);
		};
	}

}
