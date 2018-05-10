(ns my-exercise.ocd
  (:require [clojure.string :as str]
            [clojure.spec.alpha :as s]
            [clojure.set :as set]))


(s/def ::country string?)
(s/def ::state string?)
(s/def ::county string?)
(s/def ::place string?)


(s/def ::ocd-params (s/keys :req-un [::country]
                            :opt-un [::state ::county ::place]))

;;----------------------------------------------------------------------
(s/fdef normalize-component
        :args (s/cat :s string?)
        :ret string?)

(defn normalize-component
  [s]
  (-> s
      str/lower-case
      (str/replace #" " "_")))

;;----------------------------------------------------------------------
(s/fdef ensure-key
        :args (s/cat :k keyword? :ocd-params map?)
        :ret nil?)

(defn- ensure-key
  "Ensure key k is present in the ocd-params map
   otherwise throw an error."
  [k ocd-params]
  (when-not (contains? ocd-params k)
    (throw (ex-info "Key not present." {:key k
                                        :ocd-params ocd-params}))))

;;----------------------------------------------------------------------
(s/fdef country-id
        :args (s/cat :params map?)
        :ret nil?)

(defn country-id
  [{:keys [country] :as params}]
  (ensure-key :country params)
  (str "ocd-division/country:" (str/lower-case country)))

(defn state-id
  [{:keys [state] :as params}]
  (ensure-key :state params)
  (str (country-id params) "/state:" (str/lower-case state)))

(defn county-id
  [{:keys [county] :as params}]
  (ensure-key :county params)
  (str (state-id params) "/county:" (normalize-component county)))

(defn place-id
  [{:keys [place] :as params}]
  (ensure-key :place params)
  (str (state-id params) "/place:" (normalize-component place)))


(def ^:private component->id-fn
  {:country country-id
   :state   state-id
   :county  county-id
   :place   place-id})

;;----------------------------------------------------------------------
(s/fdef identifiers
        :args (s/cat :params ::ocd-params)
        :ret (s/coll-of string?))

(defn identifiers
  [params]
  (let [params (->> params
                    (remove (comp str/blank? second))
                    (into {}))
        make-id #((component->id-fn %) params)
        ks (set/intersection
            (set (keys params))
            (set (keys component->id-fn)))]
    (set (map make-id ks))))
