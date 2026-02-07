CREATE TABLE IF NOT EXISTS pessoa (
    id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(250) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    rua_id UUID NOT NULL,
    CONSTRAINT fk_pessoa_rua_id_fkey
    FOREIGN KEY(rua_id) REFERENCES rua(id)
    );