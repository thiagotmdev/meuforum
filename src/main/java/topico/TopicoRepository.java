package topico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository {

    boolean existsByTituloAndMensagem(String titulo, String mensagem);
    List<Topico> findByCurso(String curso);
}
