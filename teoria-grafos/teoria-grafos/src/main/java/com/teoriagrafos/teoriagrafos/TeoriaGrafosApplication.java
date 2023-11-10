package com.teoriagrafos.teoriagrafos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.teoriagrafos.teoriagrafos.model.Grafo;
import com.teoriagrafos.teoriagrafos.service.SearchAPIService;

@SpringBootApplication
public class TeoriaGrafosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeoriaGrafosApplication.class, args);
	}

    @Autowired
    private SearchAPIService searchAPIService;

	@Bean
	CommandLineRunner initMain() {
		return args -> {Grafo<String> grafo = new Grafo<String>();
        grafo.adicionarVertice("João");
        grafo.adicionarVertice("Lorenzo");
        grafo.adicionarVertice("Creuza");
        grafo.adicionarVertice("Créber");
        grafo.adicionarVertice("Cráudio");
        
        grafo.adicionarAresta(2.0, "João", "Lorenzo");
        grafo.adicionarAresta(3.0, "Lorenzo", "Créber");
        grafo.adicionarAresta(1.0, "Créber", "Creuza");
        grafo.adicionarAresta(1.0, "João", "Creuza");
        grafo.adicionarAresta(3.0, "Cráudio", "João");
        grafo.adicionarAresta(2.0, "Cráudio", "Lorenzo");
        
        
        //grafo.buscaEmLargura();
        searchAPIService.searchAPIService();
	};

	}

}