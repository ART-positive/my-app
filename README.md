# Лабораторная работа 1


## Проект Эйлера №8, №23

  * Студент: `Березовский Артемий Сергеевич`
  * Группа: `P3330`
  * ИСУ: `367097`
  * Функциональный язык: `Clojure`

## Проблема №8

  * **Название**: `Largest Product in a Series`
  * **Описание**: The four adjacent digits in the $1000$-digit number that have the greatest product are $9 \times 9 \times 8 \times 9 = 5832$.

>$731671765313306249192251196744265747423553491949349698352031277450632623957831801698480186947885184385861560789112949495459501737958331952853208805511125406987471585238630507156932909632952274430435576689664895044524452316173185640309871112172238311362229893423380308135336276614282806444486645238749303589072962904915604407723907138105158593079608667017242712188399879790879227492190169972088809377665727333001053367881220235421809751254540594752243525849077116705560136048395864467063244157221553975369781797784617406495514929086256932197846862248283972241375657056057490261407972968652414535100474216637048440319989000889524345065854122758866688116427171479924442928230863465674813919123162824586178664583591245665294765456828489128831426076900422421902267105562632111110937054421750694165896040807198403850962455444362981230987879927244284909188845801561660979191338754992005240636899125607176060588611646710940507754100225698315520005593572972571636269561882670428252483600823257530420752963450$

  * **Задание**: Find the thirteen adjacent digits in the $1000$-digit number that have the greatest product. What is the value of this product?


### Основная идея решения
---
Проходимся окном длинной в $n$, перемножаем числа и вычисляем максимальное среди всех таких окон. Можно оптимизировать, допустим убираем все окна где присутствует $0$. 

### Переводим строку в последовательность

```clojure
(defn digits-from-string [s] 
  (->> s
       seq ; \a \1 \b \2 ...
       (filter #(Character/isDigit ^Character %)) 
       (map #(Character/digit ^Character % 10))))
```

### Решение через рекурсию

```clojure
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
```

### Решение через хвостовую рекурсию 

```clojure
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
```

### Решение с генерацией последовательности при помощи отображения (map)

```clojure
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
```

### Решение через ленивые исполнения (и модульность)

```clojure
(defn lazy [s n]
  (let [digits (digits-from-string s)]
    (->> (partition n 1 digits)
         (filter (fn [w] (not-any? zero? w)))
         (map (fn [w] (reduce * 1 w)))
         (reduce max 0))))
```

### Решение через императивный язык (C++)

```c++
#include <bits/stdc++.h>
using namespace std;

void solve() {
    string s = "731671765313306249192251196744265747423553491949349698352031277450632623957831801698480186947885184385861560789112949495459501737958331952853208805511125406987471585238630507156932909632952274430435576689664895044524452316173185640309871112172238311362229893423380308135336276614282806444486645238749303589072962904915604407723907138105158593079608667017242712188399879790879227492190169972088809377665727333001053367881220235421809751254540594752243525849077116705560136048395864467063244157221553975369781797784617406495514929086256932197846862248283972241375657056057490261407972968652414535100474216637048440319989000889524345065854122758866688116427171479924442928230863465674813919123162824586178664583591245665294765456828489128831426076900422421902267105562632111110937054421750694165896040807198403850962455444362981230987879927244284909188845801561660979191338754992005240636899125607176060588611646710940507754100225698315520005593572972571636269561882670428252483600823257530420752963450";
    int n = 13;
    long long mx = 0;
    for(int i = 0; i + 13 < s.size(); i++) {
        long long ans = 1;
        for(int j = i; j < i + 13; j++) {
            ans *= (s[j] - '0');
        }
        mx = max(mx, ans);
    }
    cout<< mx;
}

int main(){
    int test_case = 1;
    while (test_case--)
    {
        solve();
    }
}
```

---

## Проблема №28

  * **Название**: `Non-Abundant Sums`
  * **Описание**: A **perfect number** is a number for which the sum of its proper divisors is exactly equal to the number.  
  For example, the sum of the proper divisors of `28` would be `1 + 2 + 4 + 7 + 14 = 28`, which means that `28` is a perfect number.  
    A number `n` is called **deficient** if the sum of its proper divisors is less than `n`, and it is called **abundant** if this sum exceeds `n`.  
    
    As `12` is the smallest abundant number, `1 + 2 + 3 + 4 + 6 = 16`, the smallest number that can be written as the sum of two abundant numbers is `24`.  
  
    By mathematical analysis, it can be shown that all integers greater than `28123` can be written as the sum of two abundant numbers. However, this upper limit cannot be reduced any further by analysis even though it is known that the greatest number that cannot be expressed as the sum of two abundant numbers is less than this limit.  

  * **Задание**: Find the sum of all the positive integers which cannot be written as the sum of two abundant numbers.

---

### Основная идея решения
Находим все избыточные числа до 28123:
   - Для каждого числа от 1 до 28123 вычисляем сумму его собственных делителей $O(n*sqrt(n))$. Можно оптимизировать до $O(n*log(n))$.
   - Если сумма делителей больше числа, добавляем его в список избыточных чисел.

Определяем числа, которые можно представить как сумму двух избыточных чисел:
   - Создаем массив `F` размером 28124, заполненный `False`.
   - Для каждой пары избыточных чисел `(a, b)` вычисляем их сумму `s = a + b`.
   - Если `s <= 28123`, помечаем `F[s] = True`.

Суммируем числа, которые нельзя представить как сумму двух избыточных чисел.

---

### Решение через рекурсию

```clojure
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
```

### Решение через хвостовую рекурсию

```clojure
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
```

### Решение через модульность

```clojure
(defn sum-non-abundant-modular
  "Берём все abundant <= limit, помечаем все суммы a+b<=limit
   в примитивном boolean массиве — затем суммируем непомеченные числа." 
  [limit]
  (let [abunds (->> (range 1 (inc limit)) (filter abundant?) vec)
        can-be (boolean-array (inc limit))]
    (doseq [i (range (count abunds))
            j (range i (count abunds))
            :let [s (+ (abunds i) (abunds j))]
            :when (<= s limit)]
      (aset can-be s true))
    (sum-of-non-summable limit can-be)))
```

### Решение через бесконечные структуры и ленивые исполнения (Stream)

```clojure
(def abundant-seq
  "Ленивая бесконечная последовательность всех abundant" 
  (->> (iterate inc 1) (filter abundant?)))

(defn sum-non-abundant-lazy
  "Берём ленивую последовательность abundant, отсекаем до limit, генерируем суммы и считаем." 
  [limit]
  (let [abunds (->> abundant-seq (take-while #(<= % limit)) vec)
        sums   (->> (for [a abunds, b abunds :let [s (+ a b)] :when (<= s limit)] s) set)]
    (reduce (fn [acc k] (if (contains? sums k) acc (+ acc k))) 0 (range 1 (inc limit)))))
```

### Решение через императивный язык (C++)

```c++
#include <bits/stdc++.h>

using namespace std;

void solve() {
    int n = 28123;
    vector<int> sm(n + 1, 0);
    for(int i = 1; i <= n; i++){
        for(int j = 2; i * j <= n; j++){
            sm[i * j] += i;
        }
    }

    vector<bool> F(n + 1, 0);
    int ans = 0;
    for(int i = 1; i <= n; i++){
        if(sm[i] > i){
            for(int j = 1; j + i <= n; j++){
                if(sm[j] > j) F[j + i] = 1;
            }
        }
        if(!F[i]) ans += i;
    }
    cout<< ans;
}

int main(){
    int test_case = 1;
    while (test_case--)
    {
        solve();
    }
}

```

---

## Выводы

