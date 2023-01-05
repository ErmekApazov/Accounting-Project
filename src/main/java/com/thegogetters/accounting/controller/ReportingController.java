package com.thegogetters.accounting.controller;

import com.thegogetters.accounting.dto.InvoiceDTO;
import com.thegogetters.accounting.dto.InvoiceProductDTO;
import com.thegogetters.accounting.entity.InvoiceProduct;
import com.thegogetters.accounting.enums.InvoiceType;
import com.thegogetters.accounting.service.InvoiceProductService;
import com.thegogetters.accounting.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
public class ReportingController {

    private final InvoiceService invoiceService;

    private final InvoiceProductService invoiceProductService;

    public ReportingController(InvoiceService invoiceService, InvoiceProductService invoiceProductService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
    }


    @GetMapping("/profitLossData")
    public String showProfitLossReports(Model model){

        List<InvoiceDTO> allApprovedSalesInvoicesBelongsToCompany = invoiceService.findAllInvoicesBelongsToCompany(InvoiceType.SALES);

        Map< String,Double> monthlyProfitLossDataMap = new LinkedHashMap<>();

        allApprovedSalesInvoicesBelongsToCompany.stream()
                .sorted(Comparator.comparing(InvoiceDTO::getDate).reversed()).map(invoiceDTO -> {

            LocalDate date = invoiceDTO.getDate();
            int year = date.getYear();
            Month month = date.getMonth();
            String name = month.name();

            String result = year + " " + name;

            double profitLoss = invoiceDTO.getInvoiceProducts().stream()
                    .map(InvoiceProductDTO::getProfitLoss)
                    .mapToDouble(BigDecimal::doubleValue)
                    .sum();

            monthlyProfitLossDataMap.put(result,profitLoss);
            return invoiceDTO;
        }).collect(Collectors.toList());

        model.addAttribute("monthlyProfitLossDataMap", monthlyProfitLossDataMap);


        return "report/profit-loss-report";
    }



    @GetMapping("/stockData")
    public String showStockReports(Model model){

        List<InvoiceProductDTO> invoiceProductDTOList = invoiceProductService.findAllInvoiceProductsOfCompany();


        model.addAttribute("invoiceProducts", invoiceProductDTOList);


        return "report/stock-report";
    }


}
