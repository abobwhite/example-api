package com.daugherty.e2c.business.mutator;

import java.util.Locale;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.daugherty.e2c.business.ProductCollectionHydrator;
import com.daugherty.e2c.business.Validator;
import com.daugherty.e2c.domain.ApprovalStateTransitionVisitor;
import com.daugherty.e2c.domain.Product;
import com.daugherty.e2c.persistence.data.ProductCategoryWriteDao;
import com.daugherty.e2c.persistence.data.ProductImageWriteDao;
import com.daugherty.e2c.persistence.data.ProductWriteDao;

/**
 * Mutator for Product domain objects.
 */
@Service("productMutator")
@Transactional
public class ProductMutator extends BaseMutator<Product> {

    @Inject
    private Validator<Product> productValidator;
    @Inject
    private ProductWriteDao productWriteDao;
    @Inject
    private ProductCategoryWriteDao productCategoryWriteDao;
    @Inject
    private ApprovalStateTransitionVisitor changeDataVisitor;
    @Inject
    private ProductImageWriteDao productImageWriteDao;
    @Inject
    protected ProductCollectionHydrator productCollectionHydrator;

    @Override
    public Product create(Product product) {
        productValidator.validate(product);
        Product createdProduct = productWriteDao.insert(product);
        productCategoryWriteDao.updateCategories(createdProduct.getSnapshotId(), product.getMetadata().getCategories());
        return productCollectionHydrator.hydrateLatest(createdProduct, Locale.ENGLISH);
    }

    @Override
    public Product update(Product product) {
        product.visit(changeDataVisitor);

        productValidator.validate(product);
        Product updatedProduct = productWriteDao.update(product);
        productCategoryWriteDao.updateCategories(updatedProduct.getSnapshotId(), product.getMetadata().getCategories());
        productImageWriteDao.updateImages(updatedProduct.getSnapshotId(), product.getImages());

        return productCollectionHydrator.hydrateLatest(updatedProduct, Locale.ENGLISH);
    }

    @Override
    public void delete(Long productId) {
        throw new UnsupportedOperationException();
    }

}
