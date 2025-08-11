package com.teoman.service.impl;

import com.teoman.exception.BusinessRuleException;
import com.teoman.exception.RejectedInvoiceException;
import com.teoman.exception.ResourceNotFoundException;
import com.teoman.model.Invoice;
import com.teoman.model.Product;
import com.teoman.model.User;
import com.teoman.model.UserAuth;
import com.teoman.repository.InvoiceRepository;
import com.teoman.repository.ProductRepository;
import com.teoman.repository.UserRepository;
import com.teoman.service.CreditLimitService;
import com.teoman.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    //private final ProductRepository productRepository;
    private final CreditLimitService creditLimitService;

    @Override
    @Transactional(noRollbackFor = RejectedInvoiceException.class)
    public Invoice saveInvoice(UserAuth auth, double amount, String productName, String billNo) {

        log.info("Yeni fatura işlemi: kullanıcı={}, ürün={}, tutar={}, faturaNo={}",
                 auth.getUsername(), productName, amount, billNo);

        // Login olan kim? → onun profili (User)

        User user = userRepository.findByAuth(auth)
                                  .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı profili bulunamadı"));

        // Ürün bul oluştur
        Product product = Product.builder().productName(productName).build();

        // Kredi limiti kontrolü
        List<Invoice> approvedInvoices = invoiceRepository.findByUserAndApproved(user, true);
        double totalApproved = approvedInvoices.stream().mapToDouble(Invoice::getAmount).sum();
        boolean isApproved = (totalApproved + amount) <= creditLimitService.getCreditLimit();

        log.info("Toplam onaylı fatura: {}, Yeni fatura: {}, Onaylandı mı? {}", totalApproved, amount, isApproved);

        Invoice invoice = Invoice.builder()
                                 .billNo(billNo)
                                 .amount(amount)
                                 .user(user)
                                 .product(product)
                                 .approved(isApproved)
                                 .build();

        Invoice saved = invoiceRepository.save(invoice);

        log.info("Fatura veritabanına kaydedildi. ID: {}, Onay Durumu: {}", saved.getId(), saved.isApproved());

        if (!isApproved) {
            // Kayıt DB’de duracak, sonra 422 döneceğiz:
            throw new RejectedInvoiceException(saved.getId(), saved.getBillNo(),
                                               "Kredi limiti aşıldı. Fatura reddedildi.");
        }

        return saved;

    }


    @Override
    public List<Invoice> getApprovedInvoices() {

        log.info("Onaylı faturalar listeleniyor");

        return invoiceRepository.findByApproved(true);
    }

    @Override
    public List<Invoice> getRejectedInvoices() {

        log.info("Reddedilen faturalar listeleniyor");

        return invoiceRepository.findByApproved(false);
    }
}
