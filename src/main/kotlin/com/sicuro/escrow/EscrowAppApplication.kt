package com.sicuro.escrow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EscrowAppApplication

fun main(args: Array<String>) {
	runApplication<EscrowAppApplication>(*args)
}
