## CineView: Software de Reviews de Filmes

## Introdução

O projeto consiste em uma aplicação que permite ao usuário pesquisar, visualizar e
expressar suas opiniões, através de reviews e comentários, sobre filmes além de poder fazer
listas de filmes personalizadas. Dessa maneira, com base no comportamento de cada usuário,
pode-se recomendar filmes semelhantes e gerar um relatório com os filmes e reviews mais
comentados semanalmente.
A aplicação terá um banco de dados relacional atualizado sobre filmes. Ela também
importará dados da API de referência do The Movie Database (TMDB) para manter os dados
atualizados. Os usuários poderão separar os filmes em diversas listas, de acordo com suas
preferências, e poderão, também, separar os filmes em categorias, tais como: filmes
assistidos, filmes que desejo assistir, entre outros.
Além disso, a aplicação será uma rede social, na qual os usuários podem interagir
entre si, curtindo e respondendo comentários já existentes, ou comentando em reviews de
outros usuários. Os usuários poderão, também, seguir outras pessoas e acumular seguidores.

## Pré-requisitos

* Java 22
* PostgreSQL 15 e PgAdmin 4

Nossa base de dados de filmes será rotineiramente atualizada através da API do TMDB: https://api.themoviedb.org/3/movie/now_playing
Base para pegarmos os gêneros de filmes: https://api.themoviedb.org/3/genre/movie/list'

## Instalação e Configuração

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/StephanyMil/CineView.git
   cd CineView
   ```

2. **Configure o banco de dados:**

    * Crie um banco de dados chamado `CineView-api` no PostgreSQL.
    * Configure as credenciais do banco de dados no arquivo `application.properties`. O exemplo abaixo mostra as configurações padrão:

      ```properties
      spring.application.name=CineView
      spring.datasource.url=jdbc:postgresql://localhost:5432/CineView-api
      spring.datasource.username=postgres
      spring.datasource.password=123456
      spring.jpa.hibernate.ddlAuto=update
      spring.jpa.properties.hibernate.jdbc.lon.non_contextual_creation=true
      spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
      spring.jpa.open-in-view=false
      # Security configuration
      spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/auth/
      spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/auth/jwks
      # Enable H2 console
      spring.h2.console.enabled=true
      spring.h2.console.path=/h2-console
      #chave da api
      tmdb.api.key=d13942dd547cde524c1167f5f07b2890
      tmdb.api.url.movie=https://api.themoviedb.org/3/movie/now_playing
      tmdb.api.url.movie.keywods=https://api.themoviedb.org/3/movie/
      tmdb.api.url.genre=https://api.themoviedb.org/3/genre/movie/list
      ```

3. **Execute o projeto:**

   ```bash
   mvn spring-boot:run
   ```

## Testando a Aplicação

Após iniciar o projeto, você pode testar as funcionalidades através de chamadas HTTP para os endpoints da API. Utilize ferramentas como Postman ou curl.

**Exemplo de testes:**

* **Cadastro de usuário:**

   ```bash
   POST http://localhost:8080/users
   BODY: application/json
   {
      "Name": "Nome de Usuário",
      "email": "usuario@email.com",
      "password": "senha123",
      "birthDate": "2000-05-06"
   }
   ```

* **Adicionar um filme à lista de favoritos:**

   ```bash
   POST http://localhost:8080/users/{userId}/favorites/{filmListId}

   ```

## Observações

* Certifique-se de que a aplicação esteja em execução e que as dependências estejam corretamente configuradas antes de realizar os testes.

## Conclusão

CineView é uma plataforma completa para amantes de cinema, que busca oferecer uma experiência interativa e personalizada, facilitando a descoberta e a discussão de filmes com outros usuários.
