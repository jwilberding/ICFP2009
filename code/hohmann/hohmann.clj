(clojure.core/ns hohmann
		 (:use clojure.contrib.math)
		 (:use utils.physics))

(def mu (* *G* *earth-mass*))

(defn- get-vs-magnitude
  "Get magnitude of delta-v and delta-v'"
  [r1 r2]
  (list (* (sqrt (/ mu r1)) (- (sqrt (/ (* 2 r2) (+ r1 r2))) 1))
	(* (sqrt (/ mu r2)) (- 1 (sqrt (/ (* 2 r1) (+ r1 r2)))))))