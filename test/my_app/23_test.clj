(ns my-app.23-test
  (:require [clojure.test :refer :all]
            [my-app.23 :refer :all]))

(deftest divisors-and-abundant-tests
  (testing "sum-proper-divs basic cases"
    (is (= 0  (sum-proper-divs 1)))
    (is (= 16 (sum-proper-divs 12)))
    (is (= 28 (sum-proper-divs 28))))

  (testing "abundant? on known numbers"
    (is (true?  (abundant? 12)))
    (is (false? (abundant? 28)))   ;; perfect
    (is (false? (abundant? 1)))))

(deftest small-limit-24-test
  (testing "Для limit = 24, ans = 276"
    (is (= 276 (sum-non-abundant-modular 24)))
    (is (= 276 (sum-non-abundant-tailrec 24)))
    (is (= 276 (sum-non-abundant-recursive 24)))
    (is (= 276 (sum-non-abundant-lazy 24)))))

(deftest implementations-agree-on-small-and-default
  (testing "Все реализации должны давать одинаковый результат для нескольких лимитов"
    (let [vals-small (map #(% 1000) [sum-non-abundant-modular
                                     sum-non-abundant-tailrec
                                     sum-non-abundant-recursive
                                     sum-non-abundant-lazy])]
      (is (apply = vals-small)))
    (let [vals-default (map #(% 28123) [sum-non-abundant-modular
                                        sum-non-abundant-tailrec
                                        sum-non-abundant-recursive
                                        sum-non-abundant-lazy])]
      (is (apply = vals-default)))))

(deftest euler-23-expected-answer
  (testing "Project Euler #23 — ожидаемый ответ"
    (is (= 4179871 (sum-non-abundant-modular 28123)))
    (is (= 4179871 (sum-non-abundant-tailrec 28123)))
    (is (= 4179871 (sum-non-abundant-recursive 28123)))
    (is (= 4179871 (sum-non-abundant-lazy 28123)))))
