package com.daugherty.e2c.business.uploader;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrimaryProductImageUploader extends ProductImageUploader {

    @Override
    protected Boolean isPrimaryImage() {
        return true;
    }

}
