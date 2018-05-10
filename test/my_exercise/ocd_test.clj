(ns my-exercise.ocd-test
  (:require [my-exercise.ocd :as sut]
            [expectations.clojure.test :refer [defexpect expect expecting]]))


(defexpect normalize-component-test
  (expect ""         (sut/normalize-component ""))
  (expect "essex"    (sut/normalize-component "essex"))
  (expect "new_york" (sut/normalize-component "New York")))


(defexpect country-id-test
  (expect "ocd-division/country:us"
          (sut/country-id {:country "US"}))
  (expect "ocd-division/country:us/state:nj"
          (sut/state-id {:country "US"
                         :state "NJ"}))
  (expect "ocd-division/country:us/state:nj/county:essex"
          (sut/county-id {:country "US"
                          :state "NJ"
                          :county "Essex"}))
  (expect "ocd-division/country:us/state:nj/place:newark"
          (sut/place-id {:country "US"
                         :state "NJ"
                         :county "Essex"
                         :place "Newark"}))

  (expect Exception (sut/country-id {}))
  (expect Exception (sut/state-id {}))
  (expect Exception (sut/county-id {}))
  (expect Exception (sut/place-id {})))


(defexpect identifiers-test
  (expect #{"ocd-division/country:us"}
          (sut/identifiers {:country "US"}))

  (expect #{"ocd-division/country:us"
            "ocd-division/country:us/state:nj"}
          (sut/identifiers {:country "US"
                            :state "NJ"}))

  (expect #{"ocd-division/country:us"
            "ocd-division/country:us/state:nj"
            "ocd-division/country:us/state:nj/county:essex"}
          (sut/identifiers {:country "US"
                            :state "NJ"
                            :county "Essex"}))

  (expect #{"ocd-division/country:us"
            "ocd-division/country:us/state:nj"
            "ocd-division/country:us/state:nj/county:essex"
            "ocd-division/country:us/state:nj/place:newark"}
          (sut/identifiers {:country "US"
                            :state "NJ"
                            :county "Essex"
                            :place "Newark"}))

  (expecting "Deal with nil/blank strings"
    (expect #{"ocd-division/country:us"
              "ocd-division/country:us/state:nj"}
            (sut/identifiers {:country "US"
                              :state "NJ"
                              :place ""}))))
