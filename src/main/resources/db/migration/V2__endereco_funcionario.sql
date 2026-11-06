ALTER TABLE funcionarios
    ADD COLUMN cep character varying(255),
    ADD COLUMN logradouro character varying(255),
    ADD COLUMN bairro character varying(255),
    ADD COLUMN localidade character varying(255),
    ADD COLUMN uf character varying(255);
