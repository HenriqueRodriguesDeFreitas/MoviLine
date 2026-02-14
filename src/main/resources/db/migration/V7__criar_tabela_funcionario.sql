CREATE TABLE IF NOT EXISTS funcionario (
 id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    matricula VARCHAR(20) NOT NULL UNIQUE,
    data_matricula DATE NOT NULL,
    pessoa_id UUID NOT NULL,
    cargo_id UUID NOT NULL,
    CONSTRAINT fk_funcionario_pessoa_fkey
     FOREIGN KEY(pessoa_id) REFERENCES pessoa(id),
    CONSTRAINT fk_funcionario_cargo_fkey
     FOREIGN KEY(cargo_id) REFERENCES cargo(id)
);