(ns conflitos-belicos.grupos-armados.database
  (:require [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [conflitos-belicos.shared.database-config :as db]))

(defn create-army-group
  [nome]
  (let [sql "INSERT INTO grupo_armado (nome) VALUES (?) RETURNING codigo"]
    (-> (sql/query (db/get-datasource) [sql nome] {:builder-fn rs/as-unqualified-lower-maps})
        first
        :codigo)))

(defn create-division
  [codigo-ga num-baixas num-homens num-avioes num-tanques num-barcos]
  (let [sql "INSERT INTO divisao (codigo_ga, num_baixas, num_homens, num_avioes, num_tanque, num_barcos) VALUES ( ?, ?, ?, ?, ?, ?)"]
    (sql/query (db/get-datasource) [sql codigo-ga num-baixas num-homens num-avioes num-tanques num-barcos] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-all-army-groups []
  (let [sql "SELECT * FROM grupo_armado"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-army-group-code []
  (let [sql "SELECT codigo FROM grupo_armado"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-division-from-army-group [codigo-ga]
  (let [sql "SELECT numero_divisao FROM divisao WHERE codigo_ga = (?)"]
    (sql/query (db/get-datasource) [sql codigo-ga] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-all-division []
  (let [sql "SELECT * FROM divisao"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-name-and-code-from-army-group []
  (let [sql "SELECT codigo, nome FROM grupo_armado"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))