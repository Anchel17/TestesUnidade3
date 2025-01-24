# Projeto Terceira Unidade (AnchelSilva-AndreLima-JoaoSaturnino)

Este projeto foi configurado para utilizar o Maven como gerenciador de dependências, o JUnit 5 para execução de testes unitários e a ferramenta PIT para análise de mutações.

## Requisitos

- **Java 17** ou superior
- **Maven 4.0.0** ou superior

## Como executar o projeto

1. Clone o repositório para o seu ambiente local:
   ```bash
   git clone https://github.com/Anchel17/TestesUnidade3.git
   ```
2. Navegue até o diretório do projeto:
   ```bash
   cd TestesUnidade3
   ```
3. Compile o projeto com o Maven:
   ```bash
   mvn clean compile
   ```
4. Execute o projeto (se aplicável):
   ```bash
   mvn spring-boot:run
   ```

## Como executar os testes

Para executar os testes configurados com o JUnit 5, utilize o comando abaixo:

```bash
mvn test
```

Para executar os testes configurados com o JUnit 5, utilizando uma extensão no eclipse para visualizar a cobertura de arestas:

![alt text](<doc-images/example.jpg>)


Os resultados dos testes serão exibidos no console.

## Como verificar a cobertura dos testes

1. Gere o relatório de cobertura usando a ferramenta PIT:
   (certifique-se de executar o comando `mvn test` antes)
   ```bash
   mvn org.pitest:pitest-maven:mutationCoverage
   ```

2. Após a execução, os relatórios de cobertura serão gerados no diretório `target/pit-reports`. Abra o arquivo `index.html` em qualquer navegador para visualizar os resultados detalhados.

## Configurações adicionais

### Dependências principais

No arquivo `pom.xml`, estão configuradas as seguintes dependências:

- **JUnit 5** para execução de testes:
  ```xml
   <dependency>
   	<groupId>org.instancio</groupId>
   	<artifactId>instancio-junit</artifactId>
   	<version>5.0.1</version>
   	<scope>test</scope>
   </dependency>
  ```

- **PIT Mutation Testing** para análise de mutações:
  ```xml
   <plugin>
	   <groupId>org.pitest</groupId>
	   <artifactId>pitest-maven</artifactId>
	   <version>1.17.3</version>
	   <dependencies>
	   	<dependency>
	   		<groupId>org.pitest</groupId>
	   		<artifactId>pitest-junit5-plugin</artifactId>
	   		<version>1.2.1</version>
	   	</dependency>
	   </dependencies>
   </plugin>
  ```

### Configuração do Maven

Certifique-se de que o projeto está configurado corretamente no arquivo `pom.xml`. Para referência, veja as configurações padrão para o JUnit 5 e o PIT fornecidas acima.

### Mutantes sobreviventes

![Todos os mutantes sobreviventes](<doc-images/x-men.jpg>)

#### Primeiros mutantes sobreviventes
![Primeiros mutantes sobreviventes devido a utilização de mock para chamada de função, fazendo com que a exceção não seja lançada.](<doc-images/xavier-&-magneto.jpg>)<br/>
Primeiros mutantes sobreviventes devido a utilização de mock da função `verificarDisponibilidade`, fazendo com que as exceções validadas na função real não seja lançada.

#### Terceiro mutante sobrevivente
![Terceiro mutante sobrevivente que não interfere no fluxo de finalização de compra, logo os testes unitários não falham independente de sua execução.](<doc-images/wolverine.jpg>) <br/>
Terceiro mutante sobrevivente que não interfere no fluxo de finalização de compra, logo os testes unitários não falham independente de sua execução.