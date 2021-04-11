package com.test.tournament.util

class CommonFunctions {
    companion object {
        fun Int.isPowerOfTwo(): Boolean {
            var num = this
            while (num > 1 && num % 2 == 0) {
                num = num.div(2)
            }
            return num == 1;
        }
    }
}