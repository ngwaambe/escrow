package com.sicuro.escrow.persistence.dao

import com.sicuro.escrow.model.BaseBoStatus
import com.sicuro.escrow.persistence.entity.RoleEntity
import com.sicuro.escrow.persistence.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserDao : JpaRepository<UserEntity, Long> {

    fun findByUsernameAndPassword(username: String, password: String): UserEntity?

    fun findByUsernameAndStatus(username: String, status: BaseBoStatus): Optional<UserEntity?>?

    /**
     * Retreive for given username , use with agent role if available otherwise null.
     *
     * @param username
     * @return
     */
    @Query("Select u from UserEntity u Join u.roles r  Where u.username = :username and u.status = 'active'"
        + " and (r.roleName ='ROLE_ADMIN' or r.roleName ='ROLE_AGENT' or r.roleName = 'ROLE_PARTNER') ")
    fun findBackendUserByUsername(@Param("username") username: String): UserEntity?

    fun findByUsernameAndRolesIn(username: String, roles: List<RoleEntity>): UserEntity?

    /**
     * Retreive for given username , use with agent role if available otherwise null.
     *
     * @param username
     * @return
     */
    @Query("Select u from UserEntity u Join u.roles r  Where u.username = :username and u.status = 'active'"
        + " and (r.roleName = 'ROLE_PARTNER') ")
    fun findPartnerUserByUsername(@Param("username") username: String): Optional<UserEntity?>?

    fun findByUsername(username: String): Optional<UserEntity?>?

    /**
     * Get Masked password.
     *
     * @param username
     * @return
     */
    @Query("SELECT U.password FROM UserEntity U Where U.username = :username")
    fun getMaskedPassword(@Param("username") username: String): String?

    /**
     * Get User status.
     *
     * @param username
     * @return
     */
    @Query("SELECT U.status FROM UserEntity U Where U.username = :username")
    fun getUserStatus(@Param("username") username: String): Optional<BaseBoStatus?>?

    fun countByUsername(username: String): Long?

    fun countByIdAndSecurityQuestionAnswerNotNullAndSecurityQuestionNotNull(userId: Long?): Long?

    fun countByUsernameAndSecurityQuestionNotNullAndSecurityQuestionAnswerNotNull(username: String?): Long?
}
