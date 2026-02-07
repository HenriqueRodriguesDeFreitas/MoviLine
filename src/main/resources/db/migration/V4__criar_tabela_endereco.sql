CREATE TABLE IF NOT EXISTS endereco(
    id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    numero int,
    complemento VARCHAR(250),
    rua_id UUID NOT NULL,
    CONSTRAINT fk_endereco_rua_id_fkey
    FOREIGN KEY(rua_id) REFERENCES rua(id)
);
