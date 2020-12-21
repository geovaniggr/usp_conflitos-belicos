(ns conflitos-belicos.grupos-armados.handler
  (:require [conflitos-belicos.grupos-armados.database :as army-group-repository]))

(defn get-all-army-group [_]
  {:status 200
   :body   (army-group-repository/get-all-army-groups)})

(defn get-army-group-code [_]
  {:status 200
   :body   (army-group-repository/get-army-group-code)})

(defn get-division [_]
  {:status 200
   :body   (army-group-repository/get-all-division)})

(defn create
  [{:keys [parameters]}]
  (let [nome (get-in parameters [:body :nome])]
    (army-group-repository/create-army-group nome)
    {:status 201
     :body   "Created"}))

(defn create-division
  [{:keys [parameters]}]
  (println parameters)
  (let [[codigo_ga num_baixas num_homens num_avioes num_tanque num_barcos] (map read-string (-> parameters
                                                                                                :body
                                                                                                (select-keys [:codigo_ga :num_baixas :num_homens :num_avioes :num_tanque :num_barcos])
                                                                                                vals))]
    (army-group-repository/create-division codigo_ga num_baixas num_homens num_avioes num_tanque num_barcos)
    {:status 201
     :body   "Created"}))

(defn get-division-of-army-group
  [{:keys [parameters]}]
  (println parameters)
  (let [cod-ga (get-in parameters [:path :codigo])]
    {:status 200
     :body   (army-group-repository/get-division-from-army-group cod-ga)}))
