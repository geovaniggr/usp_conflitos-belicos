(ns conflitos-belicos.lider-politico.database
  (:require [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [conflitos-belicos.shared.database-config :as db]
            [clojure.string :as str]))

(defn create-political-lider
  [nome cod-ga apoios]
  (let [sql "INSERT INTO lider_politico (nome, cod_ga, apoios) VALUES (?, ?, ?) RETURNING nome"
        uppercased-name (str/upper-case nome)]
    (sql/query (db/get-datasource) [sql uppercased-name cod-ga apoios] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-political-liders-name []
  (let [sql "SELECT nome FROM lider_politico"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn get-all-political []
  (let [sql "SELECT * FROM lider_politico"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))