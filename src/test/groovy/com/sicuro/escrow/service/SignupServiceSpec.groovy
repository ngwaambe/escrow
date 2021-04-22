package com.sicuro.escrow.service

import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.model.Contact
import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.Organisation
import com.sicuro.escrow.model.SignupRequest
import com.sicuro.escrow.model.Title
import com.sicuro.escrow.persistence.CustomerRepository
import com.sicuro.escrow.persistence.UserRepository
import com.sicuro.escrow.persistence.dao.UserDao
import com.sicuro.escrow.persistence.dao.ActivationLinkDao
import com.sicuro.escrow.persistence.entity.UserEntity
import com.sicuro.escrow.persistence.entity.ActivationLinkEntity
import com.sicuro.escrow.persistence.entity.CustomerEntity
import com.sicuro.escrow.exception.UserAlreadyExistException
import com.sicuro.escrow.exception.SendMailException
import com.sicuro.escrow.exception.ExpiredAccountActivationLink
import org.springframework.mail.MailSendException
import spock.lang.Shared
import spock.lang.Specification
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Subject

import java.time.OffsetDateTime

class SignupServiceSpec extends Specification{
    def customerRepository = Mock(CustomerRepository)
    def userDao = Mock(UserDao)
    def userRepository = Mock(UserRepository)
    def passwordEncoder = Mock(PasswordEncoder)
    def mailService = Mock(MailService)
    def activationLinkDao = Mock(ActivationLinkDao)

    @Shared
    def request = new SignupRequest(
            new Organisation("sicuro", "taxNumber"),
            new Contact(
                    Title.Mr,
                    "en",
                    "firstName",
                    "lastName",
                    "email",
                    "password"
            )
    )
    @Subject
    def service = new SignupService(
            customerRepository,
            userDao,
            activationLinkDao,
            userRepository,
            passwordEncoder,
            mailService,
            "address",
            24
    )

    def "Signup  - happy path"() {
        given:
        def activationId = UUID.randomUUID().toString()
        def encodedPassword ="password"
        def customer = createCustomer([:])

        when:
        service.signup(request)

        then:
        1 * userDao.findByUsername(request.contact.email) >> null

        then:
        1 * customerRepository.createCustomer(request) >> [customer, activationId]

        then:
        1 * passwordEncoder.encode(request.contact.password) >> encodedPassword

        then:
        1 * userRepository.createUser(request.contact.email, encodedPassword, _)

        then:
        1 * mailService.sendMail(*_)
    }

    def "Signup email already exist: Exception is thrown"() {
        when:
        service.signup(request)

        then:
        1 * userDao.findByUsername(request.contact.email) >> createUserEntity()

        then:
        thrown(UserAlreadyExistException)

    }

    def "Signup mail service is not avialable: Exception is thrown"() {
        given:
        def activationId = UUID.randomUUID().toString()
        def encodedPassword ="password"
        def customer = createCustomer([:])

        when:
        service.signup(request)

        then:
        1 * userDao.findByUsername(request.contact.email) >> null

        then:
        1 * customerRepository.createCustomer(request) >> [customer, activationId]

        then:
        1 * passwordEncoder.encode(request.contact.password) >> encodedPassword

        then:
        1 * userRepository.createUser(request.contact.email, encodedPassword, _)

        then:
        1 * mailService.sendMail(*_) >> {
            throw new MailSendException("Failed")
        }

        then:
        thrown(SendMailException)

    }

    def "Activate account - happy path"(){
        given:
        def activationId = UUID.randomUUID().toString()
        def customerEntity = new CustomerEntity(1L,"customerNr",Title.Mr,"firstName", "lastName", Title.Mr.gender, "email","en")
        def activationLinkEntity = new ActivationLinkEntity(activationId, false, customerEntity, OffsetDateTime.now().minusHours(24+1), OffsetDateTime.now())
        def userEntity = createUserEntity()

        when:
        service.activateAccount(activationId)

        then:
        1 * activationLinkDao.findById(activationId) >> Optional.of(activationLinkEntity)

        then:
        1 * userDao.findByUsername(activationLinkEntity.customer.email) >> userEntity

        then:
        1 * userDao.save(_)
    }

    def "Activate account - expired Link exception is thrown"(){
        given:
        def activationId = UUID.randomUUID().toString()
        def customerEntity = new CustomerEntity(1L,"customerNr",Title.Mr,"firstName", "lastName", Title.Mr.gender, "email","en")
        def activationLinkEntity = new ActivationLinkEntity(activationId, false, customerEntity, OffsetDateTime.now(), OffsetDateTime.now())

        when:
        service.activateAccount(activationId)

        then:
        1 * activationLinkDao.findById(activationId) >> Optional.of(activationLinkEntity)

        then:
        thrown(ExpiredAccountActivationLink)
    }


    def "Activate account - can not resolve user exception is thrown"(){
        given:
        def activationId = UUID.randomUUID().toString()
        def customerEntity = new CustomerEntity(1L,"customerNr",Title.Mr,"firstName", "lastName", Title.Mr.gender, "email","en")
        def activationLinkEntity = new ActivationLinkEntity(activationId, false, customerEntity, OffsetDateTime.now().minusHours(24+1), OffsetDateTime.now())

        when:
        service.activateAccount(activationId)

        then:
        1 * activationLinkDao.findById(activationId) >> Optional.of(activationLinkEntity)

        then:
        1 * userDao.findByUsername(activationLinkEntity.customer.email) >> null

        then:
        thrown(RuntimeException)
    }

    Customer createCustomer(Map custumParams = [:]) {
        def defaultParams = [
                customerNumber: "String",
                title: Title.Mr,
                firstName: "String",
                lastName: "String",
                organisation: null,
                preferedLanguage: "en",
                taxNumber: null,
                email: "Email",
        ]
        def params = defaultParams + custumParams

        new Customer(
                params['customerNumber'],
                params['title'],
                params['firstName'],
                params['lastName'],
                params['preferedLanguage'],
                params['email'],
                params['organisation'],
                params['taxNumber']
        )
    }

    CustomerEntity createCustomerEntity(Map custumParams = [:]) {
        def defaultParams = [
                id: 1,
                customerNumber: "String",
                title: Title.Mr,
                firstName: "String",
                lastName: "String",
                email: "Email",
                preferedLanguage: "en",
                applyVat: false,
                organisation: null,
                address: null,
                taxNumber: null,
                phoneNumber: null,
                partnerId: null,
                identityNumber: null,

        ]
        def params = defaultParams + custumParams

        new CustomerEntity(
                params['id'],
                params['customerNumber'],
                params['title'],
                params['firstName'],
                params['lastName'],
                params['title'].gender,
                params['email'],
                params['preferedLanguage'],
                params['appylVat'],
                params['organisation'],
                params['address'],
                params['taxNumber'],
                params['phoneNumber'],
                params['partnerId'],
                params['identityNumber']
        )
    }

    UserEntity createUserEntity() {
        new UserEntity(
                0L,"","", BaseStatus.active, [] as Set, null, null, null, null, null, null
        )
    }

    ActivationLinkEntity createActivationLinkEntity(Map custumParams = [:]){
        def defaultParams = [
                id: UUID.randomUUID().toString(),
                active: false,
                customer: new CustomerEntity(),
                lastName: "String",
                organisation: null,
                preferedLanguage: "en",
                taxNumber: null,
                email: "email",
        ]
        def params = defaultParams + custumParams
        new ActivationLinkEntity(

        )
    }
}
