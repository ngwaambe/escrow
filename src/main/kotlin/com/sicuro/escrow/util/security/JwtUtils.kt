package com.sicuro.escrow.util.security

import com.sicuro.escrow.model.CheckTokenResponse
import com.sicuro.escrow.persistence.dao.CustomerDao
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClaims
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtUtils @Autowired constructor(@Value("\${security.jwt.secret}") val jwtSecret: String,
                                      val customerDao: CustomerDao){



    fun generateJWToken(authentication: Authentication, expiration:Number): String {
        val userdata = getUserDetails(authentication)
        val customer = customerDao.findByEmail(userdata.username)
        val claims = DefaultClaims()
        claims.issuedAt = Date()
        claims.subject = userdata.username
        claims.expiration = Date(Date().time.plus(expiration.toInt()))
        claims["customerId"] = customer!!.id.toString()

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getUsernameFromJwtToken(token: String) = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject

    fun validateJwtToken( token: String): Boolean {
        return checkToken(token).active
    }

    fun checkToken(token:String): CheckTokenResponse {
        try {
            val jws:Jws<Claims> = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            val exp = jws.body.expiration.toInstant().epochSecond
            return CheckTokenResponse(true, exp, null)
        } catch (e: Exception) {
            log.error(e.localizedMessage)
            return CheckTokenResponse(false, 0, e.localizedMessage)
        }

    }

    private fun getUserDetails(authentication: Authentication) = authentication.principal as UserDetails

    companion object{
        private var log = LoggerFactory.getLogger(JwtUtils::class.java)
    }

}
