package br.com.flashstudy.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import br.com.flashstudy.error.Resposta;
import br.com.flashstudy.model.Flashcard;
import br.com.flashstudy.model.Usuario;
import br.com.flashstudy.repository.FlashcardRepository;

//Controller dos flashcards

@Component
@RestController
@RequestMapping(value = "/flashcard")
public class FlashcardController {

	@Autowired
	FlashcardRepository flashcardRepository;

	// Lista os flashcards do usuário
	@GetMapping(path = "/lista")
	public ResponseEntity<?> lista(HttpSession session) {

		return new ResponseEntity<>(flashcardRepository.getByUsuario((Usuario) session.getAttribute("usuario")),
				HttpStatus.OK);
	}

	// Salva o flashcard
	@PostMapping(path = "/salvar")
	public ResponseEntity<?> salvar(@RequestBody Flashcard flashcard, HttpSession session) {

		Usuario usuario = (Usuario) session.getAttribute("usuario");

		flashcard.setUsuario(usuario);
				
		flashcardRepository.save(flashcard);

		return new ResponseEntity<>(new Resposta("Flashcard salvo!"), HttpStatus.OK);

	}

	

	// Deleta através do código do flashcard
	@DeleteMapping("/deleta/{codigo}")
	public ResponseEntity<?> deletar(@PathVariable("codigo") Long codigo) {
		if (flashcardRepository.findById(codigo) == null)
			return new ResponseEntity<>(new Resposta("Flashcard não encontrado no banco de dados!!"),
					HttpStatus.NOT_FOUND);

		flashcardRepository.deleteById(codigo);
		return new ResponseEntity<>(new Resposta("Card deletado com sucesso!"), HttpStatus.OK);
	}

}