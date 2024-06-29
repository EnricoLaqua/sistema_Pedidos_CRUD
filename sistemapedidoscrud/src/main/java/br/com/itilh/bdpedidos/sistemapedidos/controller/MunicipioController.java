package br.com.itilh.bdpedidos.sistemapedidos.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import br.com.itilh.bdpedidos.sistemapedidos.model.Municipio;
import br.com.itilh.bdpedidos.sistemapedidos.repository.MunicipioRepository;



@RestController
public class MunicipioController {

    private final MunicipioRepository repositorio;

    public MunicipioController(MunicipioRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping("/municipios")
    public List<Municipio> getMunicipios() {
        return (List<Municipio>) repositorio.findAll();
    }

    @GetMapping("/municipios/estado/{id}")
    public List<Municipio> getMunicipiosPorEstadoId(@PathVariable String id) {
        return (List<Municipio>) repositorio.findByEstadoNomeIgnoreCase(id);
    }
    
     @GetMapping("/municipio/{id}")
    public Municipio getMunicipioPorId(@PathVariable BigInteger id) throws Exception { 
        return repositorio.findById(id).orElseThrow(
                () -> new Exception("Id não encontrado"));
    }

    @PostMapping("/municipio")
    public Municipio postMunicipio(@RequestBody Municipio entity) throws Exception { 
        try {
            return repositorio.save(entity);
        } catch (Exception e) {
            throw new Exception("Não foi possível criar o Município");
        }
    }

    @PutMapping("/municipio/{id}") 
    public Municipio putMunicipio(@PathVariable BigInteger id, @RequestBody Municipio novosDados) throws Exception { 

        Optional<Municipio> municipioArmazenado = repositorio.findById(id);
        if (municipioArmazenado.isPresent()) {
            municipioArmazenado.get().setNome(novosDados.getNome());
            municipioArmazenado.get().setEstado(novosDados.getEstado());
            municipioArmazenado.get().setEntrega(novosDados.getEntrega());
            return repositorio.save(municipioArmazenado.get());
        }
        throw new Exception("Não foi possível alterar o Município");
    }
    
    @DeleteMapping("/municipio/{id}")
    public String deletePorId(@PathVariable BigInteger id) throws Exception { 
        Optional<Municipio> municipioArmazenado = repositorio.findById(id);
        if (municipioArmazenado.isPresent()) {
            repositorio.delete(municipioArmazenado.get());
            return "Excluído";
        }
        throw new Exception("Id não econtrado para exclusão");
    }
}
