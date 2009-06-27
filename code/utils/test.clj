(clojure.core/ns utils.test
		 (:use clojure.contrib.pprint)
		 (:use clojure.contrib.duck-streams)
		 (:use utils.physics))

(defn big-loop
  "Loop n times, and spit out the numbers."
  [n s2 v big-str]
  (if (zero? n)
    big-str
    (let [r (distance s2 *origin*)
	  a-t (get-grav-acc 1 *earth-mass* r s2)
	  s3 (st+1 s2 v a-t)]
      (recur (dec n) s3 (vt+1 v a-t) (str big-str (cl-format false "{~d,~d},~%" (:x v) (:y v)))))))

(defn str-loop
  [n s1 s2]
  (big-loop n s2 (get-v s1 s2) "")) 

(defn write-loop
  [n s1 s2 path]
  (spit path (big-loop n s2 (get-v s1 s2) "")))