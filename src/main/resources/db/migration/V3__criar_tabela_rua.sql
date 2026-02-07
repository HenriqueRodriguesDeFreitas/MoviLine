CREATE TABLE IF NOT EXISTS rua(
  id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
  nome VARCHAR(250) NOT NULL,
  cep VARCHAR(8) NOT NULL,
    bairro_id UUID NOT NULL,
    CONSTRAINT fk_rua_bairro_id_fkey
                FOREIGN KEY (bairro_id) REFERENCES bairro(id)
);