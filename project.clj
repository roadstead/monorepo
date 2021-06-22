(defproject monorepo "0.1.0-10-SNAPSHOT"
  :plugins [[lein-mono "0.5.20"]]
  :aliases {
            "clean-all" ["mono" "do-all" "clean"]
            "clean-all-fast" ["mono" "do-all" ":fast" "clean"]
            "ci-all" ["mono" "do-all" "do"  "ci,"  "install"]
            "ci-all-fast" ["mono" "do-all" ":fast" "do" "ci," "install"]
            "test-all" ["mono" "do-all" "do" "test," "install"]
            "test-all-fast" ["mono" "do-all" ":fast" "do" "test," "install"]
            "check-all" ["mono" "do-all" "do" "check," "install"]
            "check-all-fast" ["mono" "do-all" ":fast" "do" "check," "install"]
            "ci" ["do"]
            }
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :repositories [["ice-skm" {:url      "https://eog.jfrog.io/eog/skm/"
                             :username :env/EOG_MVN_USERNAME
                             :password :env/EOG_MVN_PASSWORD
                             :sign-releases false
                             :checksum :fail}]]
  :profiles {:inherited {:repositories [["releases"  { :url "https://eog.jfrog.io/eog/skm"
                                                      :username :env/EOG_MVN_USERNAME
                                                      :password :env/EOG_MVN_PASSWORD
                                                      :sign-releases false}]]
                         :plugin [[lein-modules "0.3.11"]]}})
