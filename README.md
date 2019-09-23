``` 
 █████╗  ██████╗ ███████╗███╗   ██╗██████╗  █████╗     ██████╗ ██████╗ 
██╔══██╗██╔════╝ ██╔════╝████╗  ██║██╔══██╗██╔══██╗    ██╔══██╗██╔══██╗
███████║██║  ███╗█████╗  ██╔██╗ ██║██║  ██║███████║    ██║  ██║██████╔╝
██╔══██║██║   ██║██╔══╝  ██║╚██╗██║██║  ██║██╔══██║    ██║  ██║██╔══██╗
██║  ██║╚██████╔╝███████╗██║ ╚████║██████╔╝██║  ██║    ██████╔╝██████╔╝
╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═══╝╚═════╝ ╚═╝  ╚═╝    ╚═════╝ ╚═════╝ 
                                                                                                                                                           
```
Power By: Leonardo Maximino de Mendonça

## Introdução
O Sistema **AGENDA DB** consiste em uma aplicação executavel via linha de comando com o foco em 
substituir agendas manuscristas por um sistema simples e programavel.

A estrutura do sistema está dividia no padrão MVC onde os Models se encontram no diretorio 
```src/main/java/model```, os Controllers no diretorio ```src/main/java/controller``` e a View no 
arquivo```src/main/java/AgendaDbApp.java```.

## Tecnologias aplicadas
Para a realização do projeto foi utilizado a linguagem Java na versão 8 junto com o SQLite como SGBD, 
além do maven como gerenciador de dependencias.

## Quick-start
Primeiro, tenha certeza que pussi instalado o Java vesão 8, & uma versão recente do maven.

Abra o terminal e insira os seguintes comandos.

```
git clone git@trab.dc.unifil.br:leonardo.maximino/agenda-db.git
cd agenda-db/

mvn install

java -jar target/agenda-db-1.0.0.jar

```


## Docs
O codigo utilizado para gerar os diagramas estão no diretorio ```docs/diagrams/src```, abaixo está
listado as imagens dos diagramas disponiveis:

#### Caso de Uso
![UC](docs/diagrams/images/use_case.png)

## Tutorial

#### Cadastro de Contato
Para cadastrar um novo contato no sistema deve ser informado a opção **2**, posteriormente será necessário informar os 
dados nome, sobrenome e email do contato. 

Após informar os dados básicos do contato será possivel adicionar telefones ao mesmo, caso o telefone inserido já esteja cadastrado 
no sistema o mesmo será reaproveitado apenas criando o vinculo entre contato e telefone, caso contrario um novo telefone será criado. 
Pode ser adicionados quantos telefones desejar.

Após informar os telefones será possivel adicionar contato a grupos, caso o grupo inserido já esteja cadastrado 
no sistema o mesmo será reaproveitado apenas criando o vinculo entre contato e grupo, caso contrario um novo grupo será criado. 
Pode ser adicionados quantos grupos desejar.

#### Edição de Contato
Para editar o contato o mesmo deve ser buscado. Existem 2 maneiras de se pesquisar o contato:
1. Por Nome completo
    * Nome concatenado com sobrenome
1. Por ID do contato.

Caso o contato sejá encontrado será possivel editar informações que competem ao contato como nome, sobrenome, emails, telefones e/ou grupos.


#### Remoção de Contato
Para remover o contato o mesmo deve ser buscado. Existem 2 maneiras de se pesquisar o contato:
1. Por Nome completo
    * Nome concatenado com sobrenome
1. Por ID do contato.

Caso o contato sejá encontrado o mesmo será deletado do sistema.
* Caso o contato esteja em um grupo sozinho, o grupo também será deletado.
* Caso o contato possua um telefone que seja somente seu, o telefone também será deletado.

### Listagem dos Contatos de um Grupo
Para listar os contatos a partir de um grupo deve ser informado a opção **5**, posteriormente será necessário informar a 
descricao do mesmo, se o grupo existir será listado os contatos do mesmo.
