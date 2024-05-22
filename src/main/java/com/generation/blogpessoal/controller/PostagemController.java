package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Postagem;
import com.generation.blogpessoal.repository.PostagemRepository;

import jakarta.validation.Valid;

@RestController //anotação que diz para spring que essa é uma controladora de rotas e acesso aos metodos
@RequestMapping("/postagens") //rota para chegar nessa classe "insomnia"
@CrossOrigin(origins = "*", allowedHeaders ="*") //liberar o acesso a outras maquinas /allowedHeaders = liberar passagem de parametros no header
public class PostagemController {

	@Autowired //injeção de dependencias - instanciar a classe PostagemRepository
	private PostagemRepository postagemRepository;
	
	@GetMapping //defini e verbo http que atende esse metodo
	public ResponseEntity<List<Postagem>> getAll(){
		
		//ResponseEntity - Classe do Spring que trata a saida de dados para o formato HTTP
		//List garante que a gente consiga retornar uma lista com mais de uma postagem
		//Postagem para indicar que será o objeto Postagem que queremos retornar
		// getAll nome do nosso método
		
		//return esta trazendo o status de ok- 200 caso ele consiga fazer o método findAll da JpaRepository
		return ResponseEntity.ok(postagemRepository.findAll());
		//SELECT * FROM tb_postagens
		
		
		/*
		 * Processo até aqui
		 * criar o projeto
		 * criar conexão com o banco de dados - cria o database apenas
		 * criar a model para ter a estrutura do projeto e também estrutura da tabela tb_postagens (cria a tabela apenas)
		 * realizar o teste se o banco de dados esta sendo criado junto com a tabela
		 * criar a Repository para garantir a comunicação dos Querys no banco de dados (select, insert, update e delete para os dados da tabela)
		 * criar a controller com os métodos para recuperar os dados da tabela do banco de dados
		 */
	}
	
	//localhost:8080/postagens/1
	@GetMapping("/{id}")
	public ResponseEntity<Postagem> getById(@PathVariable Long id){
		// findById = SELECT * FROM tb_postagens WHERE id = 1;
		return postagemRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	//SELECT * FROM tb_postagens WHERE titulo = "titulo";
	@GetMapping("/titulo/{titulo}")//localhost:8080/postagens/titulo/Postagem 02
	public ResponseEntity<List<Postagem>> getByTitulo(@PathVariable String titulo){
		return ResponseEntity.ok(postagemRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	//INSERT INTO tb_postagens (titulo, texto, data) VALUES ("Título", "Texto", "2024-12-31 14:05:01");
	@PostMapping //anotação PostMapping para ficar com o verbo correspondente com o que vamos executar - um cadastro novo
	public ResponseEntity<Postagem> post(@Valid @RequestBody Postagem postagem){
		/*
		 * Retorno do Metodo deve ser no formato ResponseEntity com objeto Postagem, nome do método é post
		 * @valid é a anotação que garante que vamos aplicar nessa ação as validação para registro dessa nova postagem
		 * @RequestBody é anotação que permite receber dados no body(corpo) da requisição essa informação que vem do insominia sera no formato Postagem 
		 * 
		 */
		//retorno em formato ResponseEntity
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(postagemRepository.save(postagem));
		
		/*
		 * o return esta colocando o status 201- created e esta executando o save da postagem que vem do 
		 * corpo da requisição, isso vai ser retornado no body da resposta da api tbm pq estamos passando o .body
		 */
	}
	
	//UPDATE tb_postagem SET titulo = "...", texto = "..." Where id = 1
	@PutMapping //verbo http Put para o consumo no insomnia
	public ResponseEntity<Postagem> put(@Valid @RequestBody Postagem postagem){
		/*
		 * Retorno do Metodo deve ser no formato ResponseEntity com objeto Postagem, nome do método é post
		 * @valid é a anotação que garante que vamos aplicar nessa ação as validação para registro dessa nova postagem
		 * @RequestBody é anotação que permite receber dados no body(corpo) da requisição essa informação que vem do insominia sera no formato Postagem 
		 * 
		 */
		
		return postagemRepository.findById(postagem.getId())
				.map(resposta-> ResponseEntity.status(HttpStatus.OK)
				.body(postagemRepository.save(postagem)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		/*
		 * aqui primeiro a gente forma o objeto com o getId da postagem enviada pelo corpo da requisição
		 * depois criamos um map e passamos um status ok
		 * em seguida o .body esta retornando no body da resposta da requisição o objeto que salvamos 
		 * or else só vai atuar se o objeto não puder ser mapeado, exemplo não existe o id informado na tabela do banco de dados
		 * .buil() é para entregar essa resposta sem um corpo de resposta no insomnia
		 */
	}
	
	//DELETE FROM tb_postagens WHERE id = id;
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Postagem> postagem = postagemRepository.findById(id);
		//esse método é void, não precisa de return então vamos resolver ele criando um objeto optional
		/*
		 * lembre-se que o objeto optional é utilizado quando podemos ter um objeto com as informações ok 
		 * ou quando vamos lidar com um objeto que não tem seus dados ou seja vazio, o optional evita erros 
		 * de null pointer
		 * 
		 * esse objeto sera preenchido com o findById - linha da tabela com o id informado no endpoint
		 * 
		 * lembre-se que o PathVariable esta trazendo um valor variavel da url - endpoint
		 */
		if(postagem.isEmpty()) //caso o objeto esteja vazio
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		//ele executa o response status de excessão, ele vai apenas colocar o botão como not found
		
		postagemRepository.deleteById(id);
		//caso contrário, objeto existir ele executa o metodo deleteById passando o id que informamos na url
	}
		
		/*
		 * Get - recuperar as informações do banco de dados
		 * Post - insert no banco de dados
		 * Put - update no banco de dados
		 * Delete - deleta algum registro do banco de dados
		 */

}
