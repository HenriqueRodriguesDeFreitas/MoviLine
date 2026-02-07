create table IF NOT EXISTS estado(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
nome VARCHAR(19) NOT NULL UNIQUE
);

create table IF NOT EXISTS cidade(
id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
nome VARCHAR(32) NOT NULL,
estado_id UUID NOT NULL,
CONSTRAINT fk_cidade_estado_id_fkey
 FOREIGN KEY(estado_id) REFERENCES estado(id)
);

CREATE TABLE IF NOT EXISTS bairro(
id UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
nome VARCHAR(150) NOT NULL,
cidade_id UUID NOT NULL,
 CONSTRAINT fk_bairro_cidade_id_fkey
  FOREIGN KEY(cidade_id) REFERENCES cidade(id)
);
