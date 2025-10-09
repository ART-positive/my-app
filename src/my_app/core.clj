(ns my-app.core
  (:gen-class)
  (:require [clojure.string :as str]))

(def thousand-digit-str
  (str/replace
   "73167176531330624919225119674426574742355349194934
     96983520312774506326239578318016984801869478851843
     85861560789112949495459501737958331952853208805511
     12540698747158523863050715693290963295227443043557
     66896648950445244523161731856403098711121722383113
     62229893423380308135336276614282806444486645238749
     30358907296290491560440772390713810515859307960866
     70172427121883998797908792274921901699720888093776
     65727333001053367881220235421809751254540594752243
     52584907711670556013604839586446706324415722155397
     53697817977846174064955149290862569321978468622482
     83972241375657056057490261407972968652414535100474
     82166370484403199890008895243450658541227588666881
     16427171479924442928230863465674813919123162824586
     17866458359124566529476545682848912883142607690042
     24219022671055626321111109370544217506941658960408
     07198403850962455444362981230987879927244284909188
     84580156166097919133875499200524063689912560717606
     05886116467109405077541002256983155200055935729725
     71636269561882670428252483600823257530420752963450"
   #"\s+" ""))

(defn digits-from-string [s] 
  (->> s
       seq ; \a \1 \b \2 ...
       (filter #(Character/isDigit ^Character %)) 
       (map #(Character/digit ^Character % 10))))

(defn max-product-recursive-standalone
  [s n]
  (let [digits-vec (vec (digits-from-string s))
        len (count digits-vec)
        rec (fn rec [i]
              (if (> (+ i n) len)
                0
                (let [
                      p (loop [j i acc 1]
                          (if (>= j (+ i n))
                            acc
                            (recur (inc j) (* acc (nth digits-vec j)))))]
                  (max p (rec (inc i))))))]
    (rec 0)))


(defn max-product-tailrec-standalone
  [s n]
  (let [digits-vec (vec (digits-from-string s))
        len (count digits-vec)]
    (loop [i 0 best 0]
      (if (> (+ i n) len)
        best
        (let [p (loop [j i acc 1]
                  (if (>= j (+ i n))
                    acc
                    (recur (inc j) (* acc (nth digits-vec j)))))]
          (recur (inc i) (if (> p best) p best)))))))

(defn max-product-mapindexed-standalone
  [s n]
  (let [digits-vec (vec (digits-from-string s))
        len (count digits-vec)
        last-start (max 0 (inc (- len n)))]
    (->> (range 0 last-start)
         (map (fn [i]
                (let [window (subvec digits-vec i (+ i n))]
                  (reduce * 1 window))))
         (reduce max 0))))

(defn lazy
  [s n]
  (let [digits (digits-from-string s)]
    (->> (partition n 1 digits)
         (filter (fn [w] (not-any? zero? w)))
         (map (fn [w] (reduce * 1 w)))
         (reduce max 0))))

(defn -main
  [& _args]
  (let [n 13 s thousand-digit-str]
    (println "recursive->" (max-product-recursive-standalone s n))
    (println "tailrec  ->" (max-product-tailrec-standalone s n))
    (println "mapidx   ->" (max-product-mapindexed-standalone s n))
    (println "lazy     ->" (lazy s n))))
