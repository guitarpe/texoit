# Candidato: Flavio Santos Sousa
# API de Indicados e Vencedores do Golden Raspberry Awards

Esta API Restful possibilita a leitura da lista de indicados e vencedores da categoria Pior Filme do Golden Raspberry Awards através de um arquivo CSV, salvando os dados na base de dados. Além disso, ela permite obter o produtor com o maior intervalo entre dois prêmios consecutivos e o produtor que obteve dois prêmios mais rapidamente.

## Instruções de Uso

### 1 - Iniciando a aplicação
Assim que a aplicação é iniciada, o serviço lê o arquivo .CSV localizado na pasta **src/test/resources/test-movies.csv** e importa todos os registros para o banco de dados. Mesmo asim, o endpoint de importação ainda está disponível para ser utilizado, caso o usuário queira importar novos registros.

### 2 - Executando os Endpoints

- **POST** - http://localhost:8080/api/awards/movies
    - Este endpoint lê o arquivo CSV fornecido registrando apenas os títulos que não existem e os salvando na base de dados. De forma que caso queira inserir apenas 1 registro, basta ter um CSV com apenas 1 linha.
  

- **GET** - http://localhost:8080/api/awards/movies/1
    - Este endpoint retorna os detalhes de um filme específico com o ID fornecido.
  

- **PUT** - http://localhost:8080/api/awards/movies/1
    - Este endpoint atualiza os detalhes de um filme específico com o ID fornecido. O corpo da requisição deve ser no formato:
      ```json
      {
          "year": "1980",
          "title": "Can't Stop the Music Edited",
          "studios": "Associated Film Distribution",
          "producer": "Allan Carr",
          "winner": ""
      }
      ```

- **DELETE** - http://localhost:8080/api/awards/movies/1
    - Este endpoint exclui um filme específico com o ID fornecido.


- **GET** - http://localhost:8080/api/awards/movies/ranking
    - Este endpoint retorna um ranking dos produtores, trazendo o produtor com o menor e maior intervalo entre dois prêmios consecutivos. O retorno será no formato:
      ```json
      {
          "min": [
              {
                  "producer": "Allan Carr",
                  "interval": 0,
                  "previousWin": 1980,
                  "followingWin": 1980
              }
          ],
          "max": [
              {
                  "producer": "Allan Carr",
                  "interval": 0,
                  "previousWin": 1980,
                  "followingWin": 1980
              }
          ]
      }
      ```

### 3 - Configurando o Projeto no IntelliJ

1. Clone o projeto do repositório Git localizado em https://github.com/guitarpe/texoit.git.

2. Abra o IntelliJ IDEA.

3. Selecione **File > New > Project from Version Control > Git**.

4. Cole a URL do repositório e clique em **Clone**.

5. Siga as instruções para configurar o Git no IntelliJ, se necessário.

6. Aguarde o IntelliJ importar o projeto.

### 4 - Executando o Projeto

1. Após abrir o projeto no IntelliJ, aguarde a indexação e a sincronização das dependências.

2. No canto superior direito da tela, localise uma a área `Open Run/Debug configurations` e selecione `Edit Configurations`.

3. Ao abrir a tela, no canto superior esquerdo, clique no ícone `+`.

4. Em `Add/New Configuration`, selecione `Application`.

5. No campo Name dê um nome de sua preferência, logo mais abaixo no campo `Main class`, selecione a classe principal que no caso é a classe `App.class`

6. logo após cliquem em `Apply` e depois `OK`

7. Para rodar a aplicação basta clicar no ícone `Play` em verde

### 5 - Executando os Testes com Mockito

1. Localize a classe de teste `MoviesControllerTest` no projeto.

2. Abra a classe `MoviesControllerTest`.

3. Execute a classe de teste clicando com o botão direito na mesma e selecionando **Run 'MoviesControllerTest'**.

4. Aguarde até que os testes sejam executados e verifique os resultados na saída da execução.

5. Há um teste para cada endpoint disponível e também um que valida a informação obtida no resultado do endpoint que retorna o intervalo entra os produtores premiados, de acordo com o último endpoint no item 1

### 6 - Collection Postman

Você pode encontrar dentro da pasta src/test/resources o arquivo `TEXTOT.postman_collection.json` para facilitar os testes do endpoints. 

## Observações
Para este projeto foi utilizado o `Java 11 - openjdk 19.0.2`
O servidor de aplicação onfigurado foi o `tomcat 7 (tomcat7-maven-plugin)`

Com estas instruções, você estará pronto para usar e contribuir para o projeto da API Restful Golden Raspberry Awards!
