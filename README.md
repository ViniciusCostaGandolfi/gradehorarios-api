# Backend RotaFoodAPI

Olá, eu me chamo Vinícius e estou desenvolvendo a aplicação RotaFood. Esta 
aplicação é basicamente um sistema para roterização para estabelecimentos de 
entrega de comida com um sistema para gestão de pedidos, rotas, menu, etc acoplado.

Roterizar suas entregas poupa o tempo para gerenciar seus entregadores, poupa 
o tempo para relizar todas as suas entregas, fideliza seus clientes pois a 
coomida chegará mais rapido e quente na cada deles. Mande os pedidos para a 
cozinha por ROTA, logo, haverá menos tempo entre um pedido ficar pronto e sair 
para entrega!

# Porque dividir em dois serviços?

Pela natureza do problema resolvido, o [Capacited Vehicle Routine Problem](https://en.wikipedia.org/wiki/Vehicle_routing_problem) foi necessário dividir o backend em dois serviços, um que para o banco de dados, outro para a roterização em si. Por mais otmizado que eu, Saulo e Cristiano tenhamos deixado o nosso modelo, cerca de 100 pontos de entrega roterizados por segundo. Deixar tudo em grande monolito poderia acarretar concorrencia entre usuários que querem seus dados, para com usuários que querem a roterização.
Em relação a microserviços, não tenho usuários ainda, logo Monolitics First!!! Microserviços apenas iriam atrasar o desenvlvimento deste aplicativo. No futuro se precisar irei dividir, atualmente vou apenas retirar a camada de Roterização poque ela é demorada. Teste o solucionador do [Google ORTools](https://developers.google.com/optimization/routing/cvrp) e veja o quão complexo é este problema.

# Porque utilizar FastAPI x Django?

Escolhi o framework FastAPI porque além de já ter suporte a async e await do Python, ele 
utiliza o Pydantic para validar fortemente os tipos e entruturas de dados. O Python por 
mais que seja dinamicamente tipado, atualmente ele esta desenvolvendo ferramentas para 
tipagem de dados.


# Postgres ou MongoDB?

Como banco de dados escolhi o bom e velho PostgreSQL, o melhor banco de dados relacional open source da atualidade, com o suporte a ARRAY ajuda muito para guardar listas de Strings sem ter que criar mais uma tabela. MongoDB é o mais famoso da atualidade, mas por preferir trabalhar com relacionamentos entre tabelas, PostgreSQL é mais adequado.
