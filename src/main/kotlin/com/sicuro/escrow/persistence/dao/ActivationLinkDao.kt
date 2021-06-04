package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.model.LinkType
import com.sicuro.escrow.persistence.entity.ActivationLinkEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ActivationLinkDao: JpaRepository<ActivationLinkEntity, String> {

    fun findByUserUsernameAndType( username: String, type: LinkType):ActivationLinkEntity?

    fun findByUuidAndType(id:String, type:LinkType): ActivationLinkEntity?

    fun findByUserIdAndType(userId: Long, type: LinkType): ActivationLinkEntity?
}
