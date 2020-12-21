(defproject conflitos-belicos "0.1.0-SNAPSHOT"
  :description "Trabalho para Disciplina de Banco de Dados"
  :url "https://github.com/geovaniggr"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.1"]
                 [ring-cors "0.1.13"]
                 [metosin/reitit "0.5.5"]
                 [seancorfield/next.jdbc "1.1.613"]
                 [org.postgresql/postgresql "42.2.14"]]
  :main conflitos-belicos.core
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}
             :dev {:source-paths ["dev/src"]
                   :resource-paths ["resources"]}}
  :uberjar-name "conflitos-belicos.jar")
