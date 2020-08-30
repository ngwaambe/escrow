package com.sicuro.escrow.model

data class Organisation(
  val name:String,
  val taxNumber:String
);

data class Contact(
    val title:Title,
    val preferedLaguage: String,
    val firstName:String,
    val lastName:String,
    val email:String,
    val password:String
)

data class RegistrationDetail(
    val organisation: Organisation?,
    val contact: Contact
);
