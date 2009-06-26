(clojure.core/ns utils.physics
		 (:use clojure.contrib.math))

(defstruct s :x :y)
(defstruct v :x-v :y-v)
(defstruct a :x-a :y-a)

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
  [s-t v-t a-t t]
  (struct s
	  (+ (:x s-t) (:x v-t)
	     (* (/ (:x a-t) 2) (expt t 2)))
	  (+ (:y s-t) (:y v-t)
	     (* (/ (:y a-t) 2) (expt t 2)))))