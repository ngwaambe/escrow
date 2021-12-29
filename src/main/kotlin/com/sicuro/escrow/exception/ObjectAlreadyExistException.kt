package com.sicuro.escrow.exception

class ObjectAlreadyExistException(override val message: String): ConflictException(message);
