package com.denaBank.loanApplication.Loan.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.denaBank.loanApplication.Loan.dao.EMIDetails;
import com.denaBank.loanApplication.Loan.dao.LoanDetails;
import com.denaBank.loanApplication.Loan.repo.EMIRepo;
import com.denaBank.loanApplication.Loan.repo.LoadRepository;
import com.denaBank.loanApplication.customer.Exceptions.UserNotFound;
import com.denaBank.loanApplication.customer.dao.CustomerDao;
import com.denaBank.loanApplication.customer.repo.UserRepo;
import com.denaBank.loanApplication.customer.service.CustomerService;

@Service
public class LoanService {

	@Autowired
	private LoadRepository lr;
	@Autowired
	private UserRepo ur;
	@Autowired
	private EMIService es;
	@Autowired
	private CustomerService cs;
	
	public LoanDetails addNewLoadDetails(LoanDetails loan) {
		loan.setUpdateDate(LocalDate.now());
		try {
			if(loan.getCustomer().getId() == null) {
				throw new UserNotFound("Customer is not elegible for loan application");
			}else {
				CustomerDao customer = cs.getCustomerByID(loan.getCustomer().getId());
				loan.setCustomer(customer);
			}
		}catch(NullPointerException nx) {
			throw new UserNotFound("Customer information is not present or not in proper format");
		}
		return lr.save(loan);
	}
	
	public LoanDetails updateLoanDetails(LoanDetails loan) {
		Optional<LoanDetails> loanByIdOpt = lr.findById(loan.getId());
		LoanDetails loanById = null;
		if (loanByIdOpt.isPresent()) {
			loanById = loanByIdOpt.get();
		}else {
			throw new UserNotFound("Loan with id "+loan.getId() + " not present");
		}
		loanById.setStatus(loan.getStatus());
		loanById.setUpdateDate(LocalDate.now());
		int intrest = 11;
		if (loan.getStatus().toUpperCase().equals("APPROVED")) {
			loanById.setDateOfApproval(LocalDate.now());
			
			if(loanById.getLoanType().toUpperCase().equals("PERSONAL")) {
				intrest = 9;
			}else if (loanById.getLoanType().toUpperCase().equals("HOME")) {
				intrest = 8;
			}else if (loanById.getLoanType().toUpperCase().equals("CAR")) {
				intrest = 11;
			}
			EMIDetails ed = new EMIDetails(loanById, 0, loanById.getTennureInMonth(), intrest);
			ed.setNextDate(loanById.getDateOfApproval().plusDays(30));
			ed.setLateFees(ed.getAmount() / 100);
			LoanDetails ld =  lr.save(loanById);
			es.addNewEmiDetail(ed);
			return ld;
		} 
		return lr.save(loanById);
	}
	
	public List<LoanDetails> getLoanDetailsByCustomer(Long id) {
		if (id == 0) {
			return (List<LoanDetails>) lr.findAll();
		}
		Optional<CustomerDao> customer = ur.findById(id);
		if (customer.isPresent()) {
			Optional<List<LoanDetails>> loanDetails = lr.findByCustomer(customer.get());
			if (loanDetails.isPresent()) {
				return loanDetails.get();
			}else {
				throw new RuntimeException("No Data for this customer");
			}
		}else {
			throw new UserNotFound("User id is not valid");
		}
	}
	
	public List<LoanDetails> getLoanDetails() {
		return (List<LoanDetails>) lr.findAll();
	}
	
	public LoanDetails getLoanDetailsById(Long id) {
		return lr.findById(id).get();
	}
}
