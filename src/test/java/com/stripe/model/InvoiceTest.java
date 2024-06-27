package com.stripe.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.stripe.BaseStripeTest;
import com.stripe.net.ApiResource;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class InvoiceTest extends BaseStripeTest {
  @Test
  public void testDeserialize() throws Exception {
    final String data = getFixture("/v1/invoices/in_123");
    final Invoice invoice = ApiResource.GSON.fromJson(data, Invoice.class);
    assertNotNull(invoice);
    assertNotNull(invoice.getId());
    assertEquals("invoice", invoice.getObject());
    assertNull(invoice.getChargeObject());
  }

  @Test
  public void testDeserializeWithExpansions() throws Exception {
    // TODO: Add support for expanding "transfer_data.destination" when stripe-mock supports it.
    final String[] expansions = {
      "charge", "customer",
    };
    final String data = getFixture("/v1/invoices/in_123", expansions);
    final Invoice invoice = ApiResource.GSON.fromJson(data, Invoice.class);
    assertNotNull(invoice);
    assertNotNull(invoice.getId());
    final Charge charge = invoice.getChargeObject();
    assertNotNull(charge);
    assertNotNull(charge.getId());
    assertEquals(invoice.getCharge(), charge.getId());
    final Customer customer = invoice.getCustomerObject();
    assertNotNull(customer);
    assertNotNull(customer.getId());
    assertEquals(invoice.getCustomer(), customer.getId());
  }

  @Test
  public void testDeserializeWithUnexpandedArrayExpansions() throws Exception {
    final String data = getResourceAsString("/api_fixtures/invoice_with_discount_ids.json");
    final Invoice invoice = ApiResource.GSON.fromJson(data, Invoice.class);
    assertNotNull(invoice);
    assertEquals("25_5OFF", invoice.getDiscounts().get(0));
    assertNull(invoice.getDiscountObjects().get(1));
    assertEquals("50_5OFF", invoice.getDiscounts().get(1));
    assertNull(invoice.getDiscountObjects().get(1));
    assertEquals(2, invoice.getDiscounts().size());
    assertEquals(2, invoice.getDiscountObjects().size());
  }

  @Test
  public void testDeserializeWithArrayExpansions() throws Exception {
    final Invoice invoice =
        ApiResource.GSON.fromJson(
            getResourceAsString("/api_fixtures/invoiceitem_with_discount_objects.json"),
            Invoice.class);

    assertEquals(
        invoice, ApiResource.GSON.fromJson(ApiResource.GSON.toJson(invoice), Invoice.class));
  }

  @Test
  public void testSetAccountTaxIds_NullInput() {
    Invoice invoice = new Invoice();
    invoice.setAccountTaxIds(Arrays.asList("id1", "id2"));

    invoice.setAccountTaxIds(null);

    assertNull(invoice.getAccountTaxIds());
  }

  @Test
  public void testSetAccountTaxIds_SameIds() {
    List<String> ids = Arrays.asList("id1", "id2");
    Invoice invoice = new Invoice();
    invoice.setAccountTaxIds(ids);

    invoice.setAccountTaxIds(ids);

    assertEquals(ids, invoice.getAccountTaxIds());
  }


  @Test
  public void testSetInvoiceTaxIds_DifferentIds() {
    List<String> initialIds = Arrays.asList("id1", "id2");
    List<String> newIds = Arrays.asList("id3", "id4");
    Invoice invoice = new Invoice();
    invoice.setAccountTaxIds(initialIds);

    invoice.setAccountTaxIds(newIds);

    assertEquals(newIds, invoice.getAccountTaxIds());
  }

  @Test
  public void testSetAccountTaxIdObjects_NullInput() {
    // Arrange
    Invoice invoice = new Invoice();

    // Act
    invoice.setAccountTaxIdObjects(null);

    // Assert
    assertNull(invoice.getAccountTaxIds());
  }

  @Test
  public void testSetAccountTaxIdObjects_NonNullInput() {
    // Arrange
    TaxId taxId1 = new TaxId();
    TaxId taxId2 = new TaxId();
      taxId1.setId("id1");
      taxId2.setId("id2");
    List<TaxId> taxIds = Arrays.asList(taxId1, taxId2);
    Invoice invoice = new Invoice();

    // Act
    invoice.setAccountTaxIdObjects(taxIds);

    // Assert
    List<TaxId> accountTaxIds = invoice.getAccountTaxIdObjects();
    assertNotNull(accountTaxIds);
    assertEquals(2, accountTaxIds.size());
    assertEquals("id1", accountTaxIds.get(0).getId());
    assertEquals(taxId1, accountTaxIds.get(0));
    assertEquals("id2", accountTaxIds.get(1).getId());
    assertEquals(taxId2, accountTaxIds.get(1));
  }
}
