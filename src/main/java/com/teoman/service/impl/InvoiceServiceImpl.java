package com.teoman.service.impl;

import com.teoman.dto.DtoInvoiceResponse;
import com.teoman.exception.RejectedInvoiceException;
import com.teoman.exception.ResourceNotFoundException;
import com.teoman.model.Invoice;
import com.teoman.model.Product;
import com.teoman.model.User;
import com.teoman.model.UserAuth;
import com.teoman.repository.InvoiceRepository;
import com.teoman.repository.UserRepository;
import com.teoman.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;

    @Value("${credit.limit}")
    private double creditLimit;

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
        boolean isApproved = (totalApproved + amount) <= creditLimit;

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
    public List<DtoInvoiceResponse> getApprovedInvoices() {

        log.info("Onaylı faturalar listeleniyor");

        return invoiceRepository.findByApproved(true)
                                .stream()
                                .map(this::toDto)   // toDto private kalabilir
                                .toList();
    }

    @Override
    public List<DtoInvoiceResponse> getRejectedInvoicesDto() {
        return invoiceRepository.findByApproved(false)
                                .stream()
                                .map(this::toDto)   // toDto private kalabilir
                                .toList();
    }

    private DtoInvoiceResponse toDto(Invoice i) {
        return DtoInvoiceResponse.builder()
                                 .id(i.getId())
                                 .billNo(i.getBillNo())
                                 .amount(i.getAmount())
                                 .approved(i.isApproved())
                                 .productName(i.getProduct() != null ? i.getProduct().getProductName() : null)
                                 .build();
    }

}
