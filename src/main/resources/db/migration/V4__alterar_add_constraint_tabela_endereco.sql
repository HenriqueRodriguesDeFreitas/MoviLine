ALTER TABLE endereco ADD CONSTRAINT fk_endereco_cidade_id_fkey
    FOREIGN KEY(cidade_id) REFERENCES cidade(id);
ALTER TABLE endereco ADD CONSTRAINT fk_endereco_estado_id_fkey
    FOREIGN KEY(estado_id) REFERENCES estado(id);