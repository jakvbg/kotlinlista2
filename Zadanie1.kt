package com.example.programowanie2

import kotlin.math.pow
/*
Autor: Jakub Główka
Kod przedstawia klasę Wielomian reprezentującą wielomian dowolnego stopnia. Konstruktor klasy
przyjmuje za argument wejściowy listę współczynników wielomianu, w porządku rosnącym tzn.
pierwszy element jest stopnia 0, drugi element stopnia pierwszegp itd.
*/
open class Wielomian(private val wspolczynniki: List<Int>){

    fun stopien(){
        /*
        Funkcja wyświetla stopień wielomianu. W przypadku podania wszystkich elementów jako 0 oraz
        jeśli ostatni element wielomianu jest równy 0, użytkownik otrzymuje informację o podaniu
        innych wartości. Dla tych wyjątków funkcja podaje nieprawidłowe wartości.
         */
        if (wspolczynniki.last() == 0){
            println("Ostatni element tablicy powinien być inny niż 0.")
        }
        if(wspolczynniki.all { it == 0 }){
            println("Podaj elementy różne od zera.")}
        else{
        println("Stopien podanego wielomianu jest równy: ${wspolczynniki.size - 1}.")}
    }

    fun tekst(){
        /*
        Metoda zwraca w konsoli podany wielomian w postaci tekstowej. W przypadku podania wartości 0
        dla odpowienich elementów, zwracany jest odpowiedni komunikat lub dany element jest pomijany
        w algorytmie iteracyjnym.
        */
        if (wspolczynniki.all { it == 0 }){
            println("Podaj wspolczynniki różne od zera")
        }else{
        println("Podany wielomian ma postać: ")
        for (i in wspolczynniki.size - 1 downTo 1){
            if (wspolczynniki[i] != 0) {
                print("${wspolczynniki[i]} x^${i} + ")
            }
        }//ostatni element jest obsługiwany osobno ponieważ w pętli jest znak +
        if (wspolczynniki[0] != 0){
        print("${wspolczynniki[0]}")}
        }
    }
    /*
    Funkcja dla podanej liczby całkowitej zwraca wartość wielomianu.
     */
    fun wynik(x: Int): Int {
        var suma = wspolczynniki[0]
        for (i in wspolczynniki.size - 1 downTo 1){
            suma += wspolczynniki[i] * x.toDouble().pow(i.toDouble()).toInt() //funkcja pow obsługuje
            //typ Double dlatego następuje zamiana zmiennej na typ Int
        }
        return suma
    }
    /*
    Operatory plus oraz minus zawierają ten sam kod tylko jeden operator zawiera funkcje dodającą, a
    drugi funkcję odejmującą (plus i minus). Podczas pisania tych fragmentów kodu wykorzystano dokumentację
    kotlina: https://kotlinlang.org/docs/operator-overloading.html,
    https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/get-or-null.html
    oraz obsługę błędów w programie AndroidStudio. Funkcja getOrNull umożliwia obsługę elementów
    wielomianów równych 0, dlatego też w kodzie pojawiają sięznaki '?' obsługujące nullability
     */
    operator fun plus(wielomian: Wielomian): Wielomian{
        val stopien = maxOf(this.wspolczynniki.size, wielomian.wspolczynniki.size)
        val suma = mutableListOf<Int>()
        for (i in 0 until stopien) {
            wielomian.wspolczynniki.getOrNull(i)
                ?.let { this.wspolczynniki.getOrNull(i)?.plus(it) }?.let {
                    suma.add(it)
                }
        }
        return Wielomian(suma)
    }
    operator fun minus(wielomian: Wielomian): Wielomian{
        val stopien = maxOf(this.wspolczynniki.size, wielomian.wspolczynniki.size)
        val roznica = mutableListOf<Int>()
        for (i in 0 until stopien) {
            this.wspolczynniki.getOrNull(i)?.let { wielomian.wspolczynniki.getOrNull(i)?.minus(it) }
                ?.let {
                    roznica.add(it)
                }
        }
        return Wielomian(roznica)
    }
    operator fun times(wielomian: Wielomian): Wielomian{
        //Utworzenie nowej listy wypełnionej zerami będącej wielomianem o wymiarach iloczynu wielomianów.
        val iloczyn = MutableList(this.wspolczynniki.size + wielomian.wspolczynniki.size - 1) {0}
        for (i in this.wspolczynniki.indices){ //Indices zwraca wielkość listy (wielomianu) dzięki czemu
            // możliwe jest mnożenie wielomianów o różnych wymiarach.
            for(k in wielomian.wspolczynniki.indices){
                iloczyn[i + k] += this.wspolczynniki[i] * wielomian.wspolczynniki[k]
            }
        }
        return Wielomian(iloczyn)
    }
}

fun main(){
    val test1 = Wielomian(listOf(1,2,3,4,5))
    test1.stopien()
    test1.tekst()
    println()
    println("${test1.wynik(6)}")
    val w1 = Wielomian(listOf(1,2,3,4))
    val w2 = Wielomian(listOf(5,6,7,0))
    val suma = w1 + w2
    val roznica = w1 - w2
    val iloczyn = w1 * w2
    suma.tekst()
    println()
    roznica.tekst()
    println()
    iloczyn.tekst()
}