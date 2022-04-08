package com.denaBank.loanApplication.customer.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.denaBank.loanApplication.customer.dao.CustomerDao;

public interface UserRepo extends CrudRepository<CustomerDao, Long> {

	public Optional<CustomerDao> findByUserName(String userName);
}
