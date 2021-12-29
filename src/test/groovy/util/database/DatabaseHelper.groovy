package util.database

import com.sicuro.escrow.model.LinkType
import com.sicuro.escrow.model.PaymentAccount
import com.sicuro.escrow.model.Title
import com.sicuro.escrow.persistence.dao.ActivationLinkDao
import com.sicuro.escrow.persistence.dao.CountryDao
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.persistence.dao.CustomerPaymentAccountDao
import com.sicuro.escrow.persistence.dao.PaymentAccountDao
import com.sicuro.escrow.persistence.dao.RoleDao
import com.sicuro.escrow.persistence.dao.UserDao
import com.sicuro.escrow.persistence.entity.CountryEntity
import com.sicuro.escrow.persistence.entity.CustomerEntity
import com.sicuro.escrow.persistence.entity.CustomerPaymentAccountEntity
import com.sicuro.escrow.persistence.entity.PaymentAccountEntity
import com.sicuro.escrow.persistence.entity.PaypalAcccountEntity
import com.sicuro.escrow.persistence.entity.RoleEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DatabaseHelper {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ActivationLinkDao activationLinkDao

    @Autowired
    private RoleDao roleDao

    @Autowired
    private CountryDao countryDao

    @Autowired
    private PaymentAccountDao paymentAccountDao

    @Autowired
    private CustomerPaymentAccountDao customerPaymentAccountDao

    void cleanDatabase() {
        activationLinkDao.deleteAllInBatch()
        userDao.deleteAllInBatch()
        customerPaymentAccountDao.deleteAllInBatch()
        paymentAccountDao.deleteAllInBatch()
        customerDao.deleteAllInBatch()
        roleDao.deleteAllInBatch()
        countryDao.deleteAllInBatch()
    }


    CustomerEntity findCustomerByEmail(String email) {
        customerDao.findByEmail(email)?:null
    }

    CountryEntity createCountry(String countryIso, String name) {
        def entity = new CountryEntity(
                null,
                countryIso.toUpperCase(),
                name,
                "123",
                "123",
                "EUR",
                countryIso,
                true,
                true,
                0, null, null)
        return countryDao.save(entity)
    }

    CustomerEntity createCustomerEntity(Map custumParams = [:]) {
        def defaultParams = [
                customerNumber: "String",
                title: Title.Mr,
                firstName: "String",
                lastName: "String",
                email: "Email",
                language: "en",
                applyVat: false,
                organisation: null,
                address: null,
                taxNumber: null,
                phoneNumber: null,
                partnerId: null,
                identityNumber: null,

        ]
        def params = defaultParams + custumParams
        return customerDao.save(new CustomerEntity(
                null,
                params['customerNumber'],
                params['title'],
                params['firstName'],
                params['lastName'],
                params['title'].gender,
                params['email'],
                params['language'],
                params['applyVat'],
                params['organisation'],
                params['address'],
                params['taxNumber'],
                params['phoneNumber'],
                params['partnerId'],
                params['identityNumber']
        ))
    }

    String findLinkId(String username, LinkType type) {
       return activationLinkDao.findByUserUsernameAndType(username, type)?.uuid
    }

    void createPaymentAccount(Long customerId, List<PaymentAccount> accounts){
        accounts.forEach( it -> {
            customerPaymentAccountDao.save(new CustomerPaymentAccountEntity(
                null,
                customerId,
                false,
                it.convert(),
                null,
                null))
        })
    }

    List<PaymentAccountEntity> getPaymentAccount() {
        paymentAccountDao.findAll()
    }

    PaymentAccountEntity getPaymentAccount(Long id) {
        paymentAccountDao.findById(id).orElse(null)
    }

    long countPaymentAccount() {
        paymentAccountDao.count()
    }

    void addAgentRoleToUser(String username) {
        def userEntity = userDao.findByUsername(username)
        def roleEntity = roleDao.save( new RoleEntity(null,"ROLE_AGENT", "ROLE_AGENT", null, null))
        userEntity.roles.add(roleEntity)
        userDao.saveAndFlush(userEntity)
    }

}

