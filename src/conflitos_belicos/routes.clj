(ns conflitos-belicos.routes
  (:require [conflitos-belicos.report.handler :as report-handler]
            [conflitos-belicos.conflitos.handler :as conflitos-handler]
            [conflitos-belicos.grupos-armados.handler :as army-group-handler]
            [conflitos-belicos.chefe-militar.handler :as cm-handler]
            [conflitos-belicos.lider-politico.handler :as lp-handler]
            [schema.core :as s]))

(def report-routes
  [["/report"
    ["/histogram" {:get {:handler report-handler/conflicts-histogram}}]
    ["/top/religious-country" {:get {:handler report-handler/country-with-most-number-of-religious}}]
    ["/top/number-of-death" {:get {:handler report-handler/top-5-conflicts-by-number-of-death}}]
    ["/top/ongs" {:get {:handler report-handler/top-5-ongs-by-intervention}}]
    ["/top/army-group" {:get {:handler report-handler/get-top-5-army-group}}]
    ["/top/dealers" {:get {:handler report-handler/gun-dealear-to-army-group}}]]])

(def conflicts-routes
  [["/conflicts" {:get  {:handler conflitos-handler/get-all-conflicts}
                  :post {:handler    conflitos-handler/create
                         :parameters {:body {:nome                 string?
                                             :num_mortos           string?
                                             :num_feridos          string?
                                             :tipo_conflito        string?
                                             :paises_participantes any?
                                             :causas               any?}}}}]])

(def army-group-routes
  [["/raw" {:post {:handler    report-handler/raw
                   :parameters {:body {:sql string?}}}}]
   ["/army-group" {:get  {:handler army-group-handler/get-all-army-group}
                   :post {:handler    army-group-handler/create
                          :parameters {:body {:nome string?}}}}]
   ["/army-group/code" {:get {:handler army-group-handler/get-army-group-code}}]
   ["/army-group/division" {:get  {:handler army-group-handler/get-division}
                            :post {:handler    army-group-handler/create-division
                                   :parameters {:body {:codigo_ga  any?
                                                       :num_baixas any?
                                                       :num_homens any?
                                                       :num_avioes any?
                                                       :num_tanque any?
                                                       :num_barcos any?}}}}]
   ["/army-group/division/:codigo" {:parameters {:path {:codigo int?}}
                                    :handler    army-group-handler/get-division-of-army-group}]])

(def militar-chief-routes
  [["/militar" {:get  {:handler cm-handler/get-all-militar-chief}
                :post {:handler    cm-handler/create
                       :parameters {:body {:faixa          string?
                                           :codigo_ga      any?
                                           :codigo_divisao any?
                                           :nome_lp        string?}}}}]])

(def political-lider-routes
  [["/political" {:get  {:handler lp-handler/get-all-lp}
                  :post {:handler    lp-handler/create
                         :parameters {:body {:codigo_ga any?
                                             :nome      string?
                                             :apoio     string?}}}}]
   ["/political/names" {:get {:handler lp-handler/get-all-lp-names}}]])