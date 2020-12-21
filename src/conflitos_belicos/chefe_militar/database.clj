(ns conflitos-belicos.chefe-militar.database
  (:require [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [conflitos-belicos.shared.database-config :as db]
            [clojure.string :as str]))

(defn get-all-militar-lider []
  (let [sql "SELECT * FROM chefe_militar"]
    (sql/query (db/get-datasource) [sql] {:builder-fn rs/as-unqualified-lower-maps})))

(defn create-militar-lider
  [faixa codigo-ga codigo-divisao nome-lp]
  (let [sql "INSERT INTO chefe_militar (faixa, cod_ga, cod_divisao, cod_lp) VALUES (?, ?, ?, ?)"
        uppercased-name (str/upper-case nome-lp)]
    (sql/query (db/get-datasource) [sql faixa codigo-ga codigo-divisao uppercased-name])))