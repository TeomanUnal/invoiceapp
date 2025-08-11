package com.teoman.repository;

import com.teoman.model.Invoice;
import com.teoman.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByUserAndApproved(User user, boolean approved);
    List<Invoice> findByApproved(boolean approved);

}
