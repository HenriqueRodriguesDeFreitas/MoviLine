CREATE TABLE IF NOT EXISTS permissao(
    id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(100) NOT NULL UNIQUE,
    descriao VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS permissaofuncionario(
  id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  funcionario_id UUID NOT NULL,
  permissao_id UUID NOT NULL,
    CONSTRAINT fk_permissaofuncionario_funcionario_fkey
     FOREIGN KEY(funcionario_id) REFERENCES funcionario(id),
    CONSTRAINT fk_permissaofuncionario_permissao_fkey
     FOREIGN KEY(permissao_id) REFERENCES permissao(id)
);