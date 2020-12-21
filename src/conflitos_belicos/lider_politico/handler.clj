(ns conflitos-belicos.lider-politico.handler
  (:require [conflitos-belicos.lider-politico.database :as lp-repository]))

(defn get-all-lp [_]
  {:status 200
   :body   (lp-repository/get-all-political)})

(defn create
  [{:keys [parameters]}]
  (let [{:keys [codigo_ga nome apoio]} (:body parameters)]
    (lp-repository/create-political-lider nome (Integer/parseInt codigo_ga) apoio))
  {:status 201
   :body   "Created"})

(defn get-all-lp-names [_]
  {:status 200
   :body   (lp-repository/get-political-liders-name)})
