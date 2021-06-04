package com.sicuro.escrow.service

import com.sicuro.escrow.exception.InvalidActivationLinkException
import com.sicuro.escrow.exception.ObjectNotFoundException
import com.sicuro.escrow.model.Address
import com.sicuro.escrow.model.BaseStatus
import com.sicuro.escrow.model.CompleteSignupRequest
import com.sicuro.escrow.model.Contact
import com.sicuro.escrow.model.Customer
import com.sicuro.escrow.model.LinkType
import com.sicuro.escrow.model.Organisation
import com.sicuro.escrow.model.SecurityQuestion
import com.sicuro.escrow.model.SecurityQuestionDto
import com.sicuro.escrow.model.SignupRequest
import com.sicuro.escrow.model.Title
import com.sicuro.escrow.model.User
import com.sicuro.escrow.persistence.ActivationLinkRepository
import com.sicuro.escrow.persistence.CustomerRepository
import com.sicuro.escrow.persistence.UserRepository
import com.sicuro.escrow.persistence.entity.RoleEntity
import com.sicuro.escrow.persistence.entity.UserEntity
import com.sicuro.escrow.persistence.entity.ActivationLinkEntity
import com.sicuro.escrow.persistence.entity.CustomerEntity
import com.sicuro.escrow.exception.UserAlreadyExistException
import com.sicuro.escrow.exception.SendMailException
import org.springframework.mail.MailSendException
import spock.lang.Shared
import spock.lang.Specification
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Subject

import java.time.OffsetDateTime

class SignupServiceSpec extends Specification{
    def customerRepository = Mock(CustomerRepository)
    def userRepository = Mock(UserRepository)
    def passwordEncoder = Mock(PasswordEncoder)
    def mailService = Mock(MailService)
    def activationLinkRepository = Mock(ActivationLinkRepository)

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
            activationLinkRepository,
            userRepository,
            passwordEncoder,
            mailService,
            "address")

    def "Signup  - happy path"() {
        given:
        def activationId = UUID.randomUUID().toString()
        def encodedPassword ="password"
        def customer = createCustomer([:])
        def user  = createUser([:])

        when:
        service.signup(request)

        then:
        1 * userRepository.findByUsername(request.contact.email) >> null

        then:
        1 * customerRepository.createCustomer(request) >> customer

        then:
        1 * passwordEncoder.encode(request.contact.password) >> encodedPassword

        then:
        1 * userRepository.createUser(request.contact.email, encodedPassword, customer.id, ['ROLE_CUSTOMER'], BaseStatus.deactivated) >> [user, activationId]

        then:
        1 * mailService.sendMail(*_)
    }

    def "Signup email already exist: Exception is thrown"() {
        when:
        service.signup(request)

        then:
        1 * userRepository.findByUsername(request.contact.email) >> createUser([:])

        then:
        thrown(UserAlreadyExistException)

    }

    def "Signup mail service is not avialable: Exception is thrown"() {
        given:
        def activationId = UUID.randomUUID().toString()
        def encodedPassword ="password"
        def customer = createCustomer([:])
        def user  = createUser([:])

        and:
        mailService.sendMail(*_) >> {
            throw new MailSendException("Failed")
        }

        when:
        service.signup(request)

        then:
        1 * userRepository.findByUsername(request.contact.email) >> null

        then:
        1 * customerRepository.createCustomer(request) >> customer

        then:
        1 * passwordEncoder.encode(request.contact.password) >> encodedPassword

        then:
        1 * userRepository.createUser(request.contact.email, encodedPassword, customer.id, ['ROLE_CUSTOMER'], BaseStatus.deactivated) >> [user, activationId]

        then:
        thrown(SendMailException)

    }

    def "Activate account - happy path"(){
        given:
        def activationId = UUID.randomUUID().toString()
        def userEntity = createUserEntity()
        def activationLinkEntity = new ActivationLinkEntity(
                activationId,
                LinkType.ACCOUNT_ACTIVATION,
                true,
                userEntity,
                OffsetDateTime.now().minusHours(24+1),
                OffsetDateTime.now())


        when:
        service.activateAccount(activationId)

        then:
        1 * activationLinkRepository.findByIdAndType(activationId, LinkType.ACCOUNT_ACTIVATION) >> activationLinkEntity

        then:
        1 *  userRepository.activateUser(activationLinkEntity.user.username)
    }

    def "Activate account - can not resolve user exception is thrown"(){
        given:
        def activationId = UUID.randomUUID().toString()
        def userEntity = createUserEntity()
        def activationLinkEntity = new ActivationLinkEntity(
                activationId,
                LinkType.ACCOUNT_ACTIVATION,
                false,
                userEntity,
                OffsetDateTime.now().minusHours(24+1),
                OffsetDateTime.now())

        and:
        userRepository.activateUser(activationLinkEntity.user.username) >> {
            throw new RuntimeException()
        }

        when:
        service.activateAccount(activationId)

        then:
        1 * activationLinkRepository.findByIdAndType(activationId, LinkType.ACCOUNT_ACTIVATION) >> Optional.of(activationLinkEntity)

        and:
        thrown(RuntimeException)
    }

    def "Complete signup - happy path"() {
        given:
        def address = new Address(null,'street', '12', null,'53424', 'Bonn', 'Rheinland Pflaz', 'de','123546512321321')
        def securityQuestionDto = new SecurityQuestionDto(SecurityQuestion.GRAND_MOTHERS_MAIDEN_NAME, 'Answer')
        def request = new CompleteSignupRequest(
                address,
                securityQuestionDto
        )
        def customer = createCustomer([:])
        def userEntity = createUserEntity()

        and:
        customerRepository.getCustomer(customer.id) >> customer

        when:
        service.completeSignup(customer.id, request)

        then:
        customerRepository.getCustomer(customer.id) >> customer

        then:
        customerRepository.updateAddress(_, _, _) >> { arguments ->
            def _address = arguments[0]
            _address.street == address.street
            _address.houseNumber == address.houseNumber
            _address.streetExtension == address.streetExtension
            _address.postalCode  == address.postalCode
            _address.city   == address.city
            _address.region  == address.region
            _address.countryIso == address.countryIso
            _address.phoneNumber == address.phoneNumber

            def _customer = arguments[1]
            _customer == customer.id

            def _resolveVat = arguments[2]
            _resolveVat == true

            return _address
        }

        then:
        userRepository.updateUserSequrityQuestion(_, _) >> { arguments ->
            def _secretDto = arguments[0]
            _secretDto.question == securityQuestionDto.question
            _secretDto.answer == securityQuestionDto.answer

            def _username = arguments[1]
            _username == customer.email

        }
    }

    def "initiate password reset - happy path"(){
        given:
        def customer = createCustomer(email:'test@email.com')

        when:
        service.initiatePasswordReset(customer.email)

        then:
        userRepository.initiateResetPassword(_) >> { arguments ->
            arguments[0] == 'test@email.com'
            return "linkedUuid"
        }

        then:
        customerRepository.getCustomerByEmail(customer.email) >> customer

        then:
        mailService.sendMail(_, _, _, _) >> { arguments ->
            arguments[0] == 'test@email.com'
            arguments[1] == "Mr. ${customer.firstName} ${customer.lastName}"
            arguments[2] == "/${customer.language}/initiateResetPassword.ftl"

            def params = arguments[3]
            params['link'] == 'address/resetpassword?uuid=linkedUuid'
            params['accountNumber'] == customer.customerNumber
            params['username'] == customer.email



        }
    }

    def "initiate password reset - fails unknown user"(){
        given:
        def customer = createCustomer(email:'test@email.com')

        and:
        userRepository.initiateResetPassword(_ ) >> {
            throw new ObjectNotFoundException("user does not exist")
        }
        when:
        service.initiatePasswordReset("some user")

        then:
        thrown(ObjectNotFoundException)
    }

    def "reset password - happy path"() {
        given:
        def customer = createCustomer([:])
        def activationUuid = UUID.randomUUID().toString()
        def resetPasswordLink = createActivationLinkEntity(type:LinkType.RESET_PASSWORD)
        def password = "password"

        when:
        service.resetPassword(activationUuid)

        then:
        activationLinkRepository.findByIdAndType(activationUuid, LinkType.RESET_PASSWORD) >> resetPasswordLink

        then:
        userRepository.resetPassword(resetPasswordLink.user.username) >> password

        then:
        customerRepository.getCustomerByEmail(resetPasswordLink.user.username) >> customer

        then:
        mailService.sendMail(_, _, _, _) >> { arguments ->

            arguments[0] == 'test@email.com'
            arguments[1] == "Mr. ${customer.firstName} ${customer.lastName}"
            arguments[2] == "/${customer.language}/resetPassword.ftl"

            def params = arguments[3]
            params['link'] == 'address/authenticate'
            params['accountNumber'] == customer.customerNumber
            params['username'] == customer.email
            params['password'] == 'password'
        }
}

    def "reset password - fails invalid link"() {
        given:
        def activationUuid = UUID.randomUUID().toString()

        and:
        activationLinkRepository.findByIdAndType(activationUuid, LinkType.RESET_PASSWORD) >> {
            throw new InvalidActivationLinkException('')
        }

        when:
        service.resetPassword(activationUuid)

        then:
        thrown(InvalidActivationLinkException)
    }

    User createUser(Map customParams = [:]) {
        def defaultParams = [
        username:'Email',
        status:BaseStatus.deactivated,
        roles:['ROLE_CUSTOMER'],
        securityQuestion:  null]

        def params = defaultParams + customParams
        return new User(params['username'], params['status'],params['roles'], params['securityQuestion'])
    }

    Customer createCustomer(Map custumParams = [:]) {
        def defaultParams = [
                id: 123433,
                customerNumber: "1212121212",
                title: Title.Mr,
                firstName: "String",
                lastName: "String",
                organisation: null,
                preferedLanguage: "en",
                taxNumber: null,
                email: "Email",
                address: null,
                applyVat: false,
                partnerId: null,
                identityNumber: null
        ]
        def params = defaultParams + custumParams

        new Customer(
                params['id'],
                params['customerNumber'],
                params['title'],
                params['firstName'],
                params['lastName'],
                params['title'].gender,
                params['email'],
                params['preferedLanguage'],
                params['address'],
                params['organisation'],
                params['taxNumber'],
                params['applyVat'],
                params['partnerId'],
                params['identityNumber']
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
                0L,"","", BaseStatus.active, 0L, [] as Set, null, null, null, null, null, null
        )
    }

    ActivationLinkEntity createActivationLinkEntity(Map custumParams = [:]){
        def defaultParams = [
                uuid: UUID.randomUUID().toString(),
                type: LinkType.ACCOUNT_ACTIVATION,
                active: false,
                user: new UserEntity(1L, 'username', 'password', BaseStatus.active, 1L, [new RoleEntity(1L, 'Role', 'Role',null, null)].toSet(), null, null, null, null, null, null),
                created: OffsetDateTime.now(),
                lastModified: OffsetDateTime.now()
        ]
        def params = defaultParams + custumParams
        new ActivationLinkEntity(
                params['uuid'],
                params['type'],
                params['active'],
                params['user'],
                params['created'],
                params['lastModified']
        )
    }
}
