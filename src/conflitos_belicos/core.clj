(ns conflitos-belicos.core
  (:require [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.coercion.spec :as coercion-spec]
            [reitit.ring.coercion :as coercion]
            [ring.middleware.cors :refer [wrap-cors]]
            [conflitos-belicos.routes :refer [report-routes conflicts-routes army-group-routes militar-chief-routes political-lider-routes]]
            [ring.adapter.jetty :as jetty]
            [conflitos-belicos.shared.database-config :as config])
  (:gen-class))


(def router-configurations
  {:data {:coercion   coercion-spec/coercion
          :muuntaja   m/instance
          :middleware [[wrap-cors
                        :access-control-allow-origin [#".*"]
                        :access-control-allow-methods [:get :post :put :delete]]
                       muuntaja/format-middleware
                       coercion/coerce-response-middleware
                       coercion/coerce-request-middleware]}})

(def app
  (ring/ring-handler
    (ring/router
      [["/api"
        ["/ping" {:name ::ping :get (fn [_] {:status 200 :body "ok"})}]
        report-routes
        conflicts-routes
        army-group-routes
        militar-chief-routes
        political-lider-routes]]
      router-configurations)
    (ring/routes
      (ring/redirect-trailing-slash-handler)
      (ring/create-default-handler
        {:not-found (constantly {:status 404 :body {:error "Nenhuma rota encontrada"}})}))))

(defn log [x]
  (println x)
  x)

(defn -main [& args]
  (println "Servidor Rodando na Porta [3000]")
  (jetty/run-jetty app {:port 3000 :join? false}))

;;(app {:request-method :post
;;      :uri "/api/conflicts"
;;      :body {:nome "Geovani" :paises_participantes ["Italia", "Brasil"] :tipo_conflito "Religioso" :causas ["Varias Fitas"]}})
;;(app {:request-method :get
;;      :uri "/api/report/histogram"}))
;;
;;(def study-destructuring {:body {:name "Teste", :top "Top top", :legal "Aaaa"}})
;;
;;(let [ {{:keys [ name top legal] :as all} :body} study-destructuring]
;;  (println name)
;;  (println top)
;;  (println legal)
;;  (println all))
;;
;;(let [ {:keys [name top legal]} (-> study-destructuring
;;                                    :body)]
;;  (println name top legal))

;;(def exemplo-two {:body {:nome "Legal" :idade 25 :gosta-chocolate true}})
;;
;;(-> exemplo-two
;;    :body
;;    (select-keys [:nome :idade :gosta-chocolate])
;;    vals)
;;(println (:body {}))