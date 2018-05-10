(ns my-exercise.turbovote
  (:require [clj-http.client :as http]
            [clojure.spec.alpha :as s]
            [my-exercise.ocd :as ocd]
            [taoensso.timbre :as log]
            [clojure.string :as str]))

(def ^:const +api-host+ "https://api.turbovote.org")

(def call (comp :body http/request))

;;----------------------------------------------------------------------
(s/fdef get-upcoming-elections
        :args (s/cat :ocd-ids (s/coll-of string?))
        :ret seq?)

(defn get-upcoming-elections
  "Uses turbovote api and returns data for ocd-ids specified."
  [ocd-ids]
  (log/info {:action ::get-upcoming-elections
             :ocd-ids ocd-ids})
  (call {:method :get
         :url (str +api-host+ "/elections/upcoming")
         :query-params {:district-divisions (str/join "," ocd-ids)}
         :accept :edn :as :clojure}))
