package com.example.programowanie2

import android.text.TextUtils.substring
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
/*
Autor: Jakub Główka
Program tworzy 3 klasy dziedziczone od klasy Sequence. Kazda klasa składa się z właściwości identifier,
data, VALIDCHARS i length. Klasy zawierają odpowiednie metody zawarte w treści zadania.
 */
/*
KLasa enum Type zawierająca możliwości występowania parametru identifier
 */
enum class Type{
    RNA,
    DNA,
    PROTEIN
}
/*
Tak jak w przypadku własciwosci identifier, planowano utworzyć klasę zawierającą możliwe wartości
znaków, które może zawierać klasa Sequence, jednakże klasa enum nie obsługuje list. Program zatem
nie obsługuje weryfikacji poprawności sekwencji.
 */
enum class VALIDCHARS{
    A, G, C, T, U,
    Ala, Arg, Asn, Asp, Cys, Glu, Gln, Gly, His, Ile, Leu, Lys, Met, Phe, Pro, Ser, Thr, Trp, Tyr, Val
}
abstract class Sequence(
    open val identifier: Type,
    open val data: String,
    open val valid_chars: VALIDCHARS,
    open val length: Int
) {
    /*
    Metody dziedziczone przez pozostałe klasy. Metoda toString została wygenerowana automatycznie
    przez środowisko AndroidStudio.
     */
    override fun toString(): String {
        return "Sequence(identifier=$identifier, data='$data', valid_chars=$valid_chars, length=$length)"
    }

    open fun mutate(position: Int, value: Char) {
        return
    }

    open fun findmotif(motif: String): Int {
        return 0
    }

    open fun complement() {
        return
    }

    open fun transcribe() {
        return
    }

    class DNASequence(
        override var identifier: Type,
        override var data: String,
        override val valid_chars: VALIDCHARS,
        override var length: Int
    ) : Sequence(Type.DNA, data, valid_chars, length) {
        /*
        Metoda zamienia daną zasadę na inną, podaną przez użytkownika. W przypadku podania indeksu,
        wykraczającego poza zakres zwracany jest odpowiedni komunikat. Metoda zamienia ciąg liter na
        daną typu Array zawierającą pojedyncze litery, dzięki czemu możliwe jest odwołanie się do odpowiednich
        indeksów.
         */
        override fun mutate(position: Int, value: Char) {
            if (position < 0 || position >= length) {
                throw IndexOutOfBoundsException("Podany indeks nie występuje w ciągu DNA.")
            }
            val mutacja = data.toCharArray()
            mutacja[position] = value
            this.data = mutacja.joinToString()
        }
        //Metoda spełnia założenia polecenia zadania.
        override fun toString(): String {
            return "> $identifier: $data"
        }
        /*
        Metoda znajduje w ciągu liter podany motyw i zwraca jego indeks, jeśli się w nim znajduje.
        W przypadku gdy w sekwencji nie ma danego motywu, zwracany jest komunikat, a funkcja przyjmuje
        wartość -1, ponieważ w założeniach metoda zwraca daną typu Int. Rozwiązanie wykorzystuje zmienną
        ciąg która w przypadku, gdy zostanie znaleziony motyw, przyjmuje wartość false i zatrzymuje
        działanie metody. Ten fragment kodu został napisany na podstawie wykładu nr 2. dr. inż Pawła
        Głąby z kursu Zaawansowane programowanie aplikacji mobilnych.
         */
        override fun findmotif(motif: String): Int {
            val dlmotif = motif.length
            for (i in 0..this.length - dlmotif) {
                var ciag = true
                for (j in 0 until dlmotif) {
                    if (this.data[i + j] != motif[j]) {
                        ciag = false
                        break
                    }
                }
                if (ciag == true) return i
            }
            println("W sekwencji nie ma podanego motywu.")
            return -1

        }
        //Metoda działająca w podobny sposób jak zad 6. z listy pierwszej.
        override fun complement() {
            var complement = ""
            for (elem in this.data) {
                complement += when (elem.toString()) {
                    'A'.toString() -> 'T'
                    'C'.toString() -> 'G'
                    'G'.toString() -> 'C'
                    'T'.toString() -> 'A'
                    else -> ""//W przypadku podania innej wartości metoda nie działa i wyskakuje błąd.
                }
            }
            this.data = complement.reversed()
        }

        //Metoda również pochodzi z zadania 6 z listy 1. Dodatkowo zamienia wartości obiektu klasy
        //Sequence
        override fun transcribe() {
            var transkryb = ""
            for (elem in data) {
                transkryb += when (elem.toString()) {
                    'A'.toString() -> 'U'
                    'C'.toString() -> 'G'
                    'G'.toString() -> 'C'
                    'T'.toString() -> 'A'
                    else -> "" //throw IllegalArgumentException("Wprowadź symbole opisujące zasady DNA.")
                }
            }
            this.data = transkryb
            this.identifier = Type.RNA
        }

    }

    class RNASequence(
        override var identifier: Type,
        override var data: String,
        override val valid_chars: VALIDCHARS,
        override var length: Int
    ) : Sequence(Type.RNA, data, valid_chars, length) {
        override fun mutate(position: Int, value: Char) {
            if (position < 0 || position >= length) {
                throw IndexOutOfBoundsException("Podany indeks nie występuje w ciągu DNA.")
            }
            val mutacja = data.toCharArray()
            mutacja[position] = value
            this.data = mutacja.joinToString()
        }

        override fun toString(): String {
            return "> $identifier: $data"
        }

        override fun findmotif(motif: String): Int {
            val dlmotif = motif.length
            for (i in 0..this.length - dlmotif) {
                var ciag = true
                for (j in 0 until dlmotif) {
                    if (this.data[i + j] != motif[j]) {
                        ciag = false
                        break
                    }
                }
                if (ciag == true) return i
            }
            println("W sekwencji nie ma podanego motywu.")
            return -1
        }
        /*
        Kod opisujacy białko jest trójkowy, dlatego iteracja następuje ze skokiem 3 (3 zasady kodują
        jedno białko). W przypadku wystąpienia kodonu stop, funkcja się zatrzymuje. Do napisania tej
        metody wykorzystano źródła (metoda substring) : https://hyperskill.org/learn/step/8535
         */
        override fun transcribe() {
            var translacja = ""
            for (i in 0 until this.length step 3) {
                //this.data.toCharArray()
                val kodon = this.data.substring(i, i + 3)
                translacja += when (kodon) {
                    "UUU", "UUC" -> "Phe"
                    "UUA", "UUG", "CUU", "CUC", "CUA", "CUG" -> "Leu"
                    "UCU", "UCC", "UCA", "UCG" -> "Ser"
                    "UAU", "UAC" -> "Tyr"
                    "UAA", "UAG", "UGA" -> "STOP"
                    "UGU", "UGC" -> "Cys"
                    "UGG" -> "Trp"
                    "CCU", "CCC", "CCA", "CCG" -> "Pro"
                    "CAU", "CAC" -> "His"
                    "CAA", "CAG" -> "Gin"
                    "CGU", "CGC", "CGA", "CGG", "AGA", "AGG" -> "Arg"
                    "AUU", "AUC", "AUA" -> "Ile"
                    "AUG" -> "Met"
                    "ACU", "ACC", "ACA", "ACG" -> "Thr"
                    "AAU", "AAC" -> "Asn"
                    "AAA", "AAG" -> "Lys"
                    "AGU", "AGC" -> "Ser"
                    "GUU", "GUC", "GUA", "GUG" -> "Val"
                    "GCU", "GCC", "GCA", "GCG" -> "Ala"
                    "GAU", "GAC" -> "Asp"
                    "GAA", "GAG" -> "Glu"
                    "GGU", "GGC", "GGA", "GGG" -> "Gly"
                    else -> ""
                    }
                if (kodon == "UAA" || kodon == "UAG" || kodon == "UGA") {
                    break
                    }
                }
            this.data = translacja
            this.identifier = Type.PROTEIN
            }
        }

        class ProteinSequence(
            override var identifier: Type,
            override var data: String,
            override val valid_chars: VALIDCHARS,
            override var length: Int
        ) : Sequence(Type.PROTEIN, data, valid_chars, length) {

            override fun toString(): String {
                return "> $identifier: $data"
            }

            override fun findmotif(motif: String): Int {
                val dlmotif = motif.length
                for (i in 0..this.length - dlmotif) {
                    var ciag = true
                    for (j in 0 until dlmotif) {
                        if (this.data[i + j] != motif[j]) {
                            ciag = false
                            break
                        }
                    }
                    if (ciag == true) return i
                }
                println("W sekwencji nie ma podanego motywu.")
                return -1
            }

            fun mutate(position: Int, value: String) {
                if (position < 0 || position / 3 >= length) {
                    throw IndexOutOfBoundsException("Podany indeks nie występuje w ciągu białek.")
                }
                val mutacja = StringBuilder(data)
                mutacja.replace(position, position + 3, value)
                this.data = mutacja.toString()
            }
        }
    }

fun main(){
    //Przedstawienie fukcjonalności programu.
    val sekwDNA = Sequence.DNASequence(Type.DNA,"AGCTGTCACGT", VALIDCHARS.A, 5)
    sekwDNA.length = sekwDNA.data.length
    sekwDNA.mutate(4, 'T')
    println("$sekwDNA")
    sekwDNA.complement()
    println("$sekwDNA")
    sekwDNA.transcribe()
    println("$sekwDNA")

    val sekwRNA = Sequence.RNASequence(Type.RNA, "AUGCAUCGCGUAGCU", VALIDCHARS.C, 15)
    println("${sekwRNA.findmotif("GCA")}")
    println(sekwRNA.data)
    sekwRNA.transcribe()
    println(sekwRNA.data)
}


