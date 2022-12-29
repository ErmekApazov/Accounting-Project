package com.thegogetters.accounting.service.Impl;

import com.thegogetters.accounting.dto.ClientVendorDto;
import com.thegogetters.accounting.dto.CompanyDto;
import com.thegogetters.accounting.entity.ClientVendor;
import com.thegogetters.accounting.entity.Company;
import com.thegogetters.accounting.enums.ClientVendorType;
import com.thegogetters.accounting.mapper.MapperUtil;
import com.thegogetters.accounting.repository.ClientVendorRepository;
import com.thegogetters.accounting.service.ClientVendorService;
import com.thegogetters.accounting.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;

    private final UserService userService;


    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, UserService userService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }

    /*
    @Override
    public List<ClientVendorDto> findAllByClientVendorType(ClientVendorType clientVendorType) {

        List<ClientVendor> clientVendorList = clientVendorRepository.findAllByClientVendorType(clientVendorType);
        return clientVendorList.stream().
                map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

     */


    @Override
    public List<ClientVendorDto> findAllByClientVendorTypeBelongsToCompany(ClientVendorType vendor) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();//security give user who log in
        CompanyDto companyDto = userService.findCompanyByUserName(username);
        Company company = mapperUtil.convert(companyDto, new Company());

        List<ClientVendor> clientVendorList = clientVendorRepository.findAllByClientVendorTypeAndCompany(vendor, company);

        return clientVendorList.stream().
                map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto()))
                .collect(Collectors.toList());

    }

    @Override
    public ClientVendorDto findById(long id) {

        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow();

        return mapperUtil.convert(clientVendor,new ClientVendorDto());
    }






    /*
    @Override
    public List<ClientVendorDto> findAllByCompanyId(Long companyId) {

        List<ClientVendor> clientVendorList = clientVendorRepository.findAllByCompany_Id(companyId);

       return clientVendorList.stream().
                map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

     */
}
