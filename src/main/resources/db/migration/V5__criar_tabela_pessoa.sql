CREATE TABLE IF NOT EXISTS pessoa (
    id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    nome VARCHAR(250) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    endereco_id UUID NOT NULL,
    CONSTRAINT fk_pessoa_endereco_id_fkey
    FOREIGN KEY(endereco_id) REFERENCES endereco(id)
    );
