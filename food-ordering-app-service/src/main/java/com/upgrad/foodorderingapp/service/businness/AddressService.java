package com.upgrad.foodorderingapp.service.businness;

import com.upgrad.foodorderingapp.service.common.Constants;
import com.upgrad.foodorderingapp.service.common.FoodAppUtil;
import com.upgrad.foodorderingapp.service.dao.AddressDao;
import com.upgrad.foodorderingapp.service.dao.StateDao;
import com.upgrad.foodorderingapp.service.entity.AddressEntity;
import com.upgrad.foodorderingapp.service.entity.CustomerAddressEntity;
import com.upgrad.foodorderingapp.service.entity.CustomerEntity;
import com.upgrad.foodorderingapp.service.entity.StateEntity;
import com.upgrad.foodorderingapp.service.exception.AddressNotFoundException;
import com.upgrad.foodorderingapp.service.exception.AuthorizationFailedException;
import com.upgrad.foodorderingapp.service.exception.SaveAddressException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.upgrad.foodorderingapp.service.common.GenericErrorCode.*;

@Service
public class AddressService {
    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private AddressDao addressDao;
    @Autowired
    private StateDao stateDao;

    /**
     * Method to get a State by uuid from db
     *
     * @param stateUuid
     * @return
     */
    public StateEntity getStateByUUID(final String stateUuid) throws AddressNotFoundException {
        log.debug("****** Starting getStateByUUID *******");
        final StateEntity stateEntity = stateDao.getStateByUUID(stateUuid);
        // Throws exception if no state exists in db with the uuid
        if (stateEntity == null) {
            log.info("no state with the uuid exists in db: {}",stateUuid);
            throw new AddressNotFoundException(ANF_002.getCode(), ANF_002.getDefaultMessage());
        }
        log.debug("****** Ends getStateByUUID *******");
        return stateEntity;
    }

    /**
     * Method to save AddressEntity into the database
     *
     * @param addressEntity
     * @param customerEntity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerEntity customerEntity) throws SaveAddressException {
        log.debug("****** Starting saveAddress *******");
        // Throw exception if any of the required field is Empty
        if (FoodAppUtil.isEmptyField((addressEntity.getFlatBuildingName()))
                || FoodAppUtil.isEmptyField(addressEntity.getLocality())
                || FoodAppUtil.isEmptyField(addressEntity.getCity())
                || FoodAppUtil.isEmptyField(addressEntity.getPincode())
                || FoodAppUtil.isEmptyField(addressEntity.getUuid())) {
            log.info("Mandatory address field is empty");
            throw new SaveAddressException(SAR_001.getCode(), SAR_001.getDefaultMessage());
        }
        // Throw exception if the pincode is invalid
        if (!FoodAppUtil.isValidPattern(Constants.PINCODE_PATTERN, addressEntity.getPincode())){
            log.info("Invalid pincode");
            throw new SaveAddressException(SAR_002.getCode(),SAR_002.getDefaultMessage());
        }

        final List<CustomerEntity> customerEntities = new ArrayList<>();
        customerEntities.add(customerEntity);
        addressEntity.setCustomers(customerEntities);
        final AddressEntity savedAddressEntity = addressDao.saveAddress(addressEntity);
        log.debug("****** Ends saveAddress *******");
        return savedAddressEntity;
    }

    /**
     * Method to retrieve all address for a customer
     *
     * @param customerEntity
     * @return
     */
    public List<AddressEntity> getAllAddress(final CustomerEntity customerEntity) {
        log.debug("****** Starting getAllAddress *******");
        final List<AddressEntity> addressList = new ArrayList<>();
        final List<CustomerAddressEntity> customerAddressEntities = addressDao.getAllAddress(customerEntity);
        if (!customerAddressEntities.isEmpty()) {
            log.debug("customer Address List isn't empty");
            customerAddressEntities.forEach(
                    customerAddressEntity -> addressList.add(customerAddressEntity.getAddress()));
        }
        log.debug("****** Ends getAllAddress *******");
        return addressList;
    }
    /**
     * Method to get address by address UUID for a customer
     *
     * @param addressUuid
     * @param customerEntity
     * @return
     * @throws AddressNotFoundException
     * @throws AuthorizationFailedException
     */
    public AddressEntity getAddressByUUID(final String addressUuid, final CustomerEntity customerEntity)
            throws AddressNotFoundException, AuthorizationFailedException {
        log.debug("****** Starting getAddressByUUID *******");
        final AddressEntity addressEntity = addressDao.getAddressByUUID(addressUuid);
        // Throw exception if no AddressEntity is found by UUID
        if (addressEntity == null) {
            log.info("address not found by the uuid");
            throw new AddressNotFoundException(ANF_003.getCode(), ANF_003.getDefaultMessage());
        }

        final CustomerAddressEntity customerAddressEntity = addressDao.getCustomerAddress(addressEntity, customerEntity);
        //Throw exception if the address doesn't belong to the customer
        if (customerAddressEntity == null) {
            log.info("address doesn't belong to the same customer");
            throw new AuthorizationFailedException (ATHR_004.getCode(), ATHR_004.getDefaultMessage());
        }
        log.debug("****** Ends getAddressByUUID *******");
        return addressEntity;
    }

    /**
     * Method to soft delete address
     * @param addressEntity
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        log.debug("****** Inside deleteAddress *******");
        return addressDao.deleteAddress(addressEntity);
    }
    /**
     * Method to retrieve all the states
     * @return
     */
    public List<StateEntity> getAllStates(){
        log.debug("****** Inside getAllStates *******");
        return stateDao.getAllStates();
    }
}


