ALTER TABLE endereco ADD CONSTRAINT fk_endereco_rua_id_fkey
    FOREIGN KEY (rua_id) REFERENCES rua(id);