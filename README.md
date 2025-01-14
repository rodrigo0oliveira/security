# Security Api de reservas
![image](https://github.com/user-attachments/assets/1f560f70-f137-4116-8a41-84e0d5efebed)

## Descrição do projeto 
* Durante a faculdade, enfrentei o desafio de criar uma aplicação em Java utilizando estruturas de dados. Recentemente, decidi aprimorar esse projeto, incorporando Spring Security para garantir a segurança dos dados e Docker para facilitar a implantação. Neste processo, aprendi bastante sobre construção e arquitetura de APIs, além de implementar a segurança das informações com tokens JWT e realizar a containerização, tornando o desenvolvimento mais ágil e eficiente,além de testes unitários e de integração.

## Etapas para rodar o projeto
* Clone o repositório : git clone https://github.com/rodrigo0oliveira/security.git
* Build na aplicação via docker : docker build -t projeto:latest .
* Iniciar a aplicação via docker : docker run -d -p 8080:8080 projeto:latest


## Documentação do projeto com Swagger
![image](https://github.com/user-attachments/assets/6df1c485-9b11-4f17-b84e-e878edff5bc4)

* Apenas usuários com Role -> "ADMIN" podem criar e editar quartos e visualizar todas as reservas.
* Usuários com role "GUEST" podem criar reservas,ver quartos disponíveis e ver apenas as suas reservas.

## Etapas de como usar a API
* Criar um cadastro
  ![image](https://github.com/user-attachments/assets/8861ba8d-46d5-4b9f-8cdb-d4233d1825ad)
* Realizar o login
  ![image](https://github.com/user-attachments/assets/72cbb0d9-670a-4661-8b98-ef4945f4f092)
* Colocar o token para requisições futuras
- ![image](https://github.com/user-attachments/assets/c4288b14-07d4-4db6-8e20-668b5328c3fc)
* Criar um quarto
  ![image](https://github.com/user-attachments/assets/2101ca6e-bd56-4ea4-8f21-3491c1f7982a)
* Buscando todos os quartos
  ![image](https://github.com/user-attachments/assets/4ba064f1-58e8-4410-9728-1d9712c5d1eb)

## Criando uma reserva
* Fazendo login com um usuário GUEST
  ![image](https://github.com/user-attachments/assets/413f1778-7855-4eac-b702-f91c150c9904)
* Criando uma reserva
  ![image](https://github.com/user-attachments/assets/d758c9a4-191e-47dd-b199-2dda4c558284)

## Tecnologias utilizadas nesse projeto
* Java
* Spring Boot,Security,DataJPA
* Lombok
* Docker
* Junit5
* Mockito
* TestContainers
* Rest-Assured
* Postman
* Swagger


 







