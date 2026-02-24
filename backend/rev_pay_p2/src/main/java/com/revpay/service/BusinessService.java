package com.revpay.service;

import com.revpay.dto.BusinessRegisterRequest;
import com.revpay.dto.BusinessProfileResponse;

public interface BusinessService {

    BusinessProfileResponse registerBusiness(BusinessRegisterRequest request);

    BusinessProfileResponse getMyBusinessProfile();
}