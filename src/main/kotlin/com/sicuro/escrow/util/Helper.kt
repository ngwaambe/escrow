package com.sicuro.escrow.util

class Helper {
    companion object {
        fun parseJwtToken(headerAuth: String): String? {
            if (!headerAuth.isNullOrEmpty() && headerAuth.startsWith("Bearer ")) {
                return headerAuth.substring(7, headerAuth.length);
            }
            return null;
        }
    }
}
