(ns conflitos-belicos.report.handler
  (:require [conflitos-belicos.report.database :as report]))

(defn raw
  [{:keys [parameters]}]
  (let [sql (get-in parameters [:body :sql])
        result (report/get-raw sql)]
    {:status 200
     :body   result}))

(defn conflicts-histogram [_]
  {:status 200
   :body   (report/get-histogram-data)})

(defn country-with-most-number-of-religious [_]
  {:status 200
   :body   (report/get-country-with-most-number-of-religious-conflicts)})

(defn top-5-conflicts-by-number-of-death [_]
  {:status 200
   :body   (report/get-top-5-conflicts-by-number-of-death)})

(defn top-5-ongs-by-intervention [_]
  {:status 200
   :body   (report/get-top-5-ong-by-number-of-intervention)})

(defn gun-dealear-to-army-group [_]
  {:status 200
   :body   (report/get-gun-dealer-provide-to-army-group)})

(defn get-top-5-army-group [_]
  {:status 200
   :body   (report/get-top-5-army-groups-by-number-of-guns)})