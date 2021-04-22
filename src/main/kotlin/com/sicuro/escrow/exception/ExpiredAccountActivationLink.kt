package com.sicuro.escrow.exception

import java.lang.RuntimeException

class ExpiredAccountActivationLink constructor(message:String): RuntimeException(message) {
}
