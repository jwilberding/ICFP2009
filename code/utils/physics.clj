(clojure.core/ns utils.physics
		 (:use clojure.contrib.math))

(defstruct xy-pair :x :y)
(defn make-pair [x y]
  (struct xy-pair x y))

(defn pair-op
  "Generalized ops for structs. If the second argument
isn't a struct, constantly apply it to the other values"
  [op s1 s2]
  (if (:x s2)
    (make-pair (op (:x s1) (:x s2))
	       (op (:y s1) (:y s2)))
    (make-pair (op (:x s1) s2)
	       (op (:y s1) s2))))

(defn add [& more] (reduce (fn [x y] (pair-op + x y)) {:x 0 :y 0} more))
(defn sub [& more] (reduce (fn [x y] (pair-op - x y)) {:x 0 :y 0} more))
(defn mul [& more] (reduce (fn [x y] (pair-op * x y)) {:x 1 :y 1} more))
(defn div [& more] (reduce (fn [x y] (pair-op / x y)) (first more) (rest more)))

(def *G* (* 6.67428 (expt 10 -11)))
(def *earth-radius* (* 6.357 (expt 10 6)))
(def *earth-mass* (* 6.0 (expt 10 24)))
(def *delta-t* 1)

(defn grav-force
  "Gravitational force"
  [m1 m2 r]
  (/ (* *G* m1 m2) (expt r 2)))

(defn distance
  "Distance of two points -- s1 and s2"
  [s1 s2]
  (sqrt (+ (expt (- (:x s1) (:x s2)) 2)
	   (expt (- (:y s1) (:y s2)) 2))))

(defn st+1
  "Calculates new position"
  [s-t v-t a-t]
  (add s-t (mul v-t *delta-t*) (mul (div a-t 2) (expt *delta-t* 2))))

;(defn vt+1
;  "Calculates new velocity"
;  [v-t a-t]