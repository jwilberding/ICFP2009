(clojure.core/ns hohmann
		 (:use clojure.contrib.math)
		 (:use utils.physics))

(def mu (* *G* *earth-mass*))

(defn- about=
  [n m e]
  (<= (abs (- n m)) e))

(defn- get-vs-magnitude
  "Get magnitude of delta-v and delta-v'"
  [r1 r2]
  (hash-map :dv (* (sqrt (/ mu r1)) (- (sqrt (/ (* 2 r2) (+ r1 r2))) 1))
	    :dv-prime(* (sqrt (/ mu r2)) (- 1 (sqrt (/ (* 2 r1) (+ r1 r2)))))))

(defn- fire?
  "Given the value for delta-v or delta-v', should
we thrust?"
  [dvs r s]
  (about= r (distance s *origin*)))

;(defn hohmann-transfer
;  "Let's do a hohmann transfer yeah!"
;  [s2 v target-r dvs]
;  (let [r (distance s2 *origin*)
;	a-t (get-grav-acc 1 *earth-mass* r s2)
;	s3 (st+1 s2 v a-t)]
;    (cond (fire? dvs r s3)
;	  (