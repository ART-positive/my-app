(ns my-app.core-test
  (:require [clojure.test :refer :all]
            [my-app.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest digits-basic-test
  (testing "digits-from-string returns correct digits for small samples"
    (is (= (vec (digits-from-string "12345")) [1 2 3 4 5]))
    (is (= (vec (digits-from-string "a1b2c3")) [1 2 3]))))

(deftest sample-4-test
  (testing "Пример из условия: максимальное произведение 4 подряд цифр = 5832"
    ;; используем thousand-digit-str и проверяем, что для окна 4 получаем 5832
    (is (= 5832 (max-product-recursive-standalone thousand-digit-str 4)))
    (is (= 5832 (max-product-tailrec-standalone thousand-digit-str 4)))
    (is (= 5832 (max-product-mapindexed-standalone thousand-digit-str 4)))
    (is (= 5832 (lazy thousand-digit-str 4)))))

(deftest full-13-test
  (testing "Полный 1000-значный пример: ожидаем 23514624000"
    (let [expected 23514624000]
      (is (= expected (max-product-recursive-standalone thousand-digit-str 13)))
      (is (= expected (max-product-tailrec-standalone thousand-digit-str 13)))
      (is (= expected (max-product-mapindexed-standalone thousand-digit-str 13)))
      (is (= expected (lazy thousand-digit-str 13))))))

(deftest short-input-test
  (testing "Если длина входа меньше окна, возвращаем 0"
    (is (= 0 (max-product-recursive-standalone "123" 4)))
    (is (= 0 (max-product-tailrec-standalone "123" 4)))
    (is (= 0 (max-product-mapindexed-standalone "123" 4)))
    (is (= 0 (lazy "123" 4)))))
