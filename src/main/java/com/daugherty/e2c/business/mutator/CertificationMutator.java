package com.daugherty.e2c.business.mutator;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Certification;

@Transactional
public abstract class CertificationMutator extends BaseMutator<Certification> {

    @Inject
    private ApprovalStateTransitionVisitor changeDataVisitor;

    @Override
    public Certification create(Certification certification) {
        certification.visit(changeDataVisitor);
        certification.validate();
        return doCreate(certification);
    }

    @Override
    public Certification update(Certification certification) {
        certification.visit(changeDataVisitor);
        certification.validate();
        return doUpdate(certification);
    }

    @Override
    public void delete(Long id) {
        doDelete(id);
    }

    protected abstract Certification doCreate(Certification certification);

    protected abstract Certification doUpdate(Certification certification);

    protected abstract void doDelete(Long certificationId);

}
