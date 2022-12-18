package com.sicuro.escrow.persistence

import com.sicuro.escrow.exception.InvalidResourceException
import com.sicuro.escrow.model.LinkType
import com.sicuro.escrow.persistence.dao.ActivationLinkDao
import com.sicuro.escrow.persistence.entity.ActivationLinkEntity
import com.sicuro.escrow.persistence.entity.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class ActivationLinkRepository @Autowired constructor(val activationLinkDao: ActivationLinkDao) {

    fun createLink(user: UserEntity, type: LinkType) =
        activationLinkDao.save(
            ActivationLinkEntity(
                UUID.randomUUID().toString(),
                type,
                true,
                user
            )
        ).uuid

    fun findByIdAndType(linkId: String, type: LinkType) = activationLinkDao.findByUuidAndType(linkId, type) ?: throw InvalidResourceException("Unknown activation link")

    fun findByUserIdAndType(userId: Long, type: LinkType) = activationLinkDao.findByUserIdAndType(userId, type)

    fun save(link: ActivationLinkEntity) = activationLinkDao.saveAndFlush(link)

    fun delete(linkId: String) = activationLinkDao.deleteById(linkId)
}
