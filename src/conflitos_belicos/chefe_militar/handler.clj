(ns conflitos-belicos.chefe-militar.handler
  (:require [conflitos-belicos.chefe-militar.database :as cm-repository]))

(defn get-all-militar-chief [_]
  {:status 200
   :body   (cm-repository/get-all-militar-lider)})

(defn create
  [{:keys [parameters]}]
  (let [{:keys [faixa codigo_ga codigo_divisao nome_lp]} (:body parameters)]
    (cm-repository/create-militar-lider faixa (Integer/parseInt codigo_ga) (Integer/parseInt codigo_divisao) nome_lp)
    {:status 201
     :body   "Created"}))