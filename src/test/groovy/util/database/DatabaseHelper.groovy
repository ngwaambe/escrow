package util.database

import com.sicuro.escrow.model.LinkType
import com.sicuro.escrow.model.Title
import com.sicuro.escrow.persistence.dao.ActivationLinkDao
import com.sicuro.escrow.persistence.dao.CountryDao
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.persistence.dao.RoleDao
import com.sicuro.escrow.persistence.dao.UserDao
import com.sicuro.escrow.persistence.entity.CountryEntity
import com.sicuro.escrow.persistence.entity.CustomerEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

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

    void cleanDatabase() {
        activationLinkDao.deleteAllInBatch()
        userDao.deleteAllInBatch()
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


}

