package com.sicuro.escrow.util.security

import com.sicuro.escrow.model.CheckTokenResponse
import com.sicuro.escrow.service.MyUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClaims
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.util.*

// ktlint-disable no-wildcard-imports

// ktlint-disable no-wildcard-imports

@Component
class JwtUtils @Autowired constructor(@Value("\${security.jwt.secret}") val jwtSecret: String) {

    fun generateJWToken(authentication: Authentication, expiration: Number): String {
        val userdata = getUserDetails(authentication)
        val claims = DefaultClaims()
        claims.issuedAt = Date()
        claims.subject = userdata.username
        claims.expiration = Date(Date().time.plus(expiration.toInt()))
        claims["customerId"] = userdata.customerId
        claims["tempPwd"] = userdata.temporaryPassword
        claims["completeRegistration"] = userdata.completeRegistration

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUsernameFromJwtToken(token: String): String = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject

    fun validateJwtToken(token: String): Boolean {
        return checkToken(token).active
    }

    fun checkToken(token: String): CheckTokenResponse {
        return try {
            val jws: Jws<Claims> = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            val exp = jws.body.expiration.toInstant().epochSecond
            CheckTokenResponse(true, exp, null)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            CheckTokenResponse(false, 0, e.localizedMessage)
        }
    }

    fun getCustomerIdFromToken(token: String): Long {
        return try {
            val jws: Jws<Claims> = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            (jws.body["customerId"] as Int).toLong()
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            throw UsernameNotFoundException("Could not resolve user from token")
        }
    }

    private fun getUserDetails(authentication: Authentication) = authentication.principal as MyUser

    companion object {
        private var log = LoggerFactory.getLogger(JwtUtils::class.java)
    }
}
