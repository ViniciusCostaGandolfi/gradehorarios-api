# Backend RotaFoodAPI

Olá, eu me chamo Vinícius e estou desenvolvendo a aplicação Grade Horários. Esta 
aplicação é basicamente um sistema para resolução do problema de grade de horários escolares, 
pois uma escola tem os horários das aulas de cada classe, horarios disponíveis por professores e por matéria, 
além das preferencias dos professores e as restrições de aulas duplas!.

# Porque dividir em dois serviços?

Pela natureza do problema resolvido, o [Problema de Grade de Horários Escolares]([https://en.wikipedia.org/wiki/Vehicle_routing_problem](https://en.wikipedia.org/wiki/School_timetable)) foi necessário dividir o backend em dois serviços, um que para o banco de dados, outro para a processar a grade de horários em si. Por mais otmizado que eu, Saulo e André tenhamos deixado o nosso modelo, ele processa por alguns segundos. Deixar tudo em grande monolito poderia acarretar concorrencia entre usuários que querem seus dados, e um micro serviço para a processar o modelo matemático.
Em relação a microserviços, não tenho usuários ainda, logo Monolitics First!!! Microserviços apenas iriam atrasar o desenvlvimento deste aplicativo. No futuro se precisar irei dividir, atualmente vou apenas retirar a camada de Roterização poque ela é demorada. Veja o que é [pesquisa operacional](https://developers.google.com/optimization/mip/mip_example?hl=pt-br) para entender melhor o que eu faço!

# Porque utilizar FastAPI x Django?

Escolhi o framework FastAPI porque além de já ter suporte a async e await do Python, ele 
utiliza o Pydantic para validar fortemente os tipos e entruturas de dados. O Python por 
mais que seja dinamicamente tipado, atualmente ele esta desenvolvendo ferramentas para 
tipagem de dados.


# Postgres ou MongoDB?

Como banco de dados escolhi o bom e velho PostgreSQL, o melhor banco de dados relacional open source da atualidade, com o suporte a ARRAY ajuda muito para guardar listas de Strings sem ter que criar mais uma tabela. MongoDB é o mais famoso da atualidade, mas por preferir trabalhar com relacionamentos entre tabelas, PostgreSQL é mais adequado.
