# CineView

Software de reviews de filmes

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

## Pré requistos para rodar projeto:
- Java 22
- PostgreSQL 15 e PgAdmin 4

#### Nossa base de dados de filmes será rotineiramente atualizada através da API do TMDB: https://api.themoviedb.org/3/movie/now_playing
