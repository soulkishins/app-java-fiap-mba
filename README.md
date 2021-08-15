# app-java-fiap-mba

## Backend para feature demo de antecipação da agenda de recebíveis de cartão.

A fin de demostração dois usuários podem ser utilizados

demo1@example.com ou demo2@example.com

A tela de login não valida a senha, qualquer valor informado para um dos usuários será considerado válido.

Para cadastro de uma nova liberação de acesso a agenda, o mock de dados internos retornará dados fictícios inicializados no startup da app para as Bandeiras e Adquirentes abaixo.

Bandeiras: MCC, MCD ou VCD
Adquirentes: 35722930000133, 04242332000133 ou 83859428000111

## Backend para feature demo de antecipação da agenda de recebíveis de cartão.

Para build da image docker executar o comando abaixo.

.\mvnw spring-boot:build-image

Para executar o docker criado localmente

docker run -p 8080:8080 backend-nu-credit:0.0.1-SNAPSHOT

