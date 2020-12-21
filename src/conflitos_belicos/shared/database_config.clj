(ns conflitos-belicos.shared.database-config
  (:require [next.jdbc :as jdbc]))

(def config-file "config.edn")

(def jdbc-config
  (-> config-file
      clojure.java.io/resource
      slurp
      read-string))

(defn get-datasource []
  (jdbc/get-datasource jdbc-config))
