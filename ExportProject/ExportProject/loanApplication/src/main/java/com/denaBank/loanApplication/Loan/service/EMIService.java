package com.denaBank.loanApplication.Loan.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.denaBank.loanApplication.Loan.dao.EMIDetails;
import com.denaBank.loanApplication.Loan.dao.LoanDetails;
import com.denaBank.loanApplication.Loan.dao.Payment;
import com.denaBank.loanApplication.Loan.repo.EMIRepo;
import com.denaBank.loanApplication.Loan.repo.LoadRepository;
import com.denaBank.loanApplication.Loan.repo.PaymentRepo;
import com.denaBank.loanApplication.customer.Exceptions.UserNotFound;
import com.denaBank.loanApplication.customer.dao.CustomerDao;

@Service
public class EMIService {

	@Autowired
	private EMIRepo er;
	@Autowired
	private LoadRepository lr;
	@Autowired
	private PaymentRepo pr;
	
	public EMIDetails addNewEmiDetail(EMIDetails emi) {
		return er.save(emi);
	}
	
	public EMIDetails upDateEmiDetail(EMIDetails emi) {
		if (emi.getId() != null) {
			return er.save(emi);
		}
		throw new UserNotFound("No loan details found");
	}
	
	public List<EMIDetails> getByCustomerDetail(CustomerDao customer) {
		List<EMIDetails> emiDetails = new ArrayList<EMIDetails>();
		Optional<List<LoanDetails>> loan = lr.findByCustomer(customer);
		if (loan.isPresent()) {
			System.out.println(loan.get());
			for (LoanDetails ld : loan.get()) {
				try {
					EMIDetails details = er.findByLoanDetails(ld).get();					
					emiDetails.add(details);
				}catch(Exception ex) {
					
				}
			}
			return emiDetails.stream().filter(emi-> emi.getEmiLeft() > 0).collect(Collectors.toList());
		}
		else {
			throw new UserNotFound("Not loan details found");
		}
	}
	
	public List<EMIDetails> getEMIAllDetails(){
		return (List<EMIDetails>) er.findAll();
	}
	
	public Payment makePayment(CustomerDao customer, LoanDetails loan) {
		Optional<EMIDetails> emiOpt =  er.findByLoanDetails(loan);
		EMIDetails emi = emiOpt.get();
		if (emi.getEmiLeft() == 0) {
			throw new UserNotFound("All the EMI has been paid for this loan");
		}
		if (emi.getEmiLeft() == 1) {
			loan.setStatus("Paid");
		}
		Payment payment = new Payment(loan, LocalDate.now(), emi.getNextDate(),emi.getAmount() , 0.0);
		emi.setNextDate(emi.getNextDate().plusDays(30));
		emi.setEmiPaid(emi.getEmiPaid() + 1) ;
		emi.setEmiLeft(emi.getEmiLeft() - 1);
		er.save(emi);
		lr.save(loan);
		return pr.save(payment);
	}
	
	public List<Payment> getPaymentByLoanDetails(LoanDetails loan){
		if (loan == null) {
			return (List<Payment>) pr.findAll();
		}
		return pr.findByLoanDetails(loan);
	}
}
