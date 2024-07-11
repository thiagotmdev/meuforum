package controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topico.Topico;
import topico.TopicoRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topico")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity<Topico> criarTopico(@Valid @RequestBody Topico topico) {
        if (topicoRepository.existsByTituloAndMensagem(topico.getTitulo(), topico.getMensagem())) {
            return ResponseEntity.badRequest().build();
        }
        Topico novoTopico = (Topico) topicoRepository.save(topico);
        return ResponseEntity.ok(novoTopico);
    }

    @GetMapping
    public Page<Topico> listarTopicos(@PageableDefault(size = 10, sort = {"dataCriacao"})Pageable paginacao) {
        return (Page<Topico>) topicoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topico> detalharTopico(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        return topico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topico> atualizarTopico(@PathVariable Long id, @Valid @RequestBody Topico topicoAtualizado) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if (!topicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Topico topico = topicoOptional.get();
        topico.setTitulo(topicoAtualizado.getTitulo());
        topico.setMensagem(topicoAtualizado.getMensagem());
        topico.setAutor(topicoAtualizado.getAutor());
        topico.setCurso(topicoAtualizado.getCurso());
        topicoRepository.save(topico);

        return ResponseEntity.ok(topico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTopico(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
