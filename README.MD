🎓 Projeto de Conclusão da Disciplina de POO
Sistema de Gestão de Biblioteca Acadêmica

Disciplina: POO - Programação Orientada a Objetos

Professor(a): Me. Gleidson Porto Batista

1. Objetivo Geral
   Desenvolver um sistema orientado a objetos utilizando a linguagem Java, com foco na gestão de uma biblioteca acadêmica. O sistema deverá aplicar os principais conceitos da Programação Orientada a Objetos (POO) e incluir uma interface textual (console) ou gráfica opcional (Swing ou web) , permitindo que usuários realizem consultas, empréstimos e devoluções de livros de forma funcional e organizada.



2. Descrição Geral do Sistema
   O sistema simula o funcionamento de uma biblioteca universitária. Ele deverá permitir que:

O bibliotecário cadastre usuários da biblioteca (alunos e professores), gerencie o acervo de livros e realize operações de empréstimo e devolução;
As informações sejam armazenadas e consultadas em tempo de execução utilizando estruturas de dados baseadas em Arrays ou Collections (como ArrayList e HashMap) ou com banco de dados.
3. Requisitos Funcionais
   O sistema deverá contemplar, no mínimo, as seguintes funcionalidades:

✅ Cadastro de livros: título, autor, ISBN, ano e editora.

✅ Cadastro de usuários: nome, matrícula, tipo (aluno ou professor), CPF e e-mail.

✅ Consulta de disponibilidade de livros.

✅ Empréstimos: número do empréstimo, usuário, livro(s), data e status do empréstimo.

✅ Devolução de livros: atualização do status do empréstimo e da disponibilidade do exemplar.

✅ Listagem de livros: ISBN, título, autor, editora e status (disponível ou emprestado).

4. Modelagem UML
   Os seguintes diagramas devem ser incluídos no projeto:

Diagrama de Casos de Uso: deve contemplar o ator Bibliotecário e suas interações com o sistema.
Diagrama de Classes: deve conter, no mínimo, as seguintes classes:
Livro
Usuario (classe base), Aluno, Professor
Emprestimo
(Opcional) Diagrama de Sequência: para as operações de empréstimo e devolução.
5. Requisitos Técnicos
   Linguagem: Java (JDK 8 ou superior)
   Paradigma: Programação Orientada a Objetos
   IDE sugeridas: BlueJ, Eclipse, IntelliJ IDEA ou Visual Studio Code
   Interface: Texto (console) ou Gráfica (Swing) ou interface web.
   Gravar informações no banco de dados (desejável).
   Boas práticas de programação recomendadas:
   Uso adequado de encapsulamento (atributos privados e métodos públicos com getters e setters);
   Implementação de herança (classes Aluno e Professor herdando de Usuario);
   Aplicação de polimorfismo, quando possível;
   Uso de coleções genéricas (ArrayList, HashMap, etc.) para gerenciamento de dados em memória;
   Organização do projeto em pacotes e métodos bem nomeados;
   Inclusão de comentários explicativos no código.
6. Critérios de Avaliação (Valor: 2,0 pontos)


A avaliação do projeto será realizada com base nos seguintes critérios:



Aplicação dos conceitos de Programação Orientada a Objetos (POO):

Serão analisados o uso correto de classes, objetos, herança, encapsulamento, polimorfismo e boas práticas de codificação.



Modelagem UML:

Os diagramas de Casos de Uso e de Classes devem ser coerentes com a proposta do sistema, bem estruturados e completos.



Funcionalidade do sistema:

O sistema deve atender aos requisitos definidos, com as funcionalidades operando corretamente (cadastros, empréstimos, devoluções, etc.).



Organização e clareza do código:

O código deve ser legível, bem indentado, com nomes de variáveis e métodos significativos, e conter comentários relevantes.



Relatório técnico e apresentação do projeto:

Será avaliada a entrega do relatório técnico com todos os elementos solicitados .



7. Entrega do Projeto
   7.1 Conteúdo da Entrega
   O projeto deverá ser entregue compactado em um único arquivo ZIP, contendo:

Código-fonte completo da aplicação (com pastas organizadas).
Relatório técnico em PDF, contendo:
Capa com nome dos integrantes do projeto;
Descrição das funcionalidades implementadas;
Diagrama de Casos de Uso e Diagrama de Classes (e Sequência, se houver);
Prints ou descrição das interfaces (console ou Swing);
Código-fonte completo das classes.
7.2 Prazo
Data de entrega: 05/06.



8. Considerações Finais
   O projeto poderá ser desenvolvido individualmente ou em duplas.
   Cada aluno deverá submeter seu próprio arquivo na plataforma, mesmo que em dupla.
   A nota poderá ser individual, considerando especialmente o envolvimento do aluno durante a realização em sala.
   Projetos com indícios de plágio ou funcionamento comprometido estarão sujeitos à redução ou anulação da nota, conforme critério do professor.