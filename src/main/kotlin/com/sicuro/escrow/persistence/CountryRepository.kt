package com.sicuro.escrow.persistence

import com.sicuro.escrow.persistence.dao.CountryDao
import com.sicuro.escrow.persistence.entity.CountryEntity
import org.springframework.stereotype.Component

@Component
class CountryRepository(val countryDao: CountryDao) {

    fun shouldVatBeApplied(iso:String): Boolean {
        val country: CountryEntity = countryDao.findByIso(iso.toUpperCase())?: throw IllegalStateException("Can not resolve country: $iso")
        // if country is germany always apply tax
        return if (country.iso.equals("DE", true)) {
            true
        } else country.europeanUnionMember
    }
}
