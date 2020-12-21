(ns conflitos-belicos.conflitos.model
  (:require [clojure.string :as str]))

(def conflict-types {:ETNICO      {:name         "ETNICO"
                                   :table        "conflito_etnico"
                                   :insert-query "INSERT INTO conflito_etnico (codigo_conflito, etnias) VALUES (?, ?)"}
                     :ECONOMICO   {:name         "ECONOMICO"
                                   :table        "conflito_economico"
                                   :insert-query "INSERT INTO conflito_economico (codigo_conflito, materia_prima) VALUES (?, ?)"}
                     :RELIGIOSO   {:name         "RELIGIOSO"
                                   :table        "conflito_religioso"
                                   :insert-query "INSERT INTO conflito_religioso (codigo_conflito, religiao) VALUES (?, ?)"}
                     :TERRITORIAL {:name         "TERRITORIAL"
                                   :table        "conflito_territorial"
                                   :insert-query "INSERT INTO conflito_territorial (codigo_conflito, regiao) VALUES (?, ?)"}})


(defn get-conflict-field [field name]
  (let [conflict-name (-> name
                          (str/upper-case)
                          (keyword))]
    (get-in conflict-types [conflict-name field])))

(def get-conflict-insert-query (partial get-conflict-field :insert-query))

(def get-conflict-name (partial get-conflict-field :name))