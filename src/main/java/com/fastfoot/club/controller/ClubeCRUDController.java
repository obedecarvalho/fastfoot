package com.fastfoot.club.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fastfoot.club.model.entity.Clube;
import com.fastfoot.club.service.crud.ClubeCRUDService;
import com.fastfoot.controller.CRUDController;
import com.fastfoot.model.entity.Jogo;
import com.fastfoot.service.JogoCRUDService;
import com.fastfoot.service.util.ValidatorUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class ClubeCRUDController implements CRUDController<Clube, Long> {
	
	private static final Comparator<Clube> COMPARATOR = new Comparator<Clube>() {
		@Override
		public int compare(Clube o1, Clube o2) {
			return o1.getNome().compareToIgnoreCase(o2.getNome());
		}
	};
	
	@Autowired
	private ClubeCRUDService clubeCRUDService;
	
	@Autowired
	private JogoCRUDService jogoCRUDService;

	@Override
	@PostMapping("/clubes")
	public ResponseEntity<Clube> create(@RequestBody Clube t) {
		
		try {
		
			Clube clube = clubeCRUDService.create(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
	
			return new ResponseEntity<Clube>(clube, HttpStatus.CREATED);
		
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}
	
	@Override
	public ResponseEntity<List<Clube>> getAll() {
		return null;
	}

	//@Override
	@GetMapping("/clubes")
	public ResponseEntity<List<Clube>> getAll(@RequestParam(name = "idJogo", required = true) Long idJogo,
			@RequestParam(name = "liga", required = false) Integer liga) {
		
		try {
			
			Jogo jogo = jogoCRUDService.getById(idJogo);
			if (ValidatorUtil.isEmpty(jogo)) {
				return ResponseEntity.noContent().build();
			}
			
			List<Clube> clubes;
			
			if (ValidatorUtil.isEmpty(liga)) {
				clubes = clubeCRUDService.getByJogo(jogo);
			} else {
				clubes = clubeCRUDService.getByLiga(jogo, liga);
			}
	
			if (ValidatorUtil.isEmpty(clubes)) {
				return ResponseEntity.noContent().build();
			}
			
			//
			Collections.sort(clubes, COMPARATOR);
			//
	
			return ResponseEntity.ok(clubes);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@GetMapping("/clubes/{id}")
	public ResponseEntity<Clube> getById(@PathVariable("id") Long id) {
		
		try {
		
			Clube clube = clubeCRUDService.getById(id);
	
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
	
			return ResponseEntity.ok(clube);
			
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@PutMapping("/clubes/{id}")
	public ResponseEntity<Clube> update(@PathVariable("id") Long id, @RequestBody Clube t) {
		
		try {
		
			Clube clube = clubeCRUDService.getById(id);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.notFound().build();
			}
			
			clube = clubeCRUDService.update(t);
			
			if (ValidatorUtil.isEmpty(clube)) {
				return ResponseEntity.internalServerError().build();
			}
			
			return ResponseEntity.ok(clube);

		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubes/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
		try {
			clubeCRUDService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@Override
	@DeleteMapping("/clubes")
	public ResponseEntity<HttpStatus> deleteAll() {
		try {
			clubeCRUDService.deleteAll();
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

}
