# :hotel: Hotel SN
> Backend para realização de cadastro de hóspedes e check in. <br/>
> Este projeto foi desenvolvido com foco no TDD. Foram desenvolvidos testes unitários e de integração para as camadas repository, service e controller.
> Além disso, também foram implementados testes para validar as regras de negócio bem como os cálculos dos preços das hospedagens. No total, há atualmente 125 testes implementados.
>
> Implantado em: https://hotelsn.herokuapp.com/ <br/>

# Regras de negócio
- Uma diária no hotel de segunda à sexta custa R$120,00;
- Uma diária no hotel em finais de semana custa R$150,00;
- Caso a pessoa precise de uma vaga na garagem do hotel há um acréscimo diário,
sendo R$15,00 de segunda à sexta e R$20,00 nos finais de semana;
- Caso o horário da saída seja após às 16:30h deve ser cobrada uma diária extra;

# Requisitos Funcionais
- Padrão REST para comunicação;
- Permitir cadastrar pessoas;
- Permitir realizar o checkin e checkout;
- Permitir consultar ex-hóspedes;
- Vamos usar Postman para testar sua aplicação.

# Modelo Lógico
![modelo](https://user-images.githubusercontent.com/50798315/119744081-9f616f00-be61-11eb-9222-b50e6b2b08a5.png)

# Rotas da Aplicação

Para as entidades Room, Garage, Person e Bookings foram feitas inicialmente as 5 rotas básicas do CRUD:
- insert
- findAll
- findById
- update
- delete

Após isto, foram implementadas rotas adicionais, tais como:
- [GET] ```/persons/exGuests``` para retornar ex-hóspedes;
- [GET] ```/persons/currentGuests``` para retornar os hóspedes atuais
- [PATCH] ```/bookings/:id/checkin ``` para realizar o check-in da reserva identificada pelo id
- [PATCH] ```/bookings/:id/checkin ``` para realizar o check-out da reserva identificada pelo id
- [GET] ```/bookings/confirmed ``` para retornar as reservas em que foi realizado o check-in.
- [GET] ```/bookings/completed ``` para retornar as reservas em que o check-out já foi feito.
- [GET] ```/bookings/current ``` para retornar as reservas em que foi realizado o check-in mas não o check-out.
- [GET] ```/bookings/open ``` para retornar as reservas que estão aguardando pelo check-in.

Vale salientar que como validação para o check-out, foi implementado para ele só poder ser feito caso o check-in já tenha sido realizado.

Abaixo estão as descrições de todas as rotas:

## :bed: Room

### [GET] /rooms
Retorna todos os quartos do hotel.
### [POST] /rooms 
Registra um quarto no hotel. Exemplo de corpo da requisição:
``` 
{
  "name": "601",
  "isAvailable": false
} 
```
    
### [GET] /rooms/:id
Retorna o quarto identificado pelo id.
### [PUT] /rooms/:id
Atualiza as informações do quarto identificado pelo id.
### [DELETE] /roms/:id
Remove o quarto do sistema, quando este não possui dependência com outros registros.

## :car: Garage

### [GET] /garages
Retorna todas as vagas de garagens do hotel.
### [POST] /garages 
Registra uma vaga de garagem no hotel. Exemplo de corpo da requisição:
``` 
{
  "name": "AC12",
  "isAvailable": true
} 
```
    
### [GET] /garages/:id
Retorna a garagen identificada pelo id.
### [PUT] /garages/:id
Atualiza as informações da garagem identificada pelo id.
### [DELETE] /garages/:id
Remove a garagem do sistema, quando esta não possui dependência com outros registros.

## :curly_haired_woman: Person

### [GET] /persons
Busca paginada que retorna todas as pessoas cadastradas no hotel.
### [POST] /persons 
Registra uma pessoa no sistema do hotel. Exemplo de corpo da requisição:
``` 
{
  "name": "Maria Joana",
  "email": "maria@email.com",
  "age": 24
} 
```
    
### [GET] /persons/:id
Retorna a pessoa identificada pelo id.
### [PUT] /persons/:id
Atualiza as informações da pessoa identificada pelo id.
### [DELETE] /persons/:id
Remove a pessoa do sistema, quando esta não possui dependência com outros registros.
### [GET] /persons/exGuests
Retorna uma lista de pessoas que já foram hóspedes do hotel.
### [GET] /persons/currentGuests
Retorna uma lista de pessoas que estão se hospedando atualmente no hotel.

## :bookmark: Bookings

### [GET] /bookings
Busca paginada que retorna todas as reservas do hotel.<br/>
Além dos parâmetros padrões do pageable, também pode-se utilizar o parâmetro "personId" para filtrar a reserva pelo id da pessoa. <br/>
Exemplo de requisição:
```/bookings?page=0&size=12&sort=dtCheckout,desc ```
```
{
    "content": [
        {
            "id": 1,
            "startDate": "2021-05-13T12:30:00Z",
            "endDate": "2021-05-21T19:30:00Z",
            "dtCheckin": "2021-05-13T13:00:00Z",
            "dtCheckout": "2021-05-21T20:00:00Z",
            "personId": 1,
            "roomId": 1,
            "garageId": null,
            "expectedPrice": 1140.0,
            "realPrice": 1290.0
        },
        ...
    ]
}
```
### [POST] /bookings 
Registra uma nova reserva no sistema do hotel. Exemplo de corpo da requisição:
``` 
{
  "startDate": "2021-05-13T12:30:00Z",
  "endDate": "2021-05-21T19:30:00Z",
  "personId": 1,
  "roomId": 1,
  "garageId": null,
} 
```
    
### [GET] /bookings/:id
Retorna as informações da reserva identificada pelo id. Dentre estas informações, tem-se o preço esperado no campo expectedPrice (com base nas datas definidas inicialmente de entrada e saída do hotel). <br/>
Caso a reserva já tenha sido finalizada (feito o checkout), também é mostrado o valor real da estadia através do campo realPrice. 
### [PUT] /bookings/:id
Atualiza as informações da reserva identificada pelo id.
### [DELETE] /bookings/:id
Remove a reserva do sistema.
### [GET] /bookings/confirmed
Retorna as reservas confirmadas. Isto é, em que o check-in já foi feito.
### [GET] /bookings/completed
Retorna as reservas em que o check-out já foi realizado.
### [GET] /bookings/current
Retorna as informações das reservas com os hóspedes atuais do hotel.
### [GET] /bookings/open
Retorna as informações das reservas que não foram confirmadas com check-ins.
### [PATCH] /bookings/:id/checkin
Realiza o check-in na reserva identificada.
### [PATCH] /bookings/:id/checkout
Realiza o check-out na reserva identificada.

# :abacus: Tecnologias Utilizadas
- SpringBoot, Hibernate, JPA, Maven
- Heroku, para implantação na nuvem
- Testes unitários e de Integração com JUnit 5 e Mockito, totalizando 125 testes para todas as camadas.

# :gear: Metodologias Aplicadas
- Test Driven Development (TDD)
- Domain Driven Design (DDD)
