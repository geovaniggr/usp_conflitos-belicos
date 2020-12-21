(ns conflitos-belicos.conflitos.handler
  (:require [conflitos-belicos.conflitos.database :as conflitos-repository]))

(defn get-all-conflicts [_]
  {:status 200
   :body   (conflitos-repository/list-all-conflicts)})

(defn create
  "Recebe uma requisição, faz destructuring retirando o body,
  e faz outro destructuring com o resultado do body"
  [{:keys [parameters]}]
  (let [{:keys [nome tipo_conflito paises_participantes causas num_mortos num_feridos]} (-> parameters
                                                                                            :body)]
    (conflitos-repository/create-full-conflict nome tipo_conflito num_mortos num_feridos paises_participantes causas))
  {:status 201
   :body   "Created"})

