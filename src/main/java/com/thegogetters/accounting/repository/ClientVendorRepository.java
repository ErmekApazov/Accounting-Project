package com.thegogetters.accounting.repository;

import com.thegogetters.accounting.entity.ClientVendor;
import com.thegogetters.accounting.entity.Company;
import com.thegogetters.accounting.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository < ClientVendor, Long > {


    List<ClientVendor> findAllByClientVendorTypeAndCompany(ClientVendorType clientVendorType, Company company);

    List<ClientVendor> findAllByCompany(Company company);




}
