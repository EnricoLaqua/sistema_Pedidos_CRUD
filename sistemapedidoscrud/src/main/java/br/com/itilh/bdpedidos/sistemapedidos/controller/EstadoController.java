package br.com.itilh.bdpedidos.sistemapedidos.controller;

import org.springframework.web.bind.annotation.RestController;
import br.com.itilh.bdpedidos.sistemapedidos.model.Estado;
import br.com.itilh.bdpedidos.sistemapedidos.repository.EstadoRepository;
import br.com.itilh.bdpedidos.sistemapedidos.util.ModoBusca;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
public class EstadoController {

    private final EstadoRepository repositorio;

    public EstadoController(EstadoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping("/estados")
    public List<Estado> getAll() {
        return (List<Estado>) repositorio.findAll(); 
    }

    @GetMapping("/estados/nome/{nome}")
    public List<Estado> getEstadosPorNome(@PathVariable String nome, 
    @RequestParam(required = true) ModoBusca modoBusca) {
        if(modoBusca.equals(ModoBusca.EXATO)) {
            return repositorio.findByNome(nome);
        }else if (modoBusca.equals(ModoBusca.INICIADO)) {
            return repositorio.findByNomeStartingWithIgnoreCase(nome);
        }else if (modoBusca.equals(ModoBusca.FINALIZADO)) {
            return repositorio.findByNomeEndingWithIgnoreCase(nome);    
        }else{
            return repositorio.findByNomeContainingIgnoreCase(nome);    
        }
    }
    
    @GetMapping("/estado/{id}")
    public Estado getMethodName(@PathVariable BigInteger id) throws Exception { 
        return repositorio.findById(id).orElseThrow( 
                () -> new Exception("Id não encontrado"));
    }

    @PostMapping("/estado") 
    public Estado postEstado(@RequestBody Estado entity) throws Exception { 
        try {
            return repositorio.save(entity); 
        } catch (Exception e) {
            throw new Exception("Não foi possível criar o Estado"); 
        }
    }

    @PutMapping("/estado/{id}")
    public Estado putEstado(@PathVariable BigInteger id, @RequestBody Estado novosDados) throws Exception { 

        Optional<Estado> estadoArmazenado = repositorio.findById(id);
        if (estadoArmazenado.isPresent()) {
            estadoArmazenado.get().setNome(novosDados.getNome());

            return repositorio.save(estadoArmazenado.get());
        }
        throw new Exception("Não foi possível alterar o Estado"); 
    }
    
    @DeleteMapping("/estado/{id}") 
    public String deletePorId(@PathVariable BigInteger id) throws Exception { 
        Optional<Estado> estadoArmazenado = repositorio.findById(id);
        if (estadoArmazenado.isPresent()) {
            repositorio.delete(estadoArmazenado.get());
            return "Excluído";
        }
        throw new Exception("Id não econtrado para exclusão"); 
    }

}
