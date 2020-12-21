create table if not exists conflito
(
    codigo serial not null primary key,
    nome varchar(150),
    num_mortos integer default 0,
    num_feridos integer default 0,
    tipo_conflito varchar(150)
);

create table if not exists conflito_economico
(
    codigo_conflito integer not null,
    materia_prima varchar(200),
    FOREIGN KEY (codigo_conflito) REFERENCES conflito(codigo)
);

create table if not exists conflito_territorial
(
    codigo_conflito integer not null,
    regiao varchar(200),
    FOREIGN KEY (codigo_conflito) REFERENCES conflito(codigo)
);

create table if not exists conflito_pais
(
    codigo_conflito integer not null,
    pais varchar(250),

    FOREIGN KEY (codigo_conflito) REFERENCES conflito(codigo)
);

create table if not exists conflito_religioso
(
    codigo_conflito integer not null,
    religiao varchar(200),
    FOREIGN KEY (codigo_conflito) REFERENCES conflito(codigo)
);

create table if not exists conflito_etnico
(
    codigo_conflito integer not null,
    etnias varchar(200),
    FOREIGN KEY (codigo_conflito) REFERENCES conflito(codigo)
);

create table if not exists grupo_armado
(
    codigo serial not null primary key,
    nome varchar(150),
    numero_baixas integer default 0
);

create table if not exists lider_politico
(
    nome varchar(150) not null primary key,
    cod_ga integer not null,
    apoios text,

    FOREIGN KEY (cod_ga) REFERENCES grupo_armado(codigo)
);

create table if not exists divisao
(
    numero_divisao INT NOT NULL,
    codigo_ga integer not null,
    num_baixas integer,
    num_homens integer,
    num_avioes integer,
    num_tanque integer,
    num_barcos integer,

    FOREIGN KEY (codigo_ga) REFERENCES grupo_armado(codigo),
    PRIMARY KEY (numero_divisao, codigo_ga)
);

create table if not exists chefe_militar
(
    codigo serial not null primary key ,
    faixa varchar(150),
    cod_ga integer,
    cod_divisao integer,
    cod_lp text not null,

    FOREIGN KEY (cod_ga, cod_divisao) REFERENCES divisao(codigo_ga, numero_divisao) ON UPDATE CASCADE,
    FOREIGN KEY (cod_lp) REFERENCES lider_politico(nome) ON UPDATE CASCADE

);

create table if not exists entrada_participacao (
  codigo_ga INT NOT NULL,
  codigo_conflito INT NOT NULL,
  data_entrada DATE,

  FOREIGN KEY (codigo_ga) REFERENCES grupo_armado(codigo) ON UPDATE CASCADE,
  FOREIGN KEY (codigo_conflito) REFERENCES conflito(codigo) ON UPDATE CASCADE
);

create table if not exists saida_participacao (
    codigo_ga INT NOT NULL,
    codigo_conflito INT NOT NULL,
    data_saida DATE,

    FOREIGN KEY (codigo_ga) REFERENCES grupo_armado(codigo) ON UPDATE CASCADE,
    FOREIGN KEY (codigo_conflito) REFERENCES conflito(codigo) ON UPDATE CASCADE
);


create table if not exists traficante
(
    nome varchar(150) not null primary key
);

create table if not exists arma
(
    nome varchar(150) not null primary key,
    indicador_destrutivo integer
);

create table if not exists traficante_pode_fornecer
(
    nome_traficante varchar(150),
    nome_arma varchar(150),
    quantidade integer,

    FOREIGN KEY (nome_traficante) REFERENCES traficante(nome) ON UPDATE CASCADE ,
    FOREIGN KEY  (nome_arma) REFERENCES arma(nome) ON UPDATE CASCADE
);

create table if not exists traficante_forneceu
(
    cod_ga integer not null,
    nome_traficante varchar(150) not null,
    nome_arma varchar(150) not null,
    numero_de_armas integer,

    FOREIGN KEY (cod_ga) REFERENCES grupo_armado(codigo) ON UPDATE CASCADE,
    FOREIGN KEY (nome_traficante) REFERENCES traficante(nome) ON UPDATE CASCADE,
    FOREIGN KEY (nome_arma) REFERENCES arma(nome) ON UPDATE CASCADE
);

create table if not exists organizacoes_mediadoras
(
    codigo serial not null primary key,
    nome varchar(150),
    tipo_ajuda varchar(250),
    num_pessoas integer,
    org_dependente integer,

    FOREIGN KEY (org_dependente) REFERENCES organizacoes_mediadoras(codigo) ON UPDATE CASCADE
);

create table if not exists mediam_entrada (
    codigo_org int not null,
    codigo_conf int not null,
    data_entrada date,

    foreign key (codigo_org) REFERENCES organizacoes_mediadoras(codigo) ON UPDATE CASCADE,
    foreign key (codigo_conf) REFERENCES conflito(codigo) ON UPDATE CASCADE
);

create table if not exists mediam_saida (
      codigo_org int not null,
      codigo_conf int not null,
      data_entrada date,

      foreign key (codigo_org) REFERENCES organizacoes_mediadoras(codigo) ON UPDATE CASCADE,
      foreign key (codigo_conf) REFERENCES conflito(codigo) ON UPDATE CASCADE
);

CREATE OR REPLACE FUNCTION valida_hierarquia_economico() RETURNS TRIGGER AS '
    begin
        IF EXISTS( SELECT codigo_conflito FROM conflito_etnico WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        ELSIF EXISTS( SELECT codigo_conflito FROM conflito_religioso WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        ELSIF EXISTS( SELECT codigo_conflito FROM conflito_territorial WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        end if;
        RETURN NEW;
    end;
' language plpgsql;

CREATE OR REPLACE FUNCTION valida_hierarquia_etnico() RETURNS TRIGGER AS '
    begin
        IF EXISTS( SELECT codigo_conflito FROM conflito_economico WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        ELSIF EXISTS( SELECT codigo_conflito FROM conflito_religioso WHERE codigo_conflito = NEW.codigo_conflito) THEN

            RETURN NULL;
        ELSIF EXISTS( SELECT codigo_conflito FROM conflito_territorial WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        end if;
        RETURN NEW;
    end;
' language plpgsql;

CREATE OR REPLACE FUNCTION valida_hierarquia_religioso() RETURNS TRIGGER AS '
    begin
        IF EXISTS( SELECT codigo_conflito FROM conflito_etnico WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        ELSIF EXISTS( SELECT codigo_conflito FROM conflito_economico WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        ELSIF EXISTS( SELECT codigo_conflito FROM conflito_territorial WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        end if;
        RETURN NEW;
    end;
' language plpgsql;

CREATE OR REPLACE FUNCTION valida_hierarquia_territorial() RETURNS TRIGGER AS '
    begin
        IF EXISTS( SELECT codigo_conflito FROM conflito_etnico WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        ELSIF EXISTS( SELECT codigo_conflito FROM conflito_religioso WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        ELSIF EXISTS( SELECT codigo_conflito FROM conflito_economico WHERE codigo_conflito = NEW.codigo_conflito) THEN
            RETURN NULL;
        end if;
        RETURN NEW;
    end;
' language plpgsql;

CREATE TRIGGER valida_hierarquia_economica
BEFORE INSERT OR UPDATE ON conflito_economico
FOR EACH ROW EXECUTE PROCEDURE valida_hierarquia_economico();

CREATE TRIGGER valida_hierarquia_etnica
BEFORE INSERT OR UPDATE ON conflito_etnico
FOR EACH ROW EXECUTE PROCEDURE valida_hierarquia_etnico();

CREATE TRIGGER valida_hierarquia_religiosa
BEFORE INSERT OR UPDATE ON conflito_religioso
FOR EACH ROW EXECUTE PROCEDURE valida_hierarquia_religioso();

CREATE TRIGGER valida_hierarquia_territorial
BEFORE INSERT OR UPDATE ON conflito_territorial
FOR EACH ROW EXECUTE PROCEDURE valida_hierarquia_territorial();


create or replace function valida_existencia_unico_chefe_militar() returns trigger as '
    begin
        IF (SELECT count(*) as total from chefe_militar WHERE cod_divisao = OLD.cod_divisao AND cod_ga = OLD.cod_ga) <= 1 THEN
            RETURN NULL;
        end if;
        IF (tg_op = ''UPDATE'') THEN
            RETURN NEW;
        end if;
        RETURN OLD;
    end;
' language plpgsql;

CREATE TRIGGER valida_existencia_unico_chefe_militar
BEFORE UPDATE OR DELETE ON chefe_militar
FOR EACH ROW EXECUTE PROCEDURE valida_existencia_unico_chefe_militar();

CREATE OR REPLACE FUNCTION valida_maximo_chefe_militares() RETURNS TRIGGER as '
    begin
        if(SELECT count(*) FROM chefe_militar WHERE cod_divisao = NEW.cod_divisao AND cod_ga = NEW.cod_ga) >= 3 THEN
            RETURN NULL;
        end if;
        RETURN NEW;
    end;
'language plpgsql;

CREATE TRIGGER valida_maximo_chefe_militares
BEFORE INSERT OR UPDATE ON chefe_militar
FOR EACH ROW EXECUTE PROCEDURE valida_maximo_chefe_militares();

CREATE OR REPLACE FUNCTION validez_existencia_divisao_grupo_armado() RETURNS TRIGGER as '
    begin
        IF( SELECT COUNT(*) FROM divisao WHERE codigo_ga = OLD.codigo_ga) = 1 THEN
            RETURN NULL;
        end if;
        IF(tg_op = ''UPDATE'') THEN
            RETURN NEW;
        end if;
        RETURN OLD;
    end;
' LANGUAGE plpgsql;

CREATE TRIGGER validez_existencia_divisao_grupo_armado
BEFORE DELETE OR UPDATE ON divisao
FOR EACH ROW EXECUTE PROCEDURE validez_existencia_divisao_grupo_armado();

CREATE OR REPLACE FUNCTION validez_minimo_grupos_armados_ativos_participantes() RETURNS TRIGGER AS '

    declare
        participantes_ativos INT;
    begin
        participantes_ativos = (
            SELECT count(*) FROM
                (SELECT codigo_ga, codigo_conflito, max(data_entrada) as data_entrada
                 FROM entrada_participacao e
                 WHERE e.codigo_conflito = NEW.codigo_conflito
                 GROUP BY codigo_ga, codigo_conflito
                ) ultima_entrada
            LEFT JOIN
                (SELECT codigo_ga, codigo_conflito, max(data_saida) as data_saida
                 FROM saida_participacao s
                 WHERE s.codigo_conflito = NEW.codigo_conflito
                 GROUP BY codigo_ga, codigo_conflito
                ) ultima_saida
            ON ultima_entrada.codigo_conflito = ultima_saida.codigo_conflito AND ultima_entrada.codigo_ga = ultima_saida.codigo_ga
            WHERE ultima_entrada.data_entrada > ultima_saida.data_saida OR ultima_saida.data_saida IS NULL
        );
        IF participantes_ativos <= 2 THEN
            RETURN NULL;
        end if;
        RETURN NEW;
    end;

' LANGUAGE plpgsql;

CREATE TRIGGER validez_minimo_grupos_armados_ativos_participantes
BEFORE INSERT ON saida_participacao
FOR EACH ROW EXECUTE PROCEDURE validez_minimo_grupos_armados_ativos_participantes();

CREATE OR REPLACE FUNCTION validez_unico_pais() RETURNS TRIGGER AS '
    begin
        IF( SELECT count(*) FROM conflito_pais WHERE codigo_conflito = OLD.codigo_conflito) <= 1 THEN
            RETURN NULL;
        end if;
        IF(tg_op = ''UPDATE'') THEN
            RETURN NEW;
        end if;
        RETURN OLD;
    end;
' language plpgsql;

CREATE TRIGGER validez_unico_pais
BEFORE UPDATE OR DELETE ON conflito_pais
FOR EACH ROW EXECUTE PROCEDURE validez_unico_pais();

CREATE OR REPLACE FUNCTION atualizar_numero_de_baixas() RETURNS TRIGGER AS '
    begin
        IF(tg_op = ''UPDATE'') THEN
            UPDATE grupo_armado
            SET numero_baixas = numero_baixas + NEW.num_baixas - OLD.num_baixas
            WHERE codigo = NEW.codigo_ga;
            RETURN NEW;
        end if;
        IF(tg_op = ''DELETE'') THEN
            UPDATE grupo_armado
            SET numero_baixas = numero_baixas - OLD.num_baixas
            WHERE codigo = NEW.codigo_ga;
            RETURN OLD;
        end if;
        UPDATE grupo_armado
        SET numero_baixas = numero_baixas + NEW.num_baixas
        WHERE codigo = NEW.codigo_ga;
        RETURN NEW;
    end;
' language plpgsql;

CREATE TRIGGER atualizar_numero_baixas
AFTER INSERT OR UPDATE ON divisao
FOR EACH ROW EXECUTE PROCEDURE atualizar_numero_de_baixas();

CREATE OR REPLACE FUNCTION criar_id_divisao() RETURNS TRIGGER AS '

    declare
        ultimo_id INT;
    begin
        ultimo_id = ((SELECT numero_divisao FROM divisao WHERE codigo_ga = NEW.codigo_ga ORDER BY numero_divisao DESC LIMIT 1) + 1);
        IF ultimo_id IS NOT NULL THEN
            NEW.numero_divisao = ultimo_id;
        ELSE
            NEW.numero_divisao = 1;
        end if;
        RETURN NEW;
    end;
' language  plpgsql;

CREATE TRIGGER criar_id_divisao
BEFORE INSERT ON divisao
FOR EACH ROW EXECUTE PROCEDURE criar_id_divisao();