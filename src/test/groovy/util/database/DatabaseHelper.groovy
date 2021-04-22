package util.database

import com.sicuro.escrow.persistence.dao.ActivationLinkDao
import com.sicuro.escrow.persistence.dao.CustomerDao
import com.sicuro.escrow.persistence.dao.UserDao
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

    public void cleanDatabase() {
        activationLinkDao.deleteAllInBatch()
        userDao.deleteAllInBatch()
        customerDao.deleteAllInBatch()
    }


    CustomerEntity findCustomerByEmail(String email) {
        customerDao.findByEmail(email)?:null
    }
}

