package com.daugherty.e2c.business.mapper;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.daugherty.e2c.domain.SupplierApproval;
import com.daugherty.e2c.service.json.JsonSupplierApproval;

@Component
public class SupplierApprovalMapper {
    @Inject
    private SupplierMapper supplierMapper;

    public JsonSupplierApproval fromExistingDomainObject(SupplierApproval approval) {
        JsonSupplierApproval json = new JsonSupplierApproval();
        json.setId(approval.getPublicId());
        json.setApprovalStatus(approval.getApprovalStatus().getName());
        json.setTitle(approval.getTitle());
        json.setSupplier(supplierMapper.fromDomainObject(approval.getSupplier(), Locale.ENGLISH));
        json.setApprovalType(approval.getType());
        json.setVersion(approval.getVersion());
        json.setLastUpdatedBy(approval.getLastUpdatedBy());
        json.setLastUpdatedAt(approval.getLastUpdatedAt());
        return json;
    }
}
