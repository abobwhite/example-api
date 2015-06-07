package com.daugherty.e2c.business;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;

import com.daugherty.e2c.persistence.data.Hashids;
import com.daugherty.e2c.persistence.data.InquiryWriteDao;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class InquiryProductStagerTest {

    @Mock
    private InquiryWriteDao inquiryWriteDao;
    @Mock
    private Hashids hashids;

    @InjectMocks
    private final InquiryProductStager productStager = new InquiryProductStager();

    @Test
    public void stageProductsCallsDaoInsertProductMethodForEachProduct() throws Exception {
        List<Long> productIds = Lists.newArrayList(1L, 2L, 3L);

        when(hashids.decode("g3Vl280b")).thenReturn(new long[]{99L});
        productStager.stage("g3Vl280b", productIds);

        verify(inquiryWriteDao).insertProductForParty(99L, 1L);
        verify(inquiryWriteDao).insertProductForParty(99L, 2L);
        verify(inquiryWriteDao).insertProductForParty(99L, 3L);
    }

    @Test
    public void stageProductsLogsDuplicateKeyExceptionsAndMovesOnThroughList() throws Exception {
        List<Long> productIds = Lists.newArrayList(1L, 2L, 3L);

        doThrow(new DuplicateKeyException("1 already in your basket")).when(inquiryWriteDao).insertProductForParty(99L,
                1L);

        when(hashids.decode("g3Vl280b")).thenReturn(new long[]{99L});
        productStager.stage("g3Vl280b", productIds);

        verify(inquiryWriteDao).insertProductForParty(99L, 1L);
        verify(inquiryWriteDao).insertProductForParty(99L, 2L);
        verify(inquiryWriteDao).insertProductForParty(99L, 3L);
    }

    @Test
    public void unstageProductCallsDaoDeleteProductMethod() throws Exception {
        when(hashids.decode("g3Vl280b")).thenReturn(new long[]{99L});
        productStager.unstage("g3Vl280b", 1L);

        verify(inquiryWriteDao).deleteProductForParty(99L, 1L);
    }

}
