(ns my-app.23)

(defn sum-proper-divs
  "сумма собственных делителей n"
  [n]
  (cond
    (<= n 1) 0
    :else (let [sqrt-n (long (Math/floor (Math/sqrt n)))]
            (loop [i 2, acc 1]
              (if (> i sqrt-n)
                acc
                (if (zero? (mod n i))
                  (let [j (quot n i)]
                    (recur (inc i) (+ acc (if (= i j) i (+ i j)))))
                  (recur (inc i) acc)))))))

(defn abundant?
  "true если n избыточное"
  [n]
  (> (sum-proper-divs n) n))

(defn sum-of-non-summable
  "на вход limit и boolean-массив can-be, где true означает: можно представить как сумму двух abundant.
   Возвращает сумму всех k от 1 до limit, для которых can-be[k] == false"
  [limit can-be]
  (loop [k 1, acc 0]
    (if (> k limit)
      acc
      (recur (inc k) (if (aget can-be k) acc (+ acc k))))))

(defn sum-non-abundant-modular
  "берём все abundant <= limit, помечаем все суммы a+b<=limit
   в примитивном boolean массиве — затем суммируем непомеченные числа" 
  [limit]
  (let [abunds (->> (range 1 (inc limit)) (filter abundant?) vec)
        can-be (boolean-array (inc limit))]
    (doseq [i (range (count abunds))
            j (range i (count abunds))
            :let [s (+ (abunds i) (abunds j))]
            :when (<= s limit)]
      (aset can-be s true))
    (sum-of-non-summable limit can-be)))

(defn sum-non-abundant-tailrec
  "хвостовая: проходим i от 1 до limit, поддерживаем вектор abundant и
   boolean массив can-be, который обновляем при обнаружении каждого нового abundant" 
  [limit]
  (let [can-be (boolean-array (inc limit))]
    (loop [i 1, abunds []]
      (if (> i limit)
        (sum-of-non-summable limit can-be)
        (let [s (sum-proper-divs i)]
          (if (<= s i)
            (recur (inc i) abunds)
            (do
              (doseq [a abunds
                      :let [sum (+ a i)]
                      :when (<= sum limit)]
                (aset can-be sum true))
              (when (<= (+ i i) limit) (aset can-be (+ i i) true))
              (recur (inc i) (conj abunds i)))))))))

(defn sum-non-abundant-recursive
  "Рекурсивно, с trampoline" 
  [limit]
  (let [can-be (boolean-array (inc limit)) 
         step (fn step [i abunds]
                (fn []
                  (if (> i limit)
                    (sum-of-non-summable limit can-be)
                    (let [s (sum-proper-divs i)]
                      (if (<= s i)
                        (step (inc i) abunds)
                        (do
                          (doseq [a abunds
                                  :let [sum (+ a i)]
                                  :when (<= sum limit)]
                            (aset can-be sum true))
                          (when (<= (+ i i) limit) (aset can-be (+ i i) true))
                          (step (inc i) (conj abunds i))))))))]
     (trampoline (step 1 []))))

(def abundant-seq
  "Ленивая бесконечная последовательность всех abundant" 
  (->> (iterate inc 1) (filter abundant?)))

(defn sum-non-abundant-lazy
  "Берём ленивую последовательность abundant, отсекаем до limit, генерируем суммы и считаем." 
  [limit]
  (let [abunds (->> abundant-seq (take-while #(<= % limit)) vec)
        sums   (->> (for [a abunds, b abunds :let [s (+ a b)] :when (<= s limit)] s) set)]
    (reduce (fn [acc k] (if (contains? sums k) acc (+ acc k))) 0 (range 1 (inc limit)))))

(defn ^:export -main
  []
  (let [limit 28123
        impls [["modular" sum-non-abundant-modular]
               ["tailrec" sum-non-abundant-tailrec]
               ["recursive" sum-non-abundant-recursive]
               ["lazy" sum-non-abundant-lazy]]]
    (doseq [[name f] impls]
      (println (format "%-8s -> %d" name (f limit))))))
