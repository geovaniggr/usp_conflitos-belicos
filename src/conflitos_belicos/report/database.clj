(ns conflitos-belicos.report.database
  (:require [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [conflitos-belicos.shared.database-config :as db]))

(defn get-raw [sql]
  (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps}))

(defn get-top-5-conflicts-by-number-of-death []
  (let [sql "
    SELECT nome, num_mortos
    FROM
       conflito
    ORDER BY num_mortos DESC
    LIMIT 5;"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-gun-dealer-provide-to-army-group []
  (let [sql "
       SELECT DISTINCT nome, nome_traficante, nome_arma
       FROM grupo_armado
       INNER JOIN (
           SELECT nome_traficante, nome_arma, cod_ga
           FROM traficante_forneceu
           WHERE nome_arma = 'Barret M82' OR nome_arma = 'M200 Intervention'
       ) as fornecimento
       ON grupo_armado.codigo = fornecimento.cod_ga;"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-top-5-ong-by-number-of-intervention []
  (let [sql "
    SELECT nome, intermediacoes
    FROM organizacoes_mediadoras
    INNER JOIN (
            SELECT codigo_org, COUNT(codigo_org ) as intermediacoes
            FROM mediam_entrada
            GROUP BY mediam_entrada.codigo_org
    ) intermediacoes
    ON organizacoes_mediadoras.codigo = intermediacoes.codigo_org
    ORDER BY intermediacoes DESC
    LIMIT 5;"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-country-with-most-number-of-religious-conflicts []
  (let [sql "
    SELECT pais, count( distinct codigo) as numero_conflito
    FROM conflito
        INNER JOIN conflito_pais cp on conflito.codigo = cp.codigo_conflito
        LEFT JOIN conflito_religioso cr on conflito.codigo = cr.codigo_conflito
    WHERE religiao IS NOT NULL
    GROUP BY pais
    ORDER BY numero_conflito DESC
    LIMIT 1;"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-top-5-army-groups-by-number-of-guns []
  (let [sql "
    SELECT cod_ga, sum(numero_de_armas) as qtd_armas
    FROM
       traficante_forneceu
    GROUP BY cod_ga
    ORDER BY qtd_armas DESC
    limit 5;"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-histogram-data []
  (let [sql "SELECT
  ( SELECT count(distinct codigo_conflito) FROM conflito_economico) as conflito_economico,
  ( SELECT count(distinct codigo_conflito) FROM conflito_territorial) as conflito_territorial,
  ( SELECT count(distinct codigo_conflito) FROM conflito_religioso) as conflito_religioso,
  ( SELECT count(distinct codigo_conflito) FROM conflito_etnico) as conflito_etnico"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))