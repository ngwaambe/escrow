package com.sicuro.escrow.controller

import com.sicuro.escrow.model.Role
import com.sicuro.escrow.persistence.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/roles")
class RoleController @Autowired constructor(val roleRepository: RoleRepository) {

    @PutMapping
    fun addRole(@RequestBody @Valid role: Role): ResponseEntity<Role> {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleRepository.addRole(role))
    }

    @GetMapping
    fun getRoles(): ResponseEntity<List<Role>> {
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.getAllRoles())
    }

    @GetMapping("/{roleId}")
    fun getRole(@PathVariable("roleId") id: Long): ResponseEntity<Role>{
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.getRole(id));
    }

    @DeleteMapping("/{roleId}")
    fun deleteRole(@PathVariable("roleId") id: Long): ResponseEntity<Any>{
        return ResponseEntity.status(HttpStatus.OK).body(roleRepository.deleteRole(id));
    }
}
