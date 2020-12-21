(ns conflitos-belicos.conflitos.database
  (:require [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [conflitos-belicos.shared.database-config :as db]
            [conflitos-belicos.conflitos.model :as conflito-model]))

(defn add-conflict-country [db id country]
  (let [sql "INSERT INTO conflito_pais (codigo_conflito, pais) VALUES (?, ?)"]
    (mapv (fn [country-name] (sql/query db [sql id country-name])) country)))

(defn add-conflict-reason [db type id reasons]
  (let [sql (conflito-model/get-conflict-insert-query type)]
    (mapv (fn [reason] (sql/query db [sql id reason])) reasons)))

(defn create-conflict [nome, tipo-conflito num-mortos num-feridos]
  (let [sql "INSERT INTO conflito (nome, tipo_conflito, num_mortos, num_feridos) VALUES (?, ?, ?, ?) RETURNING codigo"
        conflito (conflito-model/get-conflict-name tipo-conflito)
        datasource (db/get-datasource)]
    (if conflito
      (:codigo (first (sql/query datasource [sql nome conflito (Integer/parseInt num-mortos) (Integer/parseInt num-feridos)] {:builder-fn rs/as-unqualified-lower-maps})))
      nil)))

(defn create-full-conflict
  [nome tipo-conflito num-mortos num-feridos paises-participantes causas]
  (let [id (create-conflict nome tipo-conflito num-mortos num-feridos)
        datasource (db/get-datasource)]
    (when id
      (add-conflict-reason datasource tipo-conflito id causas)
      (add-conflict-country datasource id paises-participantes))))

(defn list-all-conflicts []
  (let [sql "SELECT * FROM conflito"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

