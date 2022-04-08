package com.denaBank.loanApplication.Loan.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.denaBank.loanApplication.Loan.dao.EMIDetails;
import com.denaBank.loanApplication.Loan.dao.LoanDetails;

public interface EMIRepo extends CrudRepository<EMIDetails, Long> {

	public Optional<EMIDetails> findByLoanDetails(LoanDetails ld);
}
