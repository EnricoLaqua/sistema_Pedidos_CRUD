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
import br.com.itilh.bdpedidos.sistemapedidos.model.Produto;
import br.com.itilh.bdpedidos.sistemapedidos.repository.ProdutoRepository;

@RestController
public class ProdutoController {

private final ProdutoRepository repositorio;

    public ProdutoController(ProdutoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping("/produtos")
    public List<Produto> getAll() {
        return (List<Produto>) repositorio.findAll(); 
    }

    @GetMapping("/produto/{id}")
    public Produto getMethodName(@PathVariable BigInteger id) throws Exception { 
        return repositorio.findById(id).orElseThrow( 
                () -> new Exception("Id não encontrado"));
    }

    @PostMapping("/produto") 
    public Produto postProduto(@RequestBody Produto entity) throws Exception { 
        try {
            return repositorio.save(entity); 
        } catch (Exception e) {
            throw new Exception("Não foi possível criar o Produto"); 
        }
    }

    @PutMapping("/produto/{id}")
    public Produto putProduto(@PathVariable BigInteger id, @RequestBody Produto novosDados) throws Exception { 

        Optional<Produto> produtoArmazenado = repositorio.findById(id);
        if (produtoArmazenado.isPresent()) {
            produtoArmazenado.get().setDescricao(novosDados.getDescricao());
            produtoArmazenado.get().setQuantidadeEstoque(novosDados.getQuantidadeEstoque());
            produtoArmazenado.get().setPrecoUnidadeAtual(novosDados.getPrecoUnidadeAtual());
            produtoArmazenado.get().setAtivo(novosDados.getAtivo());
            return repositorio.save(produtoArmazenado.get());
        }
        throw new Exception("Não foi possível alterar o Produto"); 
    }
    
    @DeleteMapping("/produto/{id}") 
    public String deletePorId(@PathVariable BigInteger id) throws Exception { 
        Optional<Produto> produtoArmazenado = repositorio.findById(id);
        if (produtoArmazenado.isPresent()) {
            repositorio.delete(produtoArmazenado.get());
            return "Excluído";
        }
        throw new Exception("Id não econtrado para exclusão"); 
    }

}
  
