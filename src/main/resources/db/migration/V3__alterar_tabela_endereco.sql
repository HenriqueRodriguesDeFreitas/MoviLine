ALTER TABLE endereco ADD COLUMN cidade_id UUID NOT NULL;
ALTER TABLE endereco ADD COLUMN estado_id UUID NOT NULL;
ALTER TABLE endereco RENAME COLUMN logradouro TO rua;
