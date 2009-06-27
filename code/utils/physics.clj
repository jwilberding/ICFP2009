(clojure.core/ns utils.physics
		 (:import (java.lang.Math))
		 (:use clojure.contrib.math))

;;;; TODO:
;;;; - 

(defstruct xy-pair :x :y)
(defn make-pair [x y]
  (struct xy-pair x y))

(defn pair-op
  "Generalized ops for structs. If the second argument
isn't a struct, constantly apply it to the other values."
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
(def *origin* (make-pair 0 0))

(defn distance
  "Distance of two points -- s1 and s2."
  [s1 s2]
  (sqrt (+ (expt (- (:x s1) (:x s2)) 2)
	   (expt (- (:y s1) (:y s2)) 2))))

(defn grav-force
  "Gravitational force"
  [m1 m2 r]
  (/ (* *G* m1 m2) (expt r 2)))

(defn- get-theta
  "Get theta given (x,y) coordinate. Assumes opposite
end is the origin. Returns the value in radians."
  [s]
  (let [hypotenuse (distance *origin* s)
	adjacent (distance *origin* (make-pair (:x s) 0))]
    (Math/acos (/ adjacent hypotenuse))))

(defn- abs-and-negate
  "Make a positive or negative number negative."
  [n]
  (* -1 (abs n)))

(defn- orient-acceleration
  "Takes an acceleration pair, a, and its location, s, and returns
and updated a with the correct sign."
  [a s]
  (cond (and (>= (:x s) 0) (>= (:y s) 0))
	(make-pair (* -1 (:x a)) (* -1 (:y a)))
	(and (< (:x s) 0) (>= (:y s) 0))
	(make-pair (:x a) (* -1 (:y a)))
	(and (>= (:x s) 0) (< (:y s) 0))
	(make-pair (* -1 (:x a)) (:y a))
	(and (< (:x s) 0) (< (:y s) 0))
	a))

(defn get-grav-acc
  "Get the (directional) gravitational acceleration.
We assume that the 'other' target is centered at (0,0). s is the position."
  [m1 m2 r s]
  (let [magnitude (grav-force m1 m2 r)
	theta (get-theta s)
	adjacent (distance *origin* (make-pair (:x s) 0))]
    (orient-acceleration (make-pair (* magnitude (Math/cos theta))
				    (* magnitude (Math/sin theta)))
			 s)))

(defn st+1
  "Calculates new position."
  [s-t v-t a-t]
  (add s-t (mul v-t *delta-t*) (mul (div a-t 2) (expt *delta-t* 2))))

(defn vt+1
  "Calculates new velocity."
  [v-t a-t]
  (add v-t (mul a-t *delta-t*)))