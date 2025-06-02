-- Usuários (superclasse)
INSERT INTO usuario_biblioteca (id, nome, cpf, email)
VALUES (1, 'Rafael Silva', '12345678901', 'rafael@email.com'),
       (2, 'Maria Oliveira', '23456789012', 'maria@email.com'),
       (3, 'João Santos', '34567890123', 'joao@email.com'),
       (4, 'Ana Pereira', '45678901234', 'ana@email.com');

 --Alunos (cada aluno tem id, nome, cpf, email, matricula)
 INSERT INTO aluno (id, nome, cpf, email, matricula)
 VALUES (1, 'Rafael Silva', '12345678901', 'rafael@email.com', 'MAT20230001'),
        (2, 'Maria Oliveira', '23456789012', 'maria@email.com', 'MAT20230002');

 -- Professores (cada professor tem id, nome, cpf, email, matricula)
 INSERT INTO professor (id, nome, cpf, email, matricula)
 VALUES (3, 'João Santos', '34567890123', 'joao@email.com', 'PROF20230001'),
       (4, 'Ana Pereira', '45678901234', 'ana@email.com', 'PROF20230002');

 --Livros
 INSERT INTO livro (id, titulo, autor, isbn, ano, editora)
 VALUES (1, 'Programação Java', 'José da Silva', '9788575224177', '2020', 'Editora Alfa'),
        (2, 'Banco de Dados', 'Maria Souza', '9788575224184', '2018', 'Editora Beta');

 -- Exemplares de livros
 INSERT INTO exemplar_livro (id, livro_id, codigo_exemplar, status_exemplar)
 VALUES (1, 1, 'EXEMP001', 'DISPONIVEL'),
        (2, 1, 'EXEMP002', 'EMPRESTADO'),
        (3, 2, 'EXEMP003', 'DISPONIVEL');

 --INSERT INTO emprestimo (id, exemplar_livro_id, matricula_id, data_emprestimo, data_entrega_prevista,
 --                        data_entrega_factual, status_emprestimo)
 --VALUES (1, 2, 'MAT20230001', DATE '2025-05-01', DATE '2025-05-15', NULL, 'ATIVO'),
 --       (2, 3, 'MAT20230002', DATE '2025-04-20', DATE '2025-05-04', DATE '2025-05-03', 'FINALIZADO');
