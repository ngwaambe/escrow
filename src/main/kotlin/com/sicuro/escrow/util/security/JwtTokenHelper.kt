package com.sicuro.escrow.util.security

import com.sicuro.escrow.exception.AccessNotAllowedException

class JwtTokenHelper {
    companion object {
        fun parseJwtToken(headerAuth: String): String? {
            if (!headerAuth.isNullOrEmpty() && headerAuth.startsWith("Bearer ")) {
                return headerAuth.substring(7, headerAuth.length);
            }
            return null;
        }
    }


}
